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

sys.path.insert(0, os.path.abspath(os.path.dirname(__file__))+os.sep+'..')
from tkltest.generate import generate
from tkltest.execute import execute
from tkltest.util import config_util, constants


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
        }
    }

    args = argparse.Namespace()

    def setUp(self) -> None:
        for app_name in self.test_apps.keys():
            app_info = self.test_apps[app_name]

            # remove directories and files created during test generation
            shutil.rmtree(app_info['test_directory'], ignore_errors=True)
            shutil.rmtree(app_name+constants.TKLTEST_MAIN_REPORT_DIR_SUFFIX, ignore_errors=True)
            shutil.rmtree(app_name+constants.TKL_EXTENDER_SUMMARY_FILE_SUFFIX, ignore_errors=True)
            shutil.rmtree(app_name+constants.TKL_EXTENDER_COVERAGE_FILE_SUFFIX, ignore_errors=True)

            # load and set config for app
            app_config = config_util.load_config(config_file=app_info['config_file'])
            app_config['general']['test_directory'] = app_info['test_directory']
            app_config['generate']['time_limit'] = 1
            app_config['generate']['ctd_amplified']['num_seq_executions'] = 1
            app_info['config'] = app_config

    def test_generate_execute_ctdamplified_combined_classlist_diffassert(self) -> None:
        """Test "generate ctd-amplified" and "execute": base_test_generator=combined scope=target_class_list"""
        for app_name in self.test_apps.keys():
            app_info = self.test_apps[app_name]

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
        for app_name in self.test_apps.keys():
            app_info = self.test_apps[app_name]

            # set up config and generate tests
            config = app_info['config']
            config['generate']['ctd_amplified']['base_test_generator'] = constants.BASE_TEST_GENERATORS['randoop']
            config['generate']['partitions_file'] = ''
            config['generate']['target_class_list'] = app_info['target_class_list']
            self.__process_generate(subcommand='ctd-amplified', config=config)

            # assert that expected generate resources are created
            self.__assert_generate_resources(app_name=app_name, generate_subcmd='ctd-amplified')

            # execute tests
            # config['execute']['build_type'] = 'maven'
            self.__process_execute(config=config)

            # assert that expected execute resources are created
            self.__assert_execute_resources(app_name=app_name)

    def test_generate_execute_ctdamplified_evosuite_classlist_diffassert(self) -> None:
        """Test "generate ctd-amplified" and "execute": base_test_generator=evosuite scope=target_class_list"""
        for app_name in self.test_apps.keys():
            app_info = self.test_apps[app_name]
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

    def test_generate_execute_ctdamplified_combined_allclasses_diffassert(self) -> None:
        """Test "generate ctd-amplified" and "execute": base_test_generator=combined scope=all_classes"""
        for app_name in self.test_apps.keys():
            app_info = self.test_apps[app_name]
            config = app_info['config']
            config['generate']['ctd_amplified']['base_test_generator'] = constants.BASE_TEST_GENERATORS['combined']
            config['generate']['partitions_file'] = ''
            config['generate']['target_class_list'] = []
            self.__process_generate(subcommand='ctd-amplified', config=config)

            # assert that expected generate resources are created
            self.__assert_generate_resources(app_name=app_name, generate_subcmd='ctd-amplified')

            # execute tests
            self.__process_execute(config=config)

            # assert that expected execute resources are created
            self.__assert_execute_resources(app_name=app_name)

    def test_generate_execute_ctdamplified_combined_allclasses_but_excluded_diffassert(self) -> None:
        """Test "generate ctd-amplified" and "execute": base_test_generator=combined scope=all_classes_but_excluded"""
        for app_name in self.test_apps.keys():
            app_info = self.test_apps[app_name]
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
        for app_name in self.test_apps.keys():
            app_info = self.test_apps[app_name]
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
        for app_name in self.test_apps.keys():
            app_info = self.test_apps[app_name]
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
        for app_name in self.test_apps.keys():
            app_info = self.test_apps[app_name]
            config = app_info['config']
            config['generate']['ctd_amplified']['base_test_generator'] = constants.BASE_TEST_GENERATORS['evosuite']
            config['generate']['ctd_amplified']['augment_coverage'] = True
            config['generate']['partitions_file'] = ''
            config['generate']['target_class_list'] = []
            self.__process_generate(subcommand='ctd-amplified', config=config)

            # assert that expected generate resources are created
            self.__assert_generate_resources(app_name=app_name, generate_subcmd='ctd-amplified')

    @unittest.skip('')
    def test_generate_execute_ctdamplified_combined_partitions_diffassert(self) -> None:
        """Test "generate ctd-amplified" and "execute": base_test_generator=combined scope=partitions_file"""
        for app_name in self.test_apps.keys():
            app_info = self.test_apps[app_name]
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
        for app_name in self.test_apps.keys():
            app_info = self.test_apps[app_name]
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
        for app_name in self.test_apps.keys():
            app_info = self.test_apps[app_name]
            config = app_info['config']
            config['generate']['ctd_amplified']['base_test_generator'] = constants.BASE_TEST_GENERATORS['evosuite']
            config['generate']['partitions_file'] = app_info['partitions_file']
            config['generate']['target_class_list'] = []
            self.__process_generate(subcommand='ctd-amplified', config=config)

            # assert that expected generate resources are created
            self.__assert_generate_resources(app_name=app_name, generate_subcmd='ctd-amplified')

            # execute tests
            self.__process_execute(config=config)

            # assert that expected execute resources are created
            self.__assert_execute_resources(app_name=app_name)

    def test_generate_execute_ctdamplified_combined_classlist_nodiffassert(self) -> None:
        """Test "generate ctd-amplified" and "execute": base_test_generator=combined scope=target_class_list no_diff_assertions"""
        for app_name in self.test_apps.keys():
            app_info = self.test_apps[app_name]
            config = app_info['config']
            config['generate']['ctd_amplified']['base_test_generator'] = constants.BASE_TEST_GENERATORS['combined']
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

    def test_generate_execute_ctdamplified_randoop_classlist_nodiffassert(self) -> None:
        """Test "generate ctd-amplified" and "execute": base_test_generator=randoop scope=target_class_list no_diff_assertions"""
        for app_name in self.test_apps.keys():
            app_info = self.test_apps[app_name]
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
        for app_name in self.test_apps.keys():
            app_info = self.test_apps[app_name]
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
        for app_name in self.test_apps.keys():
            app_info = self.test_apps[app_name]
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
        for app_name in self.test_apps.keys():
            app_info = self.test_apps[app_name]
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
        for app_name in self.test_apps.keys():
            app_info = self.test_apps[app_name]
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
        for app_name in self.test_apps.keys():
            app_info = self.test_apps[app_name]
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
        for app_name in self.test_apps.keys():
            app_info = self.test_apps[app_name]
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
        for app_name in self.test_apps.keys():
            app_info = self.test_apps[app_name]
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
        for app_name in self.test_apps.keys():
            app_info = self.test_apps[app_name]

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
        for app_name in self.test_apps.keys():
            app_info = self.test_apps[app_name]

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
        for app_name in self.test_apps.keys():
            app_info = self.test_apps[app_name]

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
        for app_name in self.test_apps.keys():
            app_info = self.test_apps[app_name]

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
        for app_name in self.test_apps.keys():
            app_info = self.test_apps[app_name]

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
        for app_name in self.test_apps.keys():
            app_info = self.test_apps[app_name]

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
        for app_name in self.test_apps.keys():
            app_info = self.test_apps[app_name]

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
        for app_name in self.test_apps.keys():
            app_info = self.test_apps[app_name]

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

    def __assert_generate_resources(self, app_name, generate_subcmd):
        if generate_subcmd == 'ctd-amplified':
            summary_file = app_name+constants.TKL_EXTENDER_SUMMARY_FILE_SUFFIX
            self.assertTrue(os.path.isfile(summary_file))
            with open(summary_file) as f:
                testgen_summary = json.load(f)
            self.assertGreater(testgen_summary['extended_sequences_info']['final_sequences'], 0)

            self.assertTrue(os.path.isfile(app_name+constants.TKL_EXTENDER_COVERAGE_FILE_SUFFIX))
            main_report_dir = app_name + constants.TKLTEST_MAIN_REPORT_DIR_SUFFIX
            self.assertTrue(os.path.isdir(main_report_dir))
            ctd_report_dir = os.path.join(main_report_dir, constants.TKL_CTD_REPORT_DIR)
            self.assertTrue(os.path.isdir(ctd_report_dir))

        self.assertTrue(os.path.isdir(self.test_apps[app_name]['test_directory']))

    def __assert_execute_resources(self, app_name, code_coverage=True, reports_path=''):
        if reports_path:
            main_report_dir = reports_path
        else:
            main_report_dir = app_name+constants.TKLTEST_MAIN_REPORT_DIR_SUFFIX
        self.assertTrue(os.path.isdir(main_report_dir))
        junit_report_dir = os.path.join(main_report_dir, constants.TKL_JUNIT_REPORT_DIR)
        self.assertTrue(os.path.isdir(junit_report_dir))
        cov_report_dir = os.path.join(main_report_dir, constants.TKL_CODE_COVERAGE_REPORT_DIR)
        if code_coverage:
            self.assertTrue(os.path.isdir(cov_report_dir))
        else:
            self.assertFalse(os.path.isdir(cov_report_dir))

    def __process_generate(self, subcommand, config):
        self.args.command = 'generate'
        self.args.sub_command = subcommand
        generate.process_generate_command(args=self.args, config=config)

    def __process_execute(self, config, subcommand=None):
        self.args.command = 'execute'
        if subcommand:
            self.args.sub_command = subcommand
        execute.process_execute_command(args=self.args, config=config)
