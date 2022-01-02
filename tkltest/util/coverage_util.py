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

import os
import csv
import glob
import logging
import subprocess
import shutil
import sys

from tkltest.util import command_util, constants
from tkltest.util.logging_util import tkltest_status


def get_coverage_for_test_suite(build_file, build_type, test_root_dir, report_dir,
                                raw_cov_data_dir, raw_cov_data_file_pref,
                                class_files = None, additional_test_suite = None):
    """Runs test cases and returns coverage information.

    Runs test cases using the given Ant build file, reads coverage information from the Jacoco CSV
    coverage file, and returns dictionary containing instruction, line, and branch coverage data.

    Args:
        build_file (str): Build file to use for running tests
        build_type (str): Type of build file (either ant, maven or gradle)
        test_root_dir (str): Root directory of test suite
        report_dir (str): Main reports directory, under which coverage report is generated

    Returns:
        dict: Information about instructions, lines, and branches covered and missed
    """

    # remove existing coverage file
    main_coverage_dir = os.path.abspath(os.path.join(report_dir,
                                                     constants.TKL_CODE_COVERAGE_REPORT_DIR,
                                                     os.path.basename(test_root_dir)))
    if build_type == 'ant':
        coverage_csv_file = os.path.join(main_coverage_dir, os.path.basename(test_root_dir) + '.csv')
    else:
        coverage_csv_file = os.path.join(main_coverage_dir, 'jacoco.csv')
    try:
        os.remove(coverage_csv_file)
    except OSError:
        pass

    # run tests using build file
    if build_type == 'ant':
        command_util.run_command("ant -f {} merge-coverage-report".format(build_file), verbose=False)
        jacoco_raw_date_file = os.path.join(test_root_dir, "merged_jacoco.exec")
    elif build_type == 'maven':
        command_util.run_command("mvn -f {} clean verify site".format(build_file), verbose=False)
        # in case of maven only, jacoco.exec is created inside the monolithic subdir of the cud-amplified tests
        if os.path.isdir(os.path.join(test_root_dir, "monolithic")):
            jacoco_raw_date_file = os.path.join(test_root_dir, "monolithic", "jacoco.exec")
        else:
            jacoco_raw_date_file = os.path.join(test_root_dir, "jacoco.exec")
    else: #gradle
        command_util.run_command("gradle --project-dir {} tklest_task".format(test_root_dir), verbose=False)
        jacoco_raw_date_file = os.path.join(test_root_dir, "jacoco.exec")

    if additional_test_suite:
        additional_build_targets = ' '.join(additional_test_suite['build_targets'])
        additional_build_file = additional_test_suite['build_file']
        dev_build_type = additional_test_suite['build_type']
        if dev_build_type == 'ant':
            command_util.run_command("ant -f {} {}".format(additional_build_file, additional_build_targets), verbose=False)
        elif dev_build_type == 'maven':
            command_util.run_command("mvn -f {} {}".format(additional_build_file, additional_build_targets), verbose=False)
        else:  # gradle
            command_util.run_command("gradle --project-dir {} {}".format(os.path.dirname(additional_build_file), additional_build_targets), verbose=False)
        additional_exec_file = additional_test_suite['coverage_exec_file']

        jacoco_cli_file = os.path.join(constants.TKLTEST_LIB_DOWNLOAD_DIR, constants.JACOCO_CLI_JAR_NAME)
        merged_exec_file = jacoco_raw_date_file + '_merged_with_' + os.path.basename(additional_exec_file)
        merged_csv_file = coverage_csv_file + '_merged_with_' + os.path.basename(additional_exec_file) + '.csv'
        command_util.run_command("java -jar {} merge {} {} --destfile {}".
                                 format(jacoco_cli_file, jacoco_raw_date_file, additional_exec_file,
                                        merged_exec_file), verbose=True)
        command_util.run_command("java -jar {} report {} --classfiles {} --csv {}".
                                 format(jacoco_cli_file, merged_exec_file, os.path.pathsep.join(class_files),
                                        merged_csv_file), verbose=True)
        jacoco_raw_date_file = merged_exec_file
        coverage_csv_file = merged_csv_file


    jacoco_new_file_name = os.path.join(raw_cov_data_dir,
                                            raw_cov_data_file_pref + constants.JACOCO_SUFFIX_FOR_AUGMENTATION)
    os.rename(jacoco_raw_date_file, jacoco_new_file_name)


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
    return {
            'instruction_covered': total_inst_covered,
            'line_covered': total_line_covered,
            'branch_covered': total_branch_covered,
            'method_covered': total_method_covered,
            'instruction_total': total_inst_covered + total_inst_missed,
            'line_total': total_line_covered + total_line_missed,
            'branch_total': total_branch_covered + total_branch_missed,
            'method_total': total_method_covered + total_method_missed,
    }



def get_delta_coverage(test, test_raw_cov_file, ctd_raw_cov_file, main_coverage_dir, base_coverage, class_files,
                       remove_merged_cov_file):

    """Merges two raw coverage data files and returns delta coverage information between respective test suites

        Runs jacoco cli merge and report commands between two given raw jacoco.exec data files,
        reads coverage information from the Jacoco CSV coverage file, and returns dictionary
        containing instruction, line, and branch delta coverage data.

        Args:
            test (str): the name of the test class whose delta coverage is being computed
            test_raw_cov_file (str): the jacoco.exec coverage data file of the test
            ctd_raw_cov_file (str): he jacoco.exec coverage data file of the ctd test suite we are comparing against
            main_coverage_dir (str): Main directory in which coverage report is generated
            base_coverage (dict): base coverage to compute coverage gain (delta) against
            remove_merged_cov_file (bool): whether to remove existing merged coverage file

        Returns:
            dict: delta instruction, line, branch coverage
            dict:
        """

    # run jacoco cli merge command

    output_exec_file = os.path.join(os.path.dirname(ctd_raw_cov_file), constants.JACOCO_MERGED_DATA_FOR_AUGMENTATION);

    if remove_merged_cov_file:
        try:
            os.remove(output_exec_file)
        except OSError:
            pass

    jacoco_cli_file = os.path.join(constants.TKLTEST_LIB_DOWNLOAD_DIR, constants.JACOCO_CLI_JAR_NAME)

    try:
        command_util.run_command("java -Xmx2048m -jar {} merge {} {} --destfile {}".
                             format(jacoco_cli_file, test_raw_cov_file, ctd_raw_cov_file,
                                    output_exec_file), verbose=True)
    except subprocess.CalledProcessError as e:
        # If merging failed we skip current test file and assume it resulted in zero delta coverage
        # The reason to continue is that we may still gain from previous augmenting test files
        tkltest_status('Merging of jacoco output failed, skipping current test file: {}\n{}'.format(e, e.stderr))
        return {
                   'instruction_cov_delta': 0,
                   'line_cov_delta': 0,
                   'branch_cov_delta': 0,
                   'method_cov_delta': 0
               }, {'instruction_covered': base_coverage['instruction_covered'],
                   'line_covered': base_coverage['line_covered'],
                   'branch_covered': base_coverage['branch_covered'],
                   'method_covered': base_coverage['method_covered'],
                   'instruction_total': base_coverage['instruction_total'],
                   'line_total': base_coverage['line_total'],
                   'branch_total': base_coverage['branch_total'],
                   'method_total': base_coverage['method_total']}

    # run jacoco cli report command

    coverage_csv_file = os.path.join(main_coverage_dir, os.path.basename(test)) + '.csv'

    command_util.run_command("java -jar {} report {} --classfiles {} --csv {} --html {}".
                             format(jacoco_cli_file, output_exec_file, os.path.pathsep.join(class_files),
                                    coverage_csv_file, main_coverage_dir), verbose=True)

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

    return {
        'instruction_cov_delta': (total_inst_covered - base_coverage['instruction_covered']),
        'line_cov_delta': (total_line_covered - base_coverage['line_covered']),
        'branch_cov_delta': (total_branch_covered - base_coverage['branch_covered']),
        'method_cov_delta': (total_branch_covered - base_coverage['method_covered']),
    },{
            'instruction_covered': total_inst_covered,
            'line_covered': total_line_covered,
            'branch_covered': total_branch_covered,
            'method_covered': total_method_covered,
            'instruction_total': total_inst_covered + total_inst_missed,
            'line_total': total_line_covered + total_line_missed,
            'branch_total': total_branch_covered + total_branch_missed,
            'method_total': total_method_covered + total_method_missed,
    }



def add_test_class_to_ctd_suite(test_class, test_directory):
    """Adds a test class to a CTD test suite directory.

    Adds the given test class (specified as a file path) to the test directory representing CTD-guided
    test suite (i.e., assuming the particular directory structure with "monolithic" in the directory path).
    Along with the test class, also adds other classes with the same base name; these could be EvoSuite
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
    Along with the test class, also removes other classes with the same base name; these could be EvoSuite
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


def generate_coverage_report(monolith_app_path, exec_file, xml_file, html_dir):
    """Generates jacoco XML file from raw coverage (.exec) files.

     runs the jacoco CLI to generate XML report from the raw coverage file.
     The XML reports contains method-level coverage information (line, instruction, branch, etc.).
     To run the jacoco CLI, directories to the application classes are needed, which is identified
     using the tkltest config files.

    """

    jacoco_cli_cmd = 'java -jar {}'.format(os.path.join(constants.TKLTEST_LIB_DOWNLOAD_DIR, constants.JACOCO_CLI_JAR_NAME))
    jacoco_classfiles_ops = ''
    for classpath in monolith_app_path:
        jacoco_classfiles_ops += '--classfiles {} '.format(classpath)
    jacoco_cmd = '{} {} {} {} --xml {} --html {}'.format(jacoco_cli_cmd, 'report', exec_file, jacoco_classfiles_ops, xml_file, html_dir)
    try:
        command_util.run_command(jacoco_cmd, verbose=True)
    except subprocess.CalledProcessError as e:
        tkltest_status('Error running jacoco cli command {}: {}\n{}'.format(jacoco_cmd, e, e.stderr), error=True)
        sys.exit(1)


