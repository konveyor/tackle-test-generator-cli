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
This module contains the specification of all tkltest-ui configuration options.
The specification drives the creation of argument parsers and TOML file checking
for required options.
"""
import tabulate

from tkltest.unit.util import config_options_unit
from tkltest.ui.util import config_options_ui


def get_options_spec(command=None, subcommand=None, load_format=True, test_level='unit'):
    """Returns options specification.

    Returns the options specification for the given command and subcommand if specified; otherwise, returns
    the entire options specification

    Args:
        command: command to load option spec for
        subcommand: subcommand (of command) to load option spec for
        load_format: whether to use loaded format (which omits some fields); used only if command
            or subcommand is specified
        test_level: level of testing (unit, ui)
    """
    if test_level == 'unit':
        return config_options_unit.get_options_spec(command=command, subcommand=subcommand, load_format=load_format)
    else:
        return config_options_ui.get_options_spec(command=command, subcommand=subcommand, load_format=load_format)


def print_options_with_help(command=None, tablefmt='simple', test_level='unit'):
    """Prints configuration options.

    Prints configuration options along with help messages for all options of the given command, if provided,
    or all CLI configuration options otherwise.

    Args:
        command: command to print configuration options for
        tablefmt: table format for the tabulate module
        test_level: level of testing (unit, ui)
    """
    opt_spec = get_options_spec(command, load_format=False, test_level=test_level)
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
        if opt_name in ['is_cli_command', 'help_message']:
            continue
        opt_info = opt_spec[opt_name]
        if opt_name == 'subcommands':
            for subcmd in opt_info.keys():
                __append_output_for_command(cmd, opt_info[subcmd], output, subcmd)
        else:
            fmtname = opt_name
            if opt_info['required'] == True:
                fmtname += '*'
            if not opt_info['is_toml_option']:
                fmtname += '^'
            output.append([
                fmtname,
                '{}/{}'.format(opt_info['short_name'], opt_info['long_name']) if opt_info['is_cli_option'] else '',
                opt_info['help_message']
            ])

if __name__ == '__main__':
    print_options_with_help(tablefmt='github', test_level='unit')
