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

import logging
import sys

import toml

from ._version import __version__
from .util import logging_util, config_util, config_options
from .util.constants import *


def __create_command_parsers(parser, options_spec):
    """
    Creates parser for each CLI command, adds subcommands and arguments to the command parser,
    and adds the command parser to the given parser

    Args:
        parser: parser to add the CLI command parsers to parser to
        options_spec: options specification for the commands
    """
    # iterate over each cli command in the spec and create parser for it
    for cmd in options_spec.keys():

        # get spec for command and spec (if any) for subcommands of the command
        cmd_options_spec = options_spec[cmd]
        subcmds_options_spec = cmd_options_spec.pop('subcommands', {})

        # add parser for command and arguments to the parser
        cmd_parser = parser.add_parser(cmd, help=cmd_options_spec.pop('help_message'))
        __add_arguments_to_parser(cmd_parser, cmd_options_spec)

        if subcmds_options_spec:
            # parsers for subcommands
            subcmd_subparsers = cmd_parser.add_subparsers(dest='sub_command', required=True)

            # add parser and arguments for each subcommand
            for subcmd_name in subcmds_options_spec.keys():
                subcmd_opt_spec = subcmds_options_spec[subcmd_name]
                subcmd_parser = subcmd_subparsers.add_parser(subcmd_name.replace('_', '-'), help=subcmd_opt_spec.pop('help_message'))
                __add_arguments_to_parser(subcmd_parser, subcmd_opt_spec)


def __add_arguments_to_parser(parser, options_spec):
    """
    Adds arguments for the options specified in the given options specification to the given parser.

    Args:
        parser: parser to add arguments to
        options_spec: specification of arguments to add

    """
    # iterate over each option in spec call parser.add_argument for each option with
    # suitable parameters
    for option_name in options_spec:
        if option_name in ['is_cli_command', 'help_message']:
            continue
        option = options_spec[option_name]
        if not option['is_cli_option']:
            continue

        # creation dictionary of parameters to be passed in the call to parser.add_argument()
        add_arg_params = dict(help=option['help_message'])

        # set parameters based on option type
        arg_type = option['type']
        if arg_type == str:
            add_arg_params['action'] = 'store'
            add_arg_params['type'] = arg_type
        elif arg_type == bool:
            add_arg_params['action'] = 'store_true'
        elif arg_type == list:
            add_arg_params['nargs'] = '+'
        else:
            add_arg_params['type'] = arg_type

        # set action/version for "version" option
        if option_name == 'version':
            add_arg_params['action'] = 'version'
            add_arg_params['version'] = __version__

        # set choices if it occurs in the option spec
        if 'choices' in option.keys():
            add_arg_params['choices'] = option['choices']
            if option_name == 'log_level':
                add_arg_params['default'] = option['default_value']

        # add argument to the parser
        parser.add_argument(*[option['short_name'], option['long_name']], **add_arg_params)


def __process_config_commands(args, test_level):
    """Processes config commands

    Processes config commands (init, list)

    Args:
        args: parsed command-line arguments
        test_level: level of testing (unit, ui)
    """
    if args.sub_command == 'init':
        # initialize config
        config = config_util.init_config(test_level=test_level)

        # if file name specified, write config to file; otherwise print to stdout
        if hasattr(args, 'file') and args.file:
            with open(args.file, 'w') as f:
                toml.dump(config, f)
            logging_util.tkltest_status('Config file written to: {}'.format(args.file))
        else:
            print('\n{}'.format(toml.dumps(config)))
    else:
        # list subcommand: list all config options with help messages
        config_options.print_options_with_help(test_level=test_level)


def parse_arguments(parser, options_spec):
    # add the arguments for the main parser for non-command top-level options in the option spec
    commands_spec = {}
    for opt_name in options_spec.keys():
        if not options_spec[opt_name]['is_cli_command']:
            __add_arguments_to_parser(parser, options_spec[opt_name])
        else:
            commands_spec[opt_name] = options_spec[opt_name]

    # set default option values
    default_config_file = [f for f in os.listdir('.') if os.path.isfile(f) and f == TKLTEST_DEFAULT_CONFIG_FILE]
    if default_config_file:
        parser.set_defaults(config_file=default_config_file[0])

    # add parsers for all CLI commands
    subparser = parser.add_subparsers(dest='command')
    __create_command_parsers(subparser, commands_spec)

    # parse arguments
    return parser.parse_args()


def perform_checks_init_logger(args, parser, test_level):
    # if no args specified, print help message and exit
    if len(sys.argv) == 1:
        parser.print_help()
        sys.exit(0)

    # process config commands
    if args.command == 'config':
        __process_config_commands(args, test_level)
        sys.exit(0)

    # if config file not specified, print help and exit
    if not args.config_file:
        logging_util.tkltest_status('No config file specified\n', error=True)
        sys.exit(1)

    # initialize logging
    logging_util.init_logging('./tkltest_{}.log'.format(test_level), args.log_level)
    logging.debug('args: {}'.format(args))


def load_configuration(args, test_level):
    # load config file
    logging_util.tkltest_status('Loading config file {}'.format(args.config_file.name))
    tkltest_config = config_util.load_config(args=args, test_level=test_level)
    logging.info('config_file: {}'.format(tkltest_config))
    return tkltest_config
