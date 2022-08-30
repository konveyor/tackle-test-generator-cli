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

"""
This module contains the specification of all tkltest-api configuration options.
The specification drives the creation of argument parsers and TOML file checking
for required options.
"""
import argparse
import copy

from tkltest.util import constants


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


__options_spec = {

    # "general" options: applicable to more than one of the subcommands (generate, execute, config)
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
        'base_url': {
            'required': True,
            'is_toml_option': True,
            'is_cli_option': True,
            'short_name': '-u',
            'long_name': '--base-url',
            'type': str,
            'default_value': '',
            'help_message': 'URL where system under test is deployed, e.g. "http://localhost:8080"'
        },
        'api_spec': {
            'required': True,
            'is_toml_option': True,
            'is_cli_option': True,
            'short_name': '-as',
            'long_name': '--api-spec',
            'type': str,
            'default_value': '',
            'help_message': 'path to Open API specification'
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
        'test_directory': {
            'required': False,
            'is_toml_option': True,
            'is_cli_option': True,
            'short_name': '-td',
            'long_name': '--test-directory',
            'type': str,
            'default_value': '',
            'relpath_fix_type': 'path',
            'help_message': 'name of root test directory containing the generated test classes'
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
        'help_message': 'Generate API test cases on the system under test',
        'subcommands': {
            'schemathesis': {
                'help_message': 'Generate tests using Schemathesis',
            }
        }
    },

    # "execute" command options
    'execute': {
        'is_cli_command': True,
        'help_message': 'Execute generated API tests on the system under test',
    },
}
