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
import os
import sys
import unittest

sys.path.insert(0, os.path.abspath(os.path.dirname(__file__))+os.sep+'..')
from tkltest.util import config_util, config_options, constants

class ConfigTest(unittest.TestCase):

    config_file = os.path.join('test', 'data', 'irs', 'tkltest_config.toml')
    # partitions_file = os.path.join('test', 'data', 'irs', 'refactored', 'PartitionsFile.json')

    def test_config_util_init(self) -> None:
        """Test config init"""
        config1 = config_util.init_config()
        config2 = config_util.init_config()
        self.assertDictEqual(config1, config2)

    def test_config_load_noargs(self) -> None:
        """Test config load without args"""
        self.assertDictEqual(config_util.init_config(), config_util.load_config())
        config = config_util.load_config(config_file=self.config_file)

    def test_config_load_args_noconfig(self) -> None:
        """Test config load with args and no config"""
        args = argparse.Namespace()
        args.command = 'generate'
        args.sub_command = 'ctd-amplified'
        args.config_file = self.config_file
        args.base_test_generator = 'evosuite'
        args.no_diff_assertions = True
        config = config_util.load_config(args=args)
        self.assertEqual(constants.BASE_TEST_GENERATORS['evosuite'],
                         config['generate']['ctd_amplified']['base_test_generator'])
        self.assertTrue(config['generate']['no_diff_assertions'])

        # add partitions file to args and load config
        # args.partitions_file = self.partitions_file
        config_util.load_config(args=args)


    def test_config_print(self) -> None:
        """Test print options"""
        config_options.print_options_with_help()
        config_options.print_options_with_help(tablefmt='github')
        config_options.print_options_with_help(command='generate')
