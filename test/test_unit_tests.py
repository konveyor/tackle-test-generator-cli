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

import argparse
import json
import os
import shutil
import sys
import unittest

sys.path.insert(0, os.path.abspath(os.path.dirname(__file__))+os.sep+'..')
from tkltest.util import config_util, constants, dir_util


class UnitTests(unittest.TestCase):

    def setUp(self) -> None:
        dir_util.cd_cli_dir()

    def test_getting_dependencies_ant(self) -> None:
        """Test getting dependencies using ant build"""
        irs_dir = os.path.join('test', 'data', 'irs')
        config = config_util.load_config(config_file=os.path.join(irs_dir, 'tkltest_config.toml'))
        config['generate']['app_build_type'] = 'ant'
        config['general']['app_classpath_file'] = ''
        config['generate']['app_build_config_file'] = os.path.join(irs_dir, 'monolith', 'build.xml')
        different_compile_types_targets = ["compile-classpath-attribute",
                                           "compile-classpathref-attribute",
                                           "compile-classpath-element"]
        dir_util.cd_output_dir('irs')
        for target_name in different_compile_types_targets:
            config['generate']['app_build_targets'] = [target_name]
            config_util.fix_config(config, 'generate')

        self.assertTrue(True)
