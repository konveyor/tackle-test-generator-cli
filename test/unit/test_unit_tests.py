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
from pathlib import PurePath
import sys
import unittest
import toml
import shutil
import copy
sys.path.insert(0, os.path.abspath(os.path.dirname(__file__))+os.sep+'..')
from tkltest.util import config_util, constants, command_util
from tkltest.util.unit import  dir_util


class UnitTests(unittest.TestCase):

    # dict with apps parameters for test
    # app_build_type, app_build_config_file are determined by the toml
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
            'is_user_defined_classpath': True,
            'targets_to_test_app_path': ['compile-classpath-attribute',
                                         'compile-classpathref-attribute',
                                         'compile-classpath-element',
                                         'compile-destdir-through-modulesourcepath',
                                         'compile-destdir-through-modulesourcepathref',
                                         'compile-destdir-through-src-elements',
                                         ],

        },
        '84_ifx-framework': {
            'standard_classpath': os.path.join('test', 'data', '84_ifx-framework', 'ifx-frameworkMonoClasspath.txt'),
            'config_file': os.path.join('test', 'data', '84_ifx-framework', 'tkltest_config.toml'),
            'build_file': os.path.join('test', 'data', '84_ifx-framework', 'build.xml'),
            'property_file': os.path.join('test', 'data', '84_ifx-framework', 'build.properties'),
            'targets_to_test_dependencies': ['compile', 'compile-antcall'],
            'is_user_defined_classpath': True,
            'targets_to_test_app_path': ['compile', 'compile-antcall'],
        },
    }

    def setUp(self) -> None:
        dir_util.cd_cli_dir()

    def test_getting_dependencies_ant(self) -> None:
        """Test getting dependencies using ant build file"""
        ant_test_apps = self.ant_test_apps

        for app_name in ant_test_apps.keys():
            dir_util.cd_cli_dir()

            config = config_util.load_config(config_file=ant_test_apps[app_name]['config_file'])
            config['generate']['app_build_type'] = 'ant'
            config['generate']['app_build_settings_files'] = [ant_test_apps[app_name]['property_file']]
            config['generate']['app_build_config_files'] = [ant_test_apps[app_name]['build_file']]
            standard_classpath = os.path.abspath(ant_test_apps[app_name]['standard_classpath'])
            dependencies_dir = app_name + constants.DEPENDENCIES_DIR_SUFFIX

            # every target is a different test case and is being compared to the standard classpath
            for target_name in ant_test_apps[app_name]['targets_to_test_dependencies']:
                config['generate']['app_build_target'] = target_name
                config['general']['app_classpath_file'] = ''
                config_util.resolve_classpath(config, 'generate')

                generated_classpath = config['general']['app_classpath_file']
                failed_assertion_message = 'failed for app = ' + app_name + ', target = ' + target_name
                self.assertTrue(generated_classpath != '', failed_assertion_message)
                self.assertTrue(os.path.isfile(generated_classpath), failed_assertion_message)
                self.__assert_classpath(standard_classpath,
                                        generated_classpath,
                                        os.path.join(dir_util.get_output_dir(app_name, config['general'].get('module_name', '')), dependencies_dir),
                                        failed_assertion_message,
                                        is_user_defined_classpath=ant_test_apps[app_name]['is_user_defined_classpath'])

    def test_getting_app_path_ant(self) -> None:
        """Test getting monolith app path using ant build file"""
        ant_test_apps = self.ant_test_apps
        for app_name in ant_test_apps.keys():
            dir_util.cd_cli_dir()
            failed_assertion_message = 'failed for app = ' + app_name

            config = config_util.load_config(config_file=ant_test_apps[app_name]['config_file'])
            config['generate']['app_build_type'] = 'ant'
            config['generate']['app_build_settings_files'] = [ant_test_apps[app_name]['property_file']]
            config['generate']['app_build_config_files'] = [ant_test_apps[app_name]['build_file']]
            monolith_app_path = config['general']['monolith_app_path']
            self.assertTrue(len(monolith_app_path) == 1, failed_assertion_message)
            if monolith_app_path[0] == '':
                continue
            monolith_app_path[0] = os.path.join(constants.TKLTEST_CLI_DIR, monolith_app_path[0])
            # every target is a different test case
            for target_name in ant_test_apps[app_name]['targets_to_test_app_path']:
                config['generate']['app_build_target'] = target_name
                config['general']['monolith_app_path'] = []
                config_util.resolve_app_path(config)

                failed_assertion_message = 'failed for app = ' + app_name + ', target = ' + target_name
                generated_monolith_app_path = config['general']['monolith_app_path']
                self.assertTrue(len(generated_monolith_app_path) == 1, failed_assertion_message)
                self.assertTrue(os.path.isdir(generated_monolith_app_path[0]), failed_assertion_message)
                self.assertTrue(os.path.samefile(generated_monolith_app_path[0], monolith_app_path[0]), failed_assertion_message)

    def test_getting_dependencies_maven(self) -> None:
        """Test getting dependencies using maven build file"""
        maven_test_apps = self.maven_test_apps
        for app_name in maven_test_apps.keys():
            dir_util.cd_cli_dir()

            config = config_util.load_config(config_file=maven_test_apps[app_name]['config_file'])
            standard_classpath = os.path.abspath(maven_test_apps[app_name]['standard_classpath'])
            config['general']['app_classpath_file'] = ''

            if maven_test_apps[app_name]['build_file_if_requires_build']:
                pom_location = maven_test_apps[app_name]['build_file_if_requires_build']
                if not os.path.isabs(pom_location):
                    pom_location = constants.TKLTEST_CLI_DIR + os.sep + pom_location
                build_command = 'mvn clean install -f ' + pom_location
                command_util.run_command(command=build_command, verbose=config['general']['verbose'])
            config_util.resolve_classpath(config, 'generate')

            generated_classpath = config['general']['app_classpath_file']
            jars_location = os.path.expanduser(os.path.join('~', '.m2', 'repository'))
            failed_assertion_message = 'failed for app = ' + app_name
            self.assertTrue(generated_classpath != '', failed_assertion_message)
            self.assertTrue(os.path.isfile(generated_classpath), failed_assertion_message)
            self.__assert_classpath(standard_classpath,
                                    generated_classpath,
                                    jars_location,
                                    failed_assertion_message,
                                    ordered_classpath=True)

    def test_getting_app_path_maven(self) -> None:
        """Test getting monolith app path using maven build file"""
        maven_test_apps = self.maven_test_apps
        for app_name in maven_test_apps.keys():
            dir_util.cd_cli_dir()
            failed_assertion_message = 'failed for app = ' + app_name

            config = config_util.load_config(config_file=maven_test_apps[app_name]['config_file'])
            monolith_app_path = config['general']['monolith_app_path']
            self.assertTrue(len(monolith_app_path) == 1, failed_assertion_message)
            if monolith_app_path[0] == '':
                continue
            config['general']['monolith_app_path'] = []
            monolith_app_path[0] = os.path.join(constants.TKLTEST_CLI_DIR, monolith_app_path[0])

            if maven_test_apps[app_name]['build_file_if_requires_build']:
                pom_location = maven_test_apps[app_name]['build_file_if_requires_build']
                if not os.path.isabs(pom_location):
                    pom_location = constants.TKLTEST_CLI_DIR + os.sep + pom_location
                build_command = 'mvn clean install -f ' + pom_location
                command_util.run_command(command=build_command, verbose=config['general']['verbose'])

            config_util.resolve_app_path(config)

            generated_monolith_app_path = config['general']['monolith_app_path']
            self.assertTrue(len(generated_monolith_app_path) == 1, failed_assertion_message)
            self.assertTrue(os.path.isdir(generated_monolith_app_path[0]), failed_assertion_message)
            self.assertTrue(os.path.samefile(generated_monolith_app_path[0], monolith_app_path[0]), failed_assertion_message)

    def test_getting_modules_maven(self) -> None:
        """Test getting list of modules using maven build file"""
        dir_util.cd_cli_dir()
        config = {}
        config['general'] = {}
        config['generate'] = {}
        app_name = 'migration-sample-app-master'
        config['general']['app_name'] = app_name
        config['general']['verbose'] = True

        pom_file1 = os.path.join('test', 'data', 'windup-sample', 'migration-sample-app-master', 'pom.xml')
        pom_file2 = os.path.join('test', 'data', 'windup-sample', 'migration-sample-app-master', 'simple-sample-web', 'pom.xml')
        config['generate']['app_build_config_files'] = [pom_file1, pom_file2]
        config['generate']['app_build_settings_files'] = ['', '']
        config['generate']['app_build_type'] = 'maven'

        modules_names = ['proprietary-stub',
                         'simple-sample-weblogic-services',
                         'simple-sample-weblogic-web']
        modules = config_util.get_modules_properties(config)
        self.__assert_modules_properties(modules_names, modules)


    def test_getting_modules_gradle(self) -> None:
        """Test getting list of modules using gradle build file"""
        dir_util.cd_cli_dir()
        config = {}
        config['general'] = {}
        config['generate'] = {}
        app_name = 'splitNjoin'
        config['general']['app_name'] = app_name
        config['general']['verbose'] = True

        build_file1 = os.path.join('test', 'data', 'splitNjoin', 'list', 'build.gradle')
        build_file2 = os.path.join('test', 'data', 'splitNjoin', 'app', 'build.gradle')
        settings_file = os.path.join('test', 'data', 'splitNjoin', 'settings.gradle')
        config['generate']['app_build_config_files'] = [build_file1, build_file2]
        config['generate']['app_build_settings_files'] = [settings_file, settings_file]
        config['generate']['app_build_type'] = 'gradle'

        modules_names = ['app',
                         'list',
                         'utilities']
        modules = config_util.get_modules_properties(config)
        self.__assert_modules_properties(modules_names, modules)

    def test_getting_modules_configs(self) -> None:
        """Test getting the configs for modules"""
        dir_util.cd_cli_dir()
        base_config = {}
        base_config['general'] = {}
        base_config['generate'] = {}
        app_name = 'splitNjoin'
        base_config['general']['app_name'] = app_name
        base_config['general']['verbose'] = True

        build_file_list = os.path.join('test', 'data', 'splitNjoin', 'list', 'build.gradle')
        build_file_app = os.path.join('test', 'data', 'splitNjoin', 'app', 'build.gradle')
        settings_file = os.path.join('test', 'data', 'splitNjoin', 'settings.gradle')
        base_config['generate']['app_build_type'] = 'gradle'
        base_config['general']['test_directory'] = 'SNJ_test_dir'
        base_config['general']['reports_path'] = 'SNJ_report_dir'

        base_config['general']['app_classpath_file'] = ''
        base_config['general']['monolith_app_path'] = ''

        base_config['generate']['app_build_config_files'] = [build_file_app]
        base_config['generate']['app_build_settings_files'] = [settings_file]

        '''
        here we check three cases, in all these cases, we should not split the config from the user to different modules: 
        1. we already have app_path from the user, so we do not look for modules
        2. we run execute - however, we did not split the user config during generate command 
        3. we have only one module
        '''
        # case 1
        configs_to_test = []
        config = copy.deepcopy(base_config)
        config['general']['monolith_app_path'] = 'dummy_path'
        configs_to_test.append((config, 'generate'))

        # case 2
        config = copy.deepcopy(base_config)
        configs_to_test.append((config, 'execute'))

        # case 3
        config = copy.deepcopy(base_config)
        config['generate']['app_build_config_files'] = [build_file_list]
        config['generate']['app_build_settings_files'] = []
        configs_to_test.append((config, 'generate'))

        shutil.rmtree(dir_util.get_app_output_dir(app_name))
        for config, command in configs_to_test:
            saved_config = copy.deepcopy(config)
            if not config['generate']['app_build_settings_files']:
                os.rename(settings_file, settings_file + '.bkp')
                resolved_configs = config_util.resolve_tkltest_configs(config, command)
                os.rename(settings_file + '.bkp', settings_file)
            else:
                resolved_configs = config_util.resolve_tkltest_configs(config, command)

        self.assertTrue(len(resolved_configs) == 1)
        resolved_config = resolved_configs[0]
        self.assertTrue('module_name' not in resolved_config['general'])
        self.assertTrue(os.path.isfile(resolved_config['general']['app_classpath_file']))
        saved_config['general']['app_classpath_file'] = resolved_config['general']['app_classpath_file']

        if not config['generate']['app_build_settings_files']:
            self.assertTrue(len(resolved_config['general']['monolith_app_path']) == 1)
        else:
            self.assertTrue(len(resolved_config['general']['monolith_app_path']) == 3)

        if saved_config['general']['monolith_app_path']:
            self.assertTrue(resolved_config['general']['monolith_app_path'] == saved_config['general']['monolith_app_path'])
        else:
            for path in resolved_config['general']['monolith_app_path']:
                self.assertTrue(os.path.isdir(path))
            saved_config['general']['monolith_app_path'] = resolved_config['general']['monolith_app_path']

        config_util.fix_relative_paths(saved_config)
        self.assertTrue(resolved_configs == [saved_config])

        '''
        here are two more cases: now we check that modules configs are generate during the generate command.
        and that the same configs are loaded during the execute command
        '''
        shutil.rmtree(dir_util.get_app_output_dir(app_name))
        modules_names = ['app', 'list', 'utilities']
        config = copy.deepcopy(base_config)
        config['generate']['app_build_config_files'] = [build_file_list, build_file_app]
        config['generate']['app_build_settings_files'] = [settings_file, settings_file]
        config['general']['app_classpath_file'] = ''
        config['general']['monolith_app_path'] = ''

        configs_generate = config_util.resolve_tkltest_configs(config, 'generate')

        config = copy.deepcopy(base_config)
        config['generate']['app_build_config_files'] = [build_file_list, build_file_app]
        config['generate']['app_build_settings_files'] = [settings_file, settings_file]
        config['general']['app_classpath_file'] = ''
        config['general']['monolith_app_path'] = ''
        configs_execute = config_util.resolve_tkltest_configs(config, 'execute')

        self.assertTrue(len(configs_generate) == len(modules_names))
        self.assertTrue(len(configs_execute) == len(modules_names))
        self.assertTrue(os.path.isdir(dir_util.get_app_output_dir(app_name)))

        for modules_name in modules_names:
            outdir = dir_util.get_output_dir(app_name, modules_name)
            self.assertTrue(os.path.isdir(outdir))
            toml_file = os.path.join(outdir, app_name + '_' + modules_name + '_generated_tkltest_config.toml')
            self.assertTrue(os.path.isfile(toml_file))
            module_config = toml.load(toml_file)
            self.assertTrue(len(module_config['general']['monolith_app_path']) == 1)
            self.assertTrue(os.path.isdir(module_config['general']['monolith_app_path'][0]))
            self.assertTrue(module_config['general']['module_name'] == modules_name)
            self.assertTrue(len(module_config['generate']['app_build_config_files']) == 1)
            self.assertTrue(os.path.isfile(module_config['generate']['app_build_config_files'][0]))
            self.assertTrue(len(module_config['generate']['app_build_settings_files']) == 1)
            self.assertTrue(os.path.isfile(module_config['generate']['app_build_settings_files'][0]))
            self.assertTrue(os.path.isfile(module_config['general']['app_classpath_file']))

            config_util.fix_relative_paths(module_config)
            self.assertTrue(module_config in configs_generate)
            self.assertTrue(module_config in configs_execute)



    def test_getting_dependencies_gradle(self) -> None:
        """Test getting dependencies using gradle build file"""
        # dict with apps parameters for test
        # app_build_type, app_build_config_file, app_build_settings_file are determined by the toml
        gradle_test_apps = {
            'splitNjoin': {
                'standard_classpath': os.path.join('test', 'data', 'splitNjoin', 'splitNjoinMonoClasspath.txt'),
                'config_file': os.path.join('test', 'data', 'splitNjoin', 'tkltest_config.toml'),
            },
        }

        for app_name in gradle_test_apps.keys():
            dir_util.cd_cli_dir()

            config = config_util.load_config(config_file=gradle_test_apps[app_name]['config_file'])
            standard_classpath = os.path.abspath(gradle_test_apps[app_name]['standard_classpath'])
            dependencies_dir = app_name + constants.DEPENDENCIES_DIR_SUFFIX
            config['general']['app_classpath_file'] = ''

            config_util.resolve_app_path(config)
            config_util.resolve_classpath(config, 'generate')

            generated_classpath = config['general']['app_classpath_file']
            failed_assertion_message = 'failed for app = ' + app_name
            self.assertTrue(generated_classpath != '', failed_assertion_message)
            self.assertTrue(os.path.isfile(generated_classpath), failed_assertion_message)
            self.__assert_classpath(standard_classpath,
                                    generated_classpath,
                                    os.path.join(dir_util.get_output_dir(app_name, config['general'].get('module_name', '')), dependencies_dir),
                                    failed_assertion_message)

    def __assert_classpath(self, standard_classpath, generated_classpath, std_classpath_prefix, message, ordered_classpath=False, is_user_defined_classpath=False):
        """
        :param standard_classpath: Path to the standard classpath for comparison.
        :param generated_classpath: Path to the generated classpath, containing absolute paths of the dependency jars.
        :param std_classpath_prefix: Prefix to add to every path in the standard classpath.
        :param message: An informative error message to print in case one of the assertions fails.
        """
        std_classpath_prefix = PurePath(std_classpath_prefix).as_posix()
        with open(standard_classpath, 'r') as file:
            lines_standard = file.read().splitlines()
        if is_user_defined_classpath:  # for ant apps
            lines_standard = [os.path.basename(line) for line in lines_standard]
        # Paths inside standard_classpath must be in unix format for the test.
        lines_standard = [std_classpath_prefix + '/' + line for line in lines_standard]

        with open(generated_classpath, 'r') as file:
            lines_generated = file.read().splitlines()
        lines_generated = [PurePath(line).as_posix() for line in lines_generated]

        self.assertTrue(len(lines_generated) == len(lines_standard), message)

        for i, jar_path in enumerate(lines_standard):
            extended_message = message + " , path = " + jar_path
            if ordered_classpath:
                self.assertTrue(lines_generated[i] == jar_path, extended_message)
            else:
                self.assertTrue(jar_path in lines_generated, extended_message)
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

