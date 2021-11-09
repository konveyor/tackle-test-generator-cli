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

import logging
import os
import subprocess
import sys

import toml

from tkltest.util import constants, build_util, command_util, dir_util, config_util
from tkltest.util.logging_util import tkltest_status


def process_execute_command(args, config):
    """Processes the execute command.

    Processes the execute command and executes test cases based on the subcommand specified

    Args:
        args: command-line arguments
        config: loaded configuration options
    """
    dir_util.cd_output_dir(config['general']['app_name'])
    config_util.fix_config(config, args.command)
    __execute_base(args, config)
    dir_util.cd_cli_dir()


def __get_test_classes(test_root_dir):
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
    tkltest_status('Total test classes: {}'.format(sum([len(test_files[d]) for d in test_files.keys()])))
    return test_files


# def __remove_test_classes(test_root_dir):
#     for root, dirs, files in os.walk(test_root_dir):
#         for f in files:
#             if f.endswith('.class'):
#                 os.remove(os.path.join(root, f))


def __execute_base(args, config):

    # get list of test classes: either the specified class or the all test classes from the specified
    # test files dir
    test_root_dir = config['general']['test_directory']
    if test_root_dir == '':
        test_root_dir = config['general']['app_name'] + constants.TKLTEST_DEFAULT_CTDAMPLIFIED_TEST_DIR_SUFFIX

    # read generate config from test directory
    gen_config = __get_generate_config(test_root_dir)

    # compute classpath for compiling and running test classes
    classpath = build_util.get_build_classpath(config, gen_config['subcommand'])

    logging.info('test root dir: {}'.format(test_root_dir))
    if 'test_class' in config['execute'].keys() and config['execute']['test_class']:
        test_dir, test_file = os.path.split(config['execute']['test_class'])
        if not test_file.endswith('.java'):
            tkltest_status('Specified test class must be a ".java" file: {}'.format(test_file), error=True)
            return
        test_files = {test_dir: [test_file]}
    else:
        test_files = __get_test_classes(test_root_dir)

    logging.info('test files: {}'.format(test_files))

    # remove test classes from previous runs
    #__remove_test_classes(test_root_dir)

    if gen_config['subcommand'] == 'ctd-amplified':
        # test directory has partitions
        test_dirs = [
            os.path.join(test_root_dir, dir) for dir in os.listdir(test_root_dir)
            if os.path.isdir(os.path.join(test_root_dir, dir)) and not dir.startswith('.')
        ]
    else:
        test_dirs = [test_root_dir]

    # run test classes
    __run_test_cases(create_build=config['execute']['create_build_file'],
        build_type=config['execute']['build_type'],
        app_name=config['general']['app_name'],
        monolith_app_path=config['general']['monolith_app_path'],
        app_classpath=classpath,
        test_root_dir=test_root_dir,
        test_dirs=test_dirs,
        collect_codecoverage=config['execute']['code_coverage'],
        app_packages=config['execute']['app_packages'],
        partitions_file=gen_config['generate']['partitions_file'],
        target_class_list=gen_config['generate']['target_class_list'],
        reports_dir=config['general']['reports_path'],
        offline_inst=not config['execute']['online_instrumentation'],
        verbose=config['general']['verbose']
    )


def __run_test_cases(create_build, build_type, app_name, monolith_app_path, app_classpath, test_root_dir, test_dirs, collect_codecoverage,
    app_packages, partitions_file, target_class_list, reports_dir, offline_inst, env_vars={}, verbose=False, micro=False):
  
    tkltest_status('Compiling and running tests in {}'.format(os.path.abspath(test_root_dir)))

    if reports_dir:
        main_reports_dir = reports_dir
    else:
        main_reports_dir = app_name + constants.TKLTEST_MAIN_REPORT_DIR_SUFFIX

    # generate build files
    if create_build:
        ant_build_file, maven_build_file, gradle_build_file = build_util.generate_build_xml(
            app_name=app_name,
            monolith_app_path=monolith_app_path,
            app_classpath=app_classpath,
            test_root_dir=test_root_dir,
            test_dirs=test_dirs,
            partitions_file=partitions_file,
            target_class_list=target_class_list,
            main_reports_dir=main_reports_dir,
            app_packages=app_packages,
            collect_codecoverage=collect_codecoverage,
            offline_instrumentation=offline_inst
        )
    else:
        ant_build_file = test_root_dir + os.sep + "build.xml"
        maven_build_file  = test_root_dir + os.sep + "pom.xml"
        gradle_build_file  = test_root_dir + os.sep + "build.gradle"

    partitions = [os.path.basename(dir) for dir in test_dirs]

    # no env vars indicate monolith application - will merge code coverage reports after running all test partitions

    # current limitation in ant script - if code coverage is requested then junit report is generated as well

    try:
        if build_type == 'maven':
            command_util.run_command("mvn -f {} clean test site".format(maven_build_file), verbose=verbose)
        elif build_type == 'gradle':
                command_util.run_command("gradle --project-dir {} tklest_task".format(test_root_dir), verbose=verbose)
        else:
            if collect_codecoverage:
                command_util.run_command("ant -f {} merge-coverage-report".format(ant_build_file), verbose=verbose)
            else:
                for partition in partitions:
                    command_util.run_command("ant -f {} {}{}".format(ant_build_file, 'test-reports_', partition),
                            verbose=verbose)

        #else:
         #   task_prefix = 'coverage-reports_' if collect_codecoverage else 'test-reports_' if gen_junit_report else 'execute-tests_'
          #  for partition in partitions:
                #if not env_vars:
             #       __run_command("ant -f {} {}{}".format(ant_build_file, task_prefix, partition),
              #          verbose=verbose)
                #else:
                    # env_vars = env_vars | os.environ # this syntax is valid in python 3.9+
                 #   for env_var in os.environ:
                  #      env_vars[env_var] = os.environ[env_var]
                  #  __run_command("ant -f {} {}{}".format(ant_build_file, task_prefix, partition),
                   #     verbose=verbose, env_vars=env_vars)
    except subprocess.CalledProcessError as e:
        tkltest_status('Error executing junit {}: {}\n{}'.format(build_type, e, e.stderr), error=True)
        sys.exit(1)


    tkltest_status("JUnit reports are saved in " +
                       os.path.abspath(main_reports_dir+os.sep+constants.TKL_JUNIT_REPORT_DIR))
    if collect_codecoverage:
        tkltest_status("Jacoco code coverage reports are saved in " +
                       os.path.abspath(main_reports_dir+os.sep+constants.TKL_CODE_COVERAGE_REPORT_DIR))


def __get_generate_config(test_directory):
    """Reads generate config file.

    Reads the config file created by the generate command from the given test directory
    """
    gen_config_file = os.path.join(test_directory, constants.TKLTEST_GENERATE_CONFIG_FILE)
    if not os.path.isfile(gen_config_file):
        tkltest_status('Generate config file not found: {}'.format(gen_config_file)+
                       '\n\tTo execute tests in {}, the file created by the generate command must be available'.format(
                           test_directory
                       ), error=True)
        sys.exit(1)
    return toml.load(gen_config_file)
