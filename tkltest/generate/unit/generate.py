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

import argparse
import logging
import logging.handlers
import os
import csv
import shutil
import subprocess
import sys
import time
import toml
from threading import Thread


from .augment import augment_with_code_coverage
from .ctd_coverage import create_test_plan_report
from .generate_standalone import generate_randoop, generate_evosuite
from tkltest.util import command_util, constants, config_util
from tkltest.util.unit import build_util, dir_util, coverage_util
from tkltest.util.logging_util import tkltest_status


def process_generate_command(args, config):
    """Processes the generate command.

    Processes the generate command and generates test cases based on the subcommand specified with
    generate: ctd-amplifies, evosuite, or randoop

    Args:
        args: command-line arguments
        config: loaded configuration options
    """
    logging.info('Processing generate command')
    output_dir = dir_util.cd_output_dir(config['general']['app_name'], config['general'].get('module_name', ''))
    # clear test directory content
    test_directory = __reset_test_directory(args, config)

    exclude_classes_covered_by_dev_test(config, output_dir)
    if args.sub_command == "ctd-amplified":
        generate_ctd_amplified_tests(config, output_dir)
    elif args.sub_command == "randoop":
        generate_randoop(config, output_dir)
    elif args.sub_command == "evosuite":
        generate_evosuite(config, output_dir)
    else:
        tkltest_status("sub command "+args.sub_command+" not supported", error=True)
        sys.exit(1)

    # add hidden config file with generate command and options to be used by the execute command
    generate_config = {
        'command': args.command,
        'subcommand': args.sub_command,
        'general': config['general'],
        'generate': config['generate']
    }
    generate_config_file = os.path.join(test_directory, constants.TKLTEST_GENERATE_CONFIG_FILE)
    with open(generate_config_file, 'w') as f:
        toml.dump(generate_config, f)
    dir_util.delete_app_output(config['general']['app_name'])
    dir_util.cd_cli_dir()


def generate_ctd_amplified_tests(config, output_dir):
    """Performs CTD-guided test generation.

    Performs CTD-guided test generation in three main steps (by invoking the related Java components):
    (1) generates the CTD model and test plans, which contain the coverage goals for all classes targeted
    for coverage, (2) generates building-block test sequences using evosuite, randoop, or a combination of
    both tools, and (3) generates extended test sequences, each covering one row of the CTD test plan. The
    final extended test sequences are stored as JUnit classes in the specified/default test directory.

    Args:
        config (dict): loaded and validated config information
    """

    # get relevant configuration options for test generation
    app_name = config['general']['app_name']
    build_type = config['general']['build_type']
    monolith_app_path = config['general']['monolith_app_path']
    app_classpath_file = config['general']['app_classpath_file']
    verbose = config['general']['verbose']
    # app_prefix = config['generate']['ctd_amplified']['refactored_app_path_prefix']
    # app_suffix = config['generate']['ctd_amplified']['refactored_app_path_suffix']

    # partitions_file = None
    # if config['generate']['partitions_file']:
    #     partitions_file = config['generate']['partitions_file']

    target_class_list = []
    if config['generate']['target_class_list']:
        target_class_list = config['generate']['target_class_list']

    excluded_class_list = []
    if config['generate']['excluded_class_list']:
        excluded_class_list = config['generate']['excluded_class_list']

    jdk_path = os.path.join(config['general']['java_jdk_home'], 'bin', 'java')

    start_time = time.time()

    # generate CTD models and test plans
    generate_CTD_models_and_test_plans(app_name,
                                       # partitions_file,
                                       target_class_list, excluded_class_list,
                                       monolith_app_path, app_classpath_file,
                                       # app_prefix, app_suffix,
                                       config['generate']['ctd_amplified']['interaction_level'], jdk_path, verbose)

    tkltest_status("Computing test plans with CTD took "+str(round(time.time()-start_time,2))+" seconds")

    start_time = time.time()

    test_generator_name = config['generate']['ctd_amplified']['base_test_generator']
    time_limit = config['generate']['time_limit']
    ctd_file = app_name+constants.TKL_CTD_TEST_PLAN_FILE_SUFFIX

    # generate building-block test sequences
    reuse_sequences = config['generate']['ctd_amplified']['reuse_base_tests']

    if reuse_sequences:
        if (test_generator_name == constants.COMBINED_TEST_GENERATOR_NAME and \
            (not os.path.isfile(app_name+"_RandoopTestGenerator"+constants.TKL_BB_SEQ_FILE_SUFFIX) \
                or not os.path.isfile(app_name+"_EvoSuiteTestGenerator"+constants.TKL_BB_SEQ_FILE_SUFFIX))) \
                or (test_generator_name != constants.COMBINED_TEST_GENERATOR_NAME and
                    not os.path.isfile(app_name + "_" + test_generator_name + constants.TKL_BB_SEQ_FILE_SUFFIX)):
                    tkltest_status("Basic block test sequence files do not exist, generating from scratch")
                    reuse_sequences = False

    if reuse_sequences:
        tkltest_status("Reusing existing basic block test sequences")
    else:
        run_bb_test_generator(app_name, ctd_file, monolith_app_path, app_classpath_file,
                              test_generator_name, time_limit, jdk_path,
                              # partitions_file,
                              verbose)
        tkltest_status("Generating basic block test sequences with "+test_generator_name+" took " +
            str(round(time.time() - start_time, 2)) + " seconds")

    if test_generator_name == constants.COMBINED_TEST_GENERATOR_NAME:
        bb_seq_file = app_name+"_RandoopTestGenerator"+constants.TKL_BB_SEQ_FILE_SUFFIX+"," + \
                      app_name + "_EvoSuiteTestGenerator" + constants.TKL_BB_SEQ_FILE_SUFFIX
    else:
        bb_seq_file = app_name + "_" + test_generator_name + constants.TKL_BB_SEQ_FILE_SUFFIX

    start_time = time.time()

    # set default test directory if unspecified in config
    if 'test_directory' not in config['general'].keys() or \
            config['general']['test_directory'] == '':
        test_directory = config['general']['app_name'] + constants.TKLTEST_DEFAULT_CTDAMPLIFIED_TEST_DIR_SUFFIX
    else:
        test_directory = config['general']['test_directory']

    tmp_test_directory = test_directory + constants.TKLTEST_TEMP_DIR_SUFFIX
    # generate extended test sequences
    extend_sequences(app_name=app_name, monolith_app_path=monolith_app_path, app_classpath_file=app_classpath_file,
                     ctd_file=ctd_file, bb_seq_file=bb_seq_file, jdk_path=jdk_path,
                     no_diff_assertions=config['generate']['no_diff_assertions'],
                     no_ctd_coverage=config['generate']['ctd_amplified']['no_ctd_coverage'],
                     interaction_level=config['generate']['ctd_amplified']['interaction_level'],
                     # jee_support=config['generate']['jee_support'],
                     bad_path=config['generate']['bad_path'],
                     num_executions=config['generate']['ctd_amplified']['num_seq_executions'],
                     test_directory=tmp_test_directory, verbose=verbose)

    if os.path.exists(test_directory):
        shutil.rmtree(test_directory)
    shutil.move(tmp_test_directory, test_directory)
    tkltest_status("JUnit tests are saved in " + os.path.abspath(test_directory))
    tkltest_status("Extending test sequences and writing junit tests took " +
                 str(round(time.time() - start_time, 2)) + " seconds")

    if not config['generate']['ctd_amplified']['no_ctd_coverage']:
        app_name = config['general']['app_name']

        if os.path.exists(os.path.abspath(app_name+constants.TKL_EXTENDER_COVERAGE_FILE_SUFFIX)):
            generate_ctd_coverage(os.path.abspath(app_name+constants.TKL_EXTENDER_COVERAGE_FILE_SUFFIX),
                                  os.path.abspath(app_name + "_ctd_models_and_test_plans.json"),
                                  app_name + constants.TKLTEST_MAIN_REPORT_DIR_SUFFIX + os.sep +
                                  constants.TKL_CTD_REPORT_DIR)
        else:
            tkltest_status('Cannot generate CTD coverage report because coverage file was not located', error=True)
            sys.exit(1)

        shutil.move(app_name + constants.TKL_EXTENDER_COVERAGE_FILE_SUFFIX,
                    os.path.join(app_name + constants.TKLTEST_MAIN_REPORT_DIR_SUFFIX + os.sep +
                                  constants.TKL_CTD_REPORT_DIR,
                                 app_name + constants.TKL_EXTENDER_COVERAGE_FILE_SUFFIX))

        # combinatorial coverage file may not exist if no methods have more than one test plan row
        if os.path.exists(app_name + constants.TKL_EXTENDER_CTD_COVERAGE_FILE_SUFFIX):
            shutil.move(app_name + constants.TKL_EXTENDER_CTD_COVERAGE_FILE_SUFFIX,
                    os.path.join(app_name + constants.TKLTEST_MAIN_REPORT_DIR_SUFFIX + os.sep +
                    constants.TKL_CTD_REPORT_DIR, app_name + constants.TKL_EXTENDER_CTD_COVERAGE_FILE_SUFFIX))

    # we create this directory, so it will be at the build files, and will be later used for augmentation
    if not os.path.isdir(os.path.join(test_directory, 'monolithic')):
        os.mkdir(os.path.join(test_directory, 'monolithic'))

    # generate a build file
    test_dirs = [
        os.path.join(test_directory, dir) for dir in os.listdir(test_directory)
        if os.path.isdir(os.path.join(test_directory, dir)) and not dir.startswith('.')
    ]

    if config['general']['reports_path']:
        reports_dir = config['general']['reports_path']
    else:
        reports_dir = app_name+constants.TKLTEST_MAIN_REPORT_DIR_SUFFIX

    build_file = build_util.generate_build_xml(
        app_name=app_name,
        build_type=build_type,
        monolith_app_path=monolith_app_path,
        app_classpath=build_util.get_build_classpath(config),
        test_root_dir=test_directory,
        test_dirs=test_dirs,
        # partitions_file=partitions_file,
        target_class_list=target_class_list,
        main_reports_dir=reports_dir,
        app_packages=config['execute']['app_packages'],  # for coverage-based augmentation
        collect_codecoverage=True,  # for coverage-based augmentation
        offline_instrumentation=True if not config['generate']['ctd_amplified']['no_augment_coverage'] else False,
        output_dir=output_dir
    )
    tkltest_status('Generated {} build file {}'.format(build_type, os.path.abspath(os.path.join(test_directory, build_file))))

    # augment CTD-guided tests with coverage-increasing base tests
    if not config['generate']['ctd_amplified']['no_augment_coverage']:
        config['general']['offline_instrumentation'] = True
        start_time = time.time()
        has_coverage = augment_with_code_coverage(config=config, build_file=build_file, build_type=build_type,
                                   ctd_test_dir=test_directory, report_dir=reports_dir)
        if not has_coverage:
            # try augmentation again with online instrumentation.
            # Build files have fixed names hence no need to update the name.
            tkltest_status('Re-running Coverage-driven test-suite augmentation with online instrumentation')
            build_util.generate_build_xml(
                app_name=app_name,
                build_type=build_type,
                monolith_app_path=monolith_app_path,
                app_classpath=build_util.get_build_classpath(config),
                test_root_dir=test_directory,
                test_dirs=test_dirs,
                # partitions_file=partitions_file,
                target_class_list=target_class_list,
                main_reports_dir=reports_dir,
                app_packages=config['execute']['app_packages'],  # for coverage-based augmentation
                collect_codecoverage=True,  # for coverage-based augmentation
                offline_instrumentation=False,
                output_dir=output_dir
            )
            config['general']['offline_instrumentation'] = False
            augment_with_code_coverage(config=config, build_file=build_file, build_type=build_type,
                                       ctd_test_dir=test_directory, report_dir=reports_dir)
        tkltest_status('Coverage-driven test-suite augmentation and optimization took {} seconds'.
                       format(round(time.time() - start_time, 2)))
    build_util.integrate_tests_into_app_build_file(config['generate']['app_build_files'],
                                                   config['general']['build_type'],
                                                   test_dirs)


def generate_CTD_models_and_test_plans(app_name,
                                       # partitions_file,
                                       target_class_list, excluded_class_list, monolith_app_path, app_classpath_file,
                                       # app_prefix, app_suffix,
                                       interaction_level, jdk_path, verbose=False):
    """Generates CTD models and test plans.

    Performs the first step in the generation of CTD-guided tests (generation of CTD models and test plans)
    by invoking the related Java component via a subprocess. CTD models and test plans are generated for
    all public methods of the targeted test classes, one model and test plan per method, and are written to
    a JSON file.

    Args:
        app_name (str): name of the app
        # partitions_file (str): name of file containing information about app partitions (if the modernization task
            involves partitioning the legacy app)
        target_class_list (list): name of specific classes or packages targeted for test generation
        excluded_class_list (list): names of classes or packages to omit from the set of test targets
        monolith_app_path (list): paths to directories containing classes of the legacy app
        app_classpath_file (str): name of file containing library dependencies of app
        # app_prefix (str): path prefix to root directory of refactored app version
        # app_suffix (list): list of paths to refactored app classes
        interaction_level (int): CTD interaction level (strength) for test-plan generation
        jdk_path (str): path to Java VM
        verbose (bool): run in verbose mode printing detailed status messages
    """
    tkltest_status('Computing coverage goals using CTD')

    # build java command to be executed
    modeling_command = "\""+jdk_path+"\" -Xmx2048m -cp "+os.path.join(constants.TKLTEST_UNIT_CORE_JAR)+os.pathsep
    modeling_command += os.path.join(constants.TKLTEST_LIB_DIR, "acts_"+constants.ACTS_VERSION+".jar") + os.pathsep
    modeling_command += os.path.join(constants.TKLTEST_LIB_DOWNLOAD_DIR, "commons-cli-1.4.jar") + os.pathsep
    modeling_command += os.path.join(constants.TKLTEST_LIB_DOWNLOAD_DIR, "soot-"+constants.SOOT_VERSION+".jar") + os.pathsep
    modeling_command += os.path.join(constants.TKLTEST_LIB_DOWNLOAD_DIR, "axml-2.0.0.jar") + os.pathsep
    modeling_command += os.path.join(constants.TKLTEST_LIB_DOWNLOAD_DIR, "slf4j-api-1.7.5.jar") + os.pathsep
    modeling_command += os.path.join(constants.TKLTEST_LIB_DOWNLOAD_DIR, "guava-29.0-jre.jar") + os.pathsep
    modeling_command += os.path.join(constants.TKLTEST_LIB_DOWNLOAD_DIR, "failureaccess-1.0.1.jar") + os.pathsep
    modeling_command += os.path.join(constants.TKLTEST_LIB_DOWNLOAD_DIR, "asm-7.1.jar") + os.pathsep
    modeling_command += os.path.join(constants.TKLTEST_LIB_DOWNLOAD_DIR, "asm-analysis-7.1.jar") + os.pathsep
    modeling_command += os.path.join(constants.TKLTEST_LIB_DOWNLOAD_DIR, "asm-commons-7.1.jar") + os.pathsep
    modeling_command += os.path.join(constants.TKLTEST_LIB_DOWNLOAD_DIR, "asm-tree-7.1.jar") + os.pathsep
    modeling_command += os.path.join(constants.TKLTEST_LIB_DOWNLOAD_DIR, "asm-utils-7.1.jar") + os.pathsep
    modeling_command += os.path.join(constants.TKLTEST_LIB_DOWNLOAD_DIR, "heros-1.2.0.jar") + os.pathsep
    modeling_command += os.path.join(constants.TKLTEST_LIB_DOWNLOAD_DIR, "httpcore-4.4.6.jar") + os.pathsep
    modeling_command += os.path.join(constants.TKLTEST_LIB_DOWNLOAD_DIR, "httpclient-4.5.13.jar") + os.pathsep
    modeling_command += os.path.join(constants.TKLTEST_LIB_DOWNLOAD_DIR, "jackson-databind-2.12.6.1.jar") + os.pathsep
    modeling_command += os.path.join(constants.TKLTEST_LIB_DOWNLOAD_DIR, "jackson-core-2.12.6.jar") + os.pathsep
    modeling_command += os.path.join(constants.TKLTEST_LIB_DOWNLOAD_DIR, "jackson-annotations-2.12.6.jar")
    modeling_command += " org.konveyor.tackle.testgen.model.CTDTestPlanGenerator "
    modeling_command += " -app "+app_name
    # if partitions_file:
    #     modeling_command += " -pf "+partitions_file
    # elif
    if target_class_list:
        modeling_command += " -cl " + '::'.join(target_class_list)

    if excluded_class_list:
        modeling_command += " -el " + '::'.join(excluded_class_list)
    modeling_command += " -pt " + os.pathsep.join(monolith_app_path)
    modeling_command += " -clpt " + app_classpath_file
    # if app_prefix:
    #     modeling_command += " -pp " + app_prefix
    # if app_suffix:
    #     modeling_command += " -ps " + os.pathsep.join(app_suffix)
    modeling_command += " -ic " + str(interaction_level)

    logging.info(modeling_command)

    try:
        command_util.run_command(command=modeling_command, verbose=verbose)
    except subprocess.CalledProcessError as e:
        tkltest_status('Computing CTD coverage goals failed: {}\n{}'.format(e, e.stderr), error=True)
        sys.exit(1)


def run_bb_test_generator(app_name, ctd_file, monolith_app_path, app_classpath_file, test_generator_name,
                          time_limit, jdk_path,
                          # partitions_file,
                          verbose=False):
    """Generates building-block test sequences.

    Generates building-block tests sequences using evosuite, randoop, or both tools in combination. Performs
    test generation by calling the Java component as a subprocess.

    Args:
        app_name (str): name of the app
        ctd_file (str): name of JSON file containing CTD models and test plans
        monolith_app_path (list): paths to directories containing classes of the legacy app
        app_classpath_file (str): name of file containing library dependencies of app
        test_generator_name (str): evosuite, randoop, or combined
        time_limit (int): time limit (in seconds) for evosuite/randoop test generation
        jdk_path (str): path to Java VM
        verbose (bool): run in verbose mode printing detailed status messages
    """
    tkltest_status('Generating basic block test sequences using '+test_generator_name)

    # build the java command to be executed
    tg_command = "\""+jdk_path+"\" -Xmx2048m -cp " + os.path.join(constants.TKLTEST_UNIT_CORE_JAR)+os.pathsep
    tg_command += os.path.join(constants.TKLTEST_LIB_DOWNLOAD_DIR, "randoop-"+constants.RANDOOP_VERSION+".jar") + os.pathsep
    tg_command += os.path.join(constants.TKLTEST_LIB_DOWNLOAD_DIR, "evosuite-standalone-runtime-"+constants.EVOSUITE_VERSION+".jar") + os.pathsep
    tg_command += os.path.join(constants.TKLTEST_LIB_DOWNLOAD_DIR, "evosuite-master-"+constants.EVOSUITE_VERSION+".jar") + os.pathsep
    tg_command += os.path.join(constants.TKLTEST_LIB_DOWNLOAD_DIR, "commons-cli-1.4.jar") + os.pathsep
    tg_command += os.path.join(constants.TKLTEST_LIB_DOWNLOAD_DIR, "soot-4.1.0.jar") + os.pathsep
    tg_command += os.path.join(constants.TKLTEST_LIB_DOWNLOAD_DIR, "commons-io-2.6.jar") + os.pathsep
    tg_command += os.path.join(constants.TKLTEST_LIB_DOWNLOAD_DIR, "javaparser-core-3.16.1.jar") + os.pathsep
    tg_command += os.path.join(constants.TKLTEST_LIB_DOWNLOAD_DIR, "javaparser-symbol-solver-core-3.16.1.jar") + os.pathsep
    tg_command += os.path.join(constants.TKLTEST_LIB_DOWNLOAD_DIR, "guava-29.0-jre.jar") + os.pathsep
    tg_command += os.path.join(constants.TKLTEST_LIB_DOWNLOAD_DIR, "failureaccess-1.0.1.jar") + os.pathsep
    tg_command += os.path.join(constants.TKLTEST_LIB_DOWNLOAD_DIR, "jackson-databind-2.12.6.1.jar") + os.pathsep
    tg_command += os.path.join(constants.TKLTEST_LIB_DOWNLOAD_DIR, "jackson-core-2.12.6.jar") + os.pathsep
    tg_command += os.path.join(constants.TKLTEST_LIB_DOWNLOAD_DIR, "jackson-annotations-2.12.6.jar")
    tg_command += " org.konveyor.tackle.testgen.core.TestSequenceInitializer"
    tg_command += " -app " + app_name
    tg_command += " -tp " + ctd_file
    tg_command += " -pt " + os.pathsep.join(monolith_app_path)
    tg_command += " -clpt " + app_classpath_file
    tg_command += " -tg " + test_generator_name
    tg_command += " -tl " + str(time_limit)
    jdk_home = os.path.split(os.path.split(jdk_path)[0])[0]
    if jdk_home:
        # add double quotes for the case of spaces in the jdk path
        tg_command += " -jdk \"" + jdk_home + "\""

    # if partitions_file:
    #     tg_command += " -tm"

    # for verbose option, redirect evosuite and randoop stdout and stderr to files
    if verbose:
        output_file = app_name + "_" + test_generator_name + '_output.log'
        # error_file = app_name + "_" + test_generator_name + '_error.log'
        tg_command += ' 1> ' + output_file #+ ' 2> ' + error_file
        # tkltest_status("Test generator output and error logs will be written to {} and {}".format(output_file, error_file))
        tkltest_status("Test generator output will be written to {}".format(output_file))
    logging.info(tg_command)

    try:
        # run command with verbose=false because, for the bb test generators, verbose option redirects
        # stdout to a file, so nothing should be printed on command line irrespective of the
        # value of verbose
        command_util.run_command(command=tg_command, verbose=False)
    except subprocess.CalledProcessError as e:
        tkltest_status('Generating basic block sequences failed: {}\n{}'.format(e, e.stderr), error=True)
        sys.exit(1)


def extend_sequences(app_name, monolith_app_path, app_classpath_file, ctd_file, bb_seq_file, jdk_path,
                     no_diff_assertions, no_ctd_coverage, interaction_level,
                     # jee_support,
                     bad_path, num_executions, test_directory, verbose=False):
    """Generates the final CTD-guided test cases.

    Generates extended test sequences for covering the CTD test plan rows that are written as JUnit
    classes in the specified test directory.

    Args:
        app_name (str): name of the app
        monolith_app_path (list): paths to directories containing classes of the legacy app
        app_classpath_file (str): name of file containing library dependencies of app
        ctd_file (str): name of JSON file containing CTD models and test plans
        bb_seq_file (str): name of JSON file containing building-block test sequences
        jdk_path (str): path to Java VM
        no_diff_assertions (bool): do not add assertions for differential testing to the generated tests
        # jee_support (bool): add support JEE mocking in generated tests cases
        bad_path (bool): whether to enerate bad path tests cases
        num_executions (int): number of executions to perform to determine pass/fail status of generated sequences
        test_directory (str): name of root test directory to write JUnit test classes to
        verbose (bool): run in verbose mode printing detailed status messages
    """
    tkltest_status('Extending sequences to reach coverage goals and generating junit tests')

    te_command = "\"" + jdk_path + "\""
    te_command += " -Xmx2048m -Xbootclasspath/a:"+constants.TKLTEST_LIB_DOWNLOAD_DIR+os.sep+"replacecall-"+constants.RANDOOP_REPLACECALL_VERSION+\
                  ".jar -javaagent:"+constants.TKLTEST_LIB_DOWNLOAD_DIR+os.sep+"replacecall-"+constants.RANDOOP_REPLACECALL_VERSION+".jar"
    te_command += " -cp \"" + os.path.join(constants.TKLTEST_UNIT_CORE_JAR) + os.pathsep
    te_command += os.path.join(constants.TKLTEST_LIB_DOWNLOAD_DIR, "randoop-"+constants.RANDOOP_VERSION+".jar") + os.pathsep
    te_command += os.path.abspath(app_name+constants.TKL_EVOSUITE_OUTDIR_SUFFIX) + os.pathsep
    # if jee_support:
    #     te_command += os.path.join(constants.TKLTEST_LIB_DIR,
    #                                "evosuite-standalone-runtime-"+constants.EVOSUITE_VERSION+"-SNAPSHOT.jar") + os.pathsep
    #     te_command += os.path.join(constants.TKLTEST_LIB_DOWNLOAD_DIR, "junit-4.13.1.jar") + os.pathsep
    te_command += os.path.join(constants.TKLTEST_LIB_DIR, "ccmcl.jar") + os.pathsep
    te_command += os.path.join(constants.TKLTEST_LIB_DOWNLOAD_DIR, "jackson-databind-2.12.6.1.jar") + os.pathsep
    te_command += os.path.join(constants.TKLTEST_LIB_DOWNLOAD_DIR, "jackson-core-2.12.6.jar") + os.pathsep
    te_command += os.path.join(constants.TKLTEST_LIB_DOWNLOAD_DIR, "jackson-annotations-2.12.6.jar") + os.pathsep
    te_command += os.path.join(constants.TKLTEST_LIB_DOWNLOAD_DIR, "commons-cli-1.4.jar") + os.pathsep
    te_command += os.path.join(constants.TKLTEST_LIB_DOWNLOAD_DIR, "commons-io-2.6.jar") + os.pathsep
    te_command += os.path.join(constants.TKLTEST_LIB_DOWNLOAD_DIR, "javaparser-core-3.16.1.jar") + os.pathsep
    te_command += os.path.join(constants.TKLTEST_LIB_DOWNLOAD_DIR,
                               "javaparser-symbol-solver-core-3.16.1.jar") + os.pathsep

    with open(app_classpath_file) as file:
        for line in file:
            if line.endswith('\n'):
                # remove new line from line
                te_command += os.path.abspath(line[:-1]) + os.pathsep
            else:
                te_command += os.path.abspath(line) + os.pathsep
    te_command += os.pathsep.join(monolith_app_path)
    te_command += "\"" # end of "-cp"
    te_command += " org.konveyor.tackle.testgen.core.extender.TestSequenceExtender"
    te_command += " -app " + app_name
    te_command += " -tp " + ctd_file
    te_command += " -ts " + bb_seq_file
    te_command += " -od " + test_directory
    # if jee_support:
    #     te_command += " -jee"

    if bad_path:
        te_command += " -bp"
    if not no_diff_assertions:
        te_command += " -da"
    if not no_ctd_coverage:
        te_command += " -oc " + str(interaction_level)
    te_command += " -ne " + str(num_executions)

    logging.info(te_command)

    coverage_file_name = app_name+constants.TKL_EXTENDER_COVERAGE_FILE_SUFFIX

    # remove existing coverage file - its presence will signal completion of extender
    if os.path.exists(coverage_file_name):
        os.remove(coverage_file_name)

    global proc, thread_error
    proc=None
    thread_error = False

    thread = Thread(target=extender_timeout, args=[te_command, coverage_file_name, verbose])
    thread.start()
    thread.join(constants.EXTENDER_INITIAL_TIMEOUT)
    while (not os.path.exists(coverage_file_name)) and thread.is_alive() and not thread_error:
        thread.join(constants.EXTENDER_REPEATED_TIMEOUT)

    if thread_error:
        sys.exit(1)

    if proc.poll() is None:
        tkltest_status('Extender process has not terminated despite its completion, forcibly terminating it\n')
        proc.kill()


def extender_timeout(command, coverage_file_name, verbose):
    global proc, thread_error
    proc = command_util.start_command(command, verbose=verbose)
    output, error = proc.communicate()

    # return code might not be zero when the extender completed but some threads are still running,
    # hence if coverage file exists we treat the run as succeeded regardless of the return code
    if os.path.exists(coverage_file_name):
        return

    if proc.returncode != 0:
        tkltest_status('Extending sequences and generating JUnit tests failed with return code {}: {}\n'.
                               format(proc.returncode, error), error=True)
        thread_error = True
        # sys.exit will cause only the thread to exit, hence we need thread_error to signal to main process
        sys.exit(1)


def generate_ctd_coverage(ctd_report_file_abs, ctd_model_file_abs, report_output_dir):
    """Generates CTD coverage report.

    Generates report showing coverage of CTD test plans broken down by classes and methods.

    Args:
        ctd_report_file_abs (str): name of coverage report file (created by the extender)
        ctd_model_file_abs (str): name of JSON file containing CTD models and test plans
        report_output_dir (str): name of directory to create report files in
    """

    create_test_plan_report(ctd_report_file_abs, ctd_model_file_abs, report_output_dir)

    tkltest_status("Test plan coverage report is saved in "+
                   os.path.abspath(report_output_dir + os.sep + constants.TEST_PLAN_SUMMARY_NAME))


def __reset_test_directory(args, config):
    # clear contents of test directory
    test_directory = config['general']['test_directory']
    if test_directory == '':
        app_name = config['general']['app_name']
        if args.sub_command == "ctd-amplified":
            test_directory = app_name + constants.TKLTEST_DEFAULT_CTDAMPLIFIED_TEST_DIR_SUFFIX
        elif args.sub_command == "randoop":
            test_directory = app_name + constants.TKLTEST_DEFAULT_RANDOOP_TEST_DIR_SUFFIX
        elif args.sub_command == "evosuite":
            test_directory = app_name + constants.TKLTEST_DEFAULT_EVOSUITE_TEST_DIR_SUFFIX

    directory_to_reset = test_directory;
    if args.sub_command == "ctd-amplified":
        directory_to_reset = directory_to_reset + constants.TKLTEST_TEMP_DIR_SUFFIX

    shutil.rmtree(directory_to_reset, ignore_errors=True)
    os.makedirs(directory_to_reset)
    return test_directory


def exclude_classes_covered_by_dev_test(config, output_dir):
    '''
    exclude classes that are already covered by the developer test suite.
    first running the user test suite and the jacoco cli to get the coverage of the developer test suite
    than add to the excluded_class_list all the classes that already covered
    '''
    if not config['generate']['app_build_files']:
        return
    if config['dev_tests']['coverage_threshold'] >= 100:
        return
    dev_coverage_xml, dev_coverage_html, dev_coverage_csv = coverage_util.get_dev_test_coverage(config, output_dir, create_csv=True)
    # read the csv file
    if not os.path.isfile(dev_coverage_csv):
        tkltest_status('Warning: Failed to obtain dev-tests coverage, generating tests for all classes')
        return
    covered_classes = []
    with open(dev_coverage_csv, newline='') as f:
        coverage_info = csv.DictReader(f)
        for row in coverage_info:
            class_name = '{}.{}'.format(row['PACKAGE'], row['CLASS'])
            inst_covered = int(row['INSTRUCTION_COVERED'])
            inst_missed = int(row['INSTRUCTION_MISSED'])
            total_inst = inst_missed + inst_covered
            if not total_inst or inst_covered*100/total_inst > config['dev_tests']['coverage_threshold']:
                covered_classes.append(class_name)
    config['generate']['excluded_class_list'] += covered_classes
    if covered_classes:
        tkltest_status('The following classes are already covered by developer test suite \n{}.'.format(covered_classes))


if __name__ == '__main__':  # pragma: no cover
    config_file = 'test/data/irs/tkltest_config.toml'
    args = argparse.Namespace()
    args.command = 'generate'
    args.sub_command = 'ctd-amplified'
    args.base_test_generator = 'evosuite'
    args.verbose = True
    config = config_util.load_config(args=args, config_file=config_file)
    config['generate']['ctd_amplified']['no_augment_coverage'] = False
    process_generate_command(args=args, config=config)
