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

import os
import shutil
import sys
import subprocess
import urllib

from tkltest.ui.util import dir_util, browser_util
from tkltest.util import command_util, constants
from tkltest.util.logging_util import tkltest_status

def process_execute_command(config):
    """Processes the ui execute command.

    Processes the execute command and executes either Selenium API test cases or Crawljax API test cases
    based on the execution command api-type option.

    Args:
        config: loaded configuration options
    """

    app_name = config['general']['app_name']
    app_url = config['general']['app_url']
    browser = config['generate']['browser']
    api_type = config['execute']['api_type']
    host_name = urllib.parse.urlparse(app_url).netloc.split(':')[0]
    test_directory = dir_util.get_test_directory(config, host_name)
    output_dir = dir_util.get_crawl_output_dir(test_directory, host_name)

    # cd to root dir of crawljax API tests or selenium API tests
    cur_dir = os.path.abspath(os.curdir)
    os.chdir(output_dir)
    if api_type == 'selenium':
        os.chdir(constants.SELENIUM_API_TEST_ROOT)

    tkltest_status('Executing {} API test suite with config: app={}, url="{}", browser={}'.format(
        api_type, app_name, app_url, browser
    ))
    try:
        command_util.run_command("mvn test --no-transfer-progress", verbose=config['general']['verbose'])
    except subprocess.CalledProcessError as e:
        tkltest_status('Error executing tests: {}\n{}'.format(e, e.stderr), error=True)
        os.chdir(cur_dir)
        browser_util.cleanup_browser_instances(browser)
        sys.exit(1)

    os.chdir(cur_dir)

    # cleanup after execution of crawljax API tests
    if api_type == 'crawljax':
        shutil.rmtree(os.path.join(output_dir, 'testOutput'))

    browser_util.cleanup_browser_instances(browser)
