# ***************************************************************************
# Copyright IBM Corporation 2021
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
import shutil

# from . import constants
from ..constants import *


cli_dir = os.getcwd()
def cd_cli_dir():
    os.chdir(cli_dir)

def get_app_dir(app_name):
    app_dir = os.path.join(cli_dir, TKLTEST_UNIT_OUTPUT_DIR_PREFIX + app_name)
    if not os.path.isdir(app_dir):
        os.mkdir(app_dir)
    return app_dir

def cd_output_dir(app_name):
    # first we make sure that:
    # 1. the cli dir is set (by referring to it)
    # 2. we are at the cli dir
    if os.getcwd() != cli_dir:
        cd_cli_dir()
    output_dir = TKLTEST_UNIT_OUTPUT_DIR_PREFIX + app_name
    # creating output dir if not exist
    if not os.path.isdir(output_dir):
        os.mkdir(output_dir)

    # todo - consider resolve the following code. (BTW, soft links do not work on windows).
    # (currently, at the core, the locations of these jars are hard coded)
    if os.path.isdir(os.path.join(output_dir, "lib")):
        shutil.rmtree(os.path.join(output_dir, "lib"))
    os.makedirs(os.path.join(output_dir, "lib", "download"))
    shutil.copy(os.path.join("lib", "download", "replacecall-"+RANDOOP_VERSION+".jar"), os.path.join(output_dir, "lib", "download"))
    shutil.copy(os.path.join("lib", "download", "randoop-all-"+RANDOOP_VERSION+".jar"), os.path.join(output_dir, "lib", "download"))
    # end of todo
    # cd output dir
    os.chdir(output_dir)


def delete_app_output(app_name):
    for filename in os.listdir('.'):
        if filename.startswith(app_name):
            continue
        try:
            if os.path.isfile(filename) or os.path.islink(filename):
                os.unlink(filename)
            elif os.path.isdir(filename):
                shutil.rmtree(filename)
        except Exception as e:
            print('Failed to delete %s. Reason: %s' % (filename, e))