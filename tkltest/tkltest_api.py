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

import argparse

from tkltest.tkltest import *
from tkltest.util.api import config_options_api
from tkltest.util.logging_util import *
from tkltest.generate.api import generate
from tkltest.execute.api import execute


def main():
    """Main entry point for the CLI.

    This is the main entry point for the CLI, which parses command-line arguments, loads configuration
    information and executes the specified command (config, generate, or execute).
    """
    # create the main argument parser
    parser = argparse.ArgumentParser(prog='tkltest-api',
        description='Command-line interface for generating and executing API test cases')

    # get spec for CLI commands and config options for API testing
    api_options_spec = config_options_api.get_options_spec()

    # parse arguments, perform checks, and load configuration
    args = parse_arguments(parser, api_options_spec)
    perform_checks_init_logger(args, parser, 'api')
    tkltest_config = load_configuration(args, 'api')
    logging.debug('Loaded config: {}'.format(tkltest_config))

    # TODO: process execute commands
    if args.command == 'generate':
        generate.process_generate_command(args=args, config=tkltest_config)
    elif args.command == 'execute':
        execute.process_execute_command(args=args, config=tkltest_config)


if __name__ == '__main__':  # pragma: no cover
    main()
