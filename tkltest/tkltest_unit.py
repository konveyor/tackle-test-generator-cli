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
import shutil
from zipfile import ZipFile

from .execute.unit import execute
from .generate.unit import generate
from .tkltest import *
from .util import logging_util, config_options_unit
from .util.constants import *


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


def main():
    """Main entry point for the tkltest-unit command.

    This is the main entry point for the tkltest-unit command, which parses command-line arguments, loads configuration
    information and executes the specified command (config, generate, or execute).
    """
    # create the main argument parser
    parser = argparse.ArgumentParser(prog='tkltest-unit',
        description='Command-line interface for generating and executing Java unit test cases')

    # get spec for CLI commands and config options for unit testing
    unit_options_spec = config_options_unit.get_options_spec()

    # parse arguments, perform checks, and load configuration
    args = parse_arguments(parser, unit_options_spec)
    perform_checks_init_logger(args, parser, 'unit')
    tkltest_config = load_configuration(args)

    unjar_paths = __unjar_path(tkltest_config)

    # process generate/execute commands
    try:
        if args.command == 'execute':
            execute.process_execute_command(args, tkltest_config)
        elif args.command == 'generate':
            generate.process_generate_command(args, tkltest_config)
    finally:
        for path in unjar_paths:
            if os.path.isdir(path):
                shutil.rmtree(path)


if __name__ == '__main__':  # pragma: no cover
    main()
