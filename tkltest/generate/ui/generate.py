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

import logging
import os.path
import re
import subprocess
import sys
import time
import urllib.parse
from importlib import resources
from threading import Thread

import toml
from tqdm import trange

from tkltest.generate.ui import generate_selenium
from tkltest.util import command_util
from tkltest.util.constants import *
from tkltest.util.logging_util import tkltest_status
from tkltest.util.ui import dir_util, browser_util


def process_generate_command(config):
    """Processes the tkltest-ui generate command.

    Processes the generate command and generates UI test cases by invoking the crawljax runner using the options
    set in the given config info. Writes the config info to a file for passing as argument to the runner. After
    crawljax run, cleans up browser instances.

    Args:
        config: loaded configuration options (with internal options)
    """
    logging.info('Processing generate command')

    browser = config['generate']['browser']
    host_name = urllib.parse.urlparse(config['general']['app_url']).netloc.split(':')[0]

    # set default test directory if unspecified
    test_directory = dir_util.get_test_directory(config, host_name)
    logging.info('test directory: {}'.format(test_directory))

    # run crawljax and get output crawl directory
    __run_crawljax(config=config)
    output_crawl_dir = dir_util.get_crawl_output_dir(test_directory=test_directory, host_name=host_name)
    tkltest_status('Crawl results written to {}'.format(output_crawl_dir))

    # print message about generated crwaljax API tests
    test_class_file = os.path.join(output_crawl_dir, CRAWLJAX_API_TEST_FILE)
    test_count = __get_generated_test_count(test_class_file=test_class_file)
    tkltest_status('Generated {} Crawljax API test cases; written to test class file "{}"'
                   .format(test_count, test_class_file))

    # generate Selenium API tests
    tkltest_status('Creating Selenium API test cases from crawl paths')
    try:
        generate_selenium.generate_selenium_api_tests(config=config, crawl_dir=output_crawl_dir)
        test_class_file = os.path.join(output_crawl_dir, SELENIUM_API_TEST_FILE)
        test_count = __get_generated_test_count(test_class_file=test_class_file)
        tkltest_status('Generated {} Selenium API test cases; written to test class file "{}"'
                       .format(test_count, test_class_file))
    except Exception as e:
        tkltest_status('Exception occurred while creating Selenium API tests: {}'.format(str(e)), error=True)
        browser_util.cleanup_browser_instances(browser)
        sys.exit(1)

    # cleanup browser instances
    browser_util.cleanup_browser_instances(browser)

def __run_crawljax(config):
    app_name = config['general']['app_name']
    app_url = config['general']['app_url']
    verbose = config['general']['verbose']
    browser = config['generate']['browser']
    time_limit = config['generate']['time_limit']

    # write config (with internal options added) to toml file to be passed as argument to the crawljax runner
    config_file_name = '__{}_tkltest_ui_config.toml'.format(app_name)
    with open(config_file_name, 'w') as f:
        toml.dump(config, f)

    # create java command for running crawljax runner
    uitestgen_command = 'java -Xmx2048m -cp '
    with resources.path('tkltest-lib', TKLTEST_UI_CORE_JAR) as core_ui_jar:
        uitestgen_command += str(core_ui_jar)
    uitestgen_command += ' org.konveyor.tackletest.ui.crawljax.CrawljaxRunner -cf {}'.format(config_file_name)

    # if verbose option specified, redirect crawljax log to file
    if verbose:
        crawljax_log_file = '{}_crawljax_runner.log'.format(app_name)
        uitestgen_command += ' 1> ' + crawljax_log_file
        tkltest_status('Crawljax runner log will be written to {}'.format(crawljax_log_file))
    logging.info(uitestgen_command)

    tkltest_status('Running UI test generator with config: app={}, url="{}", time_limit={}min, browser={}'.format(
        app_name, app_url, time_limit, browser
    ))

    # start CrawlJax in a separate thread and display a progress bar while it is generating tests
    global crawler_error, crawler_e
    crawler_error = False
    crawler_e = None

    thread = Thread(target=__run_crawljax_command, args=[uitestgen_command, verbose])
    thread.start()
    closed_before_limit = False
    for b in trange(
        time_limit * 60, unit="seconds", desc="Generating tests",
        bar_format="{l_bar}{bar}|{n:.1f}/{total_fmt} [{elapsed}<{remaining}]"
):
        if not thread.is_alive():
            closed_before_limit = True
            break
        time.sleep(1)

    if crawler_error:
        tkltest_status('UI test genenration failed: {}\n{}'.format(crawler_e, crawler_e.stderr), error=True)
        sys.exit(1)
    elif closed_before_limit:
        tkltest_status("Crawler exploration exhausted before time limit reached")
    elif thread.is_alive():
        tkltest_status("Waiting for the crawler to terminate")
        thread.join()

    # need to check crawler error again because it might have occurred after progress bar ended
    # and while still waiting for the crawler to terminate
    if crawler_error:
        tkltest_status('UI test generation failed: {}\n{}'.format(crawler_e, crawler_e.stderr), error=True)
        sys.exit(1)



def __run_crawljax_command(uitestgen_command, verbose):
    global crawler_error, crawler_e
    try:
        command_util.run_command(command=uitestgen_command, verbose=verbose)
    except subprocess.CalledProcessError as e:
        crawler_e = e
        crawler_error = True


def __get_generated_test_count(test_class_file):
    """Returns the number of generated test cases

    Computes test case count by counting @Test annotations in the generated test class file
    """
    with open(test_class_file, 'r') as f:
        teststr = f.read()
    return len(re.findall('@Test', teststr))

if __name__ == '__main__':  # pragma: no cover
    app_config = {
        'general': {
            'log-level': 'WARNING',
            'app_name': 'petclinic',
            'app_url': 'http://localhost:8080',
            # 'app_name': 'addressbook',
            # 'app_url': 'http://localhost:3000/addressbook/',
            'test_directory': '',
            'verbose': False
        },
        'generate': {
            'browser': 'chrome_headless',
            'wait_after_event': 500,
            'wait_after_reload': 500,
            'time_limit': 2
        }
    }
    process_generate_command(app_config)
