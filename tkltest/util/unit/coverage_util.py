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
import csv
import glob
import logging
import subprocess
import shutil
import pathlib
import sys

from tkltest.util import command_util, constants
from tkltest.util.logging_util import tkltest_status
from tkltest.execute.unit import execute


def get_coverage_for_test_suite(build_file, build_type, test_root_dir, report_dir,
                                raw_cov_data_dir, raw_cov_data_file_pref,
                                jdk_path, class_files=None, additional_test_suite=None):
    """Runs test cases and returns coverage information.

    Runs test cases using the given Ant build file, reads coverage information from the Jacoco CSV
    coverage file, and returns dictionary containing instruction, line, and branch coverage data.

    Args:
        build_file (str): Build file to use for running tests
        build_type (str): Type of build file (either ant, maven or gradle)
        test_root_dir (str): Root directory of test suite
        report_dir (str): Main reports directory, under which coverage report is generated
        jdk_path (str): path to the jdk home to be used for executing the tests and measuring their coverage
        class_files (str): the class file of the app
        additional_test_suite (dict): information of additional test suite, to add its coverage to the tests coverage
    Returns:
        dict: Information about instructions, lines, and branches covered and missed
    """

    has_test_suite = list(pathlib.Path(test_root_dir).glob('**/*.java'))
    # remove existing coverage file
    main_coverage_dir = os.path.abspath(os.path.join(report_dir,
                                                     constants.TKL_CODE_COVERAGE_REPORT_DIR,
                                                     os.path.basename(test_root_dir)))
    if not os.path.isdir(main_coverage_dir):
        os.makedirs(main_coverage_dir)
    if build_type == 'ant':
        coverage_csv_file = os.path.join(main_coverage_dir, os.path.basename(test_root_dir) + '.csv')
    else:
        coverage_csv_file = os.path.join(main_coverage_dir, 'jacoco.csv')
    try:
        os.remove(coverage_csv_file)
    except OSError:
        pass

    jacoco_raw_data_file = ''
    env_vars = dict(os.environ.copy())
    env_vars['JAVA_HOME'] = jdk_path
    if has_test_suite:
        jacoco_raw_data_file = get_jacoco_exec_file(build_type, test_root_dir)
        try:
            os.remove(jacoco_raw_data_file)
        except OSError:
            pass
        # run tests using build file to get the .exec file
        if build_type == 'ant':
            cmd = "ant -f {} merge-coverage-report".format(build_file)
        elif build_type == 'maven':
            cmd = "mvn -f {} clean verify site".format(build_file)
        else:
            cmd = "gradle --project-dir {} tklest_task".format(test_root_dir)
        try:
            command_util.run_command(cmd, verbose=False, env_vars=env_vars)
        except subprocess.CalledProcessError as e:
            tkltest_status('Warning: Error while running test suite for coverage computing, skipping current test file: {}\n{}'.format(e, e.stderr))
        if not os.path.exists(jacoco_raw_data_file):
            tkltest_status('Warning: {} was not created by : {}.\n Skipping current test file'.format(jacoco_raw_data_file, cmd))
            # we allow error when trying to get test suite coverage.
            # will handle it like we do not have test files at all:
            jacoco_raw_data_file = ''
        elif not os.path.exists(coverage_csv_file):
            tkltest_status('Warning: {} was not created by : {}.\n Skipping current test file'.format(coverage_csv_file, cmd))
            # if csv file was not created, we will not use the jacoco file
            jacoco_raw_data_file = ''

    if additional_test_suite:
        '''
        when we have additional_test_suite, we allow it to fail.
        in case of failure, we will use the original .csv & .exec files
        as long as no_failure flag is true, we continue with the code 
        '''
        no_failure = True
        additional_build_targets = ' '.join(additional_test_suite['build_targets'])
        additional_build_file = additional_test_suite['build_file']
        if build_type == 'ant':
            cmd = "ant -f {} {}".format(additional_build_file, additional_build_targets)
        elif build_type == 'maven':
            cmd = "mvn -f {} {}".format(additional_build_file, additional_build_targets)
        else:  # gradle
            cmd = "gradle --project-dir {} {}".format(os.path.dirname(additional_build_file), additional_build_targets)
        try:
            command_util.run_command(cmd, verbose=False, env_vars=env_vars)
        except subprocess.CalledProcessError as e:
            tkltest_status('Warning: Error while running dev-written test suite for coverage computing:\n {}\n{}'.format(e, e.stderr))
            # no_failure is still true, we will look for .exec file
        additional_exec_file = additional_test_suite['coverage_exec_file']
        if not os.path.exists(additional_exec_file):
            tkltest_status('Warning: {} was not created using command: {}'.format(additional_exec_file, cmd))
            no_failure = False
        if no_failure:
            jacoco_cli_file = os.path.join(constants.TKLTEST_LIB_DOWNLOAD_DIR, constants.JACOCO_CLI_JAR_NAME)
            if has_test_suite:
                merged_exec_file = jacoco_raw_data_file + '_merged_with_' + os.path.basename(additional_exec_file)
                merged_csv_file = coverage_csv_file + '_merged_with_' + os.path.basename(additional_exec_file) + '.csv'
                try:
                    command_util.run_command("java -jar {} merge {} {} --destfile {}".
                                             format(jacoco_cli_file, jacoco_raw_data_file, additional_exec_file,
                                                    merged_exec_file), verbose=True, env_vars=env_vars)
                except subprocess.CalledProcessError as e:
                    tkltest_status('Warning: Failed to merge coverage data files {} and {}:\n {}\n{}'.format(jacoco_raw_data_file, additional_exec_file, e, e.stderr))
                    no_failure = False
            else:
                merged_exec_file = additional_exec_file
                merged_csv_file = os.path.join(main_coverage_dir, os.path.basename(additional_exec_file) + '.csv')
        if no_failure:
            try:
                jacoco_classfiles_ops = ''
                for classpath in class_files:
                    jacoco_classfiles_ops += '--classfiles {} '.format(classpath)
                command_util.run_command("java -jar {} report {} {} --csv {}".
                                         format(jacoco_cli_file, merged_exec_file, jacoco_classfiles_ops,
                                                merged_csv_file), verbose=True, env_vars=env_vars)
            except subprocess.CalledProcessError as e:
                tkltest_status('Warning: Failed to create CSV coverage report {} from {}:\n {}\n{}'.format(merged_csv_file, merged_exec_file, e, e.stderr))
                no_failure = False

        if no_failure:
            jacoco_raw_data_file = merged_exec_file
            coverage_csv_file = merged_csv_file
        else:
            tkltest_status('Warning: Failed to obtain coverage from dev-written test suite using {}, using only coverage from ctd-amplified test suite {}'.format(additional_build_file, build_file))
    if not jacoco_raw_data_file:
        return {
            'instruction_covered': 0,
            'line_covered': 0,
            'branch_covered': 0,
            'method_covered': 0,
            'instruction_total': 0,
            'line_total': 0,
            'branch_total': 0,
            'method_total': 0,
        }

    jacoco_new_file_name = os.path.join(raw_cov_data_dir,
                                            raw_cov_data_file_pref + constants.JACOCO_SUFFIX_FOR_AUGMENTATION)
    shutil.move(jacoco_raw_data_file, jacoco_new_file_name)


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
                       remove_merged_cov_file, max_memory, jdk_path):

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
            max_memory (int): maximal memory to use for merging coverage reports
            jdk_path (str): path to the jdk home to be used for executing the tests and measuring their coverage

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

    no_delta_coverage = {
        'instruction_cov_delta': 0,
        'line_cov_delta': 0,
        'branch_cov_delta': 0,
        'method_cov_delta': 0
        }, {
        'instruction_covered': base_coverage['instruction_covered'],
        'line_covered': base_coverage['line_covered'],
        'branch_covered': base_coverage['branch_covered'],
        'method_covered': base_coverage['method_covered'],
        'instruction_total': base_coverage['instruction_total'],
        'line_total': base_coverage['line_total'],
        'branch_total': base_coverage['branch_total'],
        'method_total': base_coverage['method_total']}

    jacoco_cli_file = os.path.join(constants.TKLTEST_LIB_DOWNLOAD_DIR, constants.JACOCO_CLI_JAR_NAME)
    env_vars = dict(os.environ.copy())
    env_vars['JAVA_HOME'] = jdk_path
    if os.path.isfile(ctd_raw_cov_file) and os.path.isfile(test_raw_cov_file):
        try:
            command_util.run_command("java -Xmx"+str(max_memory)+"m -jar {} merge {} {} --destfile {}".
                                 format(jacoco_cli_file, test_raw_cov_file, ctd_raw_cov_file,
                                        output_exec_file), verbose=True, env_vars=env_vars)
        except subprocess.CalledProcessError as e:
            # If merging failed we stop augmentation because subsequent merging will most probably also fail due
            # to same memory issues
            tkltest_status('Warning: merging of jacoco output failed for test file: {}\n{}'.format(e, e.stderr))
            return {},{}
    elif os.path.isfile(test_raw_cov_file):
        shutil.copy(test_raw_cov_file, output_exec_file)
    else:
        tkltest_status('Warning: coverage data file {} does not exist for test {}, skipping current test file'.format(test_raw_cov_file, test))
        return no_delta_coverage
    # run jacoco cli report command
    if not os.path.isdir(main_coverage_dir):
        os.makedirs(main_coverage_dir)
    coverage_csv_file = os.path.join(main_coverage_dir, os.path.basename(test)) + '.csv'
    coverage_xml_file = os.path.join(main_coverage_dir, 'jacoco.xml')

    jacoco_classfiles_ops = ''
    for classpath in class_files:
        jacoco_classfiles_ops += '--classfiles {} '.format(classpath)
    try:
        create_report_command ="java -jar {} report {} {} --csv {} --html {} --xml {}".format(
            jacoco_cli_file, output_exec_file, jacoco_classfiles_ops,
            coverage_csv_file, main_coverage_dir, coverage_xml_file)
        command_util.run_command(create_report_command, verbose=True, env_vars=env_vars)
    except subprocess.CalledProcessError as e:
        tkltest_status('Warning: Generating coverage report failed, skipping current test file {}: {}\n{}'.format(create_report_command, e, e.stderr))
        return no_delta_coverage

    # read the coverage CSV file and compute total instruction, line, and branch coverage
    total_inst_covered = 0
    total_line_covered = 0
    total_branch_covered = 0
    total_method_covered = 0
    total_inst_missed = 0
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

def get_dev_test_coverage(config, output_dir, create_csv=False, create_xml=False, create_html=False):

    # running the developer test, to obtain the .exec file
    execute.run_dev_tests(config)
    dev_coverage_exec = config['dev_tests']['coverage_exec_file']
    app_name = config['general']['app_name']
    main_reports_dir = config['general']['reports_path']
    if not main_reports_dir:
        main_reports_dir = os.path.join(output_dir, app_name + constants.TKLTEST_MAIN_REPORT_DIR_SUFFIX)
    dev_report_dir = os.path.join(main_reports_dir, constants.TKL_CODE_COVERAGE_DEV_REPORT_DIR)
    if os.path.isdir(dev_report_dir):
        shutil.rmtree(dev_report_dir)
    os.makedirs(dev_report_dir)
    # calling generate_coverage_report() to create the csv file:
    dev_test_name = os.path.basename(os.path.dirname(config['generate']['app_build_files'][0]))
    dev_coverage_csv = os.path.join(dev_report_dir, dev_test_name + '_coverage.csv') if create_csv else ''
    dev_coverage_xml = os.path.join(dev_report_dir, dev_test_name + '_coverage.xml') if create_xml else ''
    dev_coverage_html = os.path.join(dev_report_dir, dev_test_name + '-coverage-html') if create_html else ''
    generate_coverage_report(monolith_app_path=config['general']['monolith_app_path'],
                             jdk_path=config['general']['java_jdk_home'],
                             exec_file=dev_coverage_exec,
                             xml_file=dev_coverage_xml,
                             html_dir=dev_coverage_html,
                             csv_file=dev_coverage_csv)
    return dev_coverage_xml, dev_coverage_html, dev_coverage_csv


def generate_coverage_report(monolith_app_path, jdk_path, exec_file, xml_file='', html_dir='', csv_file=''):
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
    jacoco_cmd = '{} {} {} {}'.format(jacoco_cli_cmd, 'report', exec_file, jacoco_classfiles_ops)
    if xml_file:
        jacoco_cmd += ' --xml {}'.format(xml_file)
    if html_dir:
        jacoco_cmd += ' --html {}'.format(html_dir)
    if csv_file:
        jacoco_cmd += ' --csv {}'.format(csv_file)
    env_vars = dict(os.environ.copy())
    env_vars['JAVA_HOME'] = jdk_path
    try:
        command_util.run_command(jacoco_cmd, verbose=True, env_vars=env_vars)
    except subprocess.CalledProcessError as e:
        tkltest_status('Error running jacoco cli command {}: {}\n{}'.format(jacoco_cmd, e, e.stderr), error=True)
        sys.exit(1)


def get_jacoco_exec_file(build_type, test_root_dir):
    if build_type == 'ant':
        jacoco_raw_data_file = os.path.join(test_root_dir, "merged_jacoco.exec")
    elif build_type == 'maven':
        # in case of maven only, jacoco.exec is created inside the monolithic subdir of the cud-amplified tests
        if os.path.isdir(os.path.join(test_root_dir, "monolithic")):
            jacoco_raw_data_file = os.path.join(test_root_dir, "monolithic", "jacoco.exec")
        else:
            jacoco_raw_data_file = os.path.join(test_root_dir, "jacoco.exec")
    else: #gradle
        jacoco_raw_data_file = os.path.join(test_root_dir, "jacoco.exec")
    return jacoco_raw_data_file
