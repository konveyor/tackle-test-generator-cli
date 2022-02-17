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

import os
import sys
import subprocess
import urllib

from tkltest.util.ui import dir_util, browser_util
from tkltest.util import command_util
from tkltest.util.logging_util import tkltest_status

def process_execute_command(config):
    """Processes the ui execute command.

    Processes the execute command and executes test cases

    Args:
        args: command-line arguments
        config: loaded configuration options
    """

    host_name = urllib.parse.urlparse(config['general']['app_url']).netloc.split(':')[0]
    test_directory = dir_util.get_test_directory(config, host_name)
    output_dir = dir_util.get_crawl_output_dir(test_directory, host_name)

    cur_dir = os.curdir
    os.chdir(output_dir)

    try:
        command_util.run_command("mvn test", verbose=config['general']['verbose'])
    except subprocess.CalledProcessError as e:
        tkltest_status('Error executing tests: {}\n{}'.format(e, e.stderr), error=True)
        os.chdir(cur_dir)
        browser_util.cleanup_browser_instances(config['generate']['browser'])
        sys.exit(1)

    os.chdir(cur_dir)
    browser_util.cleanup_browser_instances(config['generate']['browser'])
