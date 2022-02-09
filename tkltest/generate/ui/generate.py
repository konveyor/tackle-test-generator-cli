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

import glob
import logging
import os.path
import re
import sys
import urllib.parse

import psutil
import toml

from tkltest.util.logging_util import tkltest_status
from tkltest.util import command_util
from tkltest.util.constants import *

def process_generate_command(config):
    """Processes the tkltest-ui generate command.

    Processes the generate command and generates UI test cases by invoking the crawljax runner using the options
    set in the given config info. Writes the config info to a file for passing as argument to the runner. After
    crawljax run, cleans up browser instances.

    Args:
        config: loaded configuration options (with internal options)
    """
    logging.info('Processing generate command')

    app_name = config['general']['app_name']
    app_url = config['general']['app_url']
    verbose = config['general']['verbose']
    browser = config['generate']['browser']
    time_limit = config['generate']['time_limit']

    # write config (with internal options added) to toml file to be passed as argument to the crawljax runner
    config_file_name = '__{}_tkltest_ui_config.toml'.format(config['general']['app_name'])
    with open(config_file_name, 'w') as f:
        toml.dump(config, f)

    # create java command for running crawljax runner
    uitestgen_command = 'java -Xmx2048m -cp '
    uitestgen_command += TKLTEST_UI_CORE_JAR + os.pathsep + CRAWLJAX_JAR
    uitestgen_command += os.pathsep + COMMONS_CLI_JAR + os.pathsep + TOML_JAR + os.pathsep + ANTLR_JAR
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
    command_util.run_command(command=uitestgen_command, verbose=verbose)

    # TODO: print progress messages: states discovered, %time

    # TODO: print info about generated tests
    tkltest_status('Crawl results written to {}'.format(
        __get_crawl_output_dir(app_name=app_name, app_url=app_url, time_limit=time_limit)))
    test_count, test_class_file = __get_generated_test_count(app_name=app_name, app_url=app_url, time_limit=time_limit)
    tkltest_status('Generated {} test cases; written to test class file "{}"'.format(test_count, test_class_file))

    # cleanup browser instances
    __cleanup_browser_instances(browser)


def __run_ui_test_generator(command, verbose):
    pass


def __get_crawl_output_dir(app_name, app_url, time_limit):
    hostport = urllib.parse.urlparse(app_url).netloc
    output_crawl_dirs = os.path.join(TKLTEST_UI_OUTPUT_DIR_PREFIX + app_name,
                                    '{}_{}_{}mins'.format(app_name, hostport.split(':')[0], time_limit),
                                    'crawl*')
    return sorted(glob.iglob(output_crawl_dirs), key=os.path.getctime, reverse=True)[0]


def __get_generated_test_count(app_name, app_url, time_limit):
    last_crawl_dir = __get_crawl_output_dir(app_name=app_name, app_url=app_url, time_limit=time_limit)
    generated_test_class = os.path.join(last_crawl_dir, 'src', 'test', 'java', 'generated', 'GeneratedTests.java')
    with open(generated_test_class, 'r') as f:
        teststr = f.read()
    return len(re.findall('@Test', teststr)), generated_test_class


def __cleanup_browser_instances(browser):
    """Performs process cleanup based on platform and browser"""
    if browser in ['chrome', 'chrome_headless']:
        if sys.platform == 'darwin':
            __kill_processes(['chromedriver', 'chrome'])
        elif sys.platform in ['linux', 'linux2']:
             __kill_processes(['chromedriver'])
        elif sys.platform in ['win32', 'win64']:
            # TODO: check
            __kill_processes(['chromedriver.exe'])
    else:
        # TODO: firefox
        pass


def __kill_processes(proc_names):
    """Kills process with the given names"""
    for proc in psutil.process_iter():
        procname = proc.name()
        if procname in proc_names:
            logging.info('Killing {} instance'.format(procname))
            try:
                proc.kill()
            except ProcessLookupError:
                logging.info('Could not find process "{}"'.format(procname))
                pass
