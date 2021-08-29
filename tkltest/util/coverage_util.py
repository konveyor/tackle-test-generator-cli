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

import csv
import glob
import logging
import os
import shutil

from tkltest.util import command_util, constants


def get_coverage_for_test_suite(ant_build_file, test_root_dir, report_dir, base_coverage=None):
    """Runs test cases and returns coverage information.

    Runs test cases using the given Ant build file, reads coverage information from the Jacoco CSV
    coverage file, and returns dictionary containing instruction, line, and branch coverage data.

    Args:
        ant_build_file (str): Build file to use for running tests
        test_root_dir (str): Root directory of test suite
        report_dir (str): Main reports directory, under which coverage report is generated
        base_coverage (dict): base coverage to compute coverage gain (delta) against

    Returns:
        dict: Information about instructions, lines, and branches covered and missed (if base_coverage
            not specified), or delta instruction, line, branch coverage (if base_coverage specified)
    """

    # remove existing coverage file
    main_coverage_dir = os.path.abspath(os.path.join(report_dir,
                                                     constants.TKL_CODE_COVERAGE_REPORT_DIR,
                                                     os.path.basename(test_root_dir)))
    coverage_csv_file = os.path.join(main_coverage_dir, os.path.basename(test_root_dir)) + '.csv'
    try:
        os.remove(coverage_csv_file)
    except OSError:
        pass

    # run tests using ant build file
    command_util.run_command("ant -f {} merge-coverage-report".format(ant_build_file), verbose=False)

    # read the coverage CSV file and compute total instruction, line, and branch coverage
    total_inst_covered = 0;
    total_line_covered = 0
    total_branch_covered = 0
    total_method_covered = 0
    total_inst_missed = 0;
    total_line_missed = 0
    total_branch_missed = 0
    total_method_missed = 0
    with open(coverage_csv_file, newline='') as f:
        coverage_info = csv.DictReader(f)
        for row in coverage_info:
            total_inst_covered += int(row['INSTRUCTION_COVERED'])
            total_line_covered += int(row['LINE_COVERED'])
            total_branch_covered += int(row['BRANCH_COVERED'])
            total_method_covered += int(row['METHOD_COVERED'])
            total_inst_missed += int(row['INSTRUCTION_MISSED'])
            total_line_missed += int(row['LINE_MISSED'])
            total_branch_missed += int(row['BRANCH_MISSED'])
            total_method_missed += int(row['METHOD_MISSED'])

    logging.info('total_inst_cov={}. total_line_cov={}, total_branch_cov={}'.format(
        total_inst_covered, total_line_covered, total_branch_covered))

    # if base coverage specified, return the additional coverage (delta); otherwise return coverage achieved
    if base_coverage is not None:
        return {
            'instruction_cov_delta': (total_inst_covered - base_coverage['instruction_covered']),
            'line_cov_delta': (total_line_covered - base_coverage['line_covered']),
            'branch_cov_delta': (total_branch_covered - base_coverage['branch_covered']),
            'method_cov_delta': (total_branch_covered - base_coverage['method_covered'])
        }
    else:
        return {
            'instruction_covered': total_inst_covered,
            'line_covered': total_line_covered,
            'branch_covered': total_branch_covered,
            'method_covered': total_method_covered,
            'instruction_total': total_inst_covered + total_inst_missed,
            'line_total': total_line_covered + total_line_missed,
            'branch_total': total_branch_covered + total_branch_missed,
            'method_total': total_method_covered + total_method_missed
        }


def add_test_class_to_ctd_suite(test_class, test_directory):
    """Adds a test class to a CTD test suite directory.

    Adds the given test class (specified as a file path) to the test directory representing CTD-guided
    test suite (i.e., assuming the particular directory structure with "monolithic" in the directory path).
    Along with the test class, also adds other classes with the same base name; these could EvoSuite
    scaffolding or JEE-support classes.

    Args:
        test_class (str): Test class to add
        test_directory (str): Test directory to add the class to
    """
    test_base, test_ext = os.path.splitext(test_class)
    test_path, _ = os.path.split(test_class)
    test_path_comp = os.path.normpath(test_path).split(os.sep)
    for test_file in glob.glob(test_base+'*.java'):
        dst_dir = os.path.join(test_directory, 'monolithic', os.sep.join(test_path_comp[1:]))
        os.makedirs(dst_dir, exist_ok=True)
        shutil.copy(test_file, dst_dir)


def remove_test_class_from_ctd_suite(test_class, test_directory):
    """Removes a test class from a CTD test suite directory.

    Removes the given test class (specified as a file path) to the test directory representing CTD-guided
    test suite (i.e., assuming the particular directory structure with "monolithic" in the directory path).
    Along with the test class, also removes other classes with the same base name; these could EvoSuite
    scaffolding or JEE-support classes.

    Args:
        test_class (str): Test class to remove
        test_directory (str): Test directory to remove the class from
    """
    test_base, test_ext = os.path.splitext(test_class)
    test_path, _ = os.path.split(test_class)
    test_path_comp = os.path.normpath(test_path).split(os.sep)
    for test_file in glob.glob(test_base+'*.java'):
        test_suite_file = os.path.join(test_directory, 'monolithic',
                                       os.sep.join(test_path_comp[1:]), os.path.split(test_file)[1])
        os.remove(test_suite_file)


def get_test_classes(test_root_dir):
    """Returns all test class in a test suite.

    Returns all test classes (as file paths) from a test suite, represented as root test directory.

    Args:
        test_root_dir: Test directory (suite) to return paths for

    Returns:
        dict: mapping for test directories to file names in a directory
    """
    # need to recursively traverse files because they are located in sub folders; create map from dir path
    # to .java test files in that dir
    test_files = {
        dp : [f for f in filenames if os.path.splitext(f)[1] == '.java']
        for dp, dn, filenames in os.walk(test_root_dir)
    }
    # remove entries with empty file list
    test_files = {
        dir : test_files[dir] for dir in test_files.keys() if test_files[dir]
    }
    return test_files
