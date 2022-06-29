# ***************************************************************************
# Copyright IBM Corporation 2021
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#       http://www.apache.org/licenses/LICENSE-2.0
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

from heuristic_labels import HeuristicLabel

from importlib import resources
import pandas as pd

# names and paths for generated code files
_POM_FILE = 'pom.xml'

_CRAWL_PATHS_FILE = 'crawl_paths_tmf.json'



def generate_selenium_api_tests(config, crawl_dir):
    """
    Modified generate_selenium.py to directly take in a crawl paths file

    Generates test cases that use the Selenium API

    Processes crawl path information created by crawljax to generate test cases that use the Selenium API
    for performing actions on the app web UI. Removes crawljax-generated artifacts that are not required
    for Selenium API test cases.

    Args:
        config (dict): Configuration information for test generation
        crawl_dir (dict): Root crawl directory created by Crawljax for the current test-generation run
    """
    logging.info('Creating Selenium API tests from paths in {}'.format(os.path.join(crawl_dir, _CRAWL_PATHS_FILE)))

    # app_name = config['general']['app_name']
    app_name = _CRAWL_PATHS_FILE[12:-5]

    app_url = config['general']['app_url']
    browser = config['generate']['browser']

    # initialize jinja env
    # searchpath = [os.path.join('tkltest', 'generate', 'ui', 'templates'), 'templates']
    # logging.info('Creating jinja environment with searchpath={})'.format(searchpath))
    jinja_env = jinja2.Environment(
        # loader=jinja2.FileSystemLoader(searchpath=searchpath),
        loader=jinja2.PackageLoader('tkltest.generate.ui'),
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

    method_eventables_path_count = {}


    # get heuristic labels for each eventable based on its ranked attributes
    with resources.path('tkltest.generate.ui', 'ranked_attributes.json') as attr_file:
        heuristic_label = HeuristicLabel(str(attr_file))

    # to store analysis outputs
    heuristic_label_dict = dict()
    total_clickables = 0
    total_form_field_elements = 0
    empty_clickable_labels = 0
    empty_form_field_labels = 0
    eventable_dom_label_table = []

    form_field_dom_label_table = []

    heuristic_label.get_element_and_method_labels(crawl_paths)

    for path_num, crawl_path in enumerate(crawl_paths):
        # for each path create a jinja context for the test method to be generated
        method_eventables_path = __create_method_name_for_path(crawl_path)
        logging.info('Path {}: length={}, {}'.format(path_num, len(crawl_path), method_eventables_path))
        if method_eventables_path in method_eventables_path_count:
            method_eventables_path_count[method_eventables_path] = method_eventables_path_count[method_eventables_path] + 1
            method_eventables_path = '{}_dup{}'.format(method_eventables_path, method_eventables_path_count[method_eventables_path])
            logging.info('Creating duplicate test method: {}'.format(method_eventables_path))
        else:
            method_eventables_path_count[method_eventables_path] = 0
        method_context = {
            'comment': heuristic_label.method_labels[method_eventables_path[10:]],
            'priority': path_num,
            'name': method_eventables_path,
            'eventables': [],

        }
        for eventable in crawl_path:
            heuristic_label_dict[eventable['id']] = heuristic_label.get_label(eventable)
            eventable_dom_label_table.append([eventable['id'],
                                              eventable['element'],
                                              heuristic_label.find_element(eventable['source']['dom'],
                                                                           eventable['identification']['value'].lower(),
                                                                           'str'),
                                              heuristic_label.get_context_dom(eventable['source']['dom'],
                                                                              eventable['identification'][
                                                                                  'value'].lower()),
                                              heuristic_label_dict[eventable['id']][0]])
            total_clickables += 1
            total_form_field_elements += len(heuristic_label_dict[eventable['id']][1])
            if heuristic_label_dict[eventable['id']][0] == '':
                empty_clickable_labels += 1

            i = 0
            for form_input in eventable['relatedFormInputs']:
                form_field_dom = heuristic_label.find_element(eventable['source']['dom'],
                                                form_input['identification']['value'].lower(), 'str')
                form_field_label = heuristic_label_dict[eventable['id']][1][i]
                form_field_dom_label_table.append([form_field_dom, form_field_label])
                if form_field_label == 'Enter data into form field':
                    empty_form_field_labels += 1
                i += 1


            if eventable['id'] not in heuristic_label_dict:
                heuristic_label_dict[eventable['id']] = heuristic_label.get_label(eventable)
            label = heuristic_label_dict[eventable['id']]
            method_context['eventables'].append(__get_context_for_eventable(eventable, label))

            # method_name.append(heuristic_label_dict[eventable['id']][0].replace(' ', '_'))
        # method_context['name'] = '__'.join(method_eventables_path)
        eventable_id_path = '_'.join([
            str(eventable['id']) for eventable in crawl_path
        ])
        eventable_dom_label_table = pd.DataFrame(eventable_dom_label_table,
                                                 columns=['id', 'eventable[element]', 'curr_dom', 'context_dom',
                                                          'label'])
        form_field_dom_label_table = pd.DataFrame(form_field_dom_label_table, columns=['form_field_dom','label'])

        clickable_percentage = 'N/A'
        if total_clickables > 0:
            clickable_percentage = (1 - empty_clickable_labels / total_clickables) * 100
        form_field_percentage = 'N/A'
        if total_form_field_elements > 0:
            form_field_percentage = (1 - empty_form_field_labels / total_form_field_elements) * 100
        results = {'Number of Clickables': total_clickables, 'Number of Form Field Elements': total_form_field_elements,
                   'Empty Clickable Labels': empty_clickable_labels, 'Empty Form Field Labels': empty_form_field_labels,
                   'Percentage of Labels computed for clickables': clickable_percentage,
                   'Percentage of labels computed for form fields': form_field_percentage}

        analysis_outputs_path = os.path.join(os.curdir, 'analysis_outputs')
        if not (os.path.exists(analysis_outputs_path)):
            os.makedirs(analysis_outputs_path)


        output_file = open('analysis_outputs/label_analysis_results_' + app_name + '.json', 'w')
        output_file.write(json.dumps(results))

        output_file = open('analysis_outputs/labels_computed_' + app_name + '.json', 'w')
        output_file.write(json.dumps(heuristic_label_dict))

        eventable_dom_label_table.to_csv('analysis_outputs/eventable_dom_label_table_' + app_name + '.csv')
        form_field_dom_label_table.to_csv('analysis_outputs/form_field_dom_label_table_' + app_name + '.csv')


        jinja_context['test_methods'].append(method_context)

    # render template to generate source code for test class
    testclass_code = testclass_template.render(jinja_context)
    logging.info('Generated test class')

    # write pom and test class to files

    __write_generated_code(pom_xml=pom_xml, test_class_code=testclass_code, crawl_dir=crawl_dir, app_name=app_name)


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


def __get_context_for_eventable(eventable, label):
    """Creates jinja context for an eventable.

    Creates and returns jinja context for the given eventable for rendering the test class code template.
    """

    context = {
        'event_type': eventable['eventType'],
        'by_method': __get_by_method_for_eventable(eventable['identification']),
        'related_frame': eventable['relatedFrame'],
        'form_inputs': [],
        # 'comment': json.dumps(eventable['element'])
        'comment': label[0]
    }
    for i, form_input in enumerate(eventable['relatedFormInputs']):
        context['form_inputs'].append({
            'type': form_input['type'],
            'by_method': __get_by_method_for_eventable(form_input['identification']),
            'value': form_input['inputValues'][0]['value'],
            'checked': "true" if form_input['inputValues'][0]['checked'] is True else "false",
            'comment': label[1][i]
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


def __write_generated_code(pom_xml, test_class_code, crawl_dir, app_name):

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

    with open(os.path.join(crawl_dir, constants.SELENIUM_API_TEST_FILE[:-5] + '_' + app_name + '.java'), 'w') as f:

        f.write(test_class_code)
    logging.info('Wrote test class to {}'.format(selenium_api_test_class_dir))

    # copy testng xml from crawl root dir to test root dir
    # shutil.copy(os.path.join(crawl_dir, 'testng.xml'), selenium_api_test_root)


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
    # files_to_keep = [
    #     os.path.join(crawl_dir, file)
    #     for file in [_CRAWL_PATHS_FILE, 'config.json', 'crawlPathsInfo.json', 'result.json']
    # ]
    # for file in glob.glob(os.path.join(crawl_dir, '*.json')):
    #     if file not in files_to_keep:
    #         os.remove(file)

    # remove run scripts
    for file in glob.glob(os.path.join(crawl_dir, 'run.*')):
        os.remove(file)


if __name__ == '__main__':  # pragma: no cover
    # generate_selenium_api_tests()
    logging_util.init_logging('generate_selenium.log', 'INFO')
    app_config = {
        'general': {
            'log-level': 'WARNING',
            # 'app_name': 'petclinic',
            # 'app_url': 'http://localhost:8080'
            'app_name': 'TMF',
            'app_url': 'xxx'
        },
        'generate': {
            'browser': 'chrome_headless',
            'wait_after_event': 500,
            'wait_after_reload': 500
        }
    }
    app_crawl_dir = ''
    # app_crawl_dir = '../../../tkltest-output-ui-addressbook/addressbook_localhost_3mins/localhost/crawl0'
    generate_selenium_api_tests(app_config, app_crawl_dir)
