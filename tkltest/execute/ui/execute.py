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

from tkltest.util.ui import config_options_ui
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
    test_directory = config_options_ui.get_test_directory(config, host_name)
    output_dir = config_options_ui.get_crawl_output_dir(test_directory, host_name)

    cur_dir = os.curdir
    os.chdir(output_dir)

    try:
        command_util.run_command("mvn test", verbose=config['general']['verbose'])
    except subprocess.CalledProcessError as e:
        tkltest_status('Error executing junit: {}\n{}'.format(e, e.stderr), error=True)
        os.chdir(cur_dir)
        sys.exit(1)

    os.chdir(cur_dir)

    #dir_util.cd_output_dir(config['general']['app_name'])
    #config_util.fix_config(config, args.command)
    #__execute_base(args, config)
    #if config['dev_tests']['compare_code_coverage']:
    #    __run_dev_tests(config)
    #    __compare_to_dev_tests_coverage(config)
    #dir_util.cd_cli_dir()