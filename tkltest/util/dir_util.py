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




def __create_output_dir(app_name):
    if not os.path.isdir(TKLTEST_OUTPUT_DIR):
        os.mkdir(TKLTEST_OUTPUT_DIR)
    output_dir = os.path.join(TKLTEST_OUTPUT_DIR, app_name)
    if not os.path.isdir(output_dir):
        os.mkdir(output_dir)

    # todo - resolve the following code. BTW, soft links do not work on windows
    if os.path.isdir(os.path.join(output_dir, "lib")):
        shutil.rmtree(os.path.join(output_dir, "lib"))
    os.mkdir(os.path.join(output_dir, "lib"))
    os.mkdir(os.path.join(output_dir, "lib", "download"))
    shutil.copy(os.path.join("lib", "download", "replacecall-4.2.6.jar"), os.path.join(output_dir, "lib", "download"))
    shutil.copy(os.path.join("lib", "download", "randoop-all-4.2.6.jar"), os.path.join(output_dir, "lib", "download"))
    # end of todo


def cd_output_dir(app_name):
    output_dir = os.path.join(TKLTEST_OUTPUT_DIR, app_name)
    os.chdir(output_dir)


def return_to_cli_dir():
    os.chdir(TKLTEST_CLI_RELATIVE_DIR)


def prepare_to_run(tkltest_config):
    app_name = tkltest_config['general']['app_name']
    __create_output_dir(app_name)
    cd_output_dir(app_name)
    config_util.fix_relative_paths(tkltest_config)

def clean_app_output():
    aa=3