import errno
import json
import numpy as np
import os
import pandas as pd
import plotly.express as px
import re
import sys
import toml
from pandas.core.dtypes.common import is_numeric_dtype
from pathlib import Path

import apps_list

pd.options.mode.chained_assignment = None  # default='warn'

COLUMNS = ['App', 'Time limit', 'Interaction Level', 'Combination', 'Trial', 'Averaging criterion',
           'Instruction coverage', 'Branch coverage', 'Method coverage', 'Test methods', 'CTD coverage']


def calc_coverage(coverage_file, precision):
    all_data = pd.read_csv(coverage_file)
    cov_sums = all_data.sum(axis=0)
    inst_cov = round(cov_sums['INSTRUCTION_COVERED'] / (cov_sums['INSTRUCTION_COVERED'] + cov_sums['INSTRUCTION_MISSED']), precision)
    branch_cov = round(cov_sums['BRANCH_COVERED'] / (cov_sums['BRANCH_COVERED'] + cov_sums['BRANCH_MISSED']), precision)
    method_cov = round(cov_sums['METHOD_COVERED'] / (cov_sums['METHOD_COVERED'] + cov_sums['METHOD_MISSED']), precision)
    return inst_cov, branch_cov, method_cov


def count_test_methods(test_file_list):
    num_methods = 0

    for test_file in test_file_list:
        with open(test_file, 'r') as f:
            lines = f.readlines()
            r = re.compile(r'[\t ]*@Test(?:\(time_limit ?= ?[0-9]+\))?[\t ]*')
            evo_none = re.compile(r'EvoSuite did not generate any tests')
            for i, line in enumerate(lines):
                if r.match(line):
                    num_methods += 1
                if evo_none.search(line):
                    num_methods -= 1
    return num_methods


def snake_case(s, pre='', post=''):
    if pre:
        pre += ' '
    if post:
        post = ' ' + post
    return re.sub(r'[()]', '', re.sub(r'[ ,.-]', '_', pre + s.lower() + post))


def open_csv(csv_file, columns, categorized=True):
    with open(csv_file) as f:
        df = pd.read_csv(f)
        if categorized:
            categories = list(df.columns[1 + len(columns):])
            df.columns = ['0'] + columns + categories
            return df.drop('0', axis=1), categories
        else:
            df.columns = ['0'] + columns
            return df.drop('0', axis=1)


def print_app_df(apps, avg_df, name_df, images_folder, categories, rng_y):
    current_images_folder_base = os.path.join(images_folder, name_df)

    desired_columns = ['Instruction coverage', 'Branch coverage', 'Method coverage'] + categories

    # create scattered plot per app, averaging trials and sampling trials alike
    current_images_folder = os.path.join(current_images_folder_base, 'per-app')
    Path(current_images_folder).mkdir(parents=True, exist_ok=True)
    for app in apps:
        print('Plotting coverage graphs for app ' + app)
        app_df = avg_df[(avg_df['App'] == app)]
        if app_df.empty:
            continue
        for column in desired_columns:
            app_df_filtered = app_df[(app_df[column].astype(str) != 'nan')].astype({column: float})
            if not app_df_filtered.empty:
                app_df_filtered.sort_values(by=['Interaction Level', 'Combination'], axis=0, inplace=True)
                fig = px.line(app_df_filtered, x='Interaction Level', y=column, color='Configuration',
                              range_y=rng_y)
                fig.write_image(os.path.join(current_images_folder, f'{app}_{snake_case(column)}.png'), format='png')
                print('Written image ' + os.path.join(current_images_folder, f'{app}_{snake_case(column)}.png'))


def print_all_df(all_df, name_df, images_folder, categories, config):
    width, rng_y, pts = config['analyze']['width'], config['analyze']['rng_y'], config['analyze']['pts']
    all_df.replace('ctdamplified', 'ctdguided', regex=True, inplace=True)
    current_images_folder_base = os.path.join(images_folder, name_df)

    desired_columns = ['Instruction coverage', 'Branch coverage', 'Method coverage'] + categories

    # create box plot for all data, no averaging
    current_images_folder = os.path.join(current_images_folder_base, 'all-apps')
    Path(current_images_folder).mkdir(parents=True, exist_ok=True)
    print('Plotting coverage graphs for all data')
    for column in desired_columns:
        all_df_filtered = all_df[(all_df[column].astype(str) != 'nan')].astype({column: float})
        if not all_df_filtered.empty:
            all_df_filtered.sort_values(by=['Interaction Level', 'Combination'], axis=0, inplace=True)
            fig = px.box(all_df_filtered, x='Interaction Level', y=column, color='Configuration', points=pts,
                         width=width, range_y=rng_y).update_traces(boxmean=True).update_layout(
                font=dict(
                    size=18,
                ),
                legend=dict(
                    orientation="h",
                    yanchor="bottom",
                    y=1.002,
                    xanchor="right",
                    x=1,
                ),
                legend_title_text='',
            )
            fig.write_image(os.path.join(current_images_folder, f'{snake_case(column)}.png'), format='png')
            print('Written image ' + os.path.join(current_images_folder, f'{snake_case(column)}.png'))
            all_df_filtered.to_csv(os.path.join(images_folder, '..', f'{name_df}_all-apps_{snake_case(column)}.csv'))


def print_all_csv_cumulate(all_csvs, name_csv, columns, images_folder, config):
    width, rng_y, pts = config['analyze']['width'], config['analyze']['rng_y'], config['analyze']['pts']
    all_dfs = []
    categories = None
    for all_csv in all_csvs:
        all_df, categories_all = open_csv(all_csv, columns, categorized=True)
        all_dfs.append(all_df)
        if not categories:
            categories = categories_all
        else:
            if categories != categories_all:
                print(f'{categories} (from file {all_csvs[0]}) is different from {categories_all} (from file {all_csv})', file=sys.stderr)
                assert False

    all_df = pd.concat(all_dfs, ignore_index=True)
    print_all_df(all_df, name_csv, images_folder, categories[:-3], config)  # -3 because we add 3 columns after categories


def run_coverage_analysis_separate(app: str, config, categorizer_output):
    columns = COLUMNS

    time_limits, levels, trials, output_dir, combs = \
        config['general']['time_limits'], config['general']['levels'], config['general']['trials'], \
        config['general']['output_dir'], config['general']['combs']

    verbose, precision = config['verbosity']['verbose'], config['verbosity']['precision']

    width, rng_y, pts, plots_per_app, delete_intermediate, analyze_categories, images_dir, force = \
        config['analyze']['width'], config['analyze']['rng_y'],  config['analyze']['pts'],\
        config['analyze']['plots_per_app'], config['analyze']['delete_intermediate'], \
        config['analyze']['analyze_categories'], config['analyze']['images_dir'], config['analyze']['force']

    categories = []  # ['Instruction coverage (0)', 'Instruction coverage (1)', 'Instruction coverage (under)',
    #                   'Instruction coverage (over)', 'Instruction coverage (col)', 'Instruction coverage (total)']

    output_dir_abs = os.path.abspath(output_dir)
    app_dir = os.path.join(output_dir_abs, app)
    if os.path.exists(os.path.join(app_dir, f'{app}_cov_analysis_results.csv')) and not force:
        print(f'Skipped app {app} (results exist)')
        return

    df_apps = []
    df_time_limits = []
    df_int_levels = []
    df_combs = []
    df_trials = []
    df_averaging_criterion = []
    df_inst_cov = []
    df_branch_cov = []
    df_method_cov = []
    df_n_tests = []
    df_ctd_cov = []

    cov_df: pd.DataFrame

    if delete_intermediate:
        def remove_noexcept(filename, verbose=False):
            try:
                os.remove(filename)
                if verbose:
                    print(f'Deleted file {os.path.basename(filename)}')
            except OSError as e:
                if e.errno != errno.ENOENT:
                    raise

        remove_noexcept(os.path.join(app_dir, f'{app}_cov_analysis_results_intermediate.csv'), True)
        remove_noexcept(os.path.join(app_dir, f'{app}_cov_analysis_results_intermediate2.csv'), True)

    if os.path.exists(os.path.join(app_dir, f'{app}_cov_analysis_results_intermediate2.csv')):
        cov_df, categories = open_csv(os.path.join(app_dir, f'{app}_cov_analysis_results_intermediate2.csv'), columns, categorized=True)
        print('Loaded DataFrame from saved second intermediate file')
        print(f'Implied categories: {categories}')
    else:
        if os.path.exists(os.path.join(app_dir, f'{app}_cov_analysis_results_intermediate.csv')):
            cov_df = open_csv(os.path.join(app_dir, f'{app}_cov_analysis_results_intermediate.csv'),
                              columns, categorized=False)
            print('Loaded DataFrame from saved intermediate file')
        else:
            for app_config in os.listdir(app_dir):
                if '.' in app_config or 'experiment' in app_config:
                    continue
                short_config = app_config[app_config.find(app + '_') + len(app + '_'):]
                time_limit = short_config[:short_config.find('s_')]
                if not int(time_limit) in time_limits:
                    continue
                interaction_level = short_config[short_config.find('s_') + 2:short_config.find('l_')]
                if not int(interaction_level) in levels:
                    continue
                trial = short_config[short_config.rfind('_') + 1:]
                if not int(trial) in trials:
                    continue
                print('App: {} time limit: {}, interaction_level: {}, trial: {}'.format(app, time_limit,
                                                                                        interaction_level, trial))

                reports_dir = os.path.join(app_dir, app_config, app_config + "-tkltest-reports")

                ctd_cov_file = os.path.join(app_dir, app_config, app_config + "_test_generation_summary.json")

                if not os.path.isfile(ctd_cov_file):
                    print("** ERROR: ctd coverage file {} doesn't exist".format(ctd_cov_file))
                    continue

                with open(ctd_cov_file) as f:
                    ctd_data = json.load(f)

                ctd_total_rows = ctd_data['test_plan_coverage_info']['test_plan_rows']
                ctd_orig_cov = round(ctd_data['test_plan_coverage_info']['rows_covered_bb_sequences'] / ctd_total_rows,
                                     2)
                ctd_final_cov = round(ctd_data['test_plan_coverage_info']['rows_covered_full'] / ctd_total_rows, 2)

                if not os.path.isdir(reports_dir):
                    print("** Warning: reports dir {} doesn't exist".format(reports_dir))
                    continue

                for comb_report_dir in os.listdir(os.path.join(reports_dir, 'jacoco-reports')):
                    if comb_report_dir.startswith('.'):
                        continue
                    comb = comb_report_dir[comb_report_dir.find('_' + str(trial) + '-') + 2 + len(str(trial)):-6]
                    if comb not in combs:
                        continue
                    # comb = comb[:comb.find('sampled') + 7] if 'sampled' in comb else comb  # remove sampling trial number
                    coverage_file = os.path.join(reports_dir, 'jacoco-reports',
                                                 comb_report_dir, comb_report_dir + '.csv')

                    if not os.path.isfile(coverage_file):
                        print("** Warning: coverage file {} doesn't exist".format(coverage_file))
                        continue

                    df_apps.append(app)
                    df_time_limits.append(time_limit)
                    df_int_levels.append(interaction_level)
                    df_combs.append(comb)
                    df_trials.append(trial)
                    df_averaging_criterion.append((app, interaction_level,
                                                comb[:comb.find('sampled') + 7] if 'sampled' in comb else comb))
                    inst, branch, method = calc_coverage(coverage_file, precision)

                    df_inst_cov.append(inst)
                    df_branch_cov.append(branch)
                    df_method_cov.append(method)
                    df_n_tests.append(count_test_methods(list(Path(os.path.join(app_dir, app_config, comb_report_dir)).rglob('*.java'))))
                    df_ctd_cov.append(ctd_final_cov if 'ctdamplified' in comb or 'ctd-amplified' in comb else ctd_orig_cov)

            cov_results = dict()
            cov_results['App'] = df_apps
            cov_results['Time limit'] = df_time_limits
            cov_results['Interaction Level'] = df_int_levels
            cov_results['Combination'] = df_combs
            cov_results['Trial'] = df_trials
            cov_results['Averaging criterion'] = df_averaging_criterion
            cov_results['Instruction coverage'] = df_inst_cov
            cov_results['Branch coverage'] = df_branch_cov
            cov_results['Method coverage'] = df_method_cov
            cov_results['Test methods'] = df_n_tests
            cov_results['CTD coverage'] = df_ctd_cov
            cov_df = pd.DataFrame(cov_results, columns=columns)

            cov_df.to_csv(os.path.join(app_dir, f'{app}_cov_analysis_results_intermediate.csv'), mode='w+')
            print('Saved intermediate DataFrame to file')

        # add categorizer output to table
        if analyze_categories and categorizer_output:
            print('Reading from categorizer output')
            categories = categorizer_output[0][3:]
            print(f'Implied categories: {categories}')
            categorizer_output = categorizer_output[1:]
            cov_df[categories] = np.nan
            for cat in categorizer_output:
                # cat is ['app', 'combination', 'level', ('0', '1', 'under', 'over', 'col', 'total') * {1,2,3}]
                (cat_app, cat_comb, cat_level), cat = tuple(cat[:3]), cat[3:]
                for i in range(len(categories)):
                    cov_df.loc[(cov_df['App'] == cat_app) & (cov_df['Combination'] == cat_comb) &
                               (cov_df['Interaction Level'].astype(int) == int(cat_level)), categories[i]] = float(
                        cat[i])
            cov_df.to_csv(os.path.join(app_dir, f'{app}_cov_analysis_results_intermediate2.csv'), mode='w+')
            print('Saved second intermediate DataFrame to file')

    # add three more columns
    print('Adding three more columns')
    cov_df['Instruction coverage efficiency'] \
        = np.where(cov_df['Test methods'].astype(float) > 0,
                   cov_df['Instruction coverage'].astype(float) / cov_df['Test methods'].astype(float), 0)
    cov_df['Branch coverage efficiency'] \
        = np.where(cov_df['Test methods'].astype(float) > 0,
                   cov_df['Branch coverage'].astype(float) / cov_df['Test methods'].astype(float), 0)
    cov_df['Configuration'] = cov_df['Combination'].astype(str)

    # prepare columns for samples
    sampled_cov_df = None
    if cov_df['Combination'].astype(str).str.contains('-sampled', regex=True).any():
        print(f'Detected samples -- preparing columns for samples (assuming only trial 0 may be sampled)')
        for level in levels:
            app_df = cov_df[(cov_df['App'] == app) & (cov_df['Interaction Level'].astype(int) == int(level)) & (
                        cov_df['Trial'].astype(int) == 0)]
            ctd_amplified_counts = app_df['Combination'].str.contains('ctd-amplified-sampled',
                                                                      regex=True).value_counts()
            evosuite_counts = app_df['Combination'].str.contains('evosuite-sampled', regex=True).value_counts()
            randoop_counts = app_df['Combination'].str.contains('randoop-sampled', regex=True).value_counts()

            sampled_suites = [True in ctd_amplified_counts, True in evosuite_counts, True in randoop_counts]
            if sampled_suites.count(True) != 2:
                continue

            non_sampled = \
                ['ctd-amplified', 'evosuite', 'randoop'][sampled_suites.index(False)]

            if non_sampled != 'ctd-amplified':
                implied_sampling_trials = ctd_amplified_counts[True]
            else:
                implied_sampling_trials = evosuite_counts[True]
                assert implied_sampling_trials == randoop_counts[True]

            print(f'Duplicating {non_sampled}, {implied_sampling_trials} times (app={app}, level={level}, trial=0)')

            not_sampled_rows = app_df[app_df['Combination'].str.match(f'{non_sampled}$')]
            not_sampled_rows.replace(non_sampled, f'{non_sampled}-sampled', inplace=True)
            cov_df = cov_df.append([not_sampled_rows] * implied_sampling_trials)

        cov_df.to_csv(os.path.join(app_dir, f'{app}_cov_analysis_results_intermediate3.csv'), mode='w+')
        print('Saved non-loadable third intermediate DataFrame to file')
        sampled_cov_df = cov_df[cov_df['Combination'].str.contains('-sampled', regex=True)]
        sampled_cov_df.replace('-sampled(-[0-9]+)?', ' (sampled)', regex=True, inplace=True)
        sampled_cov_df.to_csv(os.path.join(app_dir, f'{app}_cov_analysis_results_sampled.csv'), mode='w+')

    cov_df.to_csv(os.path.join(app_dir, f'{app}_cov_analysis_results.csv'), mode='w+')
    cov_df = cov_df[~cov_df['Combination'].str.contains('-sampled', regex=True)]
    sing_cov_df = cov_df[cov_df['Combination'].isin(['ctd-amplified', 'evosuite', 'randoop'])]
    sing_cov_df.to_csv(os.path.join(app_dir, f'{app}_cov_analysis_results_singles.csv'), mode='w+')
    comb_cov_df = cov_df[cov_df['Combination'].isin(['ctdamplified-evosuite-randoop', 'evosuite-randoop'])]
    comb_cov_df.to_csv(os.path.join(app_dir, f'{app}_cov_analysis_results_combinations.csv'), mode='w+')

    # take mean over all instances of same config (dropping sampling trial number must be done after cat analysis)
    print('Averaging DataFrame values according to averaging criterion')

    def take_avg(col):
        return str(col.mean()) if is_numeric_dtype(col) else col.unique() if col.nunique() == 1 else np.NaN

    sampled_avg_df = None
    if sampled_cov_df is not None:
        sampled_avg_df = sampled_cov_df.groupby('Averaging criterion').agg(take_avg)
        sampled_avg_df.to_csv(os.path.join(app_dir, f'{app}_cov_analysis_results_sampled_averaged.csv'), mode='w+')
    sing_avg_df = sing_cov_df.groupby('Averaging criterion').agg(take_avg)
    sing_avg_df.to_csv(os.path.join(app_dir, f'{app}_cov_analysis_results_singles_averaged.csv'), mode='w+')
    comb_avg_df = comb_cov_df.groupby('Averaging criterion').agg(take_avg)
    comb_avg_df.to_csv(os.path.join(app_dir, f'{app}_cov_analysis_results_combinations_averaged.csv'), mode='w+')

    # plot graphs based on raw data
    images_folder = os.path.join(output_dir_abs, images_dir)
    Path(images_folder).mkdir(exist_ok=True)

    avg_dfs = [sing_avg_df, comb_avg_df] + ([sampled_avg_df] if sampled_avg_df is not None else [])
    all_dfs = [(sing_cov_df, 'singles'), (comb_cov_df, 'combinations')] \
        + ([(sampled_cov_df, 'sampled')] if sampled_cov_df is not None else [])

    if plots_per_app:
        for avg_df, (_, name_df) in zip(avg_dfs, all_dfs):
            if avg_df is not None:
                print_app_df([app], avg_df, name_df, images_folder, categories, rng_y)


def run_coverage_analysis(config, categorizer_output):
    """The main entry point for analyzing experiments coverage results.

    This is the main entry point for an experiment coverage analysis. For each app in the experiment,
    each interaction level, each time limit, and each combination of resulting test suites (out of
    randoop, evosuite, and ctd-guided generated test suites), it computes the average instruction, branch
    and method coverage, test suite size (in terms of test methods) and *false* ctd coverage (in terms of rows covered
    out of the ctd test plan)
    """

    for app in config['general']['apps']:
        run_coverage_analysis_separate(app, config, categorizer_output)


def parse_oc_json(file_name, pts, pts2, verbose=False, app=''):
    with open(file_name) as f:
        oc_methods = json.load(f)
    level = oc_methods['interaction_level']
    oc_methods = oc_methods['ctd_coverage']['monolithic']
    for cls in oc_methods:
        methods = oc_methods[cls]
        for name in methods:
            ctd_cov = float(methods[name]['ctd_coverage'])
            ctd_cov_bb = float(methods[name]['ctd_coverage_bb_sequences'])
            pts.append({'Configuration': 'ctd-amplified', 'Combination': 'ctd-amplified', 'Interaction Level': level, 'CTD Coverage': ctd_cov, 'Filename': file_name})
            pts.append({'Configuration': 'evosuite-randoop', 'Combination': 'ctd-amplified', 'Interaction Level': level, 'CTD Coverage': ctd_cov_bb, 'Filename': file_name})
            pts2.append({'Class': cls, 'Method': name, 'Interaction Level': level, 'Coverage by CTD-Amplified': ctd_cov, 'Coverage by Basic Block': ctd_cov_bb, 'App': app, 'Filename': file_name})
            if verbose:
                print(f'OC level:{level} appended {ctd_cov} vs. {ctd_cov_bb}')


def parse_all_oc(oc_apps, time_limit, trial, output_dir, verbose=False):
    pts, pts2 = [], []
    if not oc_apps:
        json_glob = Path(output_dir).rglob(f'*/*_{time_limit}s_*l_{trial}/*_ctd_coverage_report.json')
        for file_name in json_glob:
            parse_oc_json(file_name, pts, pts2, verbose)
            if verbose:
                print(f'Finished parsing json file {file_name} for oc')
    else:
        for app in oc_apps:
            app_json_glob = Path(output_dir).rglob(f'{app}/{app}_{time_limit}s_*l_{trial}/*_ctd_coverage_report.json')
            for file_name in app_json_glob:
                parse_oc_json(file_name, pts, pts2, verbose, app)
                if verbose:
                    print(f'Finished parsing json file {file_name} for oc')
    return pts, pts2


def plot_oc(time_limit, trial, config, csv_file=None):
    apps, output_dir = config['general']['apps'], config['general']['output_dir']
    verbose = config['verbosity']['verbose']
    images_dir, pts, rng_y = config['analyze']['images_dir'], config['analyze']['pts'], config['analyze']['rng_y']

    if not csv_file:
        pts, pts2 = parse_all_oc(apps, time_limit, trial, verbose)
        pts_df = pd.DataFrame(pts)
        pts2_df = pd.DataFrame(pts2)
        pts2_df.to_csv(os.path.join(output_dir, f'ctd_coverage_analysis_oc2_{time_limit}s_{trial}.csv'))
    else:
        f = open(csv_file)
        pts_df = pd.read_csv(f)
        pts_df.columns = ['0'] + ['Configuration', 'Combination', 'Interaction Level', 'CTD Coverage', 'Filename']
        pts_df.drop('0', axis=1, inplace=True)
    current_images_folder_base = os.path.join(output_dir, images_dir, 'ctd-coverage')
    current_images_folder = os.path.join(current_images_folder_base, 'all-apps')
    Path(current_images_folder).mkdir(parents=True, exist_ok=True)
    pts_df.to_csv(os.path.join(output_dir, f'ctd_coverage_analysis_oc_{time_limit}s_{trial}.csv'))
    print(f'Written to file {output_dir}/tkl_coverage_analysis_oc_{time_limit}s_{trial}.csv')
    print(f'Written to auxiliary file {output_dir}/tkl_coverage_analysis_oc2_{time_limit}s_{trial}.csv')
    print('Plotting *true* ctd coverage graph for all data')
    if not pts_df.empty:
        column = 'CTD Coverage'
        pts_df.sort_values(by=['Interaction Level', 'Combination'], axis=0, inplace=True)
        fig = px.box(pts_df, x='Interaction Level', y=column, color='Configuration', points=pts,
                     width=720, range_y=rng_y).update_traces(boxmean=True).update_layout(
            legend=dict(
                orientation="h",
                yanchor="bottom",
                y=1.02,
                xanchor="right",
                x=1
            ),
        )
        fig.write_image(os.path.join(current_images_folder, f'{snake_case(column, pre="true")}.png'), format='png')
        print('Written image ' + os.path.join(current_images_folder, f'{snake_case(column, pre="true")}.png'))


def parse_exceptions_json(file_name, app, time_limit, level, trial, exceptions_csv, verbose=False):
    with open(file_name) as f:
        summary = json.load(f)
    if 'execution_fail_exception_types' not in summary.keys():
        return exceptions_csv
    exceptions = summary['execution_fail_exception_types']
    for exception in exceptions:
        ex_count = exceptions[exception]
        exception = str(exception).split()[0].split('.')[-1]
        exceptions_csv.append([app, 'ctd-amplified', time_limit, level, trial, exception, ex_count])
        if verbose:
            print(f'Appended {exception} ({ex_count})')
    return exceptions_csv


def parse_exceptions(time_limit, level, trial, config):
    apps, output_dir = config['general']['apps'], config['general']['output_dir']
    verbose = config['verbosity']['verbose']

    from csv import reader, writer
    with open(f'{output_dir}/exceptions_summary.csv', 'r') as read_obj:
        csv_reader = reader(read_obj)
        exceptions_csv = list(csv_reader)
    real_apps = []
    for app in apps:
        if os.path.exists(f'{output_dir}/{app}/{app}_{time_limit}s_{level}l_{trial}/{app}_{time_limit}s_{level}l_{trial}_test_generation_summary.json'):
            real_apps.append(app)
    print(f'Real apps: {real_apps} ({len(real_apps)} out of {len(apps)})')
    for app in real_apps:
        parse_exceptions_json(f'{output_dir}/{app}/{app}_{time_limit}s_{level}l_{trial}/{app}_{time_limit}s_{level}l_{trial}_test_generation_summary.json',
                              app, time_limit, level, trial, exceptions_csv, verbose)
        if verbose:
            print(f'Finished parsing test-gen summary json for app {app} for exceptions')
    with open(f'{output_dir}/tkl_coverage_analysis_ex_{time_limit}s_{level}l_{trial}_raw.csv', 'w') as write_obj:
        csv_writer = writer(write_obj)
        csv_writer.writerows(exceptions_csv)
        print(f'Written to file {output_dir}/tkl_coverage_analysis_ex_{time_limit}s_{level}l_{trial}_raw.csv')
    exceptions_df = pd.DataFrame(exceptions_csv, columns=['App', 'Configuration', 'Timeout', 'Interaction Level', 'Trial', 'Exception', 'Count'])
    exceptions_df.sort_values(['App', 'Configuration', 'Exception'], inplace=True)
    exceptions_df.to_csv(f'{output_dir}/tkl_coverage_analysis_ex_{time_limit}s_{level}l_{trial}.csv')
    print(f'Written to file {output_dir}/tkl_coverage_analysis_ex_{time_limit}s_{level}l_{trial}.csv')
    plot_exceptions(time_limit, level, trial, output_dir, exceptions_df=exceptions_df)


def plot_exceptions(time_limit, level, trial, config, exceptions_df=None, csv_file=None):
    output_dir = config['general']['output_dir']
    images_dir = config['analyze']['images_dir']
    if exceptions_df is None:
        open_csv(csv_file, columns=['App', 'Configuration', 'Timeout', 'Interaction Level', 'Trial', 'Exception', 'Count'], categorized=False)
    for conf in ['ctd-amplified', 'evosuite', 'randoop']:
        exceptions_df.loc[(exceptions_df['Configuration'] == conf), 'Percentage'] = \
            exceptions_df[(exceptions_df['Configuration'] == conf)]['Count'] / \
            exceptions_df[(exceptions_df['Configuration'] == conf)]['Count'].sum()
    for conf in ['ctd-amplified', 'evosuite', 'randoop']:
        conf_df = exceptions_df[(exceptions_df['Configuration'] == conf) & (exceptions_df['Timeout'].astype(int) == time_limit) &
                                (exceptions_df['Interaction Level'].astype(int) == level) & (exceptions_df['Trial'].astype(int) == trial)].copy()
        if conf_df.empty:
            continue
        conf_df.sort_values('Exception', ascending=False, inplace=True)
        bars = px.bar(conf_df,
                      y='Exception', x='Percentage',
                      title=f'Histogram of exceptions for configuration {conf} and level {level}',
                      orientation='h',
                      width=1080, height=1720).update_layout(
            yaxis=dict(tickmode='linear'),
        )
        bars.write_image(os.path.join(output_dir, images_dir, f'exceptions_histogram_{conf}_{time_limit}s_{level}l_{trial}.png'), format='png')
        print(f"Written histogram image {os.path.join(output_dir, images_dir, f'exceptions_histogram_{conf}_{time_limit}s_{level}l_{trial}.png')}")

    exceptions_df.sort_values(['App', 'Configuration', 'Exception'], inplace=True)
    exceptions_df.sort_values('Exception', ascending=False, inplace=True)

    rare_exceptions = []
    rare_threshold = 0.02
    for exception in list(exceptions_df['Exception']):
        percent = {conf: exceptions_df[(exceptions_df['Exception'] == exception) &
                                       (exceptions_df['Configuration'] == conf)]['Percentage'].sum()
                   for conf in ['ctd-amplified', 'evosuite', 'randoop']}
        if percent['ctd-amplified'] <= rare_threshold and percent['evosuite'] <= rare_threshold and percent['randoop'] <= rare_threshold:
            rare_exceptions.append(exception)
        else:
            if exception[-1] == ';':
                exception_new = exception[:-1]
                exceptions_df['Exception'].replace(exception, exception_new, regex=False, inplace=True)
            if exception != 'Exception':
                exception_new = re.sub('Exception', '', exception)
                exceptions_df['Exception'].replace(exception, exception_new, regex=False, inplace=True)
    print('Rare exceptions:', rare_exceptions)
    exceptions_df = exceptions_df[~exceptions_df['Exception'].isin(rare_exceptions)]
    exceptions_df.sort_values(['Exception', 'Configuration'], ascending=[False, True], inplace=True)
    exceptions_df.replace('ctd-amplified', 'ctd-guided', inplace=True)
    bars = px.bar(exceptions_df,
                  y='Exception', x='Percentage', color='Configuration',
                  # title=f'Histogram of exceptions per configuration for level {level}',
                  orientation='h', width=1080, height=1320).update_layout(
        yaxis=dict(tickmode='linear'), yaxis_title=None,
        xaxis=dict(tick0=0, dtick=0.05, tickformat=',.0%'), xaxis_title=None,
        font=dict(size=27),
        legend=dict(orientation="h", yanchor="bottom", y=1.002, xanchor="right", x=1),
        legend_title_text='',
        barmode='group',
    )
    bars.write_image(os.path.join(output_dir, images_dir, f'exceptions_histogram_all_{time_limit}s_{level}l_{trial}.png'), format='png')
    print(f"Written histogram image {os.path.join(output_dir, images_dir, f'exceptions_histogram_all_{time_limit}s_{level}l_{trial}.png')}")


def main_cumulate(config):
    columns = COLUMNS
    output_dir_abs = os.path.abspath(config['general']['output_dir'])
    images_folder = os.path.join(output_dir_abs, config['analyze']['images_dir'])
    for name_csv in ['singles', 'combinations', 'sampled']:
        all_csvs = []
        for app in config['general']['apps']:
            app_dir = os.path.join(output_dir_abs, app)
            all_csvs.append(os.path.join(app_dir, f'{app}_cov_analysis_results_{snake_case(name_csv)}.csv'))
        print_all_csv_cumulate(all_csvs, name_csv, columns, images_folder, config)


if __name__ == '__main__':
    apps = sys.argv[1:]
    with open('parsed_config.toml') as f:
        config = toml.load(f)
    if apps:
        # parse_exceptions(60, 3, 0, config)
        plot_exceptions(60, 3, 0, config, csv_file='/Users/antonio/Desktop/FINAL/exceptions/tkl_coverage_analysis_ex_60s_3l_0.csv')
        # plot_oc(60, 0, config, csv_file='/Users/antonio/Desktop/FINAL/RQ1-ctd-coverage/45-apps/ctd_coverage_analysis_oc_60s_0.csv')
