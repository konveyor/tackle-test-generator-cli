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

import shutil

from tkltest.util import constants
from tkltest.util import coverage_util
from tkltest.execute.coverage_statistics import *
from tkltest.execute import coverage_statistics

def compare_to_dev_tests_coverage(config):
    """
    a method that compare the coverage of two test suits
    it first run the jacoco cli twice, for each suit, to create coverage xml files
    and then, call __compare_coverage_xml() to generate an html compare files

    :param config:

    """
    app_name = config['general']['app_name']
    test_root_dir = config['general']['test_directory']
    if test_root_dir == '':
        test_root_dir = app_name + constants.TKLTEST_DEFAULT_CTDAMPLIFIED_TEST_DIR_SUFFIX
    main_reports_dir = config['general']['reports_path']
    if not main_reports_dir:
        main_reports_dir = app_name + constants.TKLTEST_MAIN_REPORT_DIR_SUFFIX

    tkltest_test_name = os.path.basename(test_root_dir)
    tkltest_html_dir = os.path.join(main_reports_dir, constants.TKL_CODE_COVERAGE_REPORT_DIR, tkltest_test_name)
    tkltest_coverage_xml = os.path.join(tkltest_html_dir, 'jacoco.xml')

    compare_report_dir = os.path.join(main_reports_dir, constants.TKL_CODE_COVERAGE_COMPARE_REPORT_DIR)
    if os.path.isdir(compare_report_dir):
        shutil.rmtree(compare_report_dir)
    os.mkdir(compare_report_dir)

    dev_test_name = os.path.basename(os.path.dirname(config['dev_tests']['build_file']))
    dev_coverage_exec = config['dev_tests']['coverage_exec_file']
    dev_coverage_xml = os.path.join(compare_report_dir, dev_test_name + '_coverage.xml')
    dev_html_dir = os.path.join(compare_report_dir, dev_test_name + '-html')
    coverage_util.generate_coverage_report(monolith_app_path=config['general']['monolith_app_path'],
                                           exec_file=dev_coverage_exec,
                                           xml_file=dev_coverage_xml,
                                           html_dir=dev_html_dir)

    html_compare_dir = os.path.join(compare_report_dir, constants.TKL_CODE_COVERAGE_COMPARE_HTML_DIR)
    coverage_statistics.compare_coverage_xml(xml_file1=dev_coverage_xml, xml_file2=tkltest_coverage_xml,
                           html1_dir=dev_html_dir, html2_dir=tkltest_html_dir, html_compare_dir=html_compare_dir,
                           test_name1=dev_test_name,
                           test_name2=tkltest_test_name,
                           monolith_app_path=config['general']['monolith_app_path'],
                           app_name=app_name)


