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

import logging
import os
import shutil
import subprocess
import sys
import json

from tkltest.util import constants, command_util
from tkltest.util.unit import build_util, dir_util
from tkltest.util.logging_util import tkltest_status


def generate_evosuite(config, output_dir):
    """Generates test cases using evosuite.

    Generates test cases using the EvoSuite test generator standalone. The generated tests are stored in the
    specified test directory or by default in <app-name>-evosuite-standalone-tests. The structure of the directory
    is created by EvoSuite.

    Args:
        config (dict): loaded and validated config information
    """
    # partitions file currently not supported for evosuite
    # if config['generate']['partitions_file']:
    #     tkltest_status('-pf/--partitions-file option is currently not supported for evosuite generator')
    #     sys.exit(0)

    app_name = config['general']['app_name']
    build_type = config['general']['build_type']
    classpath = __get_classpath(config)
    tkltest_status('Generating test suite using Evosuite for application: ' + app_name)
    app_copy_folder, target_folder = __arrange_folders_for_evosuite(config['general']['monolith_app_path'], config)
    evosuite_command = "java -cp \"" + classpath + os.pathsep + os.path.join(constants.TKLTEST_LIB_DOWNLOAD_DIR,
                                        'evosuite-standalone-runtime-'+constants.EVOSUITE_VERSION+'.jar')
    if target_folder:
        evosuite_command += os.pathsep + app_copy_folder + os.sep
    evosuite_command += "\"" #end of -cp
    evosuite_command += " -jar " + os.path.join(constants.TKLTEST_LIB_DOWNLOAD_DIR, "evosuite-master-"+constants.EVOSUITE_VERSION+".jar")
    if target_folder:
        evosuite_command += " -target " + target_folder
    else:
        evosuite_command += " -target " + app_copy_folder

    evosuite_flags, evosuite_output_dir = __get_evosuite_flags(config)
    evosuite_command += evosuite_flags
    logging.info(evosuite_command)
    try:
        command_util.run_command(command=evosuite_command, verbose=config['general']['verbose'])
    except subprocess.CalledProcessError as e:
        tkltest_status('Generating test suite using Evosuite failed: {}\n{}'.format(e, e.stderr), error=True)
        sys.exit(1)
    tkltest_status('Generated Evosuite test suite written to {}'.format(evosuite_output_dir))

    if config['general']['reports_path']:
        reports_dir = config['general']['reports_path']
    else:
        reports_dir = app_name+constants.TKLTEST_MAIN_REPORT_DIR_SUFFIX

    # generate a build file
    build_file = build_util.generate_build_xml(
        app_name=app_name,
        build_type=build_type,
        monolith_app_path=config['general']['monolith_app_path'],
        app_classpath=build_util.get_build_classpath(config),
        test_root_dir=evosuite_output_dir,
        test_dirs=[evosuite_output_dir],
        # partitions_file=config['generate']['partitions_file'],
        target_class_list=config['generate']['target_class_list'],
        main_reports_dir=reports_dir,
        output_dir=output_dir
    )
    tkltest_status('Generated {} build file {}'.format(build_type, os.path.abspath(os.path.join(evosuite_output_dir, build_file))))
    build_util.integrate_tests_into_app_build_file(config['generate']['app_build_files'],
                                                   config['general']['build_type'],
                                                   [evosuite_output_dir])


def generate_randoop(config, output_dir):
    """Generates test cases using randoop.

    Generates test cases using the Randoop test generator standalone. The generated tests are stored in the
    specified test directory or by default in <app-name>-randoop-standalone-tests. The structure of the directory
    and test classes is created by Randoop.

    Args:
        config (dict): loaded and validated config information
    """
    # partitions file currently not supported for randoop
    # if config['generate']['partitions_file']:
    #     tkltest_status('-pf/--partitions-file option is currently not supported for randoop generator')
    #     sys.exit(0)

    monolith_app_path = config['general']['monolith_app_path']
    app_name = config['general']['app_name']
    build_type = config['general']['build_type']
    time_limit = config['generate']['time_limit']
    tkltest_status('Generating test suite using Randoop for application: ' + app_name)
    classpath = __get_classpath(config)
    classpath += os.pathsep + os.pathsep.join(monolith_app_path)
    classpath += os.pathsep + os.path.join(constants.TKLTEST_LIB_DOWNLOAD_DIR, 'randoop-'+constants.RANDOOP_VERSION+'.jar')
    randoop_command = "\"" + os.path.join(config['general']['java_jdk_home'], "bin", "java") + "\" -classpath \"" + \
                      classpath + "\""
    if 'test_directory' not in config['general'].keys() or \
            config['general']['test_directory'] == '':
        randoop_output_dir = app_name+constants.TKLTEST_DEFAULT_RANDOOP_TEST_DIR_SUFFIX
    else:
        randoop_output_dir = config['general']['test_directory']

    randoop_command += " randoop.main.Main gentests --junit-output-dir=" + randoop_output_dir
    # if config['generate']['partitions_file']:
    #     randoop_command += " --classlist=" + __generate_class_list_file(__parse_partitions_file(config['generate']['partitions_file']),
    #                                                                     config['general']['app_name'],
    #                                                                     config['generate']['excluded_class_list'])
    # elif
    if config['generate']['target_class_list']:
        randoop_command += " --classlist=" + __generate_class_list_file(config['generate']['target_class_list'],
                                                                        config['general']['app_name'],
                                                                        config['generate']['excluded_class_list'])
    else:
        randoop_command += " --classlist=" + __generate_class_list_all_app(monolith_app_path,
                                                                           config['general']['app_name'],
                                                                           config['generate']['excluded_class_list'])
    if 'max_memory' in config['generate']['randoop'].keys():
        randoop_command += " --jvm-max-memory="+config['generate']['randoop']['max_memory']+"mb"
    randoop_command += __get_randoop_flags(config, time_limit)
    logging.info(randoop_command)
    try:
        command_util.run_command(command=randoop_command, verbose=config['general']['verbose'])
    except subprocess.CalledProcessError as e:
        tkltest_status('Generating test suite using Randoop failed: {}\n{}'.format(e, e.stderr), error=True)
        sys.exit(1)
    tkltest_status('Generated Randoop test suite written to {}'.format(randoop_output_dir))

    # generate a build file
    build_file = build_util.generate_build_xml(
        app_name=app_name,
        build_type=build_type,
        monolith_app_path=monolith_app_path,
        app_classpath=build_util.get_build_classpath(config),
        test_root_dir=randoop_output_dir,
        test_dirs=[randoop_output_dir],
        # partitions_file=config['generate']['partitions_file'],
        target_class_list=config['generate']['target_class_list'],
        main_reports_dir=app_name+constants.TKLTEST_MAIN_REPORT_DIR_SUFFIX,
        output_dir=output_dir
    )
    tkltest_status('Generated {} build file {}'.format(build_type, os.path.abspath(os.path.join(randoop_output_dir, build_file))))
    build_util.integrate_tests_into_app_build_file(config['generate']['app_build_files'],
                                                   config['general']['build_type'],
                                                   [randoop_output_dir])


def __arrange_folders_for_evosuite(paths_list,  config):
    # first copy the app to a new folder containing all the files.
    copy_dir_name = 'evosuite-app-copy'
    shutil.rmtree(copy_dir_name, ignore_errors=True)
    for p in paths_list:
        shutil.copytree(p, copy_dir_name)

    # if config['generate']['partitions_file']:
    #     target_list = [f + ".class" for f in __parse_partitions_file(config['generate']['partitions_file'])]
    # elif
    if config['generate']['target_class_list']:
        target_list = [f.replace(".", os.sep) + ".class" for f in config['generate']['target_class_list']]
    else:
        for cl in config['generate']['excluded_class_list']:
            os.remove(os.path.join(copy_dir_name, cl.replace(".",os.sep)+".class"))
        return os.path.abspath(copy_dir_name), ""

    target_list = [cl for cl in target_list if not cl[:-len(".class")] in config['generate']['excluded_class_list']]

    target_dir_name = 'evosuite-test-targets'

    if os.path.exists(target_dir_name):
        shutil.rmtree(target_dir_name)
    os.mkdir(target_dir_name)

    target_root = os.path.abspath(target_dir_name)
    for root, directories, files in os.walk(copy_dir_name, topdown=False):
        for name in files:
            if name.endswith(".class"):
                for target in target_list:
                    if os.path.join(root, name).endswith(target):
                        target_path = os.path.join(target_root, root[len(copy_dir_name)+len(os.sep):])
                        if not os.path.exists(target_path):
                            os.makedirs(target_path)

                        os.rename(os.path.join(root, name), os.path.join(target_path, name))
                        continue

    return os.path.abspath(copy_dir_name),os.path.abspath(target_dir_name)


def __get_classpath(config):
    class_paths = []
    classpath = config['general']['app_classpath_file']
    if classpath is None:
        raise Exception('app_classpath_file not specified in config file or command line')
    class_path_file = open(os.path.abspath(classpath), "r")
    while True:
        line = class_path_file.readline()
        if not line:
            break
        class_paths.insert(0, os.path.abspath(line.strip("\n")))

    return os.pathsep.join(class_paths)


def __slicer(str, sub):
    index = str.find(sub)
    if index != -1: return str[index:]


def __format_string(str):
    formatted_str = __slicer(str, "com/")
    formatted_str = formatted_str.replace("/", ".")
    formatted_str = formatted_str[:-len(".java")]
    return formatted_str


# def __parse_partitions_file(file):
#     with open(file, "r") as f:
#         partitions_file_dict = json.loads(f.read())
#     flat_list_formatted = list(set(list(map(lambda x: __format_string(x), set([item for elem in [item[1]['Proxy'] for item in partitions_file_dict.items()] for item in elem])))))
#     # return os.pathsep.join(flat_list_formatted)
#     return flat_list_formatted


def __generate_class_list_all_app(paths, app_name, excluded_class_list):

    class_list = []
    for path in paths:
        class_files = [os.path.relpath(os.path.join(root, name), path) for root, dirs, files in os.walk(path)
                       for name in files if name.endswith('.class') and not name.endswith("package-info.class")]
        class_files = [c.replace(os.sep, '.')[:-len('.class')] for c in class_files]
        class_files = list(set(class_files))
        class_list = class_list + class_files

    return __generate_class_list_file(class_list, app_name, excluded_class_list)


def __generate_class_list_file(class_list, app_name, excluded_class_list):

    class_list = [cl for cl in class_list if not cl in excluded_class_list]

    tkltest_status('Generating test cases for {} classes'.format(len(class_list)))
    class_list_file_name = app_name + '_class_list.txt'

    if os.path.exists(class_list_file_name):
        os.remove(os.path.abspath(class_list_file_name))

    with open(class_list_file_name, "w") as outfile:
        outfile.write("\n".join(class_list))
    return os.path.abspath(class_list_file_name)


def __get_randoop_flags(config, time_limit):
    flags = " --no-error-revealing-tests=" + str(not config['generate']['bad_path']).lower()
    flags += " --no-regression-assertions=" + str(config['generate']['no_diff_assertions']).lower()
    if int(time_limit) > 0:
        flags += " --time-limit=" + str(time_limit)
    return flags


def __get_evosuite_flags(config):
    flags = ""
    time_limit = config['generate']['time_limit']
    if config['generate']['evosuite']['criterion']:
        criterion_list = ":".join(config['generate']['evosuite']['criterion'])
        flags += " -criterion " + criterion_list
    if int(time_limit) > 0:
        flags += " -Dsearch_budget=" + str(time_limit)
    flags += " -Dassertions=" + str(not config['generate']['no_diff_assertions']).lower()
    # flags += " -Djee="+str(config['generate']['jee_support']).lower()
    if 'test_directory' not in config['general'].keys() or \
            config['general']['test_directory'] == '':
        output_dir = config['general']['app_name'] + constants.TKLTEST_DEFAULT_EVOSUITE_TEST_DIR_SUFFIX
    else:
        output_dir = config['general']['test_directory']
    flags += " -Dtest_dir=" + output_dir
    return flags, output_dir

