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

from .tkltest import *
from .util import config_options_ui


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

    # TODO: process generate/execute commands


if __name__ == '__main__':  # pragma: no cover
    main()
