# ***************************************************************************
# Copyright IBM Corporation 2022
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

"""
This module contains the specification of all tkltest-ui configuration options.
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
        'app_url': {
            'required': True,
            'is_toml_option': True,
            'is_cli_option': True,
            'short_name': '-au',
            'long_name': '--app-url',
            'type': str,
            'default_value': '',
            'help_message': 'URL where application under test is deployed'
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
            'help_message': 'name of root test directory containing the generated JUnit test classes'
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
        'help_message': 'Generate UI test cases on the application under test',
        'browser': {
            'required': False,
            'is_toml_option': True,
            'is_cli_option': True,
            'short_name': '-b',
            'long_name': '--browser',
            'type': str,
            'choices': ['chrome', 'chrome_headless', 'firefox', 'firefox_headless', 'phantomjs'],
            'default_value': 'chrome',
            'help_message': 'browser on which to launch app under test for crawling and test generation; default is chrome'
        },
        'time_limit': {
            'required': False,
            'is_toml_option': True,
            'is_cli_option': False,
            'type': int,
            'default_value': 5,
            'help_message': 'maximum crawl time (in minutes); default is 5 min'
        },
        'max_states': {
            'required': False,
            'is_toml_option': True,
            'is_cli_option': False,
            'type': int,
            'default_value': 0,
            'help_message': 'maximum states to discover during crawl; default is 0 (unlimited)'
        },
        'max_depth': {
            'required': False,
            'is_toml_option': True,
            'is_cli_option': False,
            'type': int,
            'default_value': 2,
            'help_message': 'maximum depth for crawling; default is 2'
        },
        'add_state_diff_assertions': {
            'required': False,
            'is_toml_option': True,
            'is_cli_option': False,
            'type': bool,
            'default_value': False,
            'help_message': 'add assertions for comparing UI states; default is false'
        },
        'crawl_hidden_anchors': {
            'required': False,
            'is_toml_option': True,
            'is_cli_option': False,
            'type': bool,
            'default_value': False,
            'help_message': 'crawl anchors even if they are not visible in the browser'
        },
        'crawl_frames': {
            'required': False,
            'is_toml_option': True,
            'is_cli_option': False,
            'type': bool,
            'default_value': False,
            'help_message': 'crawl frames'
        },
        'click_once': {
            'required': False,
            'is_toml_option': True,
            'is_cli_option': False,
            'type': bool,
            'default_value': True,
            'help_message': 'perform action on a web element only once; default is true'
        },
        'click_randomly': {
            'required': False,
            'is_toml_option': True,
            'is_cli_option': False,
            'type': bool,
            'default_value': True,
            'help_message': 'click elements randomly instead of the order in which they are discovered'
        },
        'click_default_elements': {
            'required': False,
            'is_toml_option': True,
            'is_cli_option': False,
            'type': bool,
            'default_value': True,
            'help_message': 'click default elements'
        },
        'wait_after_event': {
            'required': False,
            'is_toml_option': True,
            'is_cli_option': False,
            'type': int,
            'default_value': 500,
            'help_message': 'the time to wait (in milliseconds) after an event has been fired; default is 500'
        },
        'wait_after_reload': {
            'required': False,
            'is_toml_option': True,
            'is_cli_option': False,
            'type': int,
            'default_value': 500,
            'help_message': 'the time to wait (in milliseconds) after URL load; default is 500'
        },
        'clickables': {
            'required': False,
            'is_toml_option': True,
            'is_cli_option': False,
            'type': list,
            'default_value': [],
            'help_message': 'list of HTML tags that should be clicked'
        },
        'form_fill_mode': {
            'required': False,
            'is_toml_option': True,
            'is_cli_option': False,
            'type': str,
            'choices': ['normal', 'random', 'training', 'xpath_training'],
            'default_value': 'random',
            'help_message': 'TBD'
        },
        'form_fill_order': {
            'required': False,
            'is_toml_option': True,
            'is_cli_option': False,
            'type': str,
            'choices': ['normal', 'dom', 'visual'],
            'default_value': 'normal',
            'help_message': 'TBD'
        },
        'dont_click_spec_file': {
            'required': False,
            'is_toml_option': True,
            'is_cli_option': False,
            'type': str,
            'default_value': '',
            'help_message': 'TOML file containing specification of elements to not be clicked'
        },
        'form_data_spec_file': {
            'required': False,
            'is_toml_option': True,
            'is_cli_option': False,
            'type': str,
            'default_value': '',
            'help_message': 'TOML file containing specification of form data'
        },
        'browser_opts_spec_file': {
            'required': False,
            'is_toml_option': True,
            'is_cli_option': False,
            'type': str,
            'default_value': '',
            'help_message': 'TOML file containing specification of browser options'
        },
        'fragment_rules_spec_file': {
            'required': False,
            'is_toml_option': True,
            'is_cli_option': False,
            'type': str,
            'default_value': '',
            'help_message': 'TOML file containing specification of fragment rules (advanced)'
        }

    },

    # "execute" command options
    'execute': {
        'is_cli_command': True,
        'help_message': 'Execute generated UI tests on the application under test',
    }

}
