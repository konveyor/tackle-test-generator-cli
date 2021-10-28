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
import tabulate
from tkltest.util import constants

"""
This module contains the specification of all tkltest CLI configuration options.
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
        spec.pop('help_message', None)
        spec.pop('subcommands', None)
    return spec


def print_options_with_help(command=None, tablefmt='simple'):
    """Prints configuration options.

    Prints configuration options along with help messages for all options of the given command, if provided,
    or all CLI configuration options otherwise.

    Args:
        command: command to print configuration options for
        tablefmt: table format for the tabulate module
    """
    opt_spec = get_options_spec(command, load_format=False)
    output = []
    if command:
        commands = [command]
        opt_spec = {command: opt_spec}
    else:
        commands = list(opt_spec.keys())

    for cmd in commands:
        __append_output_for_command(cmd, opt_spec[cmd], output)
        output.append(['', '', ''])

    tabulate.PRESERVE_WHITESPACE = True
    print(tabulate.tabulate(output, tablefmt=tablefmt,
                            headers=['TOML name ("*"=req, "^"=CLI-only)', 'CLI name', 'Description']))


def __append_output_for_command(cmd, opt_spec, output, subcmd=None):
    """Appends options list for command to output.

    Appends options list, with help messages, for the given command and subcommand (optional) to
    the output array.

    Args:
        cmd: command for which to add option information
        opt_spec: option specification for command or subcommand
        output: output array to add information to
        subcmd: subcommand (of command) for which to add option information
    """
    if subcmd is not None:
        output.append(['', '', ''])
    cmdstr = cmd if subcmd is None else '{}.{}'.format(cmd, subcmd)
    output.append([cmdstr, '', opt_spec['help_message'] if 'help_message' in opt_spec.keys() else ''])
    for opt_name in opt_spec.keys():
        if opt_name == 'help_message':
            continue
        opt_info = opt_spec[opt_name]
        if opt_name == 'subcommands':
            for subcmd in opt_info.keys():
                __append_output_for_command(cmd, opt_info[subcmd], output, subcmd)
        else:
            fmtname = opt_name
            if opt_info['required']:
                fmtname += '*'
            if not opt_info['is_toml_option']:
                fmtname += '^'
            output.append([
                fmtname,
                '{}/{}'.format(opt_info['short_name'], opt_info['long_name']) if opt_info['is_cli_option'] else '',
                opt_info['help_message']
            ])


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
    elif opt_name == 'app_classpath_file':
        # required if app_build_type is not specified
        if config['generate']['app_build_type'] == __options_spec['generate']['app_build_type']['default_value']:
            return 'required if "app_build_type" is not specified'
    elif opt_name == 'app_build_type':
        # required if app_classpath_file is not specified
        if config['general']['app_classpath_file'] == __options_spec['general']['app_classpath_file']['default_value']:
            return 'required if "app_classpath_file" is not specified'
    elif opt_name == 'app_build_config_file':
        # required if app_build_type is specified
        if config['generate']['app_build_type'] != __options_spec['generate']['app_build_type']['default_value']:
            return 'required if "app_build_type" is specified'
    return ''


__options_spec = {

    # "general" options: applicable to more than one of the subcommands (generate, execute, classify, config)
    'general': {
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
            'help_message': 'file containing paths to jar files that represent the library dependencies of app'
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
            'help_message': 'logging level for printing diagnostic messages'
        },
        'monolith_app_path': {
            'required': True,
            'is_toml_option': True,
            'is_cli_option': False,
            'type': list,
            'default_value': [],
            'relpath_fix_type': 'path',
            'help_message': 'list of paths to application classes'
        },
        'java_jdk_home': {
            'required': True,
            'is_toml_option': True,
            'is_cli_option': False,
            'type': str,
            'default_value': '',
            'relpath_fix_type': 'path',
            'help_message': 'root directory for JDK installation (must be JDK; JRE will not suffice); '
                            'can be set as environment variable JAVA_HOME'
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
        }
    },

    # "config" command options
    'config': {
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
            'help_message': 'list of target classes to perform test generation on'
        },
        'excluded_class_list': {
            'required': False,
            'is_toml_option': True,
            'is_cli_option': False,
            'type': list,
            'default_value': [],
            'help_message': 'list of classes or packages to exclude from test generation. Packages must end with a wildcard.'
        },
        'time_limit': {
            'required': False,
            'is_toml_option': True,
            'is_cli_option': False,
            'type': int,
            'default_value': 10,
            'help_message': 'time limit (in seconds) for evosuite/randoop test generation'
        },
        'app_build_type': {
            'required': __conditionally_required,
            'is_toml_option': True,
            'is_cli_option': False,
            'type': str,
            'choices': ['gradle', None],
            'default_value': None,
            'help_message': 'build type for collecting app dependencies: gradle (support for maven and ant to be added)'
        },
        'app_build_config_file': {
            'required': __conditionally_required,
            'is_toml_option': True,
            'is_cli_option': False,
            'type': str,
            'default_value': '',
            'relpath_fix_type': 'path',
            'help_message': 'path to app build file for the specified app build type'
        },
        'app_build_settings_file': {
            'required': False,
            'is_toml_option': True,
            'is_cli_option': False,
            'type': str,
            'default_value': '',
            'relpath_fix_type': 'path',
            'help_message': 'path to app build settings file for the specified app build type'
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
                'augment_coverage': {
                    'required': False,
                    'is_toml_option': True,
                    'is_cli_option': True,
                    'short_name': '-ac',
                    'long_name': '--augment-coverage',
                    'type': bool,
                    'default_value': False,
                    'help_message': 'augment CTD-guided tests with coverage-increasing base tests'
                },
                'no_ctd_coverage': {
                    'required': False,
                    'is_toml_option': True,
                    'is_cli_option': True,
                    'short_name': '-nctd',
                    'long_name': '--no-ctd-coverage',
                    'type': bool,
                    'default_value': False,
                    'help_message': 'generate CTD coverage report'
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
                'no_error_revealing_tests': {
                    'required': False,
                    'is_toml_option': True,
                    'is_cli_option': False,
                    'type': bool,
                    'default_value': False,
                    'help_message': 'do not generate error-revealing tests with randoop'
                }
            }
        }
    },

    # "execute" command options
    'execute': {
        'help_message': 'Execute generated tests on the application version under test',
        'app_packages': {
            'required': True,
            'is_toml_option': True,
            'is_cli_option': False,
            'type': list,
            'default_value': [],
            'help_message': 'list of app packages. Must end with a wildcard'
        },
        'build_type': {
            'required': False,
            'is_toml_option': True,
            'is_cli_option': True,
            'short_name': '-bt',
            'long_name': '--build-type',
            'type': str,
            'choices': ['ant', 'maven'],
            'default_value': 'ant',
            'help_message': 'build file type for compiling and running the tests - either ant or maven'
        },
        'create_build_file': {
            'required': False,
            'is_toml_option': True,
            'is_cli_option': True,
            'short_name': '-nbf',
            'long_name': '--no-build-file-creation',
            'type': bool,
            'default_value': True,
            'help_message': 'Whether to generate build files. If set to false, a build file (of type set in build_type option) should already exist and will be used'
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
        'online_instrumentation': {
            'required': False,
            'is_toml_option': True,
            'is_cli_option': True,
            'short_name': '-onli',
            'long_name': '--online-instrumentation',
            'type': bool,
            'default_value': False,
            'help_message': 'perform online instrumentation of app classes for measuring code coverage ' + \
                            '(default: app classes are instrumented offline)'
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
        }
    }

}

if __name__ == '__main__':
    print_options_with_help(tablefmt='github')
