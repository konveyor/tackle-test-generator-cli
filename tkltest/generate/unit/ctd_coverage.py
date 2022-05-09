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

import json
import os
import sys
import operator
import logging
from tkltest.util import constants

def create_test_plan_report(ctd_coverage_file, ctd_test_plans_file, report_output_dir):
    """Creates CTD coverage report.

    Creates HTML coverage report showing coverage of CTD test plan rows by the generated ctd-amplified
    test cases. The report shows coverage rates at class and method levels.

    Args:
        ctd_coverage_file (str): name of JSON file containing CTD coverage (created by extender)
        ctd_test_plans_file (str): name of JSON file containing CTD test models and test plans
        report_output_dir (str): name of directory to create the report in
    """

    with open(ctd_test_plans_file) as ctd_model:
        ctd_model_rows = json.load(ctd_model)
    with open(ctd_coverage_file) as json_file:
        coverage_data = json.load(json_file)
    cnt = 0
    summary = []
    for partition in coverage_data.items():
        classes_in_partition = []
        for proxy_class in partition[1].items():
            class_name = proxy_class[0]
            methods_in_class = []
            for proxy_method in proxy_class[1].items():  # current_method = ("",[0,0,0,0]) #(name, [cover,uncovered,patial,total_len] )
                count_covered, count_uncovered, count_partial = 0, 0, 0
                str_summary_test_plan_per_method = ""
                list_to_sort = []
                for row in proxy_method[1].items():
                    method_name = proxy_method[0]
                    current_len = len(proxy_method[1])
                    current_row = row[0].split("_")[3]
                    current_row_values = ""
                    title_for_row = ""
                    if row[1].startswith("COVERED"):
                        count_covered += 1
                        title_for_row = "<b>Covered:</b><br>"

                    elif row[1].startswith("UNCOVERED"):
                        count_uncovered += 1
                        title_for_row = "<b>Uncovered:</b><br>"
                    elif row[1].startswith("PARTIAL"):
                        count_partial += 1
                        title_for_row = "<b>Partially covered:</b><br>"
                    else:
                        logging.error("Unrecognized coverage option in coverage_report.json: "+row[1])

                    current_row_values += title_for_row
                    current_row_values += __get_test_plan_for_method_row(ctd_model_rows, class_name,
                                                                         method_name,
                                                                         int(current_row) - 1,
                                                                         partition[0])
                    current_row_values += "<br>"
                    list_to_sort.insert(0, (int(current_row), "<li>"+current_row_values+"</il>"))

                list_to_sort.sort(key=operator.itemgetter(0))
                if list_to_sort is not None:
                    if len(list_to_sort)>0:
                        for i in range(len(list_to_sort)):
                            str_summary_test_plan_per_method += list_to_sort[i][1]

                current_method = (method_name, [count_covered, count_uncovered, count_partial, current_len])
                if count_partial == 0 and count_covered == 0 and count_uncovered ==0:
                    continue
                str_method_stats, progress_method = __calculate_stats_for_method(current_method)
                str_method_stats += "<br>"
                progress_bar_str_method,cnt = __progress_bar(progress_method, cnt)
                methods_in_class.insert(0, (str_method_stats + progress_bar_str_method, current_method,
                                            "<ol>"+str_summary_test_plan_per_method+"</ol>"))
            str_class, progress, method_stats, row_stats = __calculate_stats_for_class(class_name, methods_in_class)
            progress_bar_str,cnt = __progress_bar(progress, cnt)
            if str_class == "":
                continue
            classes_in_partition.insert(0, ("class: " + class_name, (str_class + progress_bar_str, methods_in_class),
                                            method_stats, row_stats))
        summary_for_partition, progress_partition = __calculate_stats_for_partition(classes_in_partition)
        progress_bar_str_partition, cnt = __progress_bar(progress_partition, cnt)
        partition_name = "<h1>+ Partition: " + partition[0] + "</h1>" + summary_for_partition + progress_bar_str_partition
        summary.insert(0, (partition_name, classes_in_partition))

    if not os.path.exists(report_output_dir):
        os.makedirs(report_output_dir)

    ctd_file = report_output_dir + os.sep + constants.TEST_PLAN_SUMMARY_NAME

    with open(ctd_file, 'w') as f:
        f.write(
            "<!DOCTYPE html><html><head><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"><style>.collapsible {background-color: #FFFFFF;color: "
            "black;cursor: pointer;padding: 18px;width: 100%;border: none;text-align: left;outline: none;font-size: 12px;}.active, .collapsible:hover "
            "{background-color: #B8B8B8;}.content {padding: 0 18px;display: none;overflow: hidden;background-color: #FFFFFF;}</style></head><body>")
        for partition in summary:
            f.write("<button type=\"button\" class=\"collapsible\">" + partition[
                0] + "</button><div class=\"content\">")
            for class_in_p in partition[1]:
                f.write("<button type=\"button\" class=\"collapsible\">" + class_in_p[1][
                    0] + "</button><div class=\"content\">")  # print class name
                for i in range(len(class_in_p[1][1])):
                    f.write("<button type=\"button\" class=\"collapsible\">" + class_in_p[1][1][i][
                        0] + "</button><div class=\"content\">")  # print method summary
                    f.write("<p>" + class_in_p[1][1][i][2] + "</p></div>")  # print the parameters of the method
                f.write("</div>")
            f.write("</div>")  ##end of partition button
        f.write(
                "<script>var coll = document.getElementsByClassName(\"collapsible\");var i;for (i = 0; i < coll.length; i++) {coll[i].addEventListener(\"click\", function() {this.classList.toggle(\"active\");var content = this.nextElementSibling;if(content.style.display === \"block\") {content.style.display = \"none\";} else {content.style.display = \"block\";}});}</script></body></html>")


def __progress_bar(x, cnt):
    string_res = "<style>#myProgress {width: 10%;background-color: #FF0000;}"
    string_res += "#myBar" + str(cnt) + " {width: " + str(x)
    string_res += "%;height: 30px;background-color: #4CAF50;text-align: center;line-height: 30px;color: white}</style>"
    string_res += "<div id=\"myProgress\"><div id=\"myBar" + str(cnt) + "\">" + str(x) + "%</div></div>"
    return string_res, cnt+1


def __get_test_plan_for_method_row(ctd_coverage_data, class_name, method_name, test_row_number, partition_name):

    class_data = ctd_coverage_data["models_and_test_plans"][partition_name][class_name]

    method_data = [item[1] for item in class_data.items() if
                   (' ' in item[1]['formatted_signature'] and
                    method_name == item[1]['formatted_signature'][item[1]['formatted_signature'].index(' ')+1:]) or
                   (' ' not in item[1]['formatted_signature'] and method_name == item[1]['formatted_signature'])]

    if len(method_data) != 1:
        logging.error("Found "+("more than one" if len(method_data) > 1 else "no")+" matching CTD test plan for "+class_name+"."+method_name+" in "+partition_name)
        for data in method_data:
            logging.error(data)
        sys.exit(1)

    method_model_test_plan = method_data[0]

    test_row = method_model_test_plan['test_plan'][test_row_number]

    param_count = 0
    result = "<ul>"

    for attr_value_struct in test_row:

        attr_val = attr_value_struct['type']
        result += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<li><b>\tParameter </b> " + str(
            param_count) + "<b> type: </b>" + attr_val

        if 'list_types' in attr_value_struct.keys():
            result += str(" containing list types: <br><ul>")
            result += __get_test_plan_val_rec(attr_value_struct['list_types'])
            result += "</ul>"

        if 'key_types' in attr_value_struct.keys():
            result += str(" containing map key types: <br><ul>")
            result += __get_test_plan_val_rec(attr_value_struct['key_types'])
            result += "</ul>"

        if 'value_types' in attr_value_struct.keys():
            result += str(" containing map value types: <br><ul>")
            result += __get_test_plan_val_rec(attr_value_struct['value_types'])
            result += "</ul>"

        result += "</li><br>"

        param_count += 1

    return result+"</ul>"


def __get_test_plan_val_rec(type_struct):

    result = ""

    for val in type_struct['types']:
        result += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + "<li> " + \
                 val + "</li>"

    if 'list_types' in type_struct.keys():
        result += str(" containing list types: <br><ul>")
        result += __get_test_plan_val_rec(type_struct['list_types'])
        result += "</ul>"

    if 'key_types' in type_struct.keys():
        result += str(" containing map key types: <br><ul>")
        result += __get_test_plan_val_rec(type_struct['key_types'])
        result += "</ul>"

    if 'value_types' in type_struct.keys():
        result += str(" containing map value types: <br><ul>")
        result += __get_test_plan_val_rec(type_struct['value_types'])
        result += "</ul>"

    return result


def __calculate_stats_for_method(method_info):

    total_covered = method_info[1][0]
    total_uncovered = method_info[1][1]
    total_partial = method_info[1][2]
    total_rows = method_info[1][3]

    method_summary = "<h3><b>+ Method name: " + method_info[0] +  "</h3></b><br><b>Summary:</b><br>"

    method_summary += "Total ctd rows: {}, ".format( str(total_rows))
    method_summary += "covered: {} ({}%), ".format(str(total_covered),
                                            str(round(((total_covered / total_rows) * 100), 2)))
    method_summary += "partially covered: {} ({}%), ".format(str(total_partial),
                                                    str(round(((total_partial / total_rows) * 100), 2)))
    method_summary += "uncovered: {} ({}%)<br>".format(str(total_uncovered),
                                                    str(round(((total_uncovered / total_rows) * 100), 2)))

    progress_method = round(((total_covered / total_rows) * 100), 2)
    return method_summary, progress_method


def __calculate_stats_for_class(class_name, list):
    total_rows_covered, total_rows_uncovered, total_rows_partial, total_rows = 0, 0, 0, 0
    total_methods_covered, total_methods_uncovered, total_methods = 0, 0, 0

    for _, stats, _ in list:

        total_rows_covered += stats[1][0]
        total_rows_uncovered += stats[1][1]
        total_rows_partial += stats[1][2]
        total_rows += stats[1][3]
        total_methods += 1
        if stats[1][0] + stats[1][2] > 0:
            total_methods_covered += 1
        else:
            total_methods_uncovered += 1

    if total_rows == 0:
        logging.warning("No ctd rows found for class "+class_name)
        return "", 0

    progress_number = round((total_rows_covered / total_rows * 100), 2)
    class_summary = "<h3><b>+ Class name: " + class_name + "</h3></b><br><b>Summary:</b><br>"
    class_summary += "Total methods: {}, ".format(str(total_methods))
    class_summary += "covered: {} ({}%), ".format(str(total_methods_covered),
                                                    str(round(((total_methods_covered / total_methods) * 100), 2)))
    class_summary += "uncovered: {} ({}%)<br>".format(str(total_methods_uncovered),
                                                        str(round(((total_methods_uncovered / total_methods) * 100), 2)))

    class_summary += "Total ctd rows: {}, ".format(str(total_rows))
    class_summary += "covered: {} ({}%), ".format(str(total_rows_covered),
                                                    str(round(((total_rows_covered / total_rows) * 100), 2)))

    class_summary += "partially covered: {} ({}%), ".format(str(total_rows_partial),
                                                                               str(round(
                                                                                   ((total_rows_partial / total_rows) * 100),
                                                                                   2)))
    class_summary += "uncovered: {} ({}%)<br>".format(str(total_rows_uncovered),
                                                                       str(round(((total_rows_uncovered / total_rows) * 100),
                                                                                 2)))
    return class_summary, progress_number, (total_methods, total_methods_covered, total_methods_uncovered), \
           (total_rows, total_rows_covered, total_rows_partial, total_rows_uncovered)


def __calculate_stats_for_partition(classes_in_partition):
    total_rows_covered, total_rows_uncovered, total_rows_partial, total_rows = 0, 0, 0, 0
    total_methods_covered, total_methods_uncovered, total_methods = 0, 0, 0
    total_classes_covered, total_classes_uncovered, total_classes = 0, 0, 0

    for class_info in classes_in_partition:
        class_method_stats = class_info[2]
        class_row_stats = class_info[3]

        total_methods += class_method_stats[0]
        total_methods_covered += class_method_stats[1]
        total_methods_uncovered += class_method_stats[2]

        total_rows += class_row_stats[0]
        total_rows_covered += class_row_stats[1]
        total_rows_partial += class_row_stats[2]
        total_rows_uncovered += class_row_stats[3]

        total_classes += 1
        if total_methods_covered > 0:
            total_classes_covered += 1

    percentage_for_bar = round((total_rows_covered / total_rows * 100), 2)
    partition_summary = "<b>Summary:</b><br>"
    partition_summary += "Total classes: {}, ".format(str(total_classes))
    partition_summary += "covered: {} ({}%), ".format(str(total_classes_covered),
                                                                str(round(
                                                                    ((total_classes_covered / total_classes) * 100),
                                                                    2)))
    partition_summary += "uncovered: {} ({}%)<br>".format(str(total_classes_uncovered),
                                                                  str(round(
                                                                      ((total_classes_uncovered / total_classes) * 100),
                                                                      2)))
    partition_summary += "Total methods: {}, ".format(str(total_methods))
    partition_summary += "covered: {} ({}%), ".format(str(total_methods_covered),
                                                            str(round(((total_methods_covered / total_methods) * 100),
                                                                      2)))
    partition_summary += "uncovered: {} ({}%)<br>".format(str(total_methods_uncovered),
                                                              str(round(
                                                                  ((total_methods_uncovered / total_methods) * 100), 2)))

    partition_summary += "Total ctd rows: {}, ".format(str(total_rows))
    partition_summary += "covered: {} ({}%), ".format(str(total_rows_covered),
                                                         str(round(((total_rows_covered / total_rows) * 100), 2)))

    partition_summary += "partially covered: {} ({}%), ".format(str(total_rows_partial),
                                                                   str(round(
                                                                       ((total_rows_partial / total_rows) * 100),
                                                                       2)))
    partition_summary += "uncovered: {} ({}%)<br>".format(str(total_rows_uncovered),
                                                           str(round(((total_rows_uncovered / total_rows) * 100),
                                                                     2)))

    return partition_summary, percentage_for_bar

