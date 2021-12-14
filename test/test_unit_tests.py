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
        # dict with apps parameters for test
        ant_test_apps = {
            'irs': {
                'standard_classpath': os.path.join('test', 'data', 'irs', 'irsMonoClasspath.txt'),
                'config_file': os.path.join('test', 'data', 'irs', 'tkltest_config.toml'),
                'build_file': os.path.join('test', 'data', 'irs', 'monolith', 'build.xml'),
                'property_file': '',
                'targets_to_test': ['compile-classpath-attribute', 'compile-classpathref-attribute', 'compile-classpath-element'],
            },
            '84_ifx-framework': {
                'standard_classpath': os.path.join('test', 'data', '84_ifx-framework', 'ifx-frameworkMonoClasspath.txt'),
                'config_file': os.path.join('test', 'data', '84_ifx-framework', 'tkltest_config.toml'),
                'build_file': os.path.join('test', 'data', '84_ifx-framework', 'build.xml'),
                'property_file': os.path.join('test', 'data', '84_ifx-framework', 'build.properties'),
                'targets_to_test': ['compile', 'compile-antcall'],
            },
        }

        for app_name in ant_test_apps.keys():
            dir_util.cd_cli_dir()

            config = config_util.load_config(config_file=ant_test_apps[app_name]['config_file'])
            config['generate']['app_build_type'] = 'ant'
            config['generate']['app_build_settings_file'] = ant_test_apps[app_name]['property_file']
            config['generate']['app_build_config_file'] = ant_test_apps[app_name]['build_file']
            standard_classpath = os.path.abspath(ant_test_apps[app_name]['standard_classpath'])
            dependencies_dir = app_name + constants.DEPENDENCIES_DIR_SUFFIX

            dir_util.cd_output_dir(app_name)

            # every target is a different test case and is being compared to the standard classpath
            for target_name in ant_test_apps[app_name]['targets_to_test']:
                config['generate']['app_build_target'] = target_name
                config['general']['app_classpath_file'] = ''
                config_util.fix_config(config, 'generate')

                generated_classpath = config['general']['app_classpath_file']
                failed_assertion_message = 'failed for app = ' + app_name + ', target = ' + target_name
                self.assertTrue(generated_classpath != '', failed_assertion_message)
                self.assertTrue(os.path.isfile(generated_classpath), failed_assertion_message)
                self.__assert_classpath(standard_classpath,
                                        generated_classpath,
                                        os.path.join(os.getcwd(), dependencies_dir),
                                        failed_assertion_message)

    def test_getting_dependencies_maven(self) -> None:
        """Test getting dependencies using maven build file"""
        # dict with apps parameters for test
        maven_test_apps = {
            '14_spark': {
                'standard_classpath': os.path.join('test', 'data', '14_spark', 'sparkMonoClasspath.txt'),
                'config_file': os.path.join('test', 'data', '14_spark', 'tkltest_config.toml'),
                'build_file': os.path.join('test', 'data', '14_spark', 'pom.xml'),
            },
            '3_scribe-java': {
                'standard_classpath': os.path.join('test', 'data', '3_scribe-java', '3_scribe-javaMonoClasspath.txt'),
                'config_file': os.path.join('test', 'data', '3_scribe-java', 'tkltest_config.toml'),
                'build_file': os.path.join('test', 'data', '3_scribe-java', 'pom.xml'),
            },
        }

        for app_name in maven_test_apps.keys():
            dir_util.cd_cli_dir()

            config = config_util.load_config(config_file=maven_test_apps[app_name]['config_file'])
            config['generate']['app_build_type'] = 'maven'
            config['generate']['app_build_config_file'] = maven_test_apps[app_name]['build_file']
            standard_classpath = os.path.abspath(maven_test_apps[app_name]['standard_classpath'])
            dependencies_dir = app_name + constants.DEPENDENCIES_DIR_SUFFIX
            config['general']['app_classpath_file'] = ''

            dir_util.cd_output_dir(app_name)

            config_util.fix_config(config, 'generate')

            generated_classpath = config['general']['app_classpath_file']
            failed_assertion_message = 'failed for app = ' + app_name
            self.assertTrue(generated_classpath != '', failed_assertion_message)
            self.assertTrue(os.path.isfile(generated_classpath), failed_assertion_message)
            self.__assert_classpath(standard_classpath,
                                    generated_classpath,
                                    os.path.join(os.getcwd(), dependencies_dir),
                                    failed_assertion_message)

    def __assert_classpath(self, standard_classpath, generated_classpath, std_classpath_prefix, message):
        """
        :param standard_classpath: Path to the standard classpath for comparison.
        :param generated_classpath: Path to the generated classpath, containing absolute paths of the dependency jars.
        :param std_classpath_prefix: Prefix to add to every path in the standard classpath.
        :param message: An informative error message to print in case one of the assertions fails.
        """
        with open(standard_classpath, 'r') as file:
            lines_standard = file.read()
        jar_names = [os.path.basename(line) for line in lines_standard.splitlines()]
        line_list_standard = [os.path.join(std_classpath_prefix, jar_name) for jar_name in jar_names]

        with open(generated_classpath, 'r') as file:
            lines_generated = file.read()
        line_list_generated = lines_generated.splitlines()

        self.assertTrue(len(line_list_generated) == len(line_list_standard), message)

        for path in line_list_standard:
            self.assertTrue(path in line_list_generated, message)
            self.assertTrue(os.path.isfile(path), message)
