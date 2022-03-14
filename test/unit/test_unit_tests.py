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
    # app_build_type, app_build_files are determined by the toml
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

    def test_getting_dependencies_ant(self) -> None:
        """Test getting dependencies using ant build file"""
        ant_test_apps = self.ant_test_apps

        for app_name in ant_test_apps.keys():
            dir_util.cd_cli_dir()

            config = config_util.load_config(config_file=ant_test_apps[app_name]['config_file'])
            config['generate']['app_build_type'] = 'ant'
            config['generate']['app_build_settings_files'] = [ant_test_apps[app_name]['property_file']]
            config['generate']['app_build_files'] = [ant_test_apps[app_name]['build_file']]
            standard_classpath = os.path.abspath(ant_test_apps[app_name]['standard_classpath'])

            # every target is a different test case and is being compared to the standard classpath
            for target_name in ant_test_apps[app_name]['targets_to_test_dependencies']:
                config['generate']['app_build_target'] = target_name
                config['general']['app_classpath_file'] = ''
                config_util.resolve_classpath(config, 'generate')

                generated_classpath = config['general']['app_classpath_file']
                failed_assertion_message = 'failed for app = ' + app_name + ', target = ' + target_name
                self.assertTrue(generated_classpath != '', failed_assertion_message)
                self.assertTrue(os.path.isfile(generated_classpath), failed_assertion_message)
                self.__assert_classpath(standard_classpath=standard_classpath,
                                        generated_classpath=generated_classpath,
                                        build_type='ant',
                                        message=failed_assertion_message)
                self.__assert_no_artifact_at_cli(ant_test_apps.keys())

    def test_getting_app_path_ant(self) -> None:
        """Test getting monolith app path using ant build file"""
        ant_test_apps = self.ant_test_apps
        for app_name in ant_test_apps.keys():
            dir_util.cd_cli_dir()
            failed_assertion_message = 'failed for app = ' + app_name

            config = config_util.load_config(config_file=ant_test_apps[app_name]['config_file'])
            config['generate']['app_build_type'] = 'ant'
            config['generate']['app_build_settings_files'] = [ant_test_apps[app_name]['property_file']]
            config['generate']['app_build_files'] = [ant_test_apps[app_name]['build_file']]
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
            self.__assert_no_artifact_at_cli(ant_test_apps.keys())

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
            failed_assertion_message = 'failed for app = ' + app_name
            self.assertTrue(generated_classpath != '', failed_assertion_message)
            self.assertTrue(os.path.isfile(generated_classpath), failed_assertion_message)
            self.__assert_classpath(standard_classpath=standard_classpath,
                                    generated_classpath=generated_classpath,
                                    build_type='maven',
                                    message=failed_assertion_message)
        self.__assert_no_artifact_at_cli(maven_test_apps.keys())

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
        self.__assert_no_artifact_at_cli(maven_test_apps.keys())

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
        config['generate']['app_build_files'] = [pom_file1, pom_file2]
        config['generate']['app_build_settings_files'] = ['', '']
        config['generate']['app_build_type'] = 'maven'

        modules_names = ['proprietary-stub',
                         'simple-sample-weblogic-services',
                         'simple-sample-weblogic-web']
        modules = config_util.get_modules_properties(config)
        self.__assert_modules_properties(modules_names, modules)
        self.__assert_no_artifact_at_cli([app_name])

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
        config['generate']['app_build_files'] = [build_file1, build_file2]
        config['generate']['app_build_settings_files'] = [settings_file, settings_file]
        config['generate']['app_build_type'] = 'gradle'

        modules_names = ['app',
                         'list',
                         'utilities']
        modules = config_util.get_modules_properties(config)
        self.__assert_modules_properties(modules_names, modules)
        self.__assert_no_artifact_at_cli([app_name])

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

        base_config['generate']['app_build_files'] = [build_file_app]
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
        config['generate']['app_build_files'] = [build_file_list]
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
        config['generate']['app_build_files'] = [build_file_list, build_file_app]
        config['generate']['app_build_settings_files'] = [settings_file, settings_file]
        config['general']['app_classpath_file'] = ''
        config['general']['monolith_app_path'] = ''

        configs_generate = config_util.resolve_tkltest_configs(config, 'generate')

        config = copy.deepcopy(base_config)
        config['generate']['app_build_files'] = [build_file_list, build_file_app]
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
            module_config['general']['config_file_path'] = toml_file
            self.assertTrue(len(module_config['general']['monolith_app_path']) == 1)
            self.assertTrue(os.path.isdir(module_config['general']['monolith_app_path'][0]))
            self.assertTrue(module_config['general']['module_name'] == modules_name)
            self.assertTrue(len(module_config['generate']['app_build_files']) == 1)
            self.assertTrue(os.path.isfile(module_config['generate']['app_build_files'][0]))
            self.assertTrue(len(module_config['generate']['app_build_settings_files']) == 1)
            self.assertTrue(os.path.isfile(module_config['generate']['app_build_settings_files'][0]))
            self.assertTrue(os.path.isfile(module_config['general']['app_classpath_file']))

            config_util.fix_relative_paths(module_config)
            self.assertTrue(module_config in configs_generate)
            self.assertTrue(module_config in configs_execute)
        self.__assert_no_artifact_at_cli([app_name])

    def test_getting_dependencies_gradle(self) -> None:
        """Test getting dependencies using gradle build file"""
        # dict with apps parameters for test
        # app_build_type, app_build_files, app_build_settings_file are determined by the toml
        for app_name in self.gradle_test_apps.keys():
            dir_util.cd_cli_dir()

            config = config_util.load_config(config_file=self.gradle_test_apps[app_name]['config_file'])
            standard_classpath = os.path.abspath(self.gradle_test_apps[app_name]['standard_classpath'])
            config['general']['app_classpath_file'] = ''

            config_util.resolve_app_path(config)
            config_util.resolve_classpath(config, 'generate')

            generated_classpath = config['general']['app_classpath_file']
            failed_assertion_message = 'failed for app = ' + app_name
            self.assertTrue(generated_classpath != '', failed_assertion_message)
            self.assertTrue(os.path.isfile(generated_classpath), failed_assertion_message)
            self.__assert_classpath(standard_classpath=standard_classpath,
                                    generated_classpath=generated_classpath,
                                    build_type='gradle',
                                    message=failed_assertion_message)
            self.__assert_no_artifact_at_cli(self.gradle_test_apps.keys())

    def test_exclude_classes_covered_by_dev_test(self) -> None:
        """Test to exclude classes that was covered by the dev test suite"""
        test_apps = self.ant_test_apps

        for app_name in test_apps.keys():
            if 'covered_classes' not in test_apps[app_name].keys():
                continue
            dir_util.cd_cli_dir()
            config = config_util.load_config(config_file=test_apps[app_name]['config_file'])
            generate.exclude_classes_covered_by_dev_test(config, dir_util.get_app_output_dir(app_name))
            self.assertTrue(not config['generate']['excluded_class_list'])

            config['dev_tests']['coverage_threshold'] = 96
            generate.exclude_classes_covered_by_dev_test(config, dir_util.get_app_output_dir(app_name))
            self.assertTrue(set(config['generate']['excluded_class_list']) == set(test_apps[app_name]['covered_classes']))

        self.__assert_no_artifact_at_cli(test_apps.keys())

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
        ant_build_file, maven_build_file, gradle_build_file = build_util.generate_build_xml(
            app_name=app_name,
            monolith_app_path=config['general']['monolith_app_path'],
            app_classpath=build_util.get_build_classpath(config),
            test_root_dir=config['general']['test_directory'],
            test_dirs=[monolithic_dir],
            partitions_file=None,
            target_class_list=[],
            main_reports_dir=config['general']['reports_path'],
            app_packages=[],  # for coverage-based augmentation
            collect_codecoverage=True,  # for coverage-based augmentation
            offline_instrumentation=True,
            output_dir=output_dir
        )
        config['general']['build_type'] = 'gradle'
        test_files = [None, None]
        for use_for_augmentation in [False, True]:
            config['dev_tests']['use_for_augmentation'] = use_for_augmentation
            shutil.rmtree(monolithic_dir, ignore_errors=True)
            os.makedirs(monolithic_dir)
            shutil.rmtree(config['general']['reports_path'], ignore_errors=True)
            augment.augment_with_code_coverage(config, gradle_build_file,
                                               config['general']['build_type'],
                                               config['general']['test_directory'],
                                               config['general']['reports_path'])
            test_files[use_for_augmentation] = list(os.path.basename(path) for path in Path(config['general']['test_directory']).glob('**/*.java'))
            self.assertTrue(test_files[use_for_augmentation])
            for f in test_files[use_for_augmentation]:
                self.assertTrue(f.endswith('_ESTest.java') or f.endswith('_ESTest_scaffolding.java'))

        self.assertFalse(set(test_files[False]).issubset(set(test_files[True])))
        self.assertTrue(set(test_files[True]).issubset(set(test_files[False])))
        dir_util.cd_cli_dir()
        self.__assert_no_artifact_at_cli([app_name])

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

        self.assertTrue(len(lines_generated) == len(lines_standard), message)

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
