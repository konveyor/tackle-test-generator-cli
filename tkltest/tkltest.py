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
import logging
import shutil
import sys
from zipfile import ZipFile

import toml

from ._version import __version__
from .execute import execute
from .generate import generate
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


def __unjar_path(tkltest_config):
    unjar_paths = list()
    for path in tkltest_config['general']['monolith_app_path']:
        if path.endswith('.jar'):
            with ZipFile(path, 'r') as zipObj:
                unjar_path = path.replace('.jar', '')
                if not os.path.isdir(unjar_path):
                    zipObj.extractall(unjar_path)
                    tkltest_config['general']['monolith_app_path'].remove(path)
                    tkltest_config['general']['monolith_app_path'].append(unjar_path)
                    unjar_paths.append(unjar_path)
                else:
                    logging_util.tkltest_status('Unzip jar file. Folder {} already exist'.format(unjar_path), error=True)
                    sys.exit(1)
    return unjar_paths


def __process_config_commands(args):
    """Processes config commands

    Processes config commands (init, list)

    Args:
        args: parsed command-line arguments
    """
    if args.sub_command == 'init':
        # initialize config
        config = config_util.init_config()

        # if file name specified, write config to file; otherwise print to stdout
        if hasattr(args, 'file') and args.file:
            with open(args.file, 'w') as f:
                toml.dump(config, f)
            logging_util.tkltest_status('Config file written to: {}'.format(args.file))
        else:
            print('\n{}'.format(toml.dumps(config)))
    else:
        # list subcommand: list all config options with help messages
        config_options.print_options_with_help()


def main():
    """Main entry point for the CLI.

    This is the main entry point for the CLI, which parses command-line arguments, loads configuration
    information and executes the specified command (config, generate, or execute).
    """
    # create the main argument parser
    parser = argparse.ArgumentParser(prog='tkltest',
        description='Command-line interface for generating and executing test cases on '
                    'two application versions and performing differential testing (currently '
                    'supporting Java unit testing)')

    # add the arguments for the main parser for the "general" options in the option spec
    options_spec = config_options.get_options_spec()
    __add_arguments_to_parser(parser, options_spec.pop('general'))

    # set default option values
    default_config_file = [f for f in os.listdir('.') if os.path.isfile(f) and f == TKLTEST_DEFAULT_CONFIG_FILE]
    if default_config_file:
        parser.set_defaults(config_file=default_config_file[0])

    # add parsers for all CLI commands
    subparser = parser.add_subparsers(dest='command')
    __create_command_parsers(subparser, options_spec)

    # parse arguments
    args = parser.parse_args()

    # initialize logging
    logging_util.init_logging('./tkltest.log', args.log_level)
    logging.debug('args: {}'.format(args))

    # process config commands
    if args.command == 'config':
        __process_config_commands(args)
        sys.exit(0)

    # load config file
    logging_util.tkltest_status('Loading config file {}'.format(args.config_file.name))
    tkltest_config = config_util.load_config(args)
    logging.info('config_file: {}'.format(tkltest_config))

    unjar_paths = __unjar_path(tkltest_config)

    # process other commands
    try:
        if args.command == 'execute':
            execute.process_execute_command(args, tkltest_config)
        elif args.command == 'generate':
            generate.process_generate_command(args, tkltest_config)
        # elif args.command == 'classify':
        #     execute.classify_errors(args.reports_path, tkltest_config['execute']['app_packages'],
        #                             tkltest_config['general']['test_directory'])
    finally:
        for path in unjar_paths:
            if os.path.isdir(path):
                shutil.rmtree(path)


if __name__ == '__main__':  # pragma: no cover
    main()
