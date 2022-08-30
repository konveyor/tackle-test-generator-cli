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
import logging
import logging.handlers
import sys


from .generate_standalone import generate_schemathesis
from tkltest.util import config_util
from tkltest.util.api import dir_util
from tkltest.util.logging_util import tkltest_status


def process_generate_command(args, config):
    """Processes the generate command.

    Processes the generate command and generates test cases based on the subcommand specified with
    generate: {schemathesis}

    Args:
        args: command-line arguments
        config: loaded configuration options
    """
    logging.info('Processing generate command')
    test_dir = dir_util.get_test_directory(config)

    if args.sub_command == "schemathesis":
        generate_schemathesis(config, test_dir)
    else:
        tkltest_status("sub command " + args.sub_command + " not supported", error=True)
        sys.exit(1)


if __name__ == '__main__':  # pragma: no cover
    config_file = 'test/api/data/news/tkltest_api_config.toml'
    args = argparse.Namespace()
    args.command = 'generate'
    args.sub_command = 'schemathesis'
    args.verbose = True
    config = config_util.load_config(test_level='api', args=args, config_file=config_file)
    process_generate_command(args=args, config=config)
