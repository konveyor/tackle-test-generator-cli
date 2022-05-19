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
from ..constants import *


def cd_cli_dir():
    os.chdir(TKLTEST_CLI_DIR)


def get_app_output_dir(app_name):
    app_dir = os.path.join(TKLTEST_CLI_DIR, TKLTEST_UNIT_OUTPUT_DIR_PREFIX + app_name)
    if not os.path.isdir(app_dir):
        os.mkdir(app_dir)
    return app_dir


def get_output_dir(app_name, module_name):
    output_dir = get_app_output_dir(app_name)
    if module_name:
        output_dir = os.path.join(output_dir, module_name)
    # creating output dir if not exist
    if not os.path.isdir(output_dir):
        os.mkdir(output_dir)

    # todo - consider resolve the following code. (BTW, soft links do not work on windows).
    # (currently, at the core, the locations of these jars are hard coded)
    if not os.path.isdir(os.path.join(output_dir, "lib")):
        os.makedirs(os.path.join(output_dir, "lib", "download"))
    shutil.copy(os.path.join(TKLTEST_LIB_DOWNLOAD_DIR, "replacecall-"+RANDOOP_REPLACECALL_VERSION+".jar"), os.path.join(output_dir, "lib", "download"))
    shutil.copy(os.path.join(TKLTEST_LIB_DOWNLOAD_DIR, "randoop-"+RANDOOP_VERSION+".jar"), os.path.join(output_dir, "lib", "download"))
    # end of todo
    return output_dir


def cd_output_dir(app_name, module_name):
    output_dir = get_output_dir(app_name, module_name)
    os.chdir(output_dir)
    return output_dir


def cd_app_output_dir(app_name):
    output_dir = get_app_output_dir(app_name)
    os.chdir(output_dir)
    return output_dir


def delete_app_output(app_name):
    for filename in os.listdir('.'):
        if filename.startswith(app_name) or filename.startswith('tkltest'):
            continue
        try:
            if os.path.isfile(filename) or os.path.islink(filename):
                os.unlink(filename)
            elif os.path.isdir(filename):
                shutil.rmtree(filename)
        except Exception as e:
            print('Failed to delete %s. Reason: %s' % (filename, e))