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
import subprocess
import shutil
import xml.etree.ElementTree as ElementTree

from tkltest.util import command_util, constants
from tkltest.util.coverage_statistic import *


def get_coverage_for_test_suite(build_file, build_type, test_root_dir, report_dir,
                                raw_cov_data_dir, raw_cov_data_file_pref):
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
    coverage_csv_file = os.path.join(main_coverage_dir, os.path.basename(test_root_dir)) + '.csv'
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
        jacoco_raw_date_file = os.path.join(test_root_dir, "jacoco.exec")
    else: #gradle
        command_util.run_command("gradle --project-dir {} tklest_task".format(test_root_dir), verbose=False)

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

    jacoco_cli_file = os.path.join('..', 'lib', 'download', constants.JACOCO_CLI_JAR_NAME)

    command_util.run_command("java -jar {} merge {} {} --destfile {}".
                             format(jacoco_cli_file, test_raw_cov_file, ctd_raw_cov_file,
                                    output_exec_file), verbose=True)

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


def compare_to_dev_tests_coverage(config):

    app_name = config['general']['app_name']
    compare_report_dir = os.path.join(app_name + constants.TKLTEST_MAIN_REPORT_DIR_SUFFIX, constants.TKL_CODE_COVERAGE_REPORT_DIR)

    if os.path.isdir(compare_report_dir):
        shutil.rmtree(compare_report_dir)
    os.mkdir(compare_report_dir)

    test_root_dir = config['general']['test_directory']
    if test_root_dir == '':
        test_root_dir = app_name + constants.TKLTEST_DEFAULT_CTDAMPLIFIED_TEST_DIR_SUFFIX
    tkltest_test_name = os.path.basename(test_root_dir)
    dev_test_name = os.path.basename(config['dev_tests']['test_directory'])

    tkltest_coverage_exec = os.path.join(test_root_dir, 'merged_jacoco.exec') # todo  what the name with maven?
    dev_coverage_exec = config['dev_tests']['coverage_exec_file']
    tkltest_coverage_xml = os.path.join(compare_report_dir, tkltest_test_name + '_coverage.xml')
    dev_coverage_xml = os.path.join(compare_report_dir, dev_test_name + '_coverage.xml')
    tkltest_html_dir = os.path.join(compare_report_dir, tkltest_test_name + '_html')
    dev_html_dir = os.path.join(compare_report_dir, dev_test_name + '_html')


    __generate_coverage_xml(monolith_app_path=config['general']['monolith_app_path'],
                          exec_file=tkltest_coverage_exec,
                          xml_file=tkltest_coverage_xml,
                          html_dir=tkltest_html_dir)

    __generate_coverage_xml(monolith_app_path=config['general']['monolith_app_path'],
                          exec_file=dev_coverage_exec,
                          xml_file=dev_coverage_xml,
                          html_dir=dev_html_dir)

    html_compare_dir = os.path.join(compare_report_dir, 'compared_html')
    if os.path.isdir(html_compare_dir):
        shutil.rmtree(html_compare_dir)
    os.mkdir(html_compare_dir)
    shutil.copytree(dev_html_dir + os.sep + 'jacoco-resources', html_compare_dir + os.sep + 'jacoco-resources')
    shutil.copyfile('../bluebar.gif',  html_compare_dir + os.sep + 'jacoco-resources/bluebar.gif')
    shutil.copyfile('../yellowbar.gif',  html_compare_dir + os.sep + 'jacoco-resources/yellowbar.gif')
    __compare_coverage_xml(xml_file1=dev_coverage_xml, xml_file2=tkltest_coverage_xml,
                         html1_dir=dev_html_dir, html2_dir=tkltest_html_dir, html_compare_dir=html_compare_dir,
                         test_name1=dev_test_name,
                         test_name2=tkltest_test_name,
                         monolith_app_path=config['general']['monolith_app_path'])


def __generate_coverage_xml(monolith_app_path, exec_file, xml_file,html_dir):
    """Generates jacoco XML file from raw coverage (.exec) files.

     runs the jacoco CLI to generate XML report from the raw coverage file.
     The XML reports contains method-level coverage information (line, instruction, branch, etc.).
     To run the jacoco CLI, directories to the application classes are needed, which is iedentified
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


def __compare_coverage_xml(xml_file1, xml_file2, html1_dir, html2_dir, html_compare_dir, test_name1, test_name2, monolith_app_path):
    '''
    This method is the main flow of creating an html compare files between two test suits.
    for detailed description, please look at the description of coverage_statistic
    the main steps are:
    1. reading the XML files, generated by jacococli, to get the total coverage of every method/class/package/app.
    2. parse the .class files of the app, to get the source line numbers of each method
    3. iterating over the xml line information, and update the diff between the between two test suits.
    4. building the html files. one per class/package/app

    '''
    xml_tree1, xml_tree2 = ElementTree.parse(xml_file1), ElementTree.parse(xml_file2)
    app_statistic = AppCoverageStatistic(test_name1, test_name2)
    app_statistic.parse_xml(xml_tree1.getroot(), xml_tree2.getroot())

    xml_packages1, xml_packages2 = xml_tree1.getroot().iter('package'), xml_tree2.getroot().iter('package')
    for xml_package1, xml_package2 in zip(xml_packages1, xml_packages2):
        current_package = PackageCoverageStatistic(app_statistic, monolith_app_path)
        current_package.parse_xml(xml_package1, xml_package2)
        xml_classes1, xml_classes2 = xml_package1.iter('class'), xml_package2.iter('class')
        for xml_cls1, xml_cls2 in zip(xml_classes1, xml_classes2):
            current_class = ClassCoverageStatistic(current_package)
            current_class.parse_xml(xml_cls1, xml_cls2)
            methods1, methods2 = xml_cls1.findall('method'), xml_cls2.findall('method')
            for method1, method2 in zip(methods1, methods2):
                current_method = MethodCoverageStatistic(current_class)
                current_method.parse_xml(method1, method2)
            current_package.parse_class_file(current_class)

        files1, files2 = xml_package1.iter('sourcefile'), xml_package2.iter('sourcefile')
        for file1, file2 in zip(files1, files2):
            current_package.parse_sourcefile_xml(file1, file2)

    for package_statistic in app_statistic.children:
        os.mkdir(html_compare_dir + os.sep + package_statistic.get_html_name())
        for class_statistic in package_statistic.children:
            class_statistic.print_html(html_compare_dir + os.sep + package_statistic.get_html_name(), html1_dir + os.sep + package_statistic.get_html_name(), html2_dir + os.sep + package_statistic.get_html_name())
        package_statistic.print_html(html_compare_dir, html1_dir, html2_dir)
    app_statistic.print_html(html_compare_dir, html1_dir, html2_dir)



