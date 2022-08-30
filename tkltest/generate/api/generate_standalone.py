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
import subprocess
import sys
import yaml

from tkltest.util import command_util
from tkltest.util.api import dir_util
from tkltest.util.logging_util import tkltest_status


def generate_schemathesis(config, test_dir):
    """Generates test cases using Schemathesis.
    Stores the tests in a cassette (yaml), which can be replayed afterwards.

    Args:
        config (dict): loaded and validated config information
    """

    app_name = config['general']['app_name']
    base_url = config['general']['base_url']
    api_spec = config['general']['api_spec']
    cassette_path = dir_util.get_cassette_path(test_dir, app_name)
    st_command = "st run --base-url={} --cassette-path {} --checks all {}".format(base_url, cassette_path, api_spec)

    if os.path.exists(cassette_path):
        os.remove(cassette_path)
    os.makedirs(test_dir, exist_ok=True)  # Schemathesis expects the directory to exist

    tkltest_status('Creating Schemathesis API tests for application: ' + app_name)
    try:
        command_util.run_command(command=st_command, verbose=config['general']['verbose'])
    except subprocess.CalledProcessError as e:
        # Schemathesis returns exit code 1 if some of the generated tests failed, which sometimes is a normal behaviour for tkltest
        # thus the exception here is printed and ignored, and we check later for other errors
        tkltest_status('Warning: Schemathesis returned non-zero exit code: {}\n{}'.format(e, e.stderr))

    if not os.path.exists(cassette_path):
        tkltest_status('Schemathesis did not create a cassette: {}'.format(cassette_path), error=True)
        sys.exit(1)
    tkltest_status('Generated Schemathesis test suite written to {}'.format(cassette_path))

    with open(cassette_path, "r") as stream:
        try:
            data = yaml.safe_load(stream)
            if 'http_interactions' not in data or not data['http_interactions']:
                tkltest_status('Schemathesis did not generate any tests', error=True)
                sys.exit(1)
            passed_tests = len([1 for i in data['http_interactions'] if i['status'] == 'SUCCESS'])
            if not passed_tests:
                tkltest_status('All generated tests by Schemathesis failed', error=True)
                sys.exit(1)
            tkltest_status('{}/{} generated tests passed all checks'.format(passed_tests, len(data['http_interactions'])))
        except yaml.YAMLError as e:
            tkltest_status('Parsing YAML cassette failed: {}\n{}'.format(e, e.stderr), error=True)
            sys.exit(1)
