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
import json
import sys

import jinja2
import logging
import os.path
from tkltest.util import constants, logging_util
from heuristic_labels import HeuristicLabel
from importlib import resources
import pandas as pd
from generate_selenium import __create_method_name_for_path, __get_context_for_eventable, __get_by_method_for_eventable

# names and paths for generated code files
_POM_FILE = 'pom.xml'



def generate_selenium_api_tests_analysis(config, crawl_dir):
    """
    Modified generate_selenium.py to directly take in a crawl paths file

    Args:
        config (dict): Configuration information for test generation
        crawl_dir (dict): Root crawl directory created by Crawljax for the current test-generation run
    """
    app_name = config['general']['app_name']
    _CRAWL_PATHS_FILE = 'crawl_paths_' + app_name + '.json'
    logging.info('Creating Selenium API tests from paths in {} for analysis'.format(os.path.join(crawl_dir, _CRAWL_PATHS_FILE)))

    app_name = _CRAWL_PATHS_FILE[12:-5] # assuming all crawlpaths files follow the format crawl_paths_xxx.json
    app_url = config['general']['app_url']
    browser = config['generate']['browser']

    jinja_env = jinja2.Environment(
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

    total_clickables = 0
    total_form_field_elements = 0
    empty_clickable_labels = 0
    empty_form_field_labels = 0
    eventable_dom_label_table = []

    form_field_dom_label_table = []

    heuristic_label.get_element_and_method_labels(crawl_paths)

    for path_num, crawl_path in enumerate(crawl_paths):
        # for each path create a jinja context for the test method to be generated
        method_name = __create_method_name_for_path(crawl_path)
        method_eventables_path = method_name[10:]
        logging.info('Path {}: length={}, {}'.format(path_num, len(crawl_path), method_name))
        if method_eventables_path in method_eventables_path_count:
            method_eventables_path_count[method_eventables_path] = method_eventables_path_count[method_eventables_path] + 1
            method_name = '{}_dup{}'.format(method_name, method_eventables_path_count[method_eventables_path])
            logging.info('Creating duplicate test method: {}'.format(method_name))
        else:
            method_eventables_path_count[method_eventables_path] = 0
        method_context = {
            'comment': heuristic_label.method_labels[method_eventables_path],
            'priority': path_num,
            'name': method_name,
            'eventables': [],

        }
        for eventable in crawl_path:

            eventable_dom_label_table.append([eventable['id'],
                                              eventable['element'],
                                              heuristic_label.find_element(eventable['source']['dom'],
                                                                           eventable['identification'],
                                                                           'str'),
                                              heuristic_label.get_context_dom(eventable['source']['dom'],
                                                                              eventable['identification']),
                                              heuristic_label.eventable_labels[eventable['id']][0]])
            total_clickables += 1
            total_form_field_elements += len(heuristic_label.eventable_labels[eventable['id']][1])

            i = 0
            for form_input in eventable['relatedFormInputs']:
                form_field_dom = heuristic_label.find_element(eventable['source']['dom'],
                                                form_input['identification'], 'str')
                form_field_extended_dom = heuristic_label.get_form_field_extended_dom(eventable['source']['dom'],
                                                form_input['identification'], 'str')
                form_field_label = heuristic_label.eventable_labels[eventable['id']][1][i]

                form_field_dom_label_table.append([form_field_dom, form_field_extended_dom, form_field_label])
                i += 1

            label = heuristic_label.eventable_labels[eventable['id']]
            method_context['eventables'].append(__get_context_for_eventable(eventable, label))
        jinja_context['test_methods'].append(method_context)

    eventable_dom_label_table = pd.DataFrame(eventable_dom_label_table,
                                             columns=['id', 'eventable[element]', 'curr_dom', 'context_dom',
                                                      'label'])
    form_field_dom_label_table = pd.DataFrame(form_field_dom_label_table, columns=['form_field_dom','form_field_extended_dom', 'label'])

    clickable_percentage = 'N/A'
    if total_clickables > 0:
        clickable_percentage = (1 - heuristic_label.empty_eventable_labels / len(heuristic_label.eventable_labels)) * 100
    form_field_percentage = 'N/A'
    if total_form_field_elements > 0:
        form_field_percentage = (1 - heuristic_label.empty_form_field_labels / total_form_field_elements) * 100
    results = {'Number of Clickables': len(heuristic_label.eventable_labels), 'Number of Form Field Elements': total_form_field_elements,
               'Empty Clickable Labels': heuristic_label.empty_eventable_labels, 'Empty Form Field Labels': heuristic_label.empty_form_field_labels,
               'Percentage of Labels computed for clickables': clickable_percentage,
               'Percentage of labels computed for form fields': form_field_percentage}

    analysis_outputs_path = os.path.join(os.curdir, 'analysis_outputs')
    if not (os.path.exists(analysis_outputs_path)):
        os.makedirs(analysis_outputs_path)

    output_file = open('analysis_outputs/label_analysis_results_' + app_name + '.json', 'w')
    output_file.write(json.dumps(results))

    eventable_dom_label_table.to_csv('analysis_outputs/eventable_dom_label_table_' + app_name + '.csv')
    form_field_dom_label_table.to_csv('analysis_outputs/form_field_dom_label_table_' + app_name + '.csv')


    # render template to generate source code for test class
    testclass_code = testclass_template.render(jinja_context)
    logging.info('Generated test class')

    # write pom and test class to files

    __write_generated_code(pom_xml=pom_xml, test_class_code=testclass_code, crawl_dir=crawl_dir, app_name=app_name)



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



if __name__ == '__main__':  # pragma: no cover
    # generate_selenium_api_tests()
    logging_util.init_logging('generate_selenium.log', 'INFO')
    app_config = {
        'general': {
            'log-level': 'WARNING',
            'app_name': sys.argv[1],
            'app_url': 'N/A - for analysis purposes'
        },
        'generate': {
            'browser': 'chrome_headless',
            'wait_after_event': 500,
            'wait_after_reload': 500
        }
    }
    app_crawl_dir = ''
    # app_crawl_dir = '../../../tkltest-output-ui-addressbook/addressbook_localhost_3mins/localhost/crawl0'
    generate_selenium_api_tests_analysis(app_config, app_crawl_dir)