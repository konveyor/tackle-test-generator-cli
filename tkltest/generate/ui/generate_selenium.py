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
import json
import jinja2
import logging
import os.path

from tkltest.util import logging_util

# names and paths for generated code files
SELENIUM_API_TESTS_ROOT = 'selenium-api-tests'
SELENIUM_API_TEST_CLASS_DIR = os.path.join(SELENIUM_API_TESTS_ROOT, 'src', 'test', 'java', 'generated')
POM_FILE = 'pom.xml'
TEST_CLASS_NAME = 'GeneratedTests.java'

def generate_selenium_api_tests(config, crawl_dir):

    app_name = config['general']['app_name']
    app_url = config['general']['app_url']
    browser = config['generate']['browser']

    # initialize jinja env
    searchpath = [os.path.dirname(os.path.abspath(__file__)) + os.sep + 'template']
    jinja_env = jinja2.Environment(
        loader=jinja2.FileSystemLoader(searchpath=searchpath),
        trim_blocks=True,
        lstrip_blocks=True,
        extensions=['jinja2.ext.loopcontrols', 'jinja2.ext.do']
    )

    # load pom template, render it, annd write to pom.xml file
    pom_template = jinja_env.get_template('pom.jinja')
    jinja_context = {
        'appname': app_name,
        'browser': browser
    }
    pom_xml = pom_template.render(jinja_context)
    logging.info('Generated pom.xml for app {}, browser={}'.format(app_name, browser))

    # load crawl paths
    crawl_paths_file = os.path.join(crawl_dir, 'CrawlPaths.json')
    with open(crawl_paths_file) as f:
        crawl_paths = json.load(f)
    logging.info('Generating tests for {} crawl paths'.format(len(crawl_paths)))

    # load test class template and render it
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

    # iterate over crawl paths
    method_names = []
    for path_num, crawl_path in enumerate(crawl_paths):
        # for each path create a jinja context for the test method to be generated
        method_name = __create_method_name_for_path(crawl_path)
        logging.info('Path {}: length={}, {}'.format(path_num, len(crawl_path), method_name))
        if method_name in method_names:
            logging.warning('Found duplicate crawl path: {} ... skipping'.format(method_name))
            continue
        method_names.append(method_name)
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


def __create_method_name_for_path(path):
    eventable_ids = [
        str(eventable['id']) for eventable in path
    ]
    return 'test_path_{}'.format('_'.join(eventable_ids))


def __get_context_for_eventable(eventable):
    context = {
        'event_type': eventable['eventType'],
        'by_method': __get_by_method_for_eventable(eventable['identification']),
        'related_frame': eventable['relatedFrame'],
        'form_inputs': []
    }
    for form_input in eventable['relatedFormInputs']:
        context['form_inputs'].append({
            'type': form_input['type'],
            'by_method': __get_by_method_for_eventable(form_input['identification']),
            'value': form_input['inputValues'][0]['value']
        })
    return context


def __get_by_method_for_eventable(elem_identification):
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
        raise Exception('Unknown identification how elemen: {}'.format(ident_how))
    web_element = 'By.{}("{}")'.format(how, elem_identification['value'])
    return web_element


def __write_generated_code(pom_xml, test_class_code, crawl_dir):
    # write pom and test class to files
    selenium_api_tests_root = os.path.join(crawl_dir, SELENIUM_API_TESTS_ROOT)
    selenium_api_test_class_dir = os.path.join(crawl_dir, SELENIUM_API_TEST_CLASS_DIR)
    os.makedirs(selenium_api_test_class_dir, exist_ok=True)
    with open(os.path.join(selenium_api_tests_root, POM_FILE), 'w') as f:
        f.write(pom_xml)
    logging.info('Wrote {} to {}'.format(POM_FILE, selenium_api_tests_root))
    with open(os.path.join(selenium_api_test_class_dir, TEST_CLASS_NAME), 'w') as f:
        f.write(test_class_code)
    logging.info('Wrote test class to {}'.format(selenium_api_test_class_dir))


if __name__ == '__main__':  # pragma: no cover
    logging_util.init_logging('generate_selenium.log', 'INFO')
    config = {
        'general': {
            'app_name': 'petclinic',
            'app_url': 'http://localhost:8080'
        },
        'generate': {
            'browser': 'chrome_headless',
            'wait_after_event': 500,
            'wait_after_reload': 500
        }
    }
    crawl_dir = '../../../tkltest-output-ui-petclinic/petclinic_localhost_2mins/localhost/crawl0'
    generate_selenium_api_tests(config, crawl_dir)
