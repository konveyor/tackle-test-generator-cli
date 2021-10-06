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

from . import constants, config_util
from .constants import *

def __get_output_dir(app_name):
    return os.path.join(TKLTEST_OUTPUT_DIR_PREFIX + app_name)

def clear_output_dir(app_name):
    output_dir = __get_output_dir(app_name)
    if os.path.isdir(output_dir):
        shutil.rmtree(output_dir)


def __create_output_dir(app_name):
    output_dir = __get_output_dir(app_name)
    if not os.path.isdir(output_dir):
        os.mkdir(output_dir)

    # todo - consider resolve the following code. BTW, soft links do not work on windows.
    # currently, at the core, the location is hard coded
    if os.path.isdir(os.path.join(output_dir, "lib")):
        shutil.rmtree(os.path.join(output_dir, "lib"))
    os.mkdir(os.path.join(output_dir, "lib"))
    os.mkdir(os.path.join(output_dir, "lib", "download"))
    shutil.copy(os.path.join("lib", "download", "replacecall-4.2.6.jar"), os.path.join(output_dir, "lib", "download"))
    shutil.copy(os.path.join("lib", "download", "randoop-all-4.2.6.jar"), os.path.join(output_dir, "lib", "download"))
    # end of todo


def cd_output_dir(app_name):
    output_dir = __get_output_dir(app_name)
    os.chdir(output_dir)


def ch_cli_dir():
    os.chdir(ch_cli_dir.cli_dir)
ch_cli_dir.cli_dir = os.getcwd() #todo - move to main


def prepare_to_run(tkltest_config): #todo - change the name
    app_name = tkltest_config['general']['app_name']
    __create_output_dir(app_name)
    cd_output_dir(app_name)
    config_util.fix_relative_paths(tkltest_config)

def delete_app_output(app_name):
    for filename in os.listdir('.'):
        if filename.startswith(app_name):
            continue
        if filename.startswith('evosuite'): #todo - try to reconstract
            continue
        try:
            if os.path.isfile(filename) or os.path.islink(filename):
                os.unlink(filename)
            elif os.path.isdir(filename):
                shutil.rmtree(filename)
        except Exception as e:
            print('Failed to delete %s. Reason: %s' % (filename, e))