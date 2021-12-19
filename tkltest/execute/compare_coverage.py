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
import sys
import logging

import xml.etree.ElementTree as ElementTree

from tkltest.util.logging_util import tkltest_status
from tkltest.util import code_util


def compare_coverage(xml_file1, xml_file2, test_name1, test_name2, monolith_app_path, app_name):
    """
    This method is the main flow of creating an CoverageStatistics tree between two test suits.
    for detailed description, please look at the description of coverage_statistic class
    the main steps are:
    1. reading the XML files, generated by jacococli, to get the total coverage of every method/class/package/app.
    2. parse the .class files of the app, to get the source line numbers of each method
    3. iterating over the xml line information, and update the diff between the between two test suits.
    """

    xml_tree1, xml_tree2 = ElementTree.parse(xml_file1), ElementTree.parse(xml_file2)
    app_statistics = AppCoverageStatistics(test_name1, test_name2, app_name)
    app_statistics.read_data(xml_tree1.getroot(), xml_tree2.getroot())

    for xml_package1, xml_package2 in __sort_xml_trees_to_same_order(xml_tree1.getroot(), xml_tree2.getroot(), 'package'):
        current_package = PackageCoverageStatistics(app_statistics, monolith_app_path)
        current_package.read_data(xml_package1, xml_package2)
        for xml_cls1, xml_cls2 in __sort_xml_trees_to_same_order(xml_package1, xml_package2, 'class'):
            current_class = ClassCoverageStatistics(current_package)
            current_class.read_data(xml_cls1, xml_cls2)
            for method1, method2 in __sort_xml_trees_to_same_order(xml_cls1, xml_cls2, 'method'):
                current_method = MethodCoverageStatistics(current_class)
                current_method.read_data(method1, method2)
            current_package.parse_class_file(current_class)
        for file1, file2 in __sort_xml_trees_to_same_order(xml_package1, xml_package2, 'sourcefile'):
            for line1, line2 in __sort_xml_trees_to_same_order(file1, file2, 'line', atr_key='nr'):
                current_package.read_sourceline_stat(line1, line2, file1.attrib['name'])
    app_statistics.integrity_check('')
    return app_statistics


def __sort_xml_trees_to_same_order(xml_tree1, xml_tree2, key, atr_key=''):
    if atr_key:
        arttib_to_item1 = {str(xml_item.attrib[atr_key]): xml_item for xml_item in xml_tree1.iter(key)}
        arttib_to_item2 = {str(xml_item.attrib[atr_key]): xml_item for xml_item in xml_tree2.iter(key)}
    else:
        arttib_to_item1 = {str(xml_item.attrib): xml_item for xml_item in xml_tree1.iter(key)}
        arttib_to_item2 = {str(xml_item.attrib): xml_item for xml_item in xml_tree2.iter(key)}
    if str(arttib_to_item1.keys()) == str(arttib_to_item2.keys()):
        return zip(arttib_to_item1.values(), arttib_to_item2.values())
    arttib_to_item1 = dict(sorted(arttib_to_item1.items()))
    arttib_to_item2 = dict(sorted(arttib_to_item2.items()))
    if str(arttib_to_item1.keys()) != str(arttib_to_item2.keys()):
        tkltest_status('xml files can not be compared, with key {} got two different lists:\n{}\n{}'
                       .format(key, str(arttib_to_item1.keys()), str(arttib_to_item2.keys())), error=True)
        exit(1)
    return zip(arttib_to_item1.values(), arttib_to_item2.values())



'''

    CoverageStatistics class:
    A class that represent the compare between two coverage statistics of an app, package, class or method.
    it has four derived class:
    AppCoverageStatistics, PackageCoverageStatistics, ClassCoverageStatistics and MethodCoverageStatistics.
    Every class has a pointer to a parent, and a list of children.
    For example, ClassCoverageStatistics will have a pointer to PackageCoverageStatistics and list of MethodCoverageStatistics
    Basically, we get a tree of CoverageStatistics instances, with AppCoverageStatistics as a root.


    The information that we use in the xml look like:
    1. for each app/package/class/method, there is a summery of the form:
                        counter type="INSTRUCTION" missed="71" covered="994"/>
                        <counter type="BRANCH" missed="1" covered="17"/>
                        <counter type="LINE" missed="12" covered="201"/>
                        <counter type="COMPLEXITY" missed="4" covered="57"/>
                        <counter type="METHOD" missed="3" covered="49"/>
                        <counter type="CLASS" missed="0" covered="5"/>

    2. for every source line there is an entry of the form:
                        <line nr="16" mi="0" ci="2" mb="0" cb="0"/>


    CoverageStatistics holds a dict of counters, a counter per:
                'INSTRUCTION','BRANCH','COMPLEXITY','LINE','METHOD','CLASS'
    each counter holds:
    1. the total of covered/missed, the value that we read directly from the xml
    2. the diff between the test suits, which is calculated by processing the xml line coverage

    The main steps of the report creation:
    1. reading the XML files, generated by jacococli, build the tree, and update the counters with the total xml values.
    2. parse the .class files of the app, to get the source line numbers of each method
    3. iterating over the xml line information, and update only the 'LINE' counter:
                -which line was missed by both test suites,
                -which line was missed by first test suite ,
                -which line was missed by second test suite,
                -which line was not missed by any of the test suite,
        ( we can consider updating also 'METHOD' and 'CLASS' counters )
        ( we can consider updating also 'INSTRUCTION' and 'BRANCH' counters, but the result will not be accurate )


'''


class CoverageStatistics:

    # These variables are static - same values for all instances
    test_suite_name1 = ''
    test_suite_name2 = ''

    class DiffCounter:
        counter_types = ['INSTRUCTION', 'BRANCH', 'COMPLEXITY', 'LINE', 'METHOD', 'CLASS']
        def __init__(self, covered1, covered2, missed1, missed2):
            self.total_covered1 = covered1
            self.total_covered2 = covered2
            self.total_missed1 = missed1
            self.total_missed2 = missed2
            if self.total_covered1 + self.total_missed1 != self.total_covered2 + self.total_missed2:
                tkltest_status('xml files can not be compared, counters total does not match', error=True)
                sys.exit(1)
            self.total = self.total_covered1 + self.total_missed1
            self.missed_both = 0
            self.missed_only1 = 0
            self.missed_only2 = 0
            self.missed_none = 0

        def integrity_check(self, name):
            counted_total = self.missed_none + self.missed_only2 + self.missed_only1 + self.missed_both
            if self.missed_none + self.missed_only2 != self.total_covered1:
                logging.debug('DiffCounter integrity {}. total_covered1 {} vs {} '
                              .format(name, self.missed_none + self.missed_only2, self.total_covered1))
            if self.missed_none + self.missed_only1 != self.total_covered2:
                logging.debug('DiffCounter integrity {}. total_covered2 {} vs {} '
                              .format(name, self.missed_none + self.missed_only1, self.total_covered2))
            if counted_total != self.total:
                logging.debug('DiffCounter integrity {}. total_covered {} vs {} '
                              .format(name, counted_total, self.total))

    def __init__(self, parent=None, test_suite_name1='', test_suite_name2=''):
        self.name = ''
        self.pretty_name = ''
        self.counters = {}
        self.children = []

        self.parent = parent
        if self.parent:
            self.parent.children.append(self)
        if test_suite_name1:
            CoverageStatistics.test_suite_name1 = test_suite_name1
        if test_suite_name2:
            CoverageStatistics.test_suite_name2 = test_suite_name2

    def get_pretty_name(self):
        return self.pretty_name

    def read_data(self, xml_entry1, xml_entry2):
        self.set_names(xml_entry1)
        self.__update_statistics(xml_entry1, xml_entry2)

    def __update_statistics(self, xml_entry1, xml_entry2):
        '''
        Update the counters with the xml total values
        '''
        for coverage_type in self.DiffCounter.counter_types:
            coverage_counter1 = [counter for counter in xml_entry1 if 'type' in counter.attrib and counter.attrib['type'] == coverage_type]
            coverage_counter2 = [counter for counter in xml_entry2 if 'type' in counter.attrib and counter.attrib['type'] == coverage_type]
            if len(coverage_counter1) == 0 and len(coverage_counter2) == 0:
                continue
            if len(coverage_counter1) != 1 or len(coverage_counter2) != 1:
                tkltest_status('xml files can not be compared, {} has more then one counter'.format(coverage_type), error=True)
                sys.exit(1)
            coverage_counter1 = coverage_counter1[0]
            coverage_counter2 = coverage_counter2[0]
            self.counters[coverage_type] = self.DiffCounter(int(coverage_counter1.attrib['covered']),
                                                            int(coverage_counter2.attrib['covered']),
                                                            int(coverage_counter1.attrib['missed']),
                                                            int(coverage_counter2.attrib['missed']))

    def integrity_check(self, prefix):
        name = prefix + '.' + self.pretty_name
        for child in self.children:
            child.integrity_check(name)
        if 'LINE' in self.counters.keys():
            self.counters['LINE'].integrity_check(name + '.LINE')

    def update_line_statistics(self, mi1, mi2, ci1, ci2):
        '''
        update the counters (only 'LINE' counter with the diff values, using the one line statistics)
        Args:
            mi1: number of missed instructions by the first test suit
            mi2: number of missed instructions by the first second suit
            ci1: number of covered instructions by the first test suit
            ci2: number of covered instructions by the first second suit

        Returns:

        '''
        line_fully_missed1 = int(ci1) == 0
        line_fully_missed2 = int(ci2) == 0
        all_lines_missed1 = not self.counters['LINE'].total_covered1
        all_lines_missed2 = not self.counters['LINE'].total_covered2
        line_missed1 = all_lines_missed1 or line_fully_missed1
        line_missed2 = all_lines_missed2 or line_fully_missed2

        self.counters['LINE'].missed_both  +=     line_missed1 and     line_missed2
        self.counters['LINE'].missed_only1 +=     line_missed1 and not line_missed2
        self.counters['LINE'].missed_only2 += not line_missed1 and     line_missed2
        self.counters['LINE'].missed_none  += not line_missed1 and not line_missed2



class MethodCoverageStatistics(CoverageStatistics):
    def __init__(self, cls):
        super().__init__(parent=cls)
        self.signature = ''

    def set_names(self, xml_entry):
        self.name = xml_entry.attrib['name']
        desc = xml_entry.attrib['desc']
        self.signature = self.name + desc
        parameters, return_value = code_util.get_method_parameters(desc)
        self.pretty_name = self.name + '(' + ', '.join(parameters) + ')'
        self.pretty_name = self.pretty_name.replace('<init>', self.parent.pretty_name)
        self.pretty_name = self.pretty_name.replace('<clinit>()', 'static {...}')

    def get_type(self):
        return 'Method'


class ClassCoverageStatistics(CoverageStatistics):

    def __init__(self, package):
        super().__init__(parent=package)
        self.file_name = ''

    def set_names(self, xml_entry):
        self.name = xml_entry.attrib['name']
        # removing the package name from the class name
        self.pretty_name = self.name.replace(self.parent.name + '/', '', 1)
        self.file_name = xml_entry.attrib['sourcefilename']


    def get_type(self):
        return 'Class'


class PackageCoverageStatistics(CoverageStatistics):

    def __init__(self, app, monolith_app_path):
        super().__init__(parent=app)
        self.line_to_methods = {}
        self.monolith_app_path = monolith_app_path

    def set_names(self, xml_entry):
        self.name = xml_entry.attrib['name']
        self.pretty_name = self.name.replace('/', '.')

    def get_type(self):
        return 'Package'

    def parse_class_file(self, current_class):
        '''
        this method parse of the .class files. it:
        1. call the parser, to  update the dict lines_tables.
        3. update the dict line_to_methods, to be used when reading the lines info from the xml
        '''

        class_name = current_class.get_pretty_name()
        class_file_names = [path + os.sep + self.name + os.sep + class_name + '.class'
                            for path in self.monolith_app_path
                            if os.path.isfile(path + os.sep + self.name + os.sep + class_name + '.class')]
        if len(class_file_names) != 1:
            tkltest_status('Could not find .class file for class {} in package {} at {}'
                           .format(class_name, self.name, self.monolith_app_path), error=True)
            exit(1)
        class_file_name = class_file_names[0]

        methods_lines = code_util.get_methods_lines(class_file_name)
        if not self.line_to_methods.get(current_class.file_name):
            self.line_to_methods[current_class.file_name] = {}
        for method in current_class.children:
            method_lines = methods_lines[method.signature]
            for line_number in method_lines:
                if not self.line_to_methods[current_class.file_name].get(line_number):
                    self.line_to_methods[current_class.file_name][line_number] = set()
                self.line_to_methods[current_class.file_name][line_number].add(method)

    def read_sourceline_stat(self, xml_entry1, xml_entry2, file_name):
        '''
        methods to update the methods with the line coverage info
        Args:
            xml_entry1/2: the xml line entry
            file_name: relevant file name
        '''
        line_number = int(xml_entry1.attrib['nr'])
        mi1, mi2 = int(xml_entry1.attrib['mi']), int(xml_entry2.attrib['mi'])
        ci1, ci2 = int(xml_entry1.attrib['ci']), int(xml_entry2.attrib['ci'])
        classes_to_update = set()
        if line_number not in self.line_to_methods[file_name].keys():
            tkltest_status('xml files can not be compared, line number {}{} does not have methods'.format(file_name, line_number), error=True)
            sys.exit(1)
        for method in self.line_to_methods[file_name][line_number]:
            method.update_line_statistics(mi1, mi2, ci1, ci2)
            classes_to_update.add(method.parent)
        for cls in classes_to_update:
            cls.update_line_statistics(mi1, mi2, ci1, ci2)
        self.update_line_statistics(mi1, mi2, ci1, ci2)
        self.parent.update_line_statistics(mi1, mi2, ci1, ci2)


class AppCoverageStatistics(CoverageStatistics):

    def __init__(self, test_suite_name1, test_suite_name2, app_name):
        super().__init__(test_suite_name1=test_suite_name1, test_suite_name2=test_suite_name2)
        self.name = app_name

    def set_names(self, xml_entry):
        self.pretty_name = self.name

    def get_type(self):
        return 'App'





