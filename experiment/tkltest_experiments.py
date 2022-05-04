import glob
import os
import re
import shutil
import sys
import toml
from argparse import Namespace
from collections import Counter
from pathlib import Path

import distutils.dir_util

# from tkltest.util import constants
# constants.TKLTEST_LIB_DIR = os.path.abspath('../lib')
# constants.TKLTEST_LIB_DOWNLOAD_DIR = os.path.join(constants.TKLTEST_LIB_DIR, 'download')

# Requirement for running this script: tackle-test-generator-cli code must be available at the same
# directory level as this experiments project
cli_dir = ".."  # + os.sep + "tackle-test-generator-cli"
abs_cli_dir = os.path.abspath(cli_dir)
sys.path.insert(0, os.path.abspath(os.path.dirname(__file__)) + os.sep + cli_dir)
from tkltest.generate import generate
from tkltest.execute import execute
from tkltest.util import build_util, config_util, constants

# set jdk_home from env var JAVA_HOME; print error and exit if unset
jdk_home = os.getenv("JAVA_HOME", None)
if jdk_home is None:
    print('Please set JAVA_HOME to the JDK installation')
    sys.exit(0)

# create symbolic link to CLI lib directory; this is needed since the test generation and execution are run via
# python function calls instead of subprocess call to cli command
if not os.path.exists('lib'):
    os.symlink(cli_dir + os.sep + 'lib', 'lib')

# args used in the call to methods in tkltest.generate and tkltest.execute modules
args = Namespace()


def experiments_separate(app, config):
    # path to the root directory containing benchmark Java apps
    apps_path = os.path.abspath(os.path.join(os.getcwd(), config['general']['apps_dir']))

    time_limits, levels, trials, output_dir = config['general']['time_limits'], config['general']['levels'], \
                                              config['general']['trials'], config['general']['output_dir']
    verbose = config['verbosity']['verbose_execute']
    rbt, generate_standalone, force = config['generate']['reuse_base_tests'], \
                                      config['generate']['generate_standalone'], config['generate']['force']
    skip_exec = not config['sections']['execute']

    monolith_app_path = os.path.abspath(apps_path + os.sep + app + os.sep + "classes")

    # initialize config option
    tkltest_config = config_util.load_config()

    # set general options
    tkltest_config['general']['monolith_app_path'] = [monolith_app_path]
    tkltest_config['general']['app_classpath_file'] = os.path.abspath(apps_path + os.sep + app + os.sep + "classpath.txt")
    tkltest_config['general']['java_jdk_home'] = jdk_home

    # set generate options
    tkltest_config['generate']['add_assertions'] = True
    tkltest_config['generate']['jee_support'] = False
    tkltest_config['generate']['ctd_amplified']['base_test_generator'] = constants.BASE_TEST_GENERATORS['combined']
    tkltest_config['generate']['ctd_amplified']['ctd_coverage'] = True

    # set execute options
    tkltest_config['execute']['code_coverage'] = True
    tkltest_config['execute']['app_packages'] = get_app_packages(tkltest_config['general']['monolith_app_path'])
    print('\n*** set app packages to {} ***'.format(*tkltest_config['execute']['app_packages'], sep=', '))
    tkltest_config['execute']['offline_instrumentation'] = True

    old_cwd = os.getcwd()
    # if not os.path.exists(os.path.join(output_dir, app, os.path.basename(old_cwd))):
    #     os.makedirs(os.path.join(output_dir, app), exist_ok=True)
    #     os.symlink(old_cwd, os.path.join(output_dir, app, os.path.basename(old_cwd)))  # classpath reasons
    for time_limit in time_limits:
        for level in levels:
            for trial in trials:
                short_app_name = '{}_{}s_{}l_{}'.format(app, time_limit, level, trial)
                outdir = os.path.abspath(os.path.join(output_dir, app, short_app_name))
                # app_name = outdir + os.sep + short_app_name

                print('\n===== Starting trial {} ====='.format(short_app_name))
                # os.makedirs(outdir, exist_ok=True)
                # os.chdir(outdir)
                # print(f'cwd: {os.getcwd()}')
                # if not os.path.exists('lib'):
                #     os.symlink(abs_cli_dir + os.sep + 'lib', 'lib')
                tkltest_config['general']['app_name'] = short_app_name
                tkltest_config['general']['time_limit'] = time_limit
                tkltest_config['generate']['ctd_amplified']['interaction_level'] = level
                tkltest_config['generate']['ctd_amplified']['reuse_base_tests'] = rbt

                try:
                    __run_trial(short_app_name=short_app_name, app=app,
                                tkltest_config=tkltest_config, outdir=outdir, force=force, skip_exec=skip_exec,
                                generate_standalone=generate_standalone, verbose=verbose)
                except SystemExit as se:
                    print('ERROR_TRIAL: {} failed with error {}'.format(short_app_name, se))

                os.chdir(old_cwd)


def __run_trial(short_app_name, app, tkltest_config, outdir, force, skip_exec, generate_standalone, verbose=True):
    """Runs one trial of test generation and execution for the given app name and configuration.

    A trial consists of generating ctd-amplified tests with combined building-block test sequences and
    a given time budget (used for generating building-block test sequences) and performing multiple
    test executions: (1) ctd_amplified tests, (2) evosuite tests (generated for creating bb sequences), and
    (3) randoop tests (generated for creating bb sequences), (4) evosuite+randoop combined test suite,
    (5) ctd_amplified+evosuite combined test suite, (6) ctd_amplified+randoop combined test suite, and
    (7) ctd_amplified+evosuite+randoop combined test suite.

    After test generation and executions are done, all created artifacts (test cases, reports, summary files,
    and other intermediate artifacts) are moved to directory output/<app_name>. The config used for the run
    is also written to the output directory.

    Args:
        short_app_name: Name of app under test in the format <original_app_name>_<time_limit>s_<trial_num>
        app: Original name of app under test
        tkltest_config: config information for test generation/execution
    """

    # create output directory for trial
    # outdir = os.path.join(OUTPUT_DIR, original_app_name, app_name)
    # short_app_name = outdir + os.sep + short_app_name
    # shutil.rmtree(outdir, ignore_errors=True)

    first_time = not os.path.exists(outdir)
    tkltest_outdir = constants.TKLTEST_UNIT_OUTPUT_DIR_PREFIX + short_app_name if first_time else outdir
    os.makedirs(tkltest_outdir, exist_ok=True)
    os.makedirs(outdir, exist_ok=True)

    # write config file to output directory
    with open(os.path.join(tkltest_outdir, 'tkltest_config.toml'), 'w') as f:
        toml.dump(tkltest_config, f)

    # run ctd-guided test generation
    print('\n*** ctd_amplified combined generate --- {} ***'.format(short_app_name))
    os.makedirs(constants.TKLTEST_UNIT_OUTPUT_DIR_PREFIX + short_app_name, exist_ok=True)
    ctd_amplified_testdir = f'{tkltest_outdir}/{short_app_name}-ctd-amplified-tests'
    tkltest_config['general']['test_directory'] = ctd_amplified_testdir
    tkltest_config['general']['reports_path'] = os.path.join(tkltest_outdir, short_app_name + constants.TKLTEST_MAIN_REPORT_DIR_SUFFIX)
    # gen_config_file = os.path.join(outdir, f'{short_app_name}-ctd-amplified-tests', '.tkltest_generate.toml')
    gen_config_file = os.path.join(tkltest_outdir, f'{short_app_name}-ctd-amplified-tests', '.tkltest_generate.toml')
    if force or not os.path.exists(gen_config_file):
        print('[!!] Generating...')
        __run_generate(subcommand='ctd-amplified', config=tkltest_config, verbose=verbose)

    # execute ctd-amplified tests
    print('\n*** ctd-amplified combined execute --- {} ***'.format(short_app_name))
    tkltest_config['execute']['no_create_build_file'] = False
    if not skip_exec:
        __run_execute(config=tkltest_config, verbose=verbose)

    # execute evosuite tests for coverage measurement
    print('\n*** evosuite execute --- {} ***'.format(short_app_name))
    evosuite_testdir = f'{tkltest_outdir}/{short_app_name}-evosuite-tests'
    if not os.path.exists(evosuite_testdir):
        print('[!!] WARNING: Evosuite testdir does not exist...', file=sys.stderr)
    else:
        shutil.copy(gen_config_file, evosuite_testdir)
        tkltest_config['general']['test_directory'] = os.path.basename(evosuite_testdir) if first_time else os.path.abspath(evosuite_testdir)
        tkltest_config['execute']['no_create_build_file'] = True
        build_util.generate_build_xml(short_app_name, tkltest_config['general']['monolith_app_path'], build_util.get_build_classpath(tkltest_config),
                                      evosuite_testdir, [evosuite_testdir], None, [],
                                      os.path.join(tkltest_outdir, short_app_name + constants.TKLTEST_MAIN_REPORT_DIR_SUFFIX),
                                      tkltest_config['execute']['app_packages'],
                                      True, True)
        if not skip_exec:
            __run_execute(config=tkltest_config, verbose=verbose)

    # execute randoop tests for coverage measurement
    print('\n*** randoop execute --- {} ***'.format(short_app_name))
    randoop_testdir = f'{tkltest_outdir}/{short_app_name}-randoop-tests'
    if not os.path.exists(randoop_testdir):
        print('[!!] WARNING: Randoop testdir does not exist...', file=sys.stderr)
    else:
        shutil.copy(gen_config_file, randoop_testdir)
        tkltest_config['general']['test_directory'] = os.path.basename(randoop_testdir) if first_time else os.path.abspath(randoop_testdir)
        tkltest_config['execute']['no_create_build_file'] = True
        build_util.generate_build_xml(short_app_name, tkltest_config['general']['monolith_app_path'], build_util.get_build_classpath(tkltest_config),
                                      randoop_testdir, [randoop_testdir], None, [],
                                      os.path.join(tkltest_outdir, short_app_name + constants.TKLTEST_MAIN_REPORT_DIR_SUFFIX),
                                      tkltest_config['execute']['app_packages'],
                                      True, True)

        # print('Running randoop empty test class bug fix (at tkltest_experiments)')
        # moved_count = 0
        # suffix_num = re.compile(r'[0-9]+\.java$')
        # testdir_glob = list(Path(randoop_testdir).rglob('*.java'))
        # testdir_glob = [file for file in testdir_glob if not re.search(r'_(ES)?Test(_scaffolding)?.java$', str(file))]
        # counter = dict(Counter([suffix_num.sub('.java', str(jfile)) for jfile in testdir_glob]))
        # to_delete = [k for k in counter if counter[k] == 1]
        # for jfile in to_delete:
        #     jfile_new = re.sub('output', 'output_bak', jfile)
        #     if verbose:
        #         print(f'Moving {os.path.basename(jfile)} to backup dir {os.path.dirname(jfile_new)}')
        #     os.makedirs(os.path.dirname(jfile_new), exist_ok=True)
        #     os.rename(jfile, jfile_new)
        #     moved_count += 1
        # if verbose:
        #     print(f'Moved {moved_count} java files')

        if not skip_exec:
            __run_execute(config=tkltest_config, verbose=verbose)

    if generate_standalone:
        # generate and execute standalone evosuite tests
        evosuite_standalone_testdir = f'{tkltest_outdir}/{short_app_name}-evosuite-standalone-tests'
        tkltest_config['general']['test_directory'] = os.path.basename(evosuite_standalone_testdir) if first_time else os.path.abspath(evosuite_standalone_testdir)
        tkltest_config['execute']['no_create_build_file'] = True
        __run_generate(config=tkltest_config, subcommand='evosuite', verbose=verbose)
        shutil.copy(gen_config_file, evosuite_standalone_testdir)
        build_util.generate_build_xml(short_app_name, tkltest_config['general']['monolith_app_path'],
                                      build_util.get_build_classpath(tkltest_config),
                                      evosuite_standalone_testdir, [evosuite_standalone_testdir], None, [],
                                      os.path.join(tkltest_outdir, short_app_name + constants.TKLTEST_MAIN_REPORT_DIR_SUFFIX),
                                      tkltest_config['execute']['app_packages'],
                                      True, True)
        if not skip_exec:
            __run_execute(config=tkltest_config, subcommand='evosuite', verbose=verbose)

        # generate and execute standalone randoop tests
        randoop_standalone_testdir = f'{tkltest_outdir}/{short_app_name}-randoop-standalone-tests'
        tkltest_config['general']['test_directory'] = os.path.basename(randoop_standalone_testdir) if first_time else os.path.abspath(randoop_standalone_testdir)
        tkltest_config['execute']['no_create_build_file'] = True
        tkltest_config['generate']['randoop']['no_error_revealing_tests'] = True
        __run_generate(config=tkltest_config, subcommand='randoop', verbose=verbose)
        shutil.copy(gen_config_file, randoop_standalone_testdir)
        build_util.generate_build_xml(short_app_name, tkltest_config['general']['monolith_app_path'],
                                      build_util.get_build_classpath(tkltest_config),
                                      randoop_standalone_testdir, [randoop_standalone_testdir], None, [],
                                      os.path.join(tkltest_outdir, short_app_name + constants.TKLTEST_MAIN_REPORT_DIR_SUFFIX),
                                      tkltest_config['execute']['app_packages'],
                                      True, True)
        if not skip_exec:
            __run_execute(config=tkltest_config, subcommand='randoop', verbose=verbose)

    ###
    # execute combined test suites for coverage measurement:
    #   1. evosuite + randoop combined test suite
    #   2. ctd_amplified + evosuite combined test suite
    #   3. ctd_amplified + randoop combined test suite
    #   4. ctd_amplified + evosuite + randoop combined test suite
    # coverage measurement over these runs will provide data for RQ4
    ###

    # 1. execute evosuite + randoop combined test suite
    print('\n*** evosuite + randoop execute --- {} ***'.format(short_app_name))
    combined_testdir = f'{tkltest_outdir}/{short_app_name}-evosuite-randoop-tests'
    __run_combined_test_suite(short_app_name, combined_testdir=combined_testdir, gen_config_file=gen_config_file, config=tkltest_config,
                              evosuite_testdir=evosuite_testdir, randoop_testdir=randoop_testdir, verbose=verbose,
                              skip_execute=skip_exec, tkltest_outdir=tkltest_outdir, first_time=first_time)

    # 2. execute ctd_amplified + evosuite combined test suite
    print('\n*** ctd_amplified + evosuite execute --- {} ***'.format(short_app_name))
    combined_testdir = f'{tkltest_outdir}/{short_app_name}-ctdamplified-evosuite-tests'
    __run_combined_test_suite(short_app_name, combined_testdir=combined_testdir, gen_config_file=gen_config_file, config=tkltest_config,
                              ctd_amplified_testdir=ctd_amplified_testdir, evosuite_testdir=evosuite_testdir, verbose=verbose,
                              skip_execute=True, tkltest_outdir=tkltest_outdir, first_time=first_time)

    # 3. execute ctd_amplified + randoop combined test suite
    print('\n*** ctd_amplified + randoop execute --- {} ***'.format(short_app_name))
    combined_testdir = f'{tkltest_outdir}/{short_app_name}-ctdamplified-randoop-tests'
    __run_combined_test_suite(short_app_name, combined_testdir=combined_testdir, gen_config_file=gen_config_file, config=tkltest_config,
                              ctd_amplified_testdir=ctd_amplified_testdir, randoop_testdir=randoop_testdir, verbose=verbose,
                              skip_execute=True, tkltest_outdir=tkltest_outdir, first_time=first_time)

    # 4. execute ctd_amplified + evosuite + randoop combined test suite
    print('\n*** ctd_amplified + evosuite + randoop execute --- {} ***'.format(short_app_name))
    combined_testdir = f'{tkltest_outdir}/{short_app_name}-ctdamplified-evosuite-randoop-tests'
    __run_combined_test_suite(short_app_name, combined_testdir=combined_testdir, gen_config_file=gen_config_file, config=tkltest_config,
                              ctd_amplified_testdir=ctd_amplified_testdir, evosuite_testdir=evosuite_testdir,
                              randoop_testdir=randoop_testdir, verbose=verbose,
                              skip_execute=skip_exec, tkltest_outdir=tkltest_outdir, first_time=first_time)

    # move generated artifacts to output directory
    if first_time:
        for file in glob.glob(f'{app}_*'):
            shutil.move(os.path.join('.', file), os.path.join(tkltest_outdir, file))
            if verbose:
                print(f'Moved {file} to {tkltest_outdir}')
        for file in glob.glob(f'{tkltest_outdir}/*'):
            shutil.move(os.path.join('.', file), os.path.join(outdir, os.path.basename(file)))
            if verbose:
                print(f'Moved {os.path.basename(file)} to {outdir}')
        if not os.listdir(tkltest_outdir):
            os.rmdir(tkltest_outdir)
            if verbose:
                print(f'Deleted empty directory {tkltest_outdir}')


def __run_combined_test_suite(app_name, combined_testdir, gen_config_file, config, ctd_amplified_testdir=None,
                              evosuite_testdir=None, randoop_testdir=None, verbose=True, skip_execute=False, tkltest_outdir='.', first_time=True):
    shutil.rmtree(combined_testdir, ignore_errors=True)
    for testdir in [ctd_amplified_testdir, evosuite_testdir, randoop_testdir]:
        if testdir is not None and not os.path.exists(testdir):
            print('[!!] WARNING: Path does not exist (skipping combined exec): {}'.format(testdir), file=sys.stderr)
            return
        if testdir is not None:
            distutils.dir_util.copy_tree(testdir, combined_testdir)
    test_dirs = [combined_testdir]
    if ctd_amplified_testdir:
        # need to copy ctd-guided inner folders based on the ctd_amplified_testdir structure, because combined_testdir
        # may contain also evosuite inner package folders and we don't want to add those to the test_dirs list
        test_dirs += [
        os.path.join(combined_testdir, dir) for dir in os.listdir(ctd_amplified_testdir)
        if os.path.isdir(os.path.join(combined_testdir, dir)) and not dir.startswith('.')
    ]
    shutil.copy(gen_config_file, combined_testdir)
    config['general']['test_directory'] = os.path.basename(combined_testdir) if first_time else os.path.abspath(combined_testdir)
    config['execute']['no_create_build_file'] = True
    build_util.generate_build_xml(app_name, config['general']['monolith_app_path'], build_util.get_build_classpath(config), combined_testdir,
                                  test_dirs, None, [], os.path.join(tkltest_outdir, app_name + constants.TKLTEST_MAIN_REPORT_DIR_SUFFIX),
                                  config['execute']['app_packages'], True, True)
    if not skip_execute:
        __run_execute(config=config, verbose=verbose)


def __run_generate(subcommand, config, verbose=True):
    args.command = 'generate'
    args.sub_command = subcommand
    config['general']['verbose'] = verbose
    old_classpath = config['general']['app_classpath_file']
    generate.process_generate_command(args=args, config=config)
    config['general']['app_classpath_file'] = old_classpath


def __run_execute(config, subcommand=None, verbose=True):
    args.command = 'execute'
    if subcommand:
        args.sub_command = subcommand
    config['general']['verbose'] = verbose
    old_classpath = config['general']['app_classpath_file']
    execute.process_execute_command(args=args, config=config)
    config['general']['app_classpath_file'] = old_classpath


def get_app_packages(app_paths):
    """Computes all possible app package prefixes from the paths of the application classes

    """
    dirs = []
    for path in app_paths:
        dirs += [os.path.join(path, dir) for dir in os.listdir(path)]
    packages_list = [os.path.basename(dir)+".*" for dir in dirs if os.path.isdir(dir) and not os.path.basename(dir).startswith('.') and not
                     os.path.basename(dir) == "META-INF"]

    if not packages_list:
        # no packages exist, return a flat list of all classes (without the .class suffix)
        return [os.path.basename(filename)[:-len(".class")] for filename in dirs if os.path.isfile(filename)
                and filename.endswith(".class")]

    return packages_list


def main(config):
    """The main entry point for running experiments.

    This is the main entry point for an experiment run. It builds the set of apps to be used in the experiment
    and runs test generation/execution in a 3-level nested loop that iterates over apps, trials, and time limits.
    """

    # iterate over each app, and run test generation and execution
    for app in config['general']['apps']:
        experiments_separate(app, config)


if __name__ == '__main__':
    with open('parsed_config.toml') as f:
        config = toml.load(f)
    main(config)
