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
import glob

from tkltest.util import constants

def get_test_directory(config, host_name):
    test_directory = config['general']['test_directory']
    if not test_directory:
        test_directory = os.path.join(constants.TKLTEST_UI_OUTPUT_DIR_PREFIX +
                                      config['general']['app_name'],
                                      '{}_{}_{}mins'.format(config['general']['app_name'],
                                                            host_name,
                                                            config['generate']['time_limit']))
    return test_directory

def get_crawl_output_dir(test_directory, host_name):
    """Returns the crawl root directory for AUT for the latest run"""
    output_crawl_dirs = os.path.join(test_directory, host_name, 'crawl*')
    if not output_crawl_dirs:
        return None
    return sorted(glob.iglob(output_crawl_dirs), key=os.path.getctime, reverse=True)[0]
