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
import json
import os
import shutil
import sys
import unittest
import copy

sys.path.insert(0, os.path.abspath(os.path.dirname(__file__))+os.sep+'..')
from tkltest.generate.unit import generate
from tkltest.execute.unit import execute
from tkltest.util import config_util, constants, dir_util

from pathlib import Path
import xml.etree.ElementTree as ElementTree

class GenerateExecuteTest(unittest.TestCase):

    # directory containing test applications
    test_data_dir = os.path.join('test', 'data')
    # test_data_dir = os.path.join('data')

    test_apps = {
        'irs': {
            'config_file': os.path.join(test_data_dir, 'irs', 'tkltest_config.toml'),
            'test_directory': '__irs-generated-tests',
            'partitions_file': os.path.join(test_data_dir, 'irs', 'refactored', 'PartitionsFile.json'),
            'target_class_list': ["irs.IRS"],
            'excluded_class_list': ["irs.Employer"]
        },
        'splitNjoin': {
            'config_file': os.path.join(test_data_dir, 'splitNjoin', 'tkltest_config.toml'),
            'test_directory': '__splitNjoin-generated-tests',
        }
    }
    test_list1 = ['irs']
    test_list2 = ['splitNjoin']

    args = argparse.Namespace()

    def setUp(self) -> None:
        for app_name in self.test_apps.keys():
            app_info = self.test_apps[app_name]
            dir_util.cd_cli_dir()
            # remove directories and files created during test generation
            shutil.rmtree(app_info['test_directory'], ignore_errors=True)
            shutil.rmtree(os.path.join(constants.TKLTEST_UNIT_OUTPUT_DIR_PREFIX + app_name, app_name + constants.TKLTEST_MAIN_REPORT_DIR_SUFFIX), ignore_errors=True)
            shutil.rmtree(os.path.join(constants.TKLTEST_UNIT_OUTPUT_DIR_PREFIX + app_name, app_name + constants.TKL_EXTENDER_SUMMARY_FILE_SUFFIX), ignore_errors=True)
            shutil.rmtree(os.path.join(constants.TKLTEST_UNIT_OUTPUT_DIR_PREFIX + app_name, app_name + constants.TKL_EXTENDER_COVERAGE_FILE_SUFFIX), ignore_errors=True)

            # load and set config for app
            app_config = config_util.load_config(config_file=app_info['config_file'])
            app_config['general']['test_directory'] = app_info['test_directory']
            app_config['generate']['time_limit'] = 1
            app_config['generate']['ctd_amplified']['num_seq_executions'] = 1
            app_info['config'] = app_config

    def test_generate_execute_ctdamplified_combined_classlist_diffassert(self) -> None:
        """Test "generate ctd-amplified" and "execute": base_test_generator=combined scope=target_class_list"""
        for app_name in self.test_list1:
            app_info = self.test_apps[app_name]
            self.__reuse_basic_blocks(app_name)
            #config['generate']['ctd_amplified']['augment_coverage'] = False
            # set up config and generate tests
            config = app_info['config']
            config['generate']['ctd_amplified']['base_test_generator'] = constants.BASE_TEST_GENERATORS['combined']
            config['generate']['partitions_file'] = ''
            config['generate']['target_class_list'] = app_info['target_class_list']
            self.__process_generate(subcommand='ctd-amplified', config=config)

            # assert that expected generate resources are created
            self.__assert_generate_resources(app_name=app_name, generate_subcmd='ctd-amplified')

            # execute tests
            self.__process_execute(config=config)

            # assert that expected execute resources are created

            self.__assert_execute_resources(app_name=app_name)

    def test_generate_execute_ctdamplified_randoop_classlist_diffassert(self) -> None:
        """Test "generate ctd-amplified" and "execute": base_test_generator=randoop scope=target_class_list"""
        for app_name in self.test_list1:
            app_info = self.test_apps[app_name]
            self.__reuse_basic_blocks(app_name)
            #config['generate']['ctd_amplified']['augment_coverage'] = False

            # set up config and generate tests
            config = app_info['config']
            config['generate']['ctd_amplified']['base_test_generator'] = constants.BASE_TEST_GENERATORS['randoop']
            config['generate']['partitions_file'] = ''
            config['generate']['target_class_list'] = app_info['target_class_list']
            self.__process_generate(subcommand='ctd-amplified', config=config)

            # assert that expected generate resources are created
            self.__assert_generate_resources(app_name=app_name, generate_subcmd='ctd-amplified')

            # execute tests
            config['general']['offline_instrumentation'] = True
            config['general']['build_type'] = 'maven'
            self.__process_execute(config=config)

            # assert that expected execute resources are created
            self.__assert_execute_resources(app_name=app_name)

    def test_generate_execute_ctdamplified_evosuite_classlist_diffassert(self) -> None:
        """Test "generate ctd-amplified" and "execute": base_test_generator=evosuite scope=target_class_list"""
        for app_name in self.test_list1:
            app_info = self.test_apps[app_name]
            self.__reuse_basic_blocks(app_name)
            #config['generate']['ctd_amplified']['augment_coverage'] = False
            config = app_info['config']
            config['generate']['ctd_amplified']['base_test_generator'] = constants.BASE_TEST_GENERATORS['evosuite']
            config['generate']['partitions_file'] = ''
            config['generate']['target_class_list'] = app_info['target_class_list']
            self.__process_generate(subcommand='ctd-amplified', config=config)

            # assert that expected generate resources are created
            self.__assert_generate_resources(app_name=app_name, generate_subcmd='ctd-amplified')

            # execute tests
            self.__process_execute(config=config)

            # assert that expected execute resources are created
            self.__assert_execute_resources(app_name=app_name)

    def test_generate_execute_ctdamplified_combined_allclasses_diffassert_level_2(self) -> None:
        """Test "generate ctd-amplified" and "execute": base_test_generator=combined scope=all_classes"""
        for app_name in self.test_list1:
            app_info = self.test_apps[app_name]
            self.__reuse_basic_blocks(app_name)
            #config['generate']['ctd_amplified']['augment_coverage'] = False
            config = app_info['config']
            config['generate']['ctd_amplified']['base_test_generator'] = constants.BASE_TEST_GENERATORS['combined']
            config['generate']['partitions_file'] = ''
            config['generate']['target_class_list'] = []
            config['generate']['ctd_amplified']['interaction_level'] = 2
            self.__process_generate(subcommand='ctd-amplified', config=config)

            # assert that expected generate resources are created
            self.__assert_generate_resources(app_name=app_name, generate_subcmd='ctd-amplified')

            # execute tests
            self.__process_execute(config=config)

            # assert that expected execute resources are created
            self.__assert_execute_resources(app_name=app_name)

    def test_generate_execute_ctdamplified_combined_allclasses_but_excluded_diffassert(self) -> None:
        """Test "generate ctd-amplified" and "execute": base_test_generator=combined scope=all_classes_but_excluded"""
        for app_name in self.test_list1:
            app_info = self.test_apps[app_name]
            self.__reuse_basic_blocks(app_name)
            #config['generate']['ctd_amplified']['augment_coverage'] = False
            config = app_info['config']
            config['generate']['ctd_amplified']['base_test_generator'] = constants.BASE_TEST_GENERATORS['combined']
            config['generate']['partitions_file'] = ''
            config['generate']['target_class_list'] = []
            config['generate']['excluded_class_list'] = app_info['excluded_class_list']
            self.__process_generate(subcommand='ctd-amplified', config=config)

            # assert that expected generate resources are created
            self.__assert_generate_resources(app_name=app_name, generate_subcmd='ctd-amplified')

            # execute tests
            self.__process_execute(config=config)

            # assert that expected execute resources are created
            self.__assert_execute_resources(app_name=app_name)

    def test_generate_execute_ctdamplified_randoop_allclasses_diffassert(self) -> None:
        """Test "generate ctd-amplified" and "execute": base_test_generator=randoop scope=all_classes"""
        for app_name in self.test_list1:
            app_info = self.test_apps[app_name]
            self.__reuse_basic_blocks(app_name)
            #config['generate']['ctd_amplified']['augment_coverage'] = False
            config = app_info['config']
            config['generate']['ctd_amplified']['base_test_generator'] = constants.BASE_TEST_GENERATORS['randoop']
            config['generate']['partitions_file'] = ''
            config['generate']['target_class_list'] = []
            self.__process_generate(subcommand='ctd-amplified', config=config)

            # assert that expected generate resources are created
            self.__assert_generate_resources(app_name=app_name, generate_subcmd='ctd-amplified')

            # execute tests
            self.__process_execute(config=config)

            # assert that expected execute resources are created
            self.__assert_execute_resources(app_name=app_name)

    def test_generate_execute_ctdamplified_evosuite_allclasses_diffassert(self) -> None:
        """Test "generate ctd-amplified" and "execute": base_test_generator=evosuite scope=all_classes"""
        for app_name in self.test_list1:
            app_info = self.test_apps[app_name]
            self.__reuse_basic_blocks(app_name)
            #config['generate']['ctd_amplified']['augment_coverage'] = False
            config = app_info['config']
            config['generate']['ctd_amplified']['base_test_generator'] = constants.BASE_TEST_GENERATORS['evosuite']
            config['generate']['partitions_file'] = ''
            config['generate']['target_class_list'] = []
            self.__process_generate(subcommand='ctd-amplified', config=config)

            # assert that expected generate resources are created
            self.__assert_generate_resources(app_name=app_name, generate_subcmd='ctd-amplified')

            # execute tests
            self.__process_execute(config=config)

            # assert that expected execute resources are created
            self.__assert_execute_resources(app_name=app_name)

    def test_generate_ctdamplified_evosuite_allclasses_augmentcoverage(self) -> None:
        """Test "generate ctd-amplified": base_test_generator=evosuite scope=all_classes augment_coverage=true"""
        for app_name in self.test_list1:
            app_info = self.test_apps[app_name]
            config = app_info['config']
            config['generate']['ctd_amplified']['base_test_generator'] = constants.BASE_TEST_GENERATORS['evosuite']
            config['generate']['ctd_amplified']['augment_coverage'] = True
            config['generate']['partitions_file'] = ''
            config['generate']['target_class_list'] = []
            self.__process_generate(subcommand='ctd-amplified', config=config)

            # assert that expected generate resources are created
            self.__assert_generate_resources(app_name=app_name, generate_subcmd='ctd-amplified')

    def test_generate_ctdamplified_evosuite_allclasses_augmentcoverage_with_user_coverage(self) -> None:
        """Test "generate ctd-amplified": base_test_generator=evosuite scope=all_classes augment_coverage=true"""
        for app_name in self.test_list1:
            app_info = self.test_apps[app_name]
            config = app_info['config']
            config['generate']['ctd_amplified']['base_test_generator'] = constants.BASE_TEST_GENERATORS['evosuite']
            config['generate']['ctd_amplified']['augment_coverage'] = True
            config['generate']['partitions_file'] = ''
            config['generate']['target_class_list'] = []
            config['dev_tests']['use_for_augmentation'] = True
            self.__process_generate(subcommand='ctd-amplified', config=config)

            # assert that expected generate resources are created
            self.__assert_generate_resources(app_name=app_name, generate_subcmd='ctd-amplified')

    def test_generate_ctdamplified_evosuite_allclasses_augmentcoverage_all_options(self) -> None:
        """Test "generate ctd-amplified": base_test_generator=evosuite scope=all_classes """
        for app_name in self.test_list1:
            app_info = self.test_apps[app_name]
            tests_options = [
                {'build_type': 'ant', 'off_inst': True, 'use_dev': True},
                {'build_type': 'ant', 'off_inst': False, 'use_dev': True},
                {'build_type': 'maven', 'off_inst': True, 'use_dev': False},
                {'build_type': 'maven', 'off_inst': False, 'use_dev': False},
                {'build_type': 'gradle', 'off_inst': True, 'use_dev': False},
                {'build_type': 'gradle', 'off_inst': False, 'use_dev': True}

            ]
            for tests_option in tests_options:
                dir_util.cd_cli_dir()
                self.__reuse_basic_blocks(app_name)
                config = copy.deepcopy(app_info['config'])
                shutil.rmtree(config['general']['test_directory'], ignore_errors=True)
                config['general']['build_type'] = tests_option['build_type']
                config['general']['offline_instrumentation'] = tests_option['off_inst']
                config['generate']['ctd_amplified']['base_test_generator'] = constants.BASE_TEST_GENERATORS['evosuite']
                config['generate']['ctd_amplified']['reuse_base_tests'] = True
                config['generate']['partitions_file'] = ''
                config['generate']['target_class_list'] = []
                config['dev_tests']['use_for_augmentation'] = tests_option['use_dev']

                self.__process_generate(subcommand='ctd-amplified', config=config)
                # assert that expected generate resources are created
                self.__assert_generate_resources(app_name=app_name, generate_subcmd='ctd-amplified')
                self.__assert_augment_resources(
                    app_name=app_name,
                    test_directory=config['general']['test_directory'],
                    orig_test_directory=os.path.join(self.test_data_dir, app_name, 'irs-ctd-amplified-tests'))

    @unittest.skip('')
    def test_generate_execute_ctdamplified_combined_partitions_diffassert(self) -> None:
        """Test "generate ctd-amplified" and "execute": base_test_generator=combined scope=partitions_file"""
        for app_name in self.test_list1:
            app_info = self.test_apps[app_name]
            self.__reuse_basic_blocks(app_name)
            #config['generate']['ctd_amplified']['augment_coverage'] = False
            config = app_info['config']
            config['generate']['ctd_amplified']['base_test_generator'] = constants.BASE_TEST_GENERATORS['combined']
            config['generate']['partitions_file'] = app_info['partitions_file']
            config['generate']['target_class_list'] = []
            self.__process_generate(subcommand='ctd-amplified', config=config)

            # assert that expected generate resources are created
            self.__assert_generate_resources(app_name=app_name, generate_subcmd='ctd-amplified')

            # execute tests
            self.__process_execute(config=config)

            # assert that expected execute resources are created
            self.__assert_execute_resources(app_name=app_name)

    @unittest.skip('')
    def test_generate_execute_ctdamplified_randoop_partitions_diffassert(self) -> None:
        """Test "generate ctd-amplified" and "execute": base_test_generator=randoop scope=partitions_file"""
        for app_name in self.test_list1:
            app_info = self.test_apps[app_name]
            self.__reuse_basic_blocks(app_name)
            #config['generate']['ctd_amplified']['augment_coverage'] = False
            config = app_info['config']
            config['generate']['ctd_amplified']['base_test_generator'] = constants.BASE_TEST_GENERATORS['randoop']
            config['generate']['partitions_file'] = app_info['partitions_file']
            config['generate']['target_class_list'] = []
            self.__process_generate(subcommand='ctd-amplified', config=config)

            # assert that expected generate resources are created
            self.__assert_generate_resources(app_name=app_name, generate_subcmd='ctd-amplified')

            # execute tests
            self.__process_execute(config=config)

            # assert that expected execute resources are created
            self.__assert_execute_resources(app_name=app_name)

    @unittest.skip('')
    def test_generate_execute_ctdamplified_evosuite_partitions_diffassert(self) -> None:
        """Test "generate ctd-amplified" and "execute": base_test_generator=evosuite scope=partitions_file"""
        for app_name in self.test_list1:
            app_info = self.test_apps[app_name]
            self.__reuse_basic_blocks(app_name)
            #config['generate']['ctd_amplified']['augment_coverage'] = False
            config = app_info['config']
            config['generate']['partitions_file'] = app_info['partitions_file']
            config['generate']['target_class_list'] = []
            self.__process_generate(subcommand='ctd-amplified', config=config)

            # assert that expected generate resources are created
            self.__assert_generate_resources(app_name=app_name, generate_subcmd='ctd-amplified')

            # execute tests
            self.__process_execute(config=config)

            # assert that expected execute resources are created
            self.__assert_execute_resources(app_name=app_name)

    def test_generate_execute_ctdamplified_combined_classlist_nodiffassert_reuse(self) -> None:
        """Test "generate ctd-amplified" and "execute": base_test_generator=combined scope=target_class_list no_diff_assertions"""
        for app_name in self.test_list1:
            app_info = self.test_apps[app_name]
            self.__reuse_basic_blocks(app_name)
            #config['generate']['ctd_amplified']['augment_coverage'] = False
            config = app_info['config']
            config['generate']['ctd_amplified']['base_test_generator'] = constants.BASE_TEST_GENERATORS['combined']
            config['generate']['partitions_file'] = ''
            config['generate']['target_class_list'] = app_info['target_class_list']
            config['generate']['no_diff_assertions'] = True
            self.__process_generate(subcommand='ctd-amplified', config=config)

            # assert that expected generate resources are created
            self.__assert_generate_resources(app_name=app_name, generate_subcmd='ctd-amplified')

            # regenerate while reusing previously generated basic block files
            config['generate']['ctd_amplified']['reuse_base_tests'] = True

            self.__process_generate(subcommand='ctd-amplified', config=config)

            # assert that expected generate resources are created
            self.__assert_generate_resources(app_name=app_name, generate_subcmd='ctd-amplified')

            # execute tests
            self.__process_execute(config=config)

            # assert that expected execute resources are created
            self.__assert_execute_resources(app_name=app_name)

    def test_generate_execute_ctdamplified_randoop_classlist_nodiffassert(self) -> None:
        """Test "generate ctd-amplified" and "execute": base_test_generator=randoop scope=target_class_list no_diff_assertions"""
        for app_name in self.test_list1:
            app_info = self.test_apps[app_name]
            self.__reuse_basic_blocks(app_name)
            #config['generate']['ctd_amplified']['augment_coverage'] = False
            config = app_info['config']
            config['generate']['ctd_amplified']['base_test_generator'] = constants.BASE_TEST_GENERATORS['randoop']
            config['generate']['partitions_file'] = ''
            config['generate']['target_class_list'] = app_info['target_class_list']
            config['generate']['no_diff_assertions'] = True
            self.__process_generate(subcommand='ctd-amplified', config=config)

            # assert that expected generate resources are created
            self.__assert_generate_resources(app_name=app_name, generate_subcmd='ctd-amplified')

            # execute tests
            self.__process_execute(config=config)

            # assert that expected execute resources are created
            self.__assert_execute_resources(app_name=app_name)

    def test_generate_execute_ctdamplified_evosuite_classlist_nodiffassert(self) -> None:
        """Test "generate ctd-amplified" and "execute": base_test_generator=evosuite scope=target_class_list no_diff_assertions"""
        for app_name in self.test_list1:
            app_info = self.test_apps[app_name]
            self.__reuse_basic_blocks(app_name)
            #config['generate']['ctd_amplified']['augment_coverage'] = False
            config = app_info['config']
            config['generate']['ctd_amplified']['base_test_generator'] = constants.BASE_TEST_GENERATORS['evosuite']
            config['generate']['partitions_file'] = ''
            config['generate']['target_class_list'] = app_info['target_class_list']
            config['generate']['no_diff_assertions'] = True
            self.__process_generate(subcommand='ctd-amplified', config=config)

            # assert that expected generate resources are created
            self.__assert_generate_resources(app_name=app_name, generate_subcmd='ctd-amplified')

            # execute tests
            self.__process_execute(config=config)

            # assert that expected execute resources are created
            self.__assert_execute_resources(app_name=app_name)

    def test_generate_execute_ctdamplified_combined_allclasses_nodiffassert(self) -> None:
        """Test "generate ctd-amplified" and "execute": base_test_generator=combined scope=all_classes no_diff_assertions"""
        for app_name in self.test_list1:
            app_info = self.test_apps[app_name]
            self.__reuse_basic_blocks(app_name)
            #config['generate']['ctd_amplified']['augment_coverage'] = False
            config = app_info['config']
            config['generate']['ctd_amplified']['base_test_generator'] = constants.BASE_TEST_GENERATORS['combined']
            config['generate']['partitions_file'] = ''
            config['generate']['target_class_list'] = []
            config['generate']['no_diff_assertions'] = True
            self.__process_generate(subcommand='ctd-amplified', config=config)

            # assert that expected generate resources are created
            self.__assert_generate_resources(app_name=app_name, generate_subcmd='ctd-amplified')

            # execute tests
            self.__process_execute(config=config)

    def test_generate_execute_ctdamplified_randoop_allclasses_nodiffassert(self) -> None:
        """Test "generate ctd-amplified" and "execute": base_test_generator=randoop scope=all_classes no_diff_assertions"""
        for app_name in self.test_list1:
            app_info = self.test_apps[app_name]
            self.__reuse_basic_blocks(app_name)
            #config['generate']['ctd_amplified']['augment_coverage'] = False
            config = app_info['config']
            config['generate']['ctd_amplified']['base_test_generator'] = constants.BASE_TEST_GENERATORS['randoop']
            config['generate']['partitions_file'] = ''
            config['generate']['target_class_list'] = []
            config['generate']['no_diff_assertions'] = True
            config['general']['reports_path'] = app_name+"-user-reports"
            self.__process_generate(subcommand='ctd-amplified', config=config)

            # assert that expected generate resources are created
            self.__assert_generate_resources(app_name=app_name, generate_subcmd='ctd-amplified')

            # execute tests
            self.__process_execute(config=config)

            # assert that expected execute resources are created
            self.__assert_execute_resources(app_name=app_name, reports_path=app_name+"-user-reports")

    def test_generate_execute_ctdamplified_evosuite_allclasses_nodiffassert(self) -> None:
        """Test "generate ctd-amplified" and "execute": base_test_generator=evosuite scope=all_classes no_diff_assertions"""
        for app_name in self.test_list1:
            app_info = self.test_apps[app_name]
            self.__reuse_basic_blocks(app_name)
            #config['generate']['ctd_amplified']['augment_coverage'] = False
            config = app_info['config']
            config['generate']['ctd_amplified']['base_test_generator'] = constants.BASE_TEST_GENERATORS['evosuite']
            config['generate']['partitions_file'] = ''
            config['generate']['target_class_list'] = []
            config['generate']['no_diff_assertions'] = True
            self.__process_generate(subcommand='ctd-amplified', config=config)

            # assert that expected generate resources are created
            self.__assert_generate_resources(app_name=app_name, generate_subcmd='ctd-amplified')

            # execute tests
            self.__process_execute(config=config)

    @unittest.skip('')
    def test_generate_execute_ctdamplified_combined_partitions_nodiffassert(self) -> None:
        """Test "generate ctd-amplified" and "execute": base_test_generator=combined scope=partitions_file no_diff_assertions"""
        for app_name in self.test_list1:
            app_info = self.test_apps[app_name]
            self.__reuse_basic_blocks(app_name)
            #config['generate']['ctd_amplified']['augment_coverage'] = False
            config = app_info['config']
            config['generate']['ctd_amplified']['base_test_generator'] = constants.BASE_TEST_GENERATORS['combined']
            config['generate']['partitions_file'] = app_info['partitions_file']
            config['generate']['target_class_list'] = []
            config['generate']['no_diff_assertions'] = True
            self.__process_generate(subcommand='ctd-amplified', config=config)

            # assert that expected generate resources are created
            self.__assert_generate_resources(app_name=app_name, generate_subcmd='ctd-amplified')

            # execute tests
            self.__process_execute(config=config)

            # assert that expected execute resources are created
            self.__assert_execute_resources(app_name=app_name)

    @unittest.skip('')
    def test_generate_execute_ctdamplified_randoop_partitions_nodiffassert(self) -> None:
        """Test "generate ctd-amplified" and "execute": base_test_generator=randoop scope=partitions_file no_diff_assertions"""
        for app_name in self.test_list1:
            app_info = self.test_apps[app_name]
            self.__reuse_basic_blocks(app_name)
            #config['generate']['ctd_amplified']['augment_coverage'] = False
            config = app_info['config']
            config['generate']['ctd_amplified']['base_test_generator'] = constants.BASE_TEST_GENERATORS['randoop']
            config['generate']['partitions_file'] = app_info['partitions_file']
            config['generate']['target_class_list'] = []
            config['generate']['no_diff_assertions'] = True
            self.__process_generate(subcommand='ctd-amplified', config=config)

            # assert that expected generate resources are created
            self.__assert_generate_resources(app_name=app_name, generate_subcmd='ctd-amplified')

            # execute tests
            config['execute']['code_coverage'] = False
            self.__process_execute(config=config)

            # assert that expected execute resources are created
            self.__assert_execute_resources(app_name=app_name, code_coverage=False)

    @unittest.skip('')
    def test_generate_execute_ctdamplified_evosuite_partitions_nodiffassert(self) -> None:
        """Test "generate ctd-amplified" and "execute": base_test_generator=evosuite scope=partitions_file no_diff_assertions"""
        for app_name in self.test_list1:
            app_info = self.test_apps[app_name]
            self.__reuse_basic_blocks(app_name)
            #config['generate']['ctd_amplified']['augment_coverage'] = False
            config = app_info['config']
            config['generate']['ctd_amplified']['base_test_generator'] = constants.BASE_TEST_GENERATORS['evosuite']
            config['generate']['partitions_file'] = app_info['partitions_file']
            config['generate']['target_class_list'] = []
            config['generate']['no_diff_assertions'] = True
            self.__process_generate(subcommand='ctd-amplified', config=config)

            # assert that expected generate resources are created
            self.__assert_generate_resources(app_name=app_name, generate_subcmd='ctd-amplified')

            # execute tests
            self.__process_execute(config=config)

            # assert that expected execute resources are created
            self.__assert_execute_resources(app_name=app_name)

    def test_generate_execute_randoop_classlist_diffassert(self) -> None:
        """Test "generate randoop" and "execute": scope=target_class_list"""
        for app_name in self.test_list1:
            app_info = self.test_apps[app_name]
            self.__reuse_basic_blocks(app_name)
            #config['generate']['ctd_amplified']['augment_coverage'] = False

            # set up config and generate tests
            config = app_info['config']
            config['generate']['partitions_file'] = ''
            config['generate']['target_class_list'] = app_info['target_class_list']
            self.__process_generate(subcommand='randoop', config=config)

            # assert that expected generate resources are created
            self.__assert_generate_resources(app_name=app_name, generate_subcmd='randoop')

            # execute tests
            self.__process_execute(config=config)

            # assert that expected execute resources are created
            self.__assert_execute_resources(app_name=app_name)

    def test_generate_execute_randoop_allclasses_diffassert(self) -> None:
        """Test "generate randoop" and "execute": scope=all_classes"""
        for app_name in self.test_list1:
            app_info = self.test_apps[app_name]
            self.__reuse_basic_blocks(app_name)
            #config['generate']['ctd_amplified']['augment_coverage'] = False

            # set up config and generate tests
            config = app_info['config']
            config['generate']['partitions_file'] = ''
            config['generate']['target_class_list'] = []
            self.__process_generate(subcommand='randoop', config=config)

            # assert that expected generate resources are created
            self.__assert_generate_resources(app_name=app_name, generate_subcmd='randoop')

            # execute tests
            self.__process_execute(config=config)

            # assert that expected execute resources are created
            self.__assert_execute_resources(app_name=app_name)

    def test_generate_execute_randoop_classlist_nodiffassert(self) -> None:
        """Test "generate randoop" and "execute": scope=target_class_list no_diff_assertions"""
        for app_name in self.test_list1:
            app_info = self.test_apps[app_name]
            self.__reuse_basic_blocks(app_name)
            #config['generate']['ctd_amplified']['augment_coverage'] = False

            # set up config and generate tests
            config = app_info['config']
            config['generate']['partitions_file'] = ''
            config['generate']['target_class_list'] = app_info['target_class_list']
            config['generate']['no_diff_assertions'] = True
            self.__process_generate(subcommand='randoop', config=config)

            # assert that expected generate resources are created
            self.__assert_generate_resources(app_name=app_name, generate_subcmd='randoop')

            # execute tests
            self.__process_execute(config=config)

            # assert that expected execute resources are created
            self.__assert_execute_resources(app_name=app_name)

    def test_generate_execute_randoop_allclasses_nodiffassert(self) -> None:
        """Test "generate randoop" and "execute": scope=all_classes no_diff_assertions"""
        for app_name in self.test_list1:
            app_info = self.test_apps[app_name]
            self.__reuse_basic_blocks(app_name)
            #config['generate']['ctd_amplified']['augment_coverage'] = False

            # set up config and generate tests
            config = app_info['config']
            config['generate']['partitions_file'] = ''
            config['generate']['target_class_list'] = []
            config['generate']['no_diff_assertions'] = True
            self.__process_generate(subcommand='randoop', config=config)

            # assert that expected generate resources are created
            self.__assert_generate_resources(app_name=app_name, generate_subcmd='randoop')

            # execute tests
            self.__process_execute(config=config)

            # assert that expected execute resources are created
            self.__assert_execute_resources(app_name=app_name)

    @unittest.skip('')
    def test_generate_execute_evosuite_classlist_diffassert(self) -> None:
        """Test "generate evosuite" and "execute": scope=target_class_list"""
        for app_name in self.test_list1:
            app_info = self.test_apps[app_name]
            self.__reuse_basic_blocks(app_name)
            #config['generate']['ctd_amplified']['augment_coverage'] = False

            # set up config and generate tests
            config = app_info['config']
            config['generate']['partitions_file'] = ''
            config['generate']['target_class_list'] = app_info['target_class_list']
            self.__process_generate(subcommand='evosuite', config=config)

            # assert that expected generate resources are created
            self.__assert_generate_resources(app_name=app_name, generate_subcmd='evosuite')

            # execute tests
            self.__process_execute(config=config)

            # assert that expected execute resources are created
            self.__assert_execute_resources(app_name=app_name)

    def test_generate_execute_evosuite_allclasses_diffassert(self) -> None:
        """Test "generate evosuite" and "execute": scope=all_classes"""
        for app_name in self.test_list1:
            app_info = self.test_apps[app_name]
            self.__reuse_basic_blocks(app_name)
            #config['generate']['ctd_amplified']['augment_coverage'] = False

            # set up config and generate tests
            config = app_info['config']
            config['generate']['partitions_file'] = ''
            config['generate']['target_class_list'] = []
            self.__process_generate(subcommand='evosuite', config=config)

            # assert that expected generate resources are created
            self.__assert_generate_resources(app_name=app_name, generate_subcmd='evosuite')

            # execute tests
            self.__process_execute(config=config)

            # assert that expected execute resources are created
            self.__assert_execute_resources(app_name=app_name)

    @unittest.skip('')
    def test_generate_execute_evosuite_classlist_nodiffassert(self) -> None:
        """Test "generate evosuite" and "execute": scope=target_class_list no_diff_assertions"""
        for app_name in self.test_list1:
            app_info = self.test_apps[app_name]
            self.__reuse_basic_blocks(app_name)
            #config['generate']['ctd_amplified']['augment_coverage'] = False

            # set up config and generate tests
            config = app_info['config']
            config['generate']['partitions_file'] = ''
            config['generate']['target_class_list'] = app_info['target_class_list']
            config['generate']['no_diff_assertions'] = True
            self.__process_generate(subcommand='evosuite', config=config)

            # assert that expected generate resources are created
            self.__assert_generate_resources(app_name=app_name, generate_subcmd='evosuite')

            # execute tests
            self.__process_execute(config=config)

            # assert that expected execute resources are created
            self.__assert_execute_resources(app_name=app_name)

    def test_generate_execute_evosuite_allclasses_nodiffassert(self) -> None:
        """Test "generate evosuite" and "execute": scope=all_classes no_diff_assertions"""
        for app_name in self.test_list1:
            app_info = self.test_apps[app_name]
            self.__reuse_basic_blocks(app_name)
            #config['generate']['ctd_amplified']['augment_coverage'] = False

            # set up config and generate tests
            config = app_info['config']
            config['generate']['partitions_file'] = ''
            config['generate']['target_class_list'] = []
            config['generate']['no_diff_assertions'] = True
            self.__process_generate(subcommand='evosuite', config=config)

            # assert that expected generate resources are created
            self.__assert_generate_resources(app_name=app_name, generate_subcmd='evosuite')

            # execute tests
            self.__process_execute(config=config)

            # assert that expected execute resources are created
            self.__assert_execute_resources(app_name=app_name)

    def test_generate_execute_gradle(self) -> None:
        """Test getting dependencies and "execute": using gradle"""
        for app_name in self.test_list2:
            app_info = self.test_apps[app_name]

            # set up config and generate tests
            config = app_info['config']
            config['generate']['ctd_amplified']['base_test_generator'] = constants.BASE_TEST_GENERATORS['combined']
            self.__process_generate(subcommand='ctd-amplified', config=config)

            # assert that expected generate resources are created
            self.__assert_generate_resources(app_name=app_name, generate_subcmd='ctd-amplified')

            # execute tests
            config['general']['app_classpath_file'] = ''
            self.__process_execute(config=config)

            # assert that expected execute resources are created
            self.__assert_execute_resources(app_name=app_name)



    def test_execute_ctdamplified_compare_coverage(self) -> None:
        """execute": comparing coverage reports"""
        for app_name in self.test_list1:
            app_info = self.test_apps[app_name]

            main_report_dir = app_name + constants.TKLTEST_MAIN_REPORT_DIR_SUFFIX
            generated_test_directory = 'irs-generated-testing'
            pre_generated_test = os.path.join(self.test_data_dir, app_name, 'irs-ctd-amplified-tests')
            user_tests = os.path.join(self.test_data_dir, app_name, 'user-tests')
            # execute tests
            for build_type in ['ant', 'gradle', 'maven']:

                config = copy.deepcopy(app_info['config'])
                config['execute']['code_coverage'] = True
                config['dev_tests']['compare_code_coverage'] = True
                shutil.rmtree(main_report_dir, ignore_errors=True)
                shutil.rmtree(generated_test_directory, ignore_errors=True)
                shutil.copytree(pre_generated_test, generated_test_directory)
                config['general']['build_type'] = build_type
                config['general']['test_directory'] = generated_test_directory
                if build_type == 'gradle':
                    config['dev_tests']['build_type'] = 'ant'
                    config['dev_tests']['build_file'] = os.path.join(user_tests, 'build.xml')
                    config['dev_tests']['build_targets'] = ['merge-coverage-report']
                    config['dev_tests']['coverage_exec_file'] = os.path.join(user_tests, 'merged_jacoco.exec')

                if os.path.isfile(config['dev_tests']['coverage_exec_file']):
                    print('deleteing exec file')
                    os.remove(config['dev_tests']['coverage_exec_file'])
                self.__process_execute(config=config)

                # assert that expected execute resources are created
                self.__assert_execute_resources(app_name=app_name, compare_coverage=True)
                shutil.rmtree(generated_test_directory, ignore_errors=True)


    def __assert_generate_resources(self, app_name, generate_subcmd):
        dir_util.cd_output_dir(app_name)
        if generate_subcmd == 'ctd-amplified':
            summary_file = app_name+constants.TKL_EXTENDER_SUMMARY_FILE_SUFFIX
            self.assertTrue(os.path.isfile(summary_file))
            with open(summary_file) as f:
                testgen_summary = json.load(f)
            self.assertGreater(testgen_summary['extended_sequences_info']['final_sequences'], 0)

            main_report_dir = app_name + constants.TKLTEST_MAIN_REPORT_DIR_SUFFIX
            self.assertTrue(os.path.isdir(main_report_dir))
            ctd_report_dir = os.path.join(main_report_dir, constants.TKL_CTD_REPORT_DIR)
            self.assertTrue(os.path.isdir(ctd_report_dir))
            self.assertTrue(os.path.isfile(os.path.join(ctd_report_dir,
                                                        app_name + constants.TKL_EXTENDER_COVERAGE_FILE_SUFFIX)))
            self.assertTrue(os.path.isfile(os.path.join(ctd_report_dir,
                                                        constants.TEST_PLAN_SUMMARY_NAME)))

        dir_util.cd_cli_dir()
        self.assertTrue(os.path.isdir(self.test_apps[app_name]['test_directory']))

    def __assert_execute_resources(self, app_name, code_coverage=True, reports_path='', compare_coverage=False):
        if reports_path:
            main_report_dir = reports_path
        else:
            main_report_dir = app_name+constants.TKLTEST_MAIN_REPORT_DIR_SUFFIX
            dir_util.cd_output_dir(app_name)
        self.assertTrue(os.path.isdir(main_report_dir))
        junit_report_dir = os.path.join(main_report_dir, constants.TKL_JUNIT_REPORT_DIR)
        self.assertTrue(os.path.isdir(junit_report_dir))
        cov_report_dir = os.path.join(main_report_dir, constants.TKL_CODE_COVERAGE_REPORT_DIR)
        if code_coverage:
            self.assertTrue(os.path.isdir(cov_report_dir))
        else:
            self.assertFalse(os.path.isdir(cov_report_dir))
        compare_report_dir = os.path.join(main_report_dir, constants.TKL_CODE_COVERAGE_COMPARE_REPORT_DIR)
        if compare_coverage:
            self.assertTrue(os.path.isdir(compare_report_dir))
            compare_html_dir = os.path.join(compare_report_dir, constants.TKL_CODE_COVERAGE_COMPARE_HTML_DIR)
            self.assertTrue(os.path.isdir(compare_html_dir))
            compare_html_file = os.path.join(compare_html_dir, 'index.html')
            self.assertTrue(os.path.isfile(compare_html_file))
        else:
            self.assertFalse(os.path.isdir(compare_report_dir))
        if not reports_path:
            dir_util.cd_cli_dir()

    def __assert_augment_resources(self, app_name, test_directory, orig_test_directory):
        dir_util.cd_output_dir(app_name)
        orig_test_directory = os.path.join('..',orig_test_directory)
        main_report_dir = app_name + constants.TKLTEST_MAIN_REPORT_DIR_SUFFIX
        cov_report_dir = os.path.join(main_report_dir, constants.TKL_CODE_COVERAGE_REPORT_DIR, os.path.basename(test_directory))
        self.assertTrue(os.path.isdir(cov_report_dir))
        files_list = [str(path).replace(test_directory, '') for path in list(Path(test_directory).glob('**/*.java'))]
        orig_files_list = [str(path).replace(orig_test_directory, '') for path in list(Path(orig_test_directory).glob('**/*.java'))]
        self.assertTrue(len(files_list) > len(orig_files_list))

        for f in orig_files_list:
            self.assertTrue(f in files_list)
        for f in files_list:
            if f not in orig_files_list:
                if f.endswith('_ESTest.java'):
                    self.assertTrue(f.replace('_ESTest.java', '_ESTest_scaffolding.java') in files_list)
                else:
                    self.assertTrue(f.endswith('_ESTest_scaffolding.java'))

        xml_file = os.path.join(cov_report_dir, 'jacoco.xml')
        self.assertTrue(os.path.isfile(xml_file))
        xml_tree = ElementTree.parse(xml_file)
        xml_entry = xml_tree.getroot()
        for coverage_type in ['INSTRUCTION', 'BRANCH', 'LINE', 'CLASS', 'METHOD']:
            coverage_counter = [counter for counter in xml_entry if 'type' in counter.attrib and counter.attrib['type'] == coverage_type]
            self.assertTrue(len(coverage_counter) == 1)
            self.assertTrue(coverage_counter[0].attrib['missed'] == '0')

    def __process_generate(self, subcommand, config):
        self.args.command = 'generate'
        self.args.sub_command = subcommand
        generate.process_generate_command(args=self.args, config=config)

    def __process_execute(self, config, subcommand=None):
        self.args.command = 'execute'
        if subcommand:
            self.args.sub_command = subcommand
        execute.process_execute_command(args=self.args, config=config)

    def __reuse_basic_blocks(self, app_name):
        output_dir = constants.TKLTEST_UNIT_OUTPUT_DIR_PREFIX + app_name
        shutil.rmtree(output_dir, ignore_errors=True)
        shutil.copytree(os.path.join(self.test_data_dir, app_name, 'basic_blocks'), output_dir)
