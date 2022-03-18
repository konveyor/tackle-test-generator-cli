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
import copy

from tkltest.util import constants

"""
This module contains the specification of all tkltest-unit configuration options.
The specification drives the creation of argument parsers and TOML file checking
for required options.
"""


def get_options_spec(command=None, subcommand=None, load_format=True):
    """Returns options specification.

    Returns the options specification for the given command and subcommand if specified; otherwise, returns
    the entire options specification

    Args:
        command: command to load option spec for
        subcommand: subcommand (of command) to load option spec for
        load_format: whether to use loaded format (which omits some fields); used only if command
            or subcommand is specified
    """
    if command is None:
        return copy.deepcopy(__options_spec)
    if subcommand is None:
        spec = copy.copy(__options_spec[command])
    else:
        x = __options_spec
        spec = copy.copy(__options_spec[command]['subcommands'][subcommand])
    if load_format:
        spec.pop('is_cli_command', None)
        spec.pop('help_message', None)
        spec.pop('subcommands', None)
    return spec


def __conditionally_required(opt_name, config):
    """Checker for conditionally required options.

    Performs "required" check for conditionally required configuration options. Checks the given option name
    in the context of the loaded config information.

    Args:
        opt_name: option to perform the required check on
        config: the loaded configuration information

    Returns:
        true if option is required in the context; false otherwise
    """

    if opt_name in ['refactored_app_path_prefix', 'refactored_app_path_suffix']:  # pragma: no branch
        if config['generate']['partitions_file'] != __options_spec['generate']['partitions_file']['default_value']:
            return 'required if "partitions_file" is specified'
    elif opt_name in ['app_classpath_file', 'monolith_app_path']:
        # required if app_build_type is not specified
        if config['generate']['app_build_type'] == __options_spec['generate']['app_build_type']['default_value']:
            return 'required if "app_build_type" is not specified'
    elif opt_name == 'app_build_type':
        # required if app_classpath_file or monolith_app_path are not specified
        if config['general']['app_classpath_file'] == __options_spec['general']['app_classpath_file']['default_value']:
            return 'required if "app_classpath_file" is not specified'
        if config['general']['monolith_app_path'] == __options_spec['general']['monolith_app_path']['default_value']:
            return 'required if "monolith_app_path" is not specified'
    elif opt_name == 'app_build_files':
        # required if app_build_type is specified
        if config['generate']['app_build_type'] != __options_spec['generate']['app_build_type']['default_value']:
            return 'required if "app_build_type" is specified'
    elif opt_name == 'app_build_target':
        # required if app_build_type is 'ant'
        if config['generate']['app_build_type'] == 'ant':
            return 'required if "app_build_type" is ant'
    return ''


__options_spec = {

    # "general" options: applicable to more than one of the subcommands (generate, execute, config)
    # when adding a cli option, make sure its long name is similar to the option name,
    # with hyphens instead of underscores
    'general': {
        'is_cli_command': False,
        'app_name': {
            'required': True,
            'is_toml_option': True,
            'is_cli_option': False,
            'type': str,
            'default_value': '',
            'help_message': 'name of the application being tested'
        },
        'app_classpath_file': {
            'required': __conditionally_required,
            'is_toml_option': True,
            'is_cli_option': False,
            'type': str,
            'default_value': '',
            'relpath_fix_type': 'paths_list_file',
            'help_message': 'file containing paths to jar files that represent the library dependencies of app. '
                            'Required only if app_build_files is not given.'
        },
        'config_file': {
            'required': False,
            'is_toml_option': False,
            'is_cli_option': True,
            'short_name': '-cf',
            'long_name': '--config-file',
            'type': argparse.FileType('r'),
            'default_value': constants.TKLTEST_DEFAULT_CONFIG_FILE,
            'relpath_fix_type': 'path',
            'help_message': 'path to TOML file containing configuration options'
        },
        'log_level': {
            'required': False,
            'is_toml_option': False,
            'is_cli_option': True,
            'short_name': '-l',
            'long_name': '--log-level',
            'type': str,
            'choices': ['CRITICAL', 'ERROR', 'WARNING', 'INFO', 'DEBUG'],
            'default_value': 'ERROR',
            'help_message': 'logging level for printing diagnostic messages; options are CRITICAL, ERROR, WARNING, INFO, DEBUG'
        },
        'monolith_app_path': {
            'required': __conditionally_required,
            'is_toml_option': True,
            'is_cli_option': False,
            'type': list,
            'default_value': [],
            'relpath_fix_type': 'path',
            'help_message': 'list of paths to application classes.Required only if app_build_files is not given.'
        },
        'java_jdk_home': {
            'required': True,
            'is_toml_option': True,
            'is_cli_option': False,
            'type': str,
            'default_value': '',
            'relpath_fix_type': 'path',
            'help_message': 'root directory for JDK installation (must be JDK; JRE will not suffice); '
                            'alternatively, can be set as environment variable JAVA_HOME'
        },
        'test_directory': {
            'required': False,
            'is_toml_option': True,
            'is_cli_option': True,
            'short_name': '-td',
            'long_name': '--test-directory',
            'type': str,
            'default_value': '',
            'relpath_fix_type': 'path',
            'help_message': 'name of root test directory containing the generated JUnit test classes'
        },
        'reports_path': {
            'required': False,
            'is_toml_option': True,
            'is_cli_option': True,
            'short_name': '-rp',
            'long_name': '--reports-path',
            'type': str,
            'default_value': '',
            'relpath_fix_type': 'path',
            'help_message': 'path to the reports directory'
        },
        'verbose': {
            'required': False,
            'is_toml_option': True,
            'is_cli_option': True,
            'short_name': '-vb',
            'long_name': '--verbose',
            'type': bool,
            'default_value': False,
            'help_message': 'run in verbose mode printing detailed status messages'
        },
        'version': {
            'required': False,
            'is_toml_option': False,
            'is_cli_option': True,
            'short_name': '-v',
            'long_name': '--version',
            'type': bool,
            'default_value': False,
            'help_message': 'print CLI version number'
        },
        'offline_instrumentation': {
            'required': False,
            'is_toml_option': True,
            'is_cli_option': True,
            'short_name': '-offli',
            'long_name': '--offline-instrumentation',
            'type': bool,
            'default_value': False,
            'help_message': 'perform offline instrumentation of app classes for measuring code coverage ' + \
                            '(default: app classes are instrumented online)'
        },
        'build_type': {
            'required': False,
            'is_toml_option': True,
            'is_cli_option': True,
            'short_name': '-bt',
            'long_name': '--build-type',
            'type': str,
            'choices': ['ant', 'maven',  'gradle'],
            'default_value': 'ant',
            'help_message': 'build file type for compiling and running the tests: ant, maven, or gradle'
        },
        'max_memory_for_coverage': {
            'required': False,
            'is_toml_option': True,
            'is_cli_option': True,
            'short_name': '-mam',
            'long_name': '--maximal-memory-for-coverage',
            'type': int,
            'default_value': 4096,
            'help_message': 'maximal heap size (in MB) used for obtaining coverage data'
        },
    },

    # "config" command options
    'config': {
        'is_cli_command': True,
        'help_message': 'Initialize configuration file or list configuration options',
        # subcommands for the generate command
        'subcommands': {
            'init': {
                'help_message': 'Initialize configuration options and print (in TOML format) to file or stdout',
                'file': {
                    'required': False,
                    'is_toml_option': False,
                    'is_cli_option': True,
                    'short_name': '-f',
                    'long_name': '--file',
                    'type': str,
                    'default_value': '',
                    'help_message': 'name of TOML file to create with initialized configuration options'
                }
            },
            'list': {
                'help_message': 'List all configuration options with description'
            }
        }
    },

    # "generate" command options
    'generate': {
        'is_cli_command': True,
        'help_message': 'Generate test cases on the application under test',
        'jee_support': {
            'required': False,
            'is_toml_option': True,
            'is_cli_option': False,
            'type': bool,
            'default_value': False,
            'help_message': 'add support JEE mocking in generated tests cases'
        },
        'no_diff_assertions': {
            'required': False,
            'is_toml_option': True,
            'is_cli_option': True,
            'short_name': '-nda',
            'long_name': '--no-diff-assertions',
            'type': bool,
            'default_value': False,
            'help_message': 'do not add assertions for differential testing to the generated tests'
        },
        'partitions_file': {
            'required': False,
            'is_toml_option': True,
            'is_cli_option': True,
            'short_name': '-pf',
            'long_name': '--partitions-file',
            'type': str,
            'default_value': '',
            'relpath_fix_type': 'path',
            'help_message': 'path to file containing specification of partitions'
        },
        'target_class_list': {
            'required': False,
            'is_toml_option': True,
            'is_cli_option': False,
            'type': list,
            'default_value': [],
            'help_message': 'list of target classes or packages to perform test generation on; packages must end with a wildcard'
        },
        'excluded_class_list': {
            'required': False,
            'is_toml_option': True,
            'is_cli_option': False,
            'type': list,
            'default_value': [],
            'help_message': 'list of classes or packages to exclude from test generation; packages must end with a wildcard'
        },
        'time_limit': {
            'required': False,
            'is_toml_option': True,
            'is_cli_option': False,
            'type': int,
            'default_value': 10,
            'help_message': 'time limit per class (in seconds) for evosuite/randoop test generation'
        },
        'app_build_type': {
            'required': __conditionally_required,
            'is_toml_option': True,
            'is_cli_option': False,
            'type': str,
            'choices': ['gradle', 'ant', 'maven', ''],
            'default_value': '',
            'help_message': 'build type for collecting app dependencies: ant, maven, or gradle'
        },
        'app_build_files': {
            'required': __conditionally_required,
            'is_toml_option': True,
            'is_cli_option': False,
            'type': list,
            'default_value': [],
            'relpath_fix_type': 'path',
            'help_message': 'list of paths to app build files for the specified app build type'
        },
        'app_build_settings_files': {
            'required': False,
            'is_toml_option': True,
            'is_cli_option': False,
            'type': list,
            'default_value': [],
            'relpath_fix_type': 'path',
            'help_message': 'list of paths to app build settings files or property files for the specified app build type'
        },
        'bad_path': {
            'required': False,
            'is_toml_option': True,
            'is_cli_option': True,
            'short_name': '-bp',
            'long_name': '--bad-path',
            'type': bool,
            'default_value': False,
            'help_message': 'Generate also bad path tests; assertions will validate that the exception observed during generation is thrown also during execution'
        },
        'app_build_target': {
            'required': __conditionally_required,
            'is_toml_option': True,
            'is_cli_option': False,
            'type': str,
            'default_value': '',
            'help_message': 'Name of the Ant target that is being used to build the app from the build file; required only for apps that use an Ant build file'
        },

        # subcommands for the generate command
        'subcommands': {
            # "generate ctd-amplified" command options
            'ctd_amplified': {
                'help_message': 'Use CTD for computing coverage goals',
                'base_test_generator': {
                    'required': False,
                    'is_toml_option': True,
                    'is_cli_option': True,
                    'short_name': '-btg',
                    'long_name': '--base-test-generator',
                    'type': str,
                    'choices': ['combined', 'evosuite', 'randoop'],
                    'default_value': 'combined',
                    'help_message': 'base test generator to use for creating building-block test sequences'
                },
                'no_augment_coverage': {
                    'required': False,
                    'is_toml_option': True,
                    'is_cli_option': True,
                    'short_name': '-nac',
                    'long_name': '--no-augment-coverage',
                    'type': bool,
                    'default_value': False,
                    'help_message': 'do not augment CTD-guided tests with coverage-increasing base tests'
                },
                'no_ctd_coverage': {
                    'required': False,
                    'is_toml_option': True,
                    'is_cli_option': True,
                    'short_name': '-nctd',
                    'long_name': '--no-ctd-coverage',
                    'type': bool,
                    'default_value': False,
                    'help_message': 'do not generate CTD coverage report'
                },
                'interaction_level': {
                    'required': False,
                    'is_toml_option': True,
                    'is_cli_option': False,
                    'type': int,
                    'default_value': 1,
                    'help_message': 'CTD interaction level (strength) for test-plan generation'
                },
                'num_seq_executions': {
                    'required': False,
                    'is_toml_option': True,
                    'is_cli_option': False,
                    'type': int,
                    'default_value': 10,
                    'help_message': 'number of executions to perform to determine pass/fail status of generated sequences'
                },
                'refactored_app_path_prefix': {
                    # conditionally required: required if partitions_file is specified
                    'required': __conditionally_required,
                    'is_toml_option': True,
                    'is_cli_option': False,
                    'type': str,
                    'default_value': '',
                    'relpath_fix_type': 'path',
                    'help_message': 'path prefix to root directory of refactored app version'
                },
                'refactored_app_path_suffix': {
                    # conditionally required: required if partitions_file is specified
                    'required': __conditionally_required,
                    'is_toml_option': True,
                    'is_cli_option': False,
                    'type': list,
                    'default_value': [],
                    'relpath_fix_type': 'path',
                    'help_message': 'list of paths to refactored app classes'
                },
                'reuse_base_tests': {
                    'required': False,
                    'is_toml_option': True,
                    'is_cli_option': True,
                    'short_name': '-rbt',
                    'long_name': '--reuse-base-tests',
                    'type': bool,
                    'default_value': False,
                    'help_message': 'reuse existing base test cases'
                },
            },

            # "generate evosuite" command options
            'evosuite': {
                'help_message': 'Use EvoSuite for generating a test suite',
                'criterion': {
                    'required': False,
                    'is_toml_option': True,
                    'is_cli_option': False,
                    'type': list,
                    'default_value': ["BRANCH"],
                    'help_message': 'coverage criterion for evosuite'
                }
            },

            # "generate randoop" command options
            'randoop': {
                'help_message': 'Use Randoop for generating a test suite',
            }
        }
    },

    # "execute" command options
    'execute': {
        'is_cli_command': True,
        'help_message': 'Execute generated tests on the application version under test',
        'app_packages': {
            'required': True,
            'is_toml_option': True,
            'is_cli_option': False,
            'type': list,
            'default_value': [],
            'help_message': 'list of app packages; must end with a wildcard'
        },
        'create_build_file': {
            'required': False,
            'is_toml_option': True,
            'is_cli_option': True,
            'short_name': '-nbf',
            'long_name': '--no-build-file-creation',
            'type': bool,
            'default_value': True,
            'help_message': 'whether to generate build files; if set to false, a build file (of type set in build_type option) should already exist and will be used'
        },
        'code_coverage': {
            'required': False,
            'is_toml_option': True,
            'is_cli_option': True,
            'short_name': '-cc',
            'long_name': '--code-coverage',
            'type': bool,
            'default_value': False,
            'help_message': 'generate code coverage report with JaCoCo agent'
        },
        'test_class': {
            'required': False,
            'is_toml_option': True,
            'is_cli_option': True,
            'short_name': '-tc',
            'long_name': '--test-class',
            'type': str,
            'default_value': '',
            'help_message': 'path to a test class file (.java) to compile and run'
        },
        'combine_modules_coverage_reports': {
            'required': False,
            'is_toml_option': True,
            'is_cli_option': False,
            'type': bool,
            'default_value': False,
            'help_message': 'when test suites are generated per module, create a combined coverage report'
        },
    },

    # "dev_tests" options
    'dev_tests': {
        'is_cli_command': False,
        'help_message': 'information about developer-written test suite',
        'build_type': {
            'required': True,
            'is_toml_option': True,
            'is_cli_option': False,
            'type': str,
            'choices': ['ant', 'maven', 'gradle'],
            'default_value': 'ant',
            'help_message': 'build type for compiling and running the developer-written test suite: ant, maven, or gradle'
        },
        'build_file': {
            'required': True,
            'is_toml_option': True,
            'is_cli_option': False,
            'type': str,
            'default_value': '',
            'relpath_fix_type': 'path',
            'help_message': 'path to build file for compiling and running the developer-written test suite'
        },
        'build_targets': {
            'required': True,
            'is_toml_option': True,
            'is_cli_option': False,
            'type': list,
            'default_value': [],
            'help_message': 'list of build targets for compiling and running the developer-written test suite'
        },
        'coverage_exec_file': {
            'required': False,
            'is_toml_option': True,
            'is_cli_option': False,
            'type': str,
            'default_value': '',
            'relpath_fix_type': 'path',
            'help_message': 'the path to the Jacoco coverage .exec file, generated by the developer-written build file'
        },
        'compare_code_coverage': {
            'required': False,
            'is_toml_option': True,
            'is_cli_option': False,
            'type': bool,
            'default_value': False,
            'help_message': 'create a code coverage report that compares between the automatically generated test suite and the developer-written test suite'
        },
        'use_for_augmentation': {
            'required': False,
            'is_toml_option': True,
            'is_cli_option': False,
            'type': bool,
            'default_value': False,
            'help_message': 'when augmenting with evosuite tests, consider developer-written test suite coverage'
        },
        'coverage_threshold': {
            'required': False,
            'is_toml_option': True,
            'is_cli_option': False,
            'type': int,
            'default_value': 100,
            'help_message': 'classes with developer-written instruction coverage percentage higher than the threshold are excluded from test generation'
        },
    }

}
