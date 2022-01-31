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

from .tkltest import *
from .util.ui import config_options_ui


def __add_internal_config_options(loaded_config, internal_options):
    for command in internal_options.keys():
        command_spec = internal_options[command]
        command_spec.pop('help_message', None)
        command_spec.pop('is_cli_command', None)
        for option_name in command_spec.keys():
            if command_spec[option_name]['is_toml_option']:
                loaded_config[command][option_name] = command_spec[option_name]['default_value']


def main():
    """Main entry point for the CLI.

    This is the main entry point for the CLI, which parses command-line arguments, loads configuration
    information and executes the specified command (config, generate, or execute).
    """
    # create the main argument parser
    parser = argparse.ArgumentParser(prog='tkltest-ui',
        description='Command-line interface for generating and executing UI test cases for web applications')

    # get spec for CLI commands and config options for UI testing
    ui_options_spec = config_options_ui.get_options_spec()

    # parse arguments, perform checks, and load configuration
    args = parse_arguments(parser, ui_options_spec)
    perform_checks_init_logger(args, parser, 'ui')
    tkltest_config = load_configuration(args, 'ui')
    logging.debug('Loaded config (excluding internal options): {}'.format(tkltest_config))

    # add internal configuration options
    __add_internal_config_options(loaded_config=tkltest_config,
                                  internal_options=config_options_ui.get_options_spec_internal())
    logging.debug('Loaded config (including internal options): {}'.format(tkltest_config))

    # TODO: process generate/execute commands


if __name__ == '__main__':  # pragma: no cover
    main()
