import os
import logging
import subprocess
import sys
import time
import toml
import yaml

from tkltest.util.logging_util import tkltest_status
from tkltest.util import constants, build_util


def process_execute_command(args, config):
    """Processes the execute command.

    Processes the execute command and executes test cases based on the subcommand specified

    Args:
        args: command-line arguments
        config: loaded configuration options
    """
    if args.sub_command == "mono":
        __execute_mono(args, config)
        tkltest_status('"execute mono" done')
    elif args.sub_command == "micro":  # pragma: no branch
        __execute_micro(args, config)
        tkltest_status('"execute micro" done')
#    elif args.sub_command == "report":
#       generate_reports()
    else:
        print("Unsupported sub command: "+args.sub_command)


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


def __remove_test_classes(test_root_dir):
    for root, dirs, files in os.walk(test_root_dir):
        for f in files:
            if f.endswith('.class'):
                os.remove(os.path.join(root, f))


def __execute_mono(args, config):

    # compute classpath for compiling and running test classes
    classpath = build_util.get_build_classpath(config)

    # get list of test classes: either the specified class or the all test classes from the specified
    # test files dir
    test_root_dir = config['general']['test_directory']
    if test_root_dir == '':
        test_root_dir = config['general']['app_name'] + constants.TKLTEST_DEFAULT_CTDAMPLIFIED_TEST_DIR_SUFFIX

    # read generate config from test directory
    gen_config = __get_generate_config(test_root_dir)

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
    __run_test_cases(app_name=config['general']['app_name'],
        monolith_app_path=config['general']['monolith_app_path'],
        app_classpath=classpath,
        test_root_dir=test_root_dir,
        test_dirs=test_dirs,
        gen_junit_report=config['execute']['junit_report'],
        collect_codecoverage=config['execute']['code_coverage'],
        app_packages=config['execute']['app_packages'],
        partitions_file=gen_config['generate']['partitions_file'],
        target_class_list=gen_config['generate']['target_class_list'],
        reports_dir=config['execute']['reports_path'],                     
        offline_inst=config['execute']['offline_instrumentation'],
        verbose=config['general']['verbose']
    )

    # Classify errors
    # classify_errors(args.reports_path, config['execute']['app_packages'],config['general']['test_directory'])

    # print summary stats
    # tkltest_status('Total passing tests: {}/{}'.format(
    #     total_passing_tests, total_passing_tests+total_failing_tests))
    # all_failing_tests = [
    #     os.path.join(tdir, tfile) for tdir in test_exec_failures.keys() for tfile in test_exec_failures[tdir]
    # ]
    # tkltest_status('Failing tests: {}'.format(all_failing_tests))
    #
    # __print_failure_causes(test_exec_failures)


def __run_test_cases(app_name, monolith_app_path, app_classpath, test_root_dir, test_dirs, gen_junit_report, collect_codecoverage,
    app_packages, partitions_file, target_class_list, reports_dir, offline_inst, env_vars={}, verbose=False, micro=False):
  
    tkltest_status('Compiling and running tests in {}'.format(os.path.abspath(test_root_dir)))

    main_reports_dir = os.path.join(reports_dir, app_name + constants.TKLTEST_MAIN_REPORT_DIR_SUFFIX)

    # generate ant build.xml file
    build_xml_file = build_util.generate_ant_build_xml(
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
        offline_instrumentation=offline_inst,
        micro=micro
    )

    partitions = [os.path.basename(dir) for dir in test_dirs]

    # no env vars indicate monolith application - will merge code coverage reports after running all test partitions

    # current limitation in ant script - if code coverage is requested then junit report is generated as well

    try:
        if collect_codecoverage and not env_vars:
            __run_command("ant -f {} merge-coverage-report".format(build_xml_file), verbose=verbose)
        else:
            task_prefix = 'coverage-reports_' if collect_codecoverage else 'test-reports_' if gen_junit_report else 'execute-tests_'
            for partition in partitions:
                if not env_vars:
                    __run_command("ant -f {} {}{}".format(build_xml_file, task_prefix, partition), 
                        verbose=verbose)
                else:
                    # env_vars = env_vars | os.environ # this syntax is valid in python 3.9+
                    for env_var in os.environ:
                        env_vars[env_var] = os.environ[env_var]
                    __run_command("ant -f {} {}{}".format(build_xml_file, task_prefix, partition),
                        verbose=verbose, env_vars=env_vars)
    except subprocess.CalledProcessError as e:
            tkltest_status('Error executing junit ant: {}'.format(e), error=True)
            sys.exit(1)

    if gen_junit_report:
        tkltest_status("JUnit reports are saved in " +
                       os.path.abspath(main_reports_dir+os.sep+constants.TKL_JUNIT_REPORT_DIR))
    if collect_codecoverage:
        tkltest_status("Jacoco code coverage reports are saved in " +
                       os.path.abspath(main_reports_dir+os.sep+constants.TKL_CODE_COVERAGE_REPORT_DIR))


def __run_partitions(config, partition_container_names):  # pragma: no cover
    tkltest_status('Refactored app partition containers: {}'.format(partition_container_names))

    # run partition containers via docker-compose
    docker_compose_file = config['execute']['micro']['docker_compose_file']
    partition_run_command = 'docker-compose -f {} up --build --detach'.format(docker_compose_file)
    tkltest_status('Running partitions: {}'.format(partition_run_command))
    try:
        subprocess.run(partition_run_command, shell=True, check=True, capture_output=True)
    except subprocess.CalledProcessError as e:
        raise Exception('Error running {}: {}'.format(partition_run_command, str(e)))

    # check status of each container, with bounded retries
    max_retries = config['execute']['micro']['container_status_check_retries']
    sleep_interval = config['execute']['micro']['container_status_check_sleep_interval']
    success_message = config['execute']['micro']['partition_container_success_message']
    curr_attempt = 1
    container_status = {cont : False for cont in partition_container_names}
    while curr_attempt <= max_retries:
        tkltest_status('Waiting before checking partition container status ({}/{}) ... '.format(
            curr_attempt, max_retries
        ))
        __countdown(sleep_interval)
        status_false_containers = [c for c in container_status.keys() if container_status[c] == False]
        tkltest_status('Checking container status: {}'.format(status_false_containers))
        for container_name in status_false_containers:
            container_status[container_name] = __check_container(container_name, success_message)
        # all containers started successfully, break out of retry loop
        if all(list(container_status.values())):
            tkltest_status('All partition containers started successfully: {}'.format(container_status))
            break
        else:
            curr_attempt += 1

    if curr_attempt > max_retries:
        tkltest_status('Status of partition containers: {}'.format(container_status))
        raise Exception('Error running partition containers: container status check timed out '\
            'after {} seconds'.format(max_retries*sleep_interval))


def __stop_partitions(config):  # pragma: no cover
    docker_compose_file = config['execute']['micro']['docker_compose_file']
    partition_run_command = 'docker-compose -f {} down'.format(docker_compose_file)
    tkltest_status('Stopping partitions: {}'.format(partition_run_command))
    try:
        subprocess.run(partition_run_command, shell=True, check=True, capture_output=True)
    except subprocess.CalledProcessError as e:
        tkltest_status('Error running {}: {}'.format(partition_run_command, str(e)), error=True)


def __countdown(interval):  # pragma: no cover
    for remaining in range(interval, 0, -1):
        sys.stdout.write('\r')
        sys.stdout.write('{:2d} seconds remaining'.format(remaining)) 
        sys.stdout.flush()
        time.sleep(1)
    sys.stdout.write('\n')


def __check_container(container_name, success_message):  # pragma: no cover
    # check that container is running
    cmd = 'docker ps --filter \"name={}\"'.format(container_name)
    logging.info('Running cmd: [{}]'.format(cmd))
    try:
        docker_ps_out = subprocess.check_output(
            cmd, shell=True,
            stderr=subprocess.STDOUT,
            universal_newlines=True
        )
        logging.debug('docker_ps_out: [{}]'.format(docker_ps_out))
    except subprocess.CalledProcessError:
        return False

    # check success message in container log
    cmd = 'docker logs {}'.format(container_name)
    logging.info('Running cmd: [{}]'.format(cmd))
    docker_logs_out = subprocess.check_output(
        cmd, shell=True,
        stderr=subprocess.STDOUT,
        universal_newlines=True
    )
    logging.debug('docker_logs_out: [{}]'.format(docker_logs_out))
    if success_message in docker_logs_out:
        return True
    else:
        return False


def __get_partition_env_vars(config, partition):  # pragma: no cover
    # partition_config = docker_compose_info['services'][partition]
    # print('Partition env_vars: {}'.format(partition_config['environment']))
    partition_env_vars = {}
    for env in config['execute']['micro']['partition_env_vars'][partition]:
        var_val = env.split('=')
        partition_env_vars[var_val[0]] = var_val[1]
    return partition_env_vars

# def classify_errors(reports_path, app_packages, test_dir, verbose=False):
#     if not reports_path:
#         return

#     tkltest_status('Classify errors')
#     report_sub_dir = ""
#     for f in os.listdir(reports_path):
#         ff = os.path.join(reports_path, f)
#         if os.path.isdir(ff):
#             report_sub_dir += reports_path
#             report_sub_dir += os.sep
#             report_sub_dir += f
#             report_sub_dir += ','

#     error_patterns_path = os.path.abspath(constants.ERROR_PATTERNS_FILE)
#     classify_command = "java -cp "+os.path.join(constants.TKLTEST_LIB_DIR, tackle-testgen-core.jar")+os.pathsep
#     classify_command += os.path.join(constants.TKLTEST_LIB_DOWNLOAD_DIR, "commons-cli-1.4.jar") + os.pathsep
#     classify_command += os.path.join(constants.TKLTEST_LIB_DOWNLOAD_DIR, "javax.json-1.0.4.jar")
#     classify_command += " org.konveyor.tackle.testgen.core.classify.ClassifyErrors "
#     classify_command += " -pt " + report_sub_dir + " -ep " + error_patterns_path + " -ap " + ",".join(app_packages) + " -td " + test_dir

#     logging.info(classify_command)

#     try:
#         subprocess.run(classify_command, capture_output=not verbose, shell=True, check=True)
#     except subprocess.CalledProcessError as e:
#         tkltest_status('Classifying errors failed: {}'.format(e.stderr), error=True)
#         sys.exit(1)

def __execute_micro(args, config):  # pragma: no cover

    # compute partitions to run tests on
    partitions = None
    if config['execute']['micro']['partitions']:
        partitions = config['execute']['micro']['partitions']
    else:
        partitions = list(config['execute']['micro']['partition_paths'].keys())
    tkltest_status('Running tests for partitions: {}'.format(partitions))

    # perform error checking on specified test directory/file
    test_files = {}
    if 'test_class' in config['execute'].keys() and config['execute']['test_class']:
        test_dir, test_file = os.path.split(config['execute']['test_class'])
        if not test_file.endswith('.java'):
            tkltest_status('Specified test class must be a ".java" file: {}'.format(test_file), error=True)
            return
        for partition in partitions:
            if partition in test_dir:
                partitions = [partition]
                break
        if len(partitions) != 1:
            tkltest_status('Specified test class "{}" does not correspond to any partition: {}'.format(
                config['execute']['test_class'], partitions), error=True)
            return
        test_files = {test_dir: [test_file]}

    # remove test classes from previous runs
    test_directory = config['general']['test_directory']
    if test_directory == '':
        test_directory = config['general']['app_name'] + constants.TKLTEST_DEFAULT_CTDAMPLIFIED_TEST_DIR_SUFFIX
    #__remove_test_classes(test_directory)

    # read generate config from test directory
    gen_config = __get_generate_config(test_directory)

    # read docker compose information
    with open(config['execute']['micro']['docker_compose_file']) as dcf:
        docker_compose_info = yaml.full_load(dcf)

    try:
        # run all app partitions if option specified
        if config['execute']['micro']['run_partition_containers']:
            __run_partitions(config, list(docker_compose_info['services'].keys()))

        # iterate over each partition and run tests
        # total_passing_tests = 0
        # total_failing_tests = 0
        # all_test_exec_failures = {}
        for partition in partitions:
            # compute classpath for compiling and running test classes
            classpath = build_util.get_build_classpath(config, partition)
            tkltest_status('')
            tkltest_status('>>>>> Running tests for partition {} <<<<<'.format(partition))

            # get env vars to be set for partition
            part_env_vars = __get_partition_env_vars(config, partition)
            logging.info('partition env vars: {}'.format(part_env_vars))

            # get list of test classes for partition
            part_test_dir = test_directory + os.sep + partition
            if not os.path.isdir(part_test_dir):
                logging.info('Test directory not found for partition {}: {}'.format(partition, part_test_dir))
                tkltest_status("No test directory found ... skipping")
                continue
            logging.info('partition test root dir: {}'.format(part_test_dir))

            if not test_files:
                # get all test files for partition
                test_files = __get_test_classes(part_test_dir)

            logging.info('test files: {}'.format(test_files))

            # run test classes
            __run_test_cases(
                app_name=config['general']['app_name'],
                monolith_app_path=config['execute']['micro']['partition_paths'][partition],
                app_classpath=classpath,
                test_root_dir=part_test_dir,
                test_dirs=[part_test_dir],
                gen_junit_report=config['execute']['junit_report'],
                collect_codecoverage=config['execute']['code_coverage'],
                app_packages=config['execute']['app_packages'],
                partitions_file=gen_config['generate']['partitions_file'],
                target_class_list=gen_config['generate']['target_class_list'],
                reports_dir=config['execute']['reports_path'],
                offline_inst=config['execute']['offline_instrumentation'],
                env_vars=part_env_vars, verbose=config['general']['verbose'],
                micro=True
            )

            # # update test execution info
            # total_passing_tests += passing_tests
            # total_failing_tests += failing_tests
            # all_test_exec_failures[partition] = test_exec_failures

            # # print summary stats
            # tkltest_status('Partition passing tests: {}/{}'.format(
            #     passing_tests, passing_tests+failing_tests))
            # failing_test_classes = [
            #     os.path.join(part_test_dir, tfile) for part_test_dir in test_exec_failures.keys()
            #     for tfile in test_exec_failures[part_test_dir]
            # ]
            # tkltest_status('Partition failing tests: {}'.format(failing_test_classes))
            # __print_failure_causes(test_exec_failures)

            # reset test files
            test_files = {}

        # Classify errors
        # classify_errors(args.reports_path, config['execute']['app_packages'], config['general']['test_directory'])

    except Exception as e:
        tkltest_status(str(e), error=True)

    finally:
        # stop partitions if option specified
        if config['execute']['micro']['stop_partition_containers']:
            __stop_partitions(config)


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


def __run_command(command, verbose, env_vars=None):
    """Runs a command using subprocess.
    
    Runs the given command using subprocess.run. If verbose is false, stdout and stderr are 
    discarded; otherwise only stderr is discarded.
    """
    if verbose:
        if env_vars:
            subprocess.run(command, shell=True, check=True, stderr=subprocess.DEVNULL, env=env_vars)
        else:
            subprocess.run(command, shell=True, check=True, stderr=subprocess.DEVNULL)
    else:
        if env_vars:
            subprocess.run(command, shell=True, check=True, stdout=subprocess.DEVNULL,
                stderr=subprocess.STDOUT, env=env_vars)
        else:
            subprocess.run(command, shell=True, check=True, stdout=subprocess.DEVNULL,
                stderr=subprocess.STDOUT)    
