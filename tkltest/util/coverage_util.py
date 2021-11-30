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
import re
import shutil
from bs4 import BeautifulSoup
import sys
import xml.etree.ElementTree as ElementTree

from tkltest.util import command_util, constants, java_class
from tkltest.util.logging_util import tkltest_status


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




def generate_coverage_xml(monolith_app_path, exec_file, xml_file,html_dir):
    """Generates jacoco XML file from raw coverage (.exec) files.

     runs the jacoco CLI to generate XML report from the raw coverage file.
     The XML reports contains method-level coverage information (line, instruction, branch, etc.).
     To run the jacoco CLI, directories to the application classes are needed, which is iedentified
     using the tkltest config files.

    """

    jacoco_cli_cmd = 'java -jar {}'.format(os.path.join(constants.TKLTEST_LIB_DOWNLOAD_DIR, 'org.jacoco.cli-0.8.7-nodeps.jar'))
    #todo - support java files
    jacoco_classfiles_ops = ''
    for classpath in monolith_app_path:
        jacoco_classfiles_ops += '--classfiles {} '.format(classpath)
    html_ops = ''
    if html_dir:
        html_ops = '--html ' + html_dir
    jacoco_cmd = '{} {} {} {} --xml {} {}'.format(jacoco_cli_cmd, 'report', exec_file, jacoco_classfiles_ops, xml_file, html_ops)
    #jacoco_cmd = '{} {} {}'.format(jacoco_cli_cmd, 'execinfo', exec_file)
    print('jacoco CLI command: {}'.format(jacoco_cmd))
    command_util.run_command(jacoco_cmd, verbose=True)


class CoverageStatistic:
    class Counter:

        def __init__(self, covered1, covered2, missed1, missed2):
            self.covered1 = covered1
            self.covered2 = covered2
            self.missed1 = missed1
            self.missed2 = missed2
            if self.covered1 + self.missed1 != self.covered2 + self.missed2:
                tkltest_status('fail to parse', error=True)  # todo
                sys.exit(1)
            self.total = self.covered1 + self.missed1


    def __init__(self, name, xml_entry1, xml_entry2):
        self.name = name
        self.counters = {}
        self.update_xml_statistic(xml_entry1, xml_entry2)
        self.mi_both = 0
        self.mi_only1 = 0
        self.mi_only2 = 0
        self.mi_none = 0
        self.mb_both = 0
        self.mb_only1 = 0
        self.mb_only2 = 0
        self.mb_none = 0
        self.ml_both = 0
        self.ml_only1 = 0
        self.ml_only2 = 0
        self.ml_none = 0


    def update_xml_statistic(self, xml_entry1, xml_entry2):
        for coverage_type in ['INSTRUCTION', 'BRANCH', 'COMPLEXITY', 'LINE', 'METHOD', 'CLASS']:
            coverage_counter1 = [counter for counter in xml_entry1 if 'type' in counter.attrib and counter.attrib['type'] == coverage_type]
            coverage_counter2 = [counter for counter in xml_entry2 if 'type' in counter.attrib and counter.attrib['type'] == coverage_type]
            if len(coverage_counter1) == 0 and len(coverage_counter2) == 0:
                continue
            if len(coverage_counter1) != 1 or len(coverage_counter2) != 1:
                tkltest_status('fail to parse', error=True)  # todo
                sys.exit(1)
            coverage_counter1 = coverage_counter1[0]
            coverage_counter2 = coverage_counter2[0]
            self.counters[coverage_type] = self.Counter(int(coverage_counter1.attrib['covered']),
                                                        int(coverage_counter2.attrib['covered']),
                                                        int(coverage_counter1.attrib['missed']),
                                                        int(coverage_counter2.attrib['missed']))

    def update_line_statistic(self, ci1, ci2, mi1, mi2, cb1, cb2, mb1, mb2):
        self.mi_both += min([mi1, mi2])
        self.mi_none += min([ci1, ci2])
        self.mi_only1 += max([mi1 - mi2, 0])
        self.mi_only2 += max([mi2 - mi1, 0])

        self.mb_both += min([mb1, mb2])
        self.mb_none += min([cb1, cb2])
        self.mb_only1 += max([mb1 - mb2, 0])
        self.mb_only2 += max([mb2 - mb1, 0])

        self.ml_both += mi1 > 0 and mi2 > 0
        self.ml_none += mi1 == 0 and mi2 == 0
        self.ml_only1 += mi1 > 0 and mi2 == 0
        self.ml_only2 += mi1 == 0 and mi2 > 0

    def interesting(self):
        interesting1 = self.mi_only1 and self.mi_only2
        interesting3 = (self.counters['INSTRUCTION'].covered1 + self.counters['INSTRUCTION'].missed1) != self.mi_only1 + self.mi_only2 + self.mi_both + self.mi_none
        return interesting1 or interesting3

    def print(self):
        print(f'.  {self.__class__.__name__} {self.name}, interesting: {self.interesting()} ic_both:{self.mi_both} ic_none:{self.mi_none}. ci_only {self.mi_only1}/{self.mi_only2}.')

    def get_table_prefix(self):
        line = '<?xml version="1.0" encoding="UTF-8"?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">' \
               '<html xmlns="http://www.w3.org/1999/xhtml" lang="en">' \
               '<head><meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/><link rel="stylesheet" href="jacoco-resources/report.css" type="text/css"/>' \
               '<link rel="shortcut icon" href="jacoco-resources/report.gif" type="image/gif"/>'

        line += '<title>' + self.name + '</title><script type="text/javascript" src="jacoco-resources/sort.js"></script>' \
               '</head>' \
               '<body onload="initialSort([\'breadcrumb\', \'coveragetable\'])"><div class="breadcrumb" id="breadcrumb">' \
               '<span class="el_report">' + self.name + '</span></div><h1>' + self.name + '</h1><table class="coverage" cellspacing="0" id="coveragetable">' \
               '<thead><tr>' \
               '<td class="sortable" id="a" onclick="toggleSort(this)">Element</td>' \
               '<td class="down sortable bar" id="b" onclick="toggleSort(this)">Missed Instructions</td>' \
               '<td class="sortable ctr2" id="c" onclick="toggleSort(this)">Cov.</td>' \
               '<td class="sortable bar" id="d" onclick="toggleSort(this)">Missed Branches</td>' \
               '<td class="sortable ctr2" id="e" onclick="toggleSort(this)">Cov.</td>' \
               '<td class="sortable ctr1" id="f" onclick="toggleSort(this)">Missed</td>' \
               '<td class="sortable ctr2" id="g" onclick="toggleSort(this)">Cxty</td>' \
               '<td class="sortable ctr1" id="h" onclick="toggleSort(this)">Missed</td>' \
               '<td class="sortable ctr2" id="i" onclick="toggleSort(this)">Lines</td>' \
               '<td class="sortable ctr1" id="j" onclick="toggleSort(this)">Missed</td>' \
               '<td class="sortable ctr2" id="k" onclick="toggleSort(this)">Methods</td>' \
               '<td class="sortable ctr1" id="l" onclick="toggleSort(this)">Missed</td>' \
               '<td class="sortable ctr2" id="m" onclick="toggleSort(this)">Classes</td>' \
               '</tr></thead>'


        line += '<tfoot><tr><td>Total</td>'
        line += self.get_html_table_line()
        line += '</tfoot>'
        line += '<tbody><tr>'
        return line

    def get_html_table_line(self):
        line =''
        scale = self.counters['INSTRUCTION'].total/100
        ibar = '<img src = "jacoco-resources/redbar.gif" width = "{}" height = "10" title = "{}" alt = "{}" />'.format(int(self.mi_both/scale), self.mi_both, self.mi_both)
        ibar += '<img src = "jacoco-resources/yellowbar.gif" width = "{}" height = "10" title = "{}" alt = "{}" />'.format(int(self.mi_only1/scale), self.mi_only1, self.mi_only1)
        ibar += '<img src = "jacoco-resources/bluebar.gif" width = "{}" height = "10" title = "{}" alt = "{}" />'.format(int(self.mi_only2/scale), self.mi_only2, self.mi_only2)
        ibar += '<img src = "jacoco-resources/greenbar.gif" width = "{}" height = "10" title = "{}" alt = "{}" />'.format(int(self.mi_none/scale), self.mi_none, self.mi_none)
        line += '<td class="bar">' + ibar + '</td>'
        line += '<td class="clr2"> {}% vs {}% </td>'.format(int(self.counters['INSTRUCTION'].covered1*100/self.counters['INSTRUCTION'].total), int(self.counters['INSTRUCTION'].covered2*100/ self.counters['INSTRUCTION'].total))
        if self.counters.get('BRANCH') and self.counters['BRANCH'].total != 0:
            scale = self.counters['BRANCH'].total / 100
            ibar = '<img src = "jacoco-resources/redbar.gif" width = "{}" height = "10" title = "{}" alt = "{}" />'.format(int(self.mb_both / scale), self.mb_both, self.mb_both)
            ibar += '<img src = "jacoco-resources/yellowbar.gif" width = "{}" height = "10" title = "{}" alt = "{}" />'.format(int(self.mb_only1 / scale), self.mb_only1, self.mb_only1)
            ibar += '<img src = "jacoco-resources/bluebar.gif" width = "{}" height = "10" title = "{}" alt = "{}" />'.format(int(self.mb_only2 / scale), self.mb_only2, self.mb_only2)
            ibar += '<img src = "jacoco-resources/greenbar.gif" width = "{}" height = "10" title = "{}" alt = "{}" />'.format(int(self.mb_none / scale), self.mb_none, self.mb_none)
            line += '<td class="bar">' + ibar + '</td>'
            line += '<td class="clr2"> {}% vs {}% </td>'.format(int(self.counters['BRANCH'].covered1*100/self.counters['BRANCH'].total), int(self.counters['BRANCH'].covered2*100/ self.counters['BRANCH'].total))
        else:
            line += '<td class="bar"></td>'
            line += '<td class="clr2"> n/a </td>'

        line += '<td class="clr2"> {} vs {} </td>'.format(self.counters['COMPLEXITY'].missed1, self.counters['COMPLEXITY'].missed2)
        line += '<td class="clr1"> {} </td>'.format(self.counters['COMPLEXITY'].total)
        #line += '<td class="clr2"> {} vs {} </td>'.format(self.counters['LINE'].missed1, self.counters['LINE'].missed2)
        if self.counters.get('LINE') and self.counters['LINE'].total != 0:
            scale = self.counters['LINE'].total / 100
            ibar = '<img src = "jacoco-resources/redbar.gif" width = "{}" height = "10" title = "{}" alt = "{}" />'.format(int(self.ml_both / scale), self.ml_both, self.ml_both)
            ibar += '<img src = "jacoco-resources/yellowbar.gif" width = "{}" height = "10" title = "{}" alt = "{}" />'.format(int(self.ml_only1 / scale), self.ml_only1, self.ml_only1)
            ibar += '<img src = "jacoco-resources/bluebar.gif" width = "{}" height = "10" title = "{}" alt = "{}" />'.format(int(self.ml_only2 / scale), self.ml_only2, self.ml_only2)
            ibar += '<img src = "jacoco-resources/greenbar.gif" width = "{}" height = "10" title = "{}" alt = "{}" />'.format(int(self.ml_none / scale), self.ml_none, self.ml_none)
            line += '<td class="bar">' + ibar + '</td>'
        else:
            line += '<td class="bar"></td>'

        line += '<td class="clr1"> {} </td>'.format(self.counters['LINE'].total)
        line += '<td class="clr2"> {} vs {} </td>'.format(self.counters['METHOD'].missed1, self.counters['METHOD'].missed2)
        line += '<td class="clr1"> {} </td>'.format(self.counters['METHOD'].total)
        if self.counters.get('CLASS') and self.counters['CLASS'].total != 0:
            line += '<td class="clr2"> {} vs {} </td>'.format(self.counters['CLASS'].missed1, self.counters['CLASS'].missed2)
            line += '<td class="clr1"> {} </td>'.format(self.counters['CLASS'].total)
        line += '</tr>'
        return line


class MethodCoverageStatistic(CoverageStatistic):

    def __init__(self, name, class_name, line, xml_entry1, xml_entry2):
        super().__init__(name, xml_entry1, xml_entry2)
        self.class_name = class_name
        self.line = line


class ClassCoverageStatistic(CoverageStatistic):

    def __init__(self, name, xml_entry1, xml_entry2):
        self.methods = []
        super().__init__(name, xml_entry1, xml_entry2)


class SourceFileCoverageStatistic(CoverageStatistic):

    def __init__(self, name, xml_entry1, xml_entry2):
        super().__init__(name, xml_entry1, xml_entry2)


class PackageCoverageStatistic(CoverageStatistic):

    def __init__(self, name, xml_entry1, xml_entry2):
        self.classes = {}
        self.source_files = {}
        super().__init__(name, xml_entry1, xml_entry2)


class AppCoverageStatistic(CoverageStatistic):
    packages = {}

    def __init__(self, name, xml_entry1, xml_entry2):
        super().__init__(name, xml_entry1, xml_entry2)


def compare_coverage_xml(xml_file1, xml_file2, html1_dir, html2_dir, html_comapre_dir):

    line_to_method = {}
    xml_tree1, xml_tree2 = ElementTree.parse(xml_file1), ElementTree.parse(xml_file2)
    app_statistic = AppCoverageStatistic('Tackle Coverage Diff Report', xml_tree1.getroot(), xml_tree2.getroot())
    xml_packages1, xml_packages2 = xml_tree1.getroot().iter('package'), xml_tree2.getroot().iter('package')
    for xml_package1, xml_package2 in zip(xml_packages1, xml_packages2):
        package_name = xml_package1.attrib['name']
        if package_name != xml_package2.attrib['name']:
            tkltest_status('fail to parse', error=True)  # todo
            sys.exit(1)
        app_statistic.packages[package_name] = PackageCoverageStatistic(package_name, xml_package1, xml_package2)
        xml_classes1, xml_classes2 = xml_package1.iter('class'), xml_package2.iter('class')
        for xml_cls1, xml_cls2 in zip(xml_classes1, xml_classes2):
            class_name = xml_cls1.attrib['name']
            file_name = xml_cls1.attrib['sourcefilename']
            if class_name != xml_cls2.attrib['name'] or file_name != xml_cls2.attrib['sourcefilename']:
                tkltest_status('fail to parse', error=True) #todo
                sys.exit(1)
            class_name = class_name[len(package_name)+1:]
            app_statistic.packages[package_name].classes[class_name] = ClassCoverageStatistic(class_name, xml_cls1, xml_cls2)
            byte_code_data = java_class.JavaClass.from_file('../test/data/irs/monolith/target/classes/'+package_name + os.sep + class_name + '.class')
            if not line_to_method.get(file_name):
                line_to_method[file_name] = {}

            methods1, methods2 = xml_cls1.findall('method'), xml_cls2.findall('method')
            for method1, method2, byte_code_method in zip(methods1, methods2, byte_code_data.methods):
                method_name = method1.attrib['name']
                line_number1 = int(method1.attrib['line'] if 'line' in method1.attrib else 0)  # only `clinit` does not have line
                line_number2 = int(method2.attrib['line'] if 'line' in method2.attrib else 0)  # only `clinit` does not have line
                if method_name != method2.attrib['name'] or method_name != byte_code_method.name_as_str or line_number1 != line_number2:
                    tkltest_status('fail to parse', error=True)  # todo
                    sys.exit(1)
                method_name = method_name.replace('<init>', class_name)
                method_name = method_name.replace('<clinit>', 'static {...}')
                desc1, desc2 = method1.attrib['desc'], method2.attrib['desc']
                # XML inserts V (void) after the constructor, whereas JSON does not; so remove it
                fixed_name = re.sub(r'^(<init>.*\))V', r'\1', f'{method_name}{desc1}')
                #formal_name = (fixed_name, class_name1.replace(os.sep, '.'))
                current_method = MethodCoverageStatistic(fixed_name, class_name, line_number1, method1, method2)
                app_statistic.packages[package_name].classes[class_name].methods.append(current_method)
                byte_code_lines_table = byte_code_method.attributes[0].info.attributes[0].info.line_number_table
                for line_entry in byte_code_lines_table:
                    if not line_to_method[file_name].get(line_entry.line_number):
                        line_to_method[file_name][line_entry.line_number] = set()
                    line_to_method[file_name][line_entry.line_number].add(current_method)
                #coverage_counter = [counter for counter in method if counter.attrib['type'] == target_coverage]

        files1, files2 = xml_tree1.getroot().iter('sourcefile'), xml_tree2.getroot().iter('sourcefile')
        for file1, file2 in zip(files1, files2):
            file_name, file_name2 = file1.attrib['name'], file2.attrib['name']
            if file_name != file_name2:
                tkltest_status('fail to parse', error=True) #todo
                sys.exit(1)
            app_statistic.packages[package_name].source_files[file_name] = SourceFileCoverageStatistic(file_name, file1, file2)
            lines1, lines2 = file1.findall('line'), file2.findall('line')
            for line1, line2 in zip(lines1, lines2):
                line_number1, line_number2 = int(line1.attrib['nr']), int(line2.attrib['nr'])
                if line_number1 != line_number2:
                    tkltest_status('fail to parse', error=True) #todo
                    sys.exit(1)
                mi1, mi2 = int(line1.attrib['mi']), int(line2.attrib['mi'])
                ci1, ci2 = int(line1.attrib['ci']), int(line2.attrib['ci'])
                mb1, mb2 = int(line1.attrib['mb']), int(line2.attrib['mb'])
                cb1, cb2 = int(line1.attrib['cb']), int(line2.attrib['cb'])
                n_methods = len(line_to_method[file_name][line_number1])
                if (mi1 and ci1) or (mi2 and ci2):
                    print(mi1, ci1, mi2, ci2)
                if (mb1 and cb1) or (mb2 and cb2):
                    print(method_name, class_name, mb1, cb1, mb2, cb2, mi1, ci1, mi2, ci2)
                for method in line_to_method[file_name][line_number1]:
                    method.update_line_statistic(int(ci1/n_methods), int(ci2/n_methods), int(mi1/n_methods), int(mi2/n_methods), int(cb1/n_methods), int(cb2/n_methods), int(mb1/n_methods), int(mb2/n_methods))
                app_statistic.packages[package_name].classes[method.class_name].update_line_statistic(ci1, ci2, mi1, mi2, cb1, cb2, mb1, mb2)
                app_statistic.packages[package_name].source_files[file_name].update_line_statistic(ci1, ci2, mi1, mi2, cb1, cb2, mb1, mb2)
                app_statistic.packages[package_name].update_line_statistic(ci1, ci2, mi1, mi2, cb1, cb2, mb1, mb2)
                app_statistic.update_line_statistic(ci1, ci2, mi1, mi2, cb1, cb2, mb1, mb2)




        for name, cls in app_statistic.packages[package_name].classes.items():
            if cls.interesting():
                cls.print()
            for method in cls.methods:
                if method.interesting():
                    method.print()
        for name, file in app_statistic.packages[package_name].source_files.items():
            if file.interesting():
                file.print()

    app_html_table = app_statistic.get_table_prefix()
    for package_name, package_statistic in app_statistic.packages.items():
        package_html_file = package_name + '/index.html'
        app_html_table += '<tr><td><a href="'+package_html_file+'" class="el_package">' + package_name + '</a></td>'
        app_html_table += package_statistic.get_html_table_line()
        os.mkdir(html_comapre_dir + os.sep + package_name)

        package_html_table = package_statistic.get_table_prefix()
        for class_name, class_statistic in package_statistic.classes.items():
            class_html_file = class_name + '.html'
            package_html_table += '<tr><td><a href="' + class_html_file + '" class="el_class">' + class_name + '</a></td>'
            package_html_table += class_statistic.get_html_table_line()

            class_html_table = class_statistic.get_table_prefix()
            for method_statistic in class_statistic.methods:
                class_html_table += '<tr><td>' + method_statistic.name + '</a></td>'
                class_html_table += method_statistic.get_html_table_line()

            class_html_table += '</tr></tbody></table>' \
                                  '<div class="footer"><span class="right">Created with <a href="http://www.ibm.com">Tcltest</a> 123456789</span></div>' \
                                  '</body></html>'
            class_html_table = class_html_table.replace('jacoco-resources', '../jacoco-resources')
            with open(html_comapre_dir + os.sep + package_name + os.sep + class_html_file, mode='w') as new_html_file:
                new_html_file.write(class_html_table)
                with open(html1_dir + os.sep + package_name + os.sep + class_html_file) as html1_file:
                    new_html_file.write(html1_file.read().replace('greenbar', 'bluebar'))
                with open(html2_dir + os.sep + package_name + os.sep + class_html_file) as html2_file:
                    new_html_file.write(html2_file.read().replace('greenbar', 'yellowbar'))

        package_html_table += '</tr></tbody></table>' \
                          '<div class="footer"><span class="right">Created with <a href="http://www.ibm.com">Tcltest</a> 123456789</span></div>' \
                          '</body></html>'
        package_html_table = package_html_table.replace('jacoco-resources', '../jacoco-resources')
        with open(html_comapre_dir + os.sep + package_html_file, mode='w') as new_html_file:
            new_html_file.write(package_html_table)
            with open(html1_dir + os.sep + package_html_file) as html1_file:
                new_html_file.write(html1_file.read())
            with open(html2_dir + os.sep + package_html_file) as html2_file:
                new_html_file.write(html2_file.read())

    app_html_table += '</tr></tbody></table>'\
                      '<div class="footer"><span class="right">Created with <a href="http://www.ibm.com">Tcltest</a> 123456789</span></div>'\
                      '</body></html>'
    with open(html_comapre_dir + os.sep + 'index.html', mode='w') as new_html_file:
        new_html_file.write(app_html_table)
        with open(html1_dir + os.sep + 'index.html') as html1_file:
            new_html_file.write(html1_file.read())
        with open(html2_dir + os.sep + 'index.html') as html2_file:
            new_html_file.write(html2_file.read())





