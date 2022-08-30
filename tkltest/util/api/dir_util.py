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


from tkltest.util import constants


def get_test_directory(config):
    test_directory = config['general']['test_directory']
    if not test_directory:
        test_directory = constants.TKLTEST_API_OUTPUT_DIR_PREFIX + config['general']['app_name']
    return test_directory
