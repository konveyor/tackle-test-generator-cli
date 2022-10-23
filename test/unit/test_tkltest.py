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

import os
import shutil
import sys
import unittest

sys.path.insert(0, os.path.abspath(os.path.dirname(__file__))+os.sep+'..')
from tkltest.unit import tkltest_unit
from tkltest.util import constants

class TkltestTest(unittest.TestCase):

    app_name = 'irs'
    config_file = os.path.join('test', 'data', app_name, 'tkltest_config.toml')
    test_directory = '__' + app_name + constants.TKLTEST_DEFAULT_RANDOOP_TEST_DIR_SUFFIX
    gen_config_file = '__tkltest.cfg'
    sys_argv = sys.argv

    @classmethod
    def setUpClass(cls) -> None:
        shutil.rmtree(cls.test_directory, ignore_errors=True)
        shutil.rmtree(cls.gen_config_file, ignore_errors=True)

    @classmethod
    def tearDownClass(cls) -> None:
        shutil.rmtree(cls.test_directory, ignore_errors=True)
        shutil.rmtree(cls.gen_config_file, ignore_errors=True)

    def test_tkltest_main_config_list(self) -> None:
        """Test main function of tkltest with "config list" command"""
        args = ['--config-file', self.config_file, 'config', 'list']
        sys.argv = [self.sys_argv[0]] + args
        with self.assertRaises(SystemExit) as sysexit:
            tkltest_unit.main()
        self.assertEqual(sysexit.exception.code, 0)

    def test_tkltest_main_config_init(self) -> None:
        """Test main function of tkltest with "config init" command"""
        args = ['--config-file', self.config_file, 'config', 'init']
        sys.argv = [self.sys_argv[0]] + args
        with self.assertRaises(SystemExit) as sysexit:
            tkltest_unit.main()
        self.assertEqual(sysexit.exception.code, 0)

    def test_tkltest_main_config_init_file(self) -> None:
        """Test main function of tkltest with "config init file" command"""
        args = ['--config-file', self.config_file, 'config', 'init', '--file', self.gen_config_file]
        sys.argv = [self.sys_argv[0]] + args
        with self.assertRaises(SystemExit) as sysexit:
            tkltest_unit.main()
        self.assertEqual(sysexit.exception.code, 0)
        self.assertTrue(os.path.isfile(self.gen_config_file))

    def test_tkltest_main_generate_execute_randoop(self) -> None:
        """Test main function of tkltest with "generate randoop" and "execute" commands"""
        args = ['--config-file', self.config_file, '--test-directory', self.test_directory, 'generate', 'randoop']
        sys.argv = [self.sys_argv[0]] + args
        tkltest_unit.main()

        args = ['--config-file', self.config_file, '--test-directory', self.test_directory, 'execute']
        sys.argv = [self.sys_argv[0]] + args
        tkltest_unit.main()
