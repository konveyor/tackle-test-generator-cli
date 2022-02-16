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

import logging
import os.path
import re
import subprocess
import sys
import urllib.parse

import toml

from tkltest.util.logging_util import tkltest_status
from tkltest.util import command_util
from tkltest.util.constants import *
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

    app_name = config['general']['app_name']
    app_url = config['general']['app_url']
    verbose = config['general']['verbose']
    browser = config['generate']['browser']
    time_limit = config['generate']['time_limit']
    host_name = urllib.parse.urlparse(config['general']['app_url']).netloc.split(':')[0]

    # set default test directory if unspecified
    test_directory = dir_util.get_test_directory(config, host_name)
    logging.info('test directory: '.format(test_directory))

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
    try:
        # TODO: run command in a separate thread; the main thread can then monitor progress on the file system
        command_util.run_command(command=uitestgen_command, verbose=verbose)

        # print info about generated tests
        output_crawl_dir = dir_util.get_crawl_output_dir(test_directory=test_directory,
                                                                  host_name=host_name)
        tkltest_status('Crawl results written to {}'.format(output_crawl_dir))
        test_count, test_class_file = __get_generated_test_count(last_crawl_dir=output_crawl_dir)
        tkltest_status('Generated {} test cases; written to test class file "{}"'.format(test_count, test_class_file))

    except subprocess.CalledProcessError as e:
        tkltest_status('UI test genenration failed: {}\n{}'.format(e, e.stderr), error=True)
        sys.exit(1)

    # TODO: print progress messages: number of states discovered, percent time elapsed

    # cleanup browser instances
    browser_util.cleanup_browser_instances(browser)


def __get_generated_test_count(last_crawl_dir):
    """Returns the number of generated test cases

    Computes test case count by counting @Test annotations in the genearted test class file
    """
    generated_test_class = os.path.join(last_crawl_dir, 'src', 'test', 'java', 'generated', 'GeneratedTests.java')
    with open(generated_test_class, 'r') as f:
        teststr = f.read()
    return len(re.findall('@Test', teststr)), generated_test_class
