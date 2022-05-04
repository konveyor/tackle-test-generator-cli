import fileinput
import os
import random
import re
import shutil
import sys
import toml
from collections import Counter
from datetime import datetime
from pathlib import Path

import tkltest_experiments
from tkltest.util import constants


def __extract_methods(f_in_name, f_out_name, accumulated_test_lines, verbose=False):
    f_in = open(f_in_name, 'r')
    lines = f_in.readlines()
    test_lines = []
    r = re.compile(r'[\t ]*@Test(?:\(timeout ?= ?[0-9]+\))?[\t ]*')
    for i, line in enumerate(lines):
        if r.match(line):
            test_lines.append(i)
    evosuite = str(f_in_name).endswith('_ESTest.java')
    if len(test_lines) == 1 and evosuite:
        if re.search(r'EvoSuite did not generate any tests', lines[test_lines[0] + 2]):
            # if verbose:
            #     print('EvoSuite did not generate any tests')
            f_in.close()
            return

    line_count = len(test_lines)
    accumulated_test_lines.extend(zip([f_in_name] * line_count, [f_out_name] * line_count, test_lines))
    if verbose:
        print('Accumulated with {} more lines'.format(line_count))
    f_in.close()


def __extract_methods_test_suite(test_suite, full_path, sampled_path, verbose=False):
    accumulated_test_lines = []
    for i, jfile in enumerate(test_suite):
        f_in = os.path.join(full_path, jfile)
        f_out = os.path.join(sampled_path, jfile) if sampled_path else None
        __extract_methods(f_in, f_out, accumulated_test_lines, verbose)
    if verbose:
        print(f'Finished extracting from {test_suite}, got {len(accumulated_test_lines)}')
    return accumulated_test_lines


def __sample_extracted_methods(extracted_methods, sample_size, verbose=False):
    if sample_size == 0:
        return False
    non_sampled_lines = random.sample(extracted_methods, len(extracted_methods) - sample_size)
    non_sampled_lines.sort(reverse=True)  # so that deleting a line would not affect the other line numbers
    non_sampled_lines.append((None, None, None))  # tail

    f_out, lines = None, None
    for line in non_sampled_lines:
        in_name, out_name, test_line = line

        # if seen new file
        if not f_out or out_name != f_out.name:
            # handle previous file
            if lines:
                for line_out in lines:
                    f_out.write(line_out)
                if verbose:
                    print('Written to file {}'.format(f_out.name))
                f_out.close()

            # open next file (f_out is kept open, whereas f_in content is stored and f_in destroyed)
            if not out_name:  # tail detected, we are done
                break
            os.makedirs(os.path.dirname(out_name), exist_ok=True)
            f_out = open(out_name, 'w+')
            f_in = open(in_name, 'r')
            f_in.seek(0)
            lines = f_in.readlines()
            f_in.close()

        # otherwise just remove test annotation
        del lines[test_line]
    return True


def __copy_missing_files(test_suite, full_path, sampled_path, verbose=False):
    for jfile in test_suite:
        f_in = os.path.join(full_path, jfile)
        f_out = os.path.join(sampled_path, jfile) if sampled_path else None
        if not os.path.exists(f_out):
            os.makedirs(os.path.dirname(f_out), exist_ok=True)
            shutil.copy(f_in, f_out)
            if verbose:
                print('Copied file {}'.format(jfile))


def __randoop_empty_test_fix(sampled_testdir, verbose=False):
    print('Running randoop empty test class bug fix')
    moved_count = 0
    suffix_num = re.compile(r'[0-9]+\.java$')
    testdir_glob = list(Path(sampled_testdir).rglob('*.java'))
    testdir_glob = [file for file in testdir_glob if
                    '_ESTest.java' not in str(file) and
                    '_ESTest_scaffolding.java' not in str(file) and
                    '_Test.java' not in str(file)]
    counter = dict(Counter([suffix_num.sub('.java', str(jfile)) for jfile in testdir_glob]))
    to_delete = [k for k in counter if counter[k] == 1]
    for jfile in to_delete:
        jfile_new = re.sub('output', 'output_bak', jfile)
        if verbose:
            print(f'Moving {os.path.basename(jfile)} to backup dir {os.path.dirname(jfile_new)}')
        os.makedirs(os.path.dirname(jfile_new), exist_ok=True)
        os.rename(jfile, jfile_new)
        moved_count += 1
    if verbose:
        print(f'Moved {moved_count} java files')


def __execute_samples(config, build_xml_from, sampled_testdir, app_conf, conf_dir,
                      verbose=False, verbose_execute=False):
    if not os.path.exists(sampled_testdir):
        if verbose:
            print(f'Safe-skipping execution for {sampled_testdir} (does not exist)')
        return
    # __randoop_empty_test_fix(sampled_testdir, verbose)
    print('\n*** {} execute ***'.format(sampled_testdir))
    config['general']['test_directory'] = os.path.abspath(sampled_testdir)
    config['general']['reports_path'] = os.path.join(conf_dir, app_conf + constants.TKLTEST_MAIN_REPORT_DIR_SUFFIX)
    config['execute']['no_create_build_file'] = True
    build_xml = os.path.join(sampled_testdir, 'build.xml')
    if build_xml_from:
        build_xml_orig = os.path.join(build_xml_from, 'build.xml')
        gen_config_file = os.path.join(build_xml_from, '.tkltest_generate.toml')
        if not os.path.exists(build_xml_orig):
            print(f'[!!!] WARNING: No build.xml file was found at {build_xml_from}', file=sys.stderr)
            return
        if not os.path.exists(gen_config_file):
            print(f'[!!!] WARNING: No .tkltest_generate.toml file was found at {build_xml_from}', file=sys.stderr)
            return
        shutil.copy(build_xml_orig, sampled_testdir)
        shutil.copy(gen_config_file, sampled_testdir)
        if verbose:
            print(f'Copied build file from {os.path.join(build_xml_from, "build.xml")} to {build_xml}')
    test_conf = os.path.basename(sampled_testdir)

    shutil.rmtree(os.path.join(config['general']['reports_path'], constants.TKL_CODE_COVERAGE_REPORT_DIR, test_conf),
                  ignore_errors=True)
    if verbose:
        print(f'Deleted reports dir '
              f'{os.path.join(config["general"]["reports_path"], constants.TKL_CODE_COVERAGE_REPORT_DIR, test_conf)}')

    changed = False
    if not os.path.exists(build_xml_old := os.path.join(sampled_testdir, 'build.xml.old')):
        shutil.copy(build_xml, build_xml_old)
    for line in fileinput.input(build_xml, inplace=True):
        line_changed = re.sub(rf'(?<=experiment/){constants.TKLTEST_UNIT_OUTPUT_DIR_PREFIX}{app_conf}{os.sep}{app_conf}', conf_dir + os.sep + app_conf, line)
        line_changed = re.sub(rf'{app_conf}[a-z-]+-tests', test_conf, line_changed)
        if line_changed != line:
            changed = True
        print(line_changed, end="")

    if verbose and not changed:
        print(f'Build file was left unchanged ({sampled_testdir})')

    tkltest_experiments.__run_execute(config=config, verbose=verbose_execute)


def __run_standalone(app_conf=None, conf_dir=None, testdir=None, app=None, time=None, build_xml_from=None,
                     level=None, trial=None, comb=None, output_dir=None, verbose=False, verbose_execute=False):
    if not app_conf:
        app_conf = f'{app}_{time}s_{level}l_{trial}'
    if not conf_dir:
        conf_dir = os.path.join(output_dir, app, app_conf)
    if not testdir:
        testdir = os.path.join(conf_dir, f'{app_conf}-{comb}-tests')
    config_file = os.path.join(conf_dir, 'tkltest_config.toml')
    if not os.path.exists(config_file):
        print('[!!!] WARNING: No config file was found for {}'.format(app_conf), file=sys.stderr)
        return None
    config = toml.load(config_file)
    # gen_config_file = os.path.join(testdir, '.tkltest_generate.toml')
    return __execute_samples(config, build_xml_from, testdir, app_conf, conf_dir, verbose, verbose_execute)


def sample(test_suites, conf_dir, app_conf, config):
    sampling_trials = config['general']['sampling_trials']
    clean_sampling,  force = config['sample']['clean_sampling'], config['sample']['force']
    do_sample, do_execute = config['sections']['sample'], config['sections']['execute-samples']
    verbose, verbose_execute = config['verbosity']['verbose'], config['verbosity']['verbose_execute']

    if len(test_suites) == 0:
        print('ERROR: No test suites were supplied for sampling', file=sys.stderr)
        return None

    def get_java_files(testdir):
        return [os.path.relpath(jfile, testdir) for jfile in Path(testdir).rglob('*.java')]

    full_dir_dict = {test_suite: os.path.join(conf_dir, f'{app_conf}-{test_suite}-tests') for test_suite in test_suites}
    sampled_dir_dicts = {trial: {test_suite: os.path.join(conf_dir, f'{app_conf}-{test_suite}-sampled-{trial}-tests')
                                 for test_suite in test_suites} for trial in range(sampling_trials)}
    java_files_dict = {test_suite: get_java_files(full_dir_dict[test_suite]) for test_suite in test_suites}
    if verbose:
        print('Number of java files in test suites:', {k: len(v) for (k, v) in java_files_dict.items()})

    targets = []
    for sampling_trial in range(sampling_trials):
        sampled_dir_dict = sampled_dir_dicts[sampling_trial]

        if do_sample and clean_sampling:
            for test_suite in test_suites:
                shutil.rmtree(sampled_dir_dict[test_suite], ignore_errors=True)
            if verbose:
                print(f'Clean sampling: Deleted dirs '
                      f'{[os.path.basename(sampled_dir) for sampled_dir in sampled_dir_dict.values()]}')
            samples_exist = False
        else:
            samples_exist = all([os.path.exists(sampled_dir) for sampled_dir in sampled_dir_dict.values()])

        if do_sample and (not samples_exist or force):
            # extract all test methods into one cumulative list
            begin_time = datetime.now()
            extracted_methods = {test_suite: __extract_methods_test_suite(java_files_dict[test_suite],
                                                                          full_dir_dict[test_suite],
                                                                          sampled_dir_dict[test_suite], verbose=verbose)
                                 for test_suite in test_suites}
            print('Extraction took {}'.format(datetime.now() - begin_time))

            # do the sampling
            non_sampled = min(test_suites, key=lambda test_suite: len(extracted_methods[test_suite]))
            sample_size = len(extracted_methods[non_sampled])
            if verbose:
                print(f'Testsuite {non_sampled} is not sampled (sample size: {sample_size})')

            sampled_test_suites = [test_suite for test_suite in test_suites 
                                   if len(extracted_methods[test_suite]) > sample_size]
            begin_time = datetime.now()
            for test_suite in sampled_test_suites:
                success = __sample_extracted_methods(extracted_methods[test_suite], sample_size, verbose=verbose)
                if not success and verbose:
                    print(f'No tests were excluded for {test_suite}')
                __copy_missing_files(java_files_dict[test_suite], full_dir_dict[test_suite],
                                     sampled_dir_dict[test_suite], verbose)
            if verbose:
                print(f'Finished sampling and copying files for conf {app_conf}')
            print('Sampling took {}'.format(datetime.now() - begin_time))

        targets.extend([(full_dir_dict[test_suite], sampled_dir_dict[test_suite]) for test_suite in test_suites])

    if do_execute and len(targets):
        begin_time = datetime.now()
        for build_xml_from, testdir in targets:
            print(f'Executing target {testdir} (build file from {build_xml_from})')
            __run_standalone(app_conf=app_conf, conf_dir=conf_dir, build_xml_from=build_xml_from, testdir=testdir,
                             verbose=verbose, verbose_execute=verbose_execute)
        print('Execution took {}'.format(datetime.now() - begin_time))
        # remove the redundant directory containing only the two jars randoop-all and replace-call
        shutil.rmtree(constants.TKLTEST_UNIT_OUTPUT_DIR_PREFIX + app_conf)
        print(f'Removed the redundant directory created after each execution: {constants.TKLTEST_UNIT_OUTPUT_DIR_PREFIX + app_conf}')
