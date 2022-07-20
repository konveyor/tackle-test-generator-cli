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
from pathlib import Path, PurePath
import sys
import unittest
import toml
import shutil
import copy
sys.path.insert(0, os.path.abspath(os.path.dirname(__file__))+os.sep+'..')
from tkltest.util import config_util, constants, command_util
from tkltest.util.unit import dir_util, build_util
from tkltest.generate.unit import generate, augment


class UnitTests(unittest.TestCase):

    # dict with apps parameters for test
    # build_type, app_build_files are determined by the toml
    maven_test_apps = {
        '14_spark': {
            'standard_classpath': os.path.join('test', 'data', '14_spark', '14_sparkMonoClasspath.txt'),
            'config_file': os.path.join('test', 'data', '14_spark', 'tkltest_config.toml'),
            'build_file_if_requires_build': '',
        },
        '3_scribe-java': {
            'standard_classpath': os.path.join('test', 'data', '3_scribe-java', '3_scribe-javaMonoClasspath.txt'),
            'config_file': os.path.join('test', 'data', '3_scribe-java', 'tkltest_config.toml'),
            'build_file_if_requires_build': '',
        },
        'windup-sample-web': {
            'standard_classpath': os.path.join('test', 'data', 'windup-sample', 'windup-sample-webMonoClasspath.txt'),
            'config_file': os.path.join('test', 'data', 'windup-sample', 'tkltest_config_web.toml'),
            'build_file_if_requires_build': os.path.join('test', 'data', 'windup-sample', 'migration-sample-app-master',
                                                         'pom.xml'),
            'ctd_tests': os.path.join('test', 'data', 'windup-sample', 'windup-sample-web-ctd-amplified-tests')
        }
    }

    ant_test_apps = {
        'irs': {
            'standard_classpath': os.path.join('test', 'data', 'irs', 'irsMonoClasspath.txt'),
            'config_file': os.path.join('test', 'data', 'irs', 'tkltest_config.toml'),
            'build_file': os.path.join('test', 'data', 'irs', 'monolith', 'for_tests_build.xml'),
            'property_file': '',
            'targets_to_test_dependencies': ['compile-classpath-attribute',
                                             'compile-classpathref-attribute',
                                             'compile-classpath-element'],
            'targets_to_test_app_path': ['compile-classpath-attribute',
                                         'compile-classpathref-attribute',
                                         'compile-classpath-element',
                                         'compile-destdir-through-modulesourcepath',
                                         'compile-destdir-through-modulesourcepathref',
                                         'compile-destdir-through-src-elements'],
            'covered_classes': ['irs.IRS', 'irs.Employer', 'irs.Salary']

        },
        '84_ifx-framework': {
            'standard_classpath': os.path.join('test', 'data', '84_ifx-framework', 'ifx-frameworkMonoClasspath.txt'),
            'config_file': os.path.join('test', 'data', '84_ifx-framework', 'tkltest_config.toml'),
            'build_file': os.path.join('test', 'data', '84_ifx-framework', 'build.xml'),
            'property_file': os.path.join('test', 'data', '84_ifx-framework', 'build.properties'),
            'targets_to_test_dependencies': ['compile', 'compile-antcall'],
            'targets_to_test_app_path': ['compile', 'compile-antcall'],
        },
    }

    gradle_test_apps = {
        'splitNjoin': {
            'standard_classpath': os.path.join('test', 'data', 'splitNjoin', 'splitNjoinMonoClasspath.txt'),
            'config_file': os.path.join('test', 'data', 'splitNjoin', 'tkltest_config.toml'),
            'ctd_tests': os.path.join('test', 'data', 'splitNjoin', 'splitNjoin-ctd-amplified-tests'),
            'build_file_without_tests': os.path.join('test', 'data', 'splitNjoin', 'app', 'build_without_tests.gradle')
        },
    }

    bad_path_test_apps = {
        'failing': {
            'config_file': os.path.join('test', 'data', 'failingApp', 'tkltest_config.toml'),
            'basic_blocks': os.path.join('test', 'data', 'failingApp', 'basic_blocks'),
        },
    }
    def setUp(self) -> None:
        dir_util.cd_cli_dir()
        self.begin_dir_content = os.listdir(os.getcwd())


    def test_augment_on_empty_test_suite(self) -> None:
        """Test that we can augment on an empty ctd test suite"""
        app_name = 'failing'
        test_app = self.bad_path_test_apps[app_name]
        dir_util.cd_cli_dir()
        config = config_util.load_config(config_file=test_app['config_file'])
        output_dir = dir_util.get_output_dir(app_name, '')
        shutil.rmtree(output_dir)
        shutil.copytree(os.path.join(constants.TKLTEST_CLI_DIR, test_app['basic_blocks']), output_dir)
        config_util.fix_relative_paths(config)
        dir_util.cd_output_dir(app_name, '')
        if not config['general']['test_directory']:
            config['general']['test_directory'] = app_name + '-test_directory'
        if not config['general']['reports_path']:
            config['general']['reports_path'] = app_name + '-reports_dir'
        shutil.rmtree(config['general']['test_directory'], ignore_errors=True)
        os.makedirs(config['general']['test_directory'])
        monolithic_dir = os.path.join(config['general']['test_directory'], 'monolithic')
        build_file = build_util.generate_build_xml(
            app_name=app_name,
            build_type='gradle',
            monolith_app_path=config['general']['monolith_app_path'],
            app_classpath=build_util.get_build_classpath(config),
            test_root_dir=config['general']['test_directory'],
            test_dirs=[monolithic_dir],
            # partitions_file=None,
            target_class_list=[],
            main_reports_dir=config['general']['reports_path'],
            app_packages=[],  # for coverage-based augmentation
            collect_codecoverage=True,  # for coverage-based augmentation
            offline_instrumentation=True,
            output_dir=output_dir
        )
        config['general']['build_type'] = 'gradle'
        test_files = [None, None]
        for use_for_augmentation in [False]:
            config['dev_tests']['use_for_augmentation'] = use_for_augmentation
            shutil.rmtree(monolithic_dir, ignore_errors=True)
            os.makedirs(monolithic_dir)
            shutil.rmtree(config['general']['reports_path'], ignore_errors=True)
            augment.augment_with_code_coverage(config, build_file,
                                               config['general']['build_type'],
                                               config['general']['test_directory'],
                                               config['general']['reports_path'])
            test_files[use_for_augmentation] = list(os.path.basename(path) for path in Path(config['general']['test_directory']).glob('**/*.java'))
            self.assertTrue(test_files[use_for_augmentation])

    def __assert_classpath(self, standard_classpath, generated_classpath, build_type, message):
        """
        :param standard_classpath: Path to the standard classpath for comparison.
        :param generated_classpath: Path to the generated classpath, containing absolute paths of the dependency jars.
        :param build_type: ant, maven or gradle
        :param message: An informative error message to print in case one of the assertions fails.
        """
        self.assertTrue(build_type in ['ant', 'maven', 'gradle'])

        with open(standard_classpath, 'r') as file:
            lines_standard = file.read().splitlines()

        with open(generated_classpath, 'r') as file:
            lines_generated = file.read().splitlines()
        lines_generated = [PurePath(line).as_posix() for line in lines_generated]

        self.assertEqual(len(lines_generated), len(lines_standard), message)

        for i, jar_path in enumerate(lines_generated):
            extended_message = message + " , path = " + jar_path
            if build_type == 'ant':
                self.assertTrue(os.path.samefile(lines_standard[i], jar_path), extended_message)
            elif build_type == 'maven':
                std_classpath_prefix = PurePath(os.path.expanduser(os.path.join('~', '.m2', 'repository'))).as_posix()
                self.assertTrue(std_classpath_prefix + '/' + lines_standard[i] == jar_path, extended_message)
            else:
                self.assertTrue(os.path.basename(lines_standard[i]) == os.path.basename(jar_path), extended_message)
            self.assertTrue(os.path.isfile(jar_path), extended_message)

    def __assert_modules_properties(self, modules_names, modules):
        module_keys = ['name', 'directory', 'build_file', 'app_path', 'user_build_file', 'user_settings_file', 'classpath']
        self.assertTrue(len(modules) == len(modules_names))
        for module in modules:
            self.assertTrue(module['name'] in modules_names)
            self.assertTrue(len(module.keys()) == len(module_keys) or len(module.keys()) == len(module_keys) - 1 )
            for key in module.keys():
                self.assertTrue(key in module_keys)
            self.assertTrue(os.path.isdir(module['directory']))
            self.assertTrue(os.path.isfile(module['build_file']))
            for path in module['app_path']:
                self.assertTrue(os.path.isdir(path))
            for path in module['classpath']:
                self.assertTrue(os.path.isfile(path))

    def __assert_no_artifact_at_cli(self, app_names):
        '''
        Here we check that we do not leave anything in the cli directory
        '''
        current_dir_content = os.listdir(os.getcwd())
        alowed_artifacts = []
        for app_name in app_names:
            alowed_artifacts.append('tkltest-output-unit-' + app_name)
        self.assertFalse((set(current_dir_content) ^ set(self.begin_dir_content)) - set(alowed_artifacts))
