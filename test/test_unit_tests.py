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
        # dict with apps params for test
        ant_test_apps = {  # todo move params to be taken from toml?
            'irs': {
                'standard_classpath': os.path.join('test', 'data', 'irs', 'irs_abspath_classpath.txt'),
                'config_file': os.path.join('test', 'data', 'irs', 'tkltest_config.toml'),
                'build_file': os.path.join('test', 'data', 'irs', 'monolith', 'build.xml'),
                'properties_file': '',
                'targets_to_test': ["compile-classpath-attribute", "compile-classpathref-attribute", "compile-classpath-element"]
            },
            '84_ifx-framework': {
                'standard_classpath': os.path.join('test', 'data', '84_ifx-framework', 'ifx-framework_abspath_classpath.txt'),
                'config_file': os.path.join('test', 'data', '84_ifx-framework', 'tkltest_config.toml'),
                'build_file': os.path.join('test', 'data', '84_ifx-framework', 'build.xml'),
                'properties_file': os.path.join('test', 'data', '84_ifx-framework', 'build.properties'),
                'targets_to_test': ["compile"]
            },
        }

        for app_name in ant_test_apps.keys():
            dir_util.cd_cli_dir()

            config = config_util.load_config(config_file=ant_test_apps[app_name]['config_file'])
            config['generate']['app_build_type'] = 'ant'
            config['generate']['app_build_settings_file'] = ant_test_apps[app_name]['properties_file']
            config['general']['app_classpath_file'] = ''
            config['generate']['app_build_config_file'] = ant_test_apps[app_name]['build_file']
            standard_classpath = os.path.abspath(ant_test_apps[app_name]['standard_classpath'])

            dir_util.cd_output_dir(app_name)

            for target_name in ant_test_apps[app_name]['targets_to_test']:
                config['generate']['app_build_target'] = target_name
                config_util.fix_config(config, 'generate')
                generated_classpath = config['general']['app_classpath_file']
                self.assertTrue(generated_classpath != '')
                self.assertTrue(os.path.isfile(generated_classpath))
                self.__assert_classpath(standard_classpath, generated_classpath)
                print('passed: target ' + target_name)

    def __assert_classpath(self, standard_classpath, generated_classpath):
        with open(standard_classpath, 'r') as file:
            lines_standard = file.read()
        line_list_standard = lines_standard.splitlines()

        with open(generated_classpath, 'r') as file:
            lines_generated = file.read()
        line_list_generated = lines_generated.splitlines()

        self.assertTrue(len(line_list_generated) == len(line_list_standard))

        for path in line_list_standard:
            self.assertTrue(path in line_list_generated)
            self.assertTrue(os.path.isfile(path))
