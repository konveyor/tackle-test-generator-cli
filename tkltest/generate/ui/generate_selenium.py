# ***************************************************************************
# Copyright IBM Corporation 2022
#
# Licensed under the Eclipse Public License 2.0, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
# ***************************************************************************
import datetime
import glob
import json
import shutil

import jinja2
import logging
import os.path

from tkltest.util import constants, logging_util

# names and paths for generated code files
_POM_FILE = 'pom.xml'
_CRAWL_PATHS_FILE = 'CrawlPaths.json'


def generate_selenium_api_tests(config, crawl_dir):
    """Generates test cases that use the Selenium API.

    Processes crawl path information created by crawljax to generate test cases that use the Selenium API
    for performing actions on the app web UI. Removes crawljax-generated artifacts that are not required
    for Selenium API test cases.

    Args:
        config (dict): Configuration information for test generation
        crawl_dir (dict): Root crawl directory created by Crawljax for the current test-generation run
    """
    logging.info('Creating Selenium API tests from paths in {}'.format(os.path.join(crawl_dir, _CRAWL_PATHS_FILE)))
    app_name = config['general']['app_name']
    app_url = config['general']['app_url']
    browser = config['generate']['browser']

    # initialize jinja env
    searchpath = [os.path.join('tkltest', 'generate', 'ui', 'templates'), 'templates']
    logging.info('Creating jinja environment with searchpath={})'.format(searchpath))
    jinja_env = jinja2.Environment(
        loader=jinja2.FileSystemLoader(searchpath=searchpath),
        trim_blocks=True,
        lstrip_blocks=True,
        extensions=['jinja2.ext.loopcontrols', 'jinja2.ext.do']
    )
    logging.info('Jinja environment created')

    # load pom template, render it, and write to pom.xml file
    pom_template = jinja_env.get_template('pom.jinja')
    logging.info('POM template: {}'.format(pom_template))
    jinja_context = {
        'appname': app_name,
        'browser': browser
    }
    pom_xml = pom_template.render(jinja_context)
    logging.info('Generated pom.xml for app {}, browser={}'.format(app_name, browser))

    # load crawl paths
    crawl_paths_file = os.path.join(crawl_dir, _CRAWL_PATHS_FILE)
    with open(crawl_paths_file) as f:
        crawl_paths = json.load(f)
    logging.info('Generating tests for {} crawl paths'.format(len(crawl_paths)))

    # load test class template and initialize jinja context for it
    testclass_template = jinja_env.get_template('GeneratedTests.jinja')
    jinja_context = {
        'now': datetime.datetime.now().replace(microsecond=0).isoformat(),
        'appname': app_name,
        'appurl': app_url,
        'browser': browser,
        'wait_event': config['generate']['wait_after_event'],
        'wait_reload': config['generate']['wait_after_reload'],
        'test_methods': []
    }

    # iterate over crawl paths and construct context for each test method
    method_name_count = {}
    for path_num, crawl_path in enumerate(crawl_paths):
        # for each path create a jinja context for the test method to be generated
        method_name = __create_method_name_for_path(crawl_path)
        logging.info('Path {}: length={}, {}'.format(path_num, len(crawl_path), method_name))
        if method_name in method_name_count:
            method_name_count[method_name] = method_name_count[method_name] + 1
            method_name = '{}_dup{}'.format(method_name, method_name_count[method_name])
            logging.info('Creating duplicate test method: {}'.format(method_name))
        else:
            method_name_count[method_name] = 0
        method_context = {
            'priority': path_num,
            'name': method_name,
            'eventables': []
        }
        for eventable in crawl_path:
            method_context['eventables'].append(__get_context_for_eventable(eventable))
        jinja_context['test_methods'].append(method_context)

    # render template to generate source code for test class
    testclass_code = testclass_template.render(jinja_context)
    logging.info('Generated test class')

    # write pom and test class to files
    __write_generated_code(pom_xml=pom_xml, test_class_code=testclass_code, crawl_dir=crawl_dir)

    # clean up crawl folder
    __clean_up_crawl_artifacts(crawl_dir)


def __create_method_name_for_path(path):
    """Creates method name from crawl/test path.

    Creates test method name for the given path by concatenating IDs of the states in the path.

    Args:
        path (str): crawl/test path to create test method name for
    Returns:
        str: method name
    """
    eventable_ids = [
        str(eventable['id']) for eventable in path
    ]
    return 'test_path_{}'.format('_'.join(eventable_ids))


def __get_context_for_eventable(eventable):
    """Creates jinja context for an eventable.

    Creates and returns jinja context for the given eventable for rendering the test class code template.
    """
    context = {
        'event_type': eventable['eventType'],
        'by_method': __get_by_method_for_eventable(eventable['identification']),
        'related_frame': eventable['relatedFrame'],
        'form_inputs': [],
        'comment': json.dumps(eventable['element'])
    }
    for form_input in eventable['relatedFormInputs']:
        context['form_inputs'].append({
            'type': form_input['type'],
            'by_method': __get_by_method_for_eventable(form_input['identification']),
            'value': form_input['inputValues'][0]['value'],
            'checked': "true" if form_input['inputValues'][0]['checked'] is True else "false"
        })
    return context


def __get_by_method_for_eventable(elem_identification):
    """Computes Selenium's By method to use for the given web element identification info.

    Computes Selenium's By method to be used for locating the web element with the given identification
    information. Maps identification's "how" value to one of the Selenium API methods.

    Args:
        elem_identification (dict): identification information for a web element
    Returns:
        str: Java code fragment representing a By method call
    """
    ident_how = elem_identification['how']
    if ident_how in ['xpath', 'id', 'name']:
        how = ident_how
    elif ident_how == 'tag':
        how = 'tagName'
    elif ident_how == 'text':
        how = 'linkText'
    elif ident_how == 'partialText':
        how = 'partialLinkText'
    else:
        raise Exception('Unknown identification "how" element: {}'.format(ident_how))
    web_element = 'By.{}("{}")'.format(how, elem_identification['value'])
    return web_element


def __write_generated_code(pom_xml, test_class_code, crawl_dir):
    """Writes generated pom and test class code to files.

    Writes pom and test class to files under selenium API test root dir. Copies testng.xml from the crawl
    root dir to the selenium API test root dir
    """
    # write pom and test class to files
    selenium_api_test_class_dir = os.path.join(crawl_dir, constants.SELENIUM_API_TEST_CLASS_DIR)
    os.makedirs(selenium_api_test_class_dir, exist_ok=True)
    selenium_api_test_root = os.path.join(crawl_dir, constants.SELENIUM_API_TEST_ROOT)
    with open(os.path.join(selenium_api_test_root, _POM_FILE), 'w') as f:
        f.write(pom_xml)
    logging.info('Wrote {} to {}'.format(_POM_FILE, os.path.join(crawl_dir, constants.SELENIUM_API_TEST_ROOT)))
    with open(os.path.join(crawl_dir, constants.SELENIUM_API_TEST_FILE), 'w') as f:
        f.write(test_class_code)
    logging.info('Wrote test class to {}'.format(selenium_api_test_class_dir))

    # copy testng xml from crawl root dir to test root dir
    shutil.copy(os.path.join(crawl_dir, 'testng.xml'), selenium_api_test_root)


def __clean_up_crawl_artifacts(crawl_dir):
    """Cleans up crawl artifacts.

    Deletes crawl artifacts related to crawljax API test cases. Retains the crawl model and related visualization
    artifacts.
    """
    # # delete src tree for crawljax API tests
    # shutil.rmtree(os.path.join(crawl_dir, 'src'), ignore_errors=True)
    # # delete pom.xml
    # root_pom = os.path.join(crawl_dir, 'pom.xml')
    # if os.path.exists(root_pom):
    #     os.remove(root_pom)
    # # for file in glob.glob(os.path.join(crawl_dir, '*.xml')):
    # #     os.remove(file)

    # delete all json files except the crawl paths files and files required for running crawljax API tests
    files_to_keep = [
        os.path.join(crawl_dir, file)
        for file in [_CRAWL_PATHS_FILE, 'config.json', 'crawlPathsInfo.json', 'result.json']
    ]
    for file in glob.glob(os.path.join(crawl_dir, '*.json')):
        if file not in files_to_keep:
            os.remove(file)

    # remove run scripts
    for file in glob.glob(os.path.join(crawl_dir, 'run.*')):
        os.remove(file)


if __name__ == '__main__':  # pragma: no cover
    logging_util.init_logging('generate_selenium.log', 'INFO')
    app_config = {
        'general': {
            'log-level': 'WARNING',
            # 'app_name': 'petclinic',
            # 'app_url': 'http://localhost:8080'
            'app_name': 'addressbook',
            'app_url': 'http://localhost:3000/addressbook/'
        },
        'generate': {
            'browser': 'chrome_headless',
            'wait_after_event': 500,
            'wait_after_reload': 500
        }
    }
    # app_crawl_dir = '../../../tkltest-output-ui-petclinic/petclinic_localhost_2mins/localhost/crawl0'
    app_crawl_dir = '../../../tkltest-output-ui-addressbook/addressbook_localhost_3mins/localhost/crawl0'
    generate_selenium_api_tests(app_config, app_crawl_dir)
