import argparse
import csv
import os
import toml
from datetime import datetime

import apps_list
import categorizer
import coverage_util
import sampler
import tkl_coverage_analysis
import tkltest_experiments


def default_config():
    apps = [app for app in apps_list.ok_app if app in apps_list.SF110_apps and app not in
            ['9_falselight', '26_jipa', '47_dvd-homevideo', '50_biff', '82_gaj', '95_celwars2009', '100_jgaap']]
    d_config = dict(general={}, verbosity={}, generate={}, sample={}, categorize={}, analyze={}, sections=[])
    d_config['general']['apps'] = apps
    d_config['general']['time_limits'] = [10, 30, 60]
    d_config['general']['levels'] = [1, 2, 3]
    d_config['general']['trials'] = range(5)
    d_config['general']['sampling_trials'] = 10
    d_config['general']['output_dir'] = 'tkltest-output'
    d_config['general']['apps_dir'] = 'tmp_apps'
    d_config['general']['categorizer_output'] = 'categories'
    d_config['general']['combs'] = ['ctd-amplified', 'evosuite', 'randoop'] \
        + ['ctdamplified-evosuite-randoop', 'evosuite-randoop'] \
        + [f'ctd-amplified-sampled-{sampling_trial}' for sampling_trial in range(d_config['general']['sampling_trials'])] \
        + [f'evosuite-sampled-{sampling_trial}' for sampling_trial in range(d_config['general']['sampling_trials'])] \
        + [f'randoop-sampled-{sampling_trial}' for sampling_trial in range(d_config['general']['sampling_trials'])]

    d_config['verbosity']['verbose'] = 1
    d_config['verbosity']['verbose_execute'] = 0
    d_config['verbosity']['precision'] = 3

    d_config['generate']['reuse_base_tests'] = False
    d_config['generate']['force'] = False

    d_config['sample']['clean_sampling'] = True
    d_config['sample']['skip_generate_xml'] = False
    d_config['sample']['force'] = False

    d_config['categorize']['force'] = False

    d_config['analyze']['width'] = [None, 1720][1]
    d_config['analyze']['rng_y'] = [None, [0, 1]][0]
    d_config['analyze']['pts'] = [None, 'all'][1]
    d_config['analyze']['plots_per_app'] = True
    d_config['analyze']['delete_intermediate'] = False
    d_config['analyze']['analyze_categories'] = True
    d_config['analyze']['images_dir'] = 'images'
    d_config['analyze']['force'] = False

    d_config['sections'] = []

    return d_config


def main(config):
    verbose = config['verbosity']['verbose']
    for app in config['general']['apps']:
        if config['sections']['generate']:
            print(f"Running `tkltest_experiments.py`::main() on {app} "
                  f"(force: {config['generate']['force']}, skip_exec={not config['sections']['execute']})")
            tkltest_experiments.experiments_separate(app, config)

        for time_limit in config['general']['time_limits']:
            for level in config['general']['levels']:
                app_conf_base = f'{app}_{time_limit}s_{level}l'
                for trial in config['general']['trials']:
                    app_conf = f'{app_conf_base}_{trial}'
                    conf_dir = os.path.join(config['general']['output_dir'], app, app_conf)
                    if not os.path.exists(conf_dir):
                        if verbose:
                            print(f'SKIPPED CONFIGURATION: {app_conf_base}, trial {trial}')
                        continue

                    print('CONFIGURATION: {}, trial {}'.format(app_conf_base, trial))

                    if trial == 0:
                        sampler.sample(['ctd-amplified', 'evosuite', 'randoop'], conf_dir, app_conf, config)

                    if config['sections']['coverage-xml']:
                        begin_time = datetime.now()
                        if config['sample']['skip_generate_xml']:
                            only_dirs_containing = ['ctd-amplified-tests', 'evosuite-tests', 'randoop-tests']
                        else:
                            only_dirs_containing = None
                        coverage_util.generate_coverage_xml(conf_dir, only_dirs_containing=only_dirs_containing)
                        print('Coverage XML generation took {}'.format(datetime.now() - begin_time))

        categorizer_out = os.path.join(config['general']['output_dir'], app, f"{app}_{config['general']['categorizer_output']}.csv")
        cats_exist = os.path.exists(categorizer_out)
        if config['sections']['categorize'] and (config['categorize']['force'] or not cats_exist):
            begin_time = datetime.now()
            categories = categorizer.categorize_separate(app, config)
            print('Categorization ***per-app*** took {}'.format(datetime.now() - begin_time))
        else:
            categories = None

        if config['sections']['analyze']:
            if config['analyze']['analyze_categories'] and not categories:
                if cats_exist:
                    with open(categorizer_out) as f:
                        categories = list(csv.reader(f))
                elif verbose:
                    print(f"[WARNING] Parameter `config['analyze']['analyze_categories']` was set but could not find "
                          f"{app}_{config['general']['categorizer_output']}.csv")
            begin_time = datetime.now()
            tkl_coverage_analysis.run_coverage_analysis_separate(app, config, categories)
            print('Coverage analysis ***per-app*** took {}{}{}'.format(datetime.now() - begin_time,
                                                                       ' with categorization' if categories else '',
                                                                       ' (forced)' if config['analyze']['force'] else ''))

    if config['sections']['histogramize']:
        begin_time = datetime.now()
        categorizer.histogramize(config)
        print('Histogramization took {}'.format(datetime.now() - begin_time))

    if config['sections']['cumulate_coverage']:
        begin_time = datetime.now()
        tkl_coverage_analysis.main_cumulate(config)
        print('Coverage accumulation took {}'.format(datetime.now() - begin_time))


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description='Run TackleTest Experiments',
                                     formatter_class=argparse.ArgumentDefaultsHelpFormatter)

    # GENERAL
    parser.add_argument('apps', type=str, nargs='*', help='Apps to run on')
    parser.add_argument('-n', '--trials', type=int, dest='num_trials', default=5, help='Number of trials for each configuration')
    parser.add_argument('-t', '--time-limits', type=int, nargs='+', dest='time_limits', default=[10, 30, 60], help='Time limits to run with')
    parser.add_argument('-l', '--levels', type=int, nargs='+', dest='levels', default=[1, 2, 3], help='Interaction levels to run with')
    parser.add_argument('-o', '--output-dir', type=str, dest='output_dir', default='tkltest-output', help='Output directory to move results into')
    parser.add_argument('-a', '--apps-dir', type=str, dest='apps_dir', default='tmp_apps', help='Directory where the apps lie (classes dir and classpath file must be right under every app name)')
    parser.add_argument('-s', '--sampling-trials', type=int, dest='sampling_trials', default=10, help='Number of sampling trials')
    parser.add_argument('--categorizer-output', type=str, dest='categorizer_output', default='categories', help='Name of categorizer output file')

    # VERBOSITY
    parser.add_argument('-q', '--silent', dest='silent', action='store_true', default=False, help='Less verbose messages')
    parser.add_argument('-Q', '--silent-execute', dest='silent_execute', action='store_true', default=False, help='No verbose during execution')
    parser.add_argument('--precision', type=int, dest='precision', default=3, help='Number of decimal places in figures')

    # GENERATE
    parser.add_argument('-R', '--reuse-base-tests', dest='reuse_base_tests', action='store_true', default=False, help='Reuse base tests during generation')
    parser.add_argument('--generate_standalone', dest='generate_standalone', action='store_true', default=False, help='Generate standalone EvoSuite and Randoop test suites')

    # SAMPLE
    parser.add_argument('--sampling-clean', dest='sampling_clean', action='store_true', default=False, help='Clean existing sampled test suites before sampling')
    parser.add_argument('--sampling-skip-generate-xml', dest='sampling_skip_generate_xml', action='store_true', default=False, help='Do not generate JaCoCo XML summary file for the sampled test suites')
    
    # CATEGORIZE
    parser.add_argument('--threshold', type=int, dest='threshold', default=3, help="Threshold between 'under' (incl.) and 'over' categories")
    parser.add_argument('--target-coverage', type=str, nargs='+', choices=['instruction', 'branch', 'method'], 
                        dest='target_coverage', default=['instruction', 'branch', 'method'], help='Coverage type to gather categorized data for')

    # ANALYZE
    parser.add_argument('--analysis-wide-images', dest='analysis_width', action='store_const', const=1720, default=None, help='Make output images wide during analysis')
    parser.add_argument('--analysis-full-yaxis', dest='analysis_rng_y', action='store_const', const=[0, 1], default=None, help='Make y-axis values [0, 1] during analysis')
    parser.add_argument('--analysis-no-boxplot-pts', dest='analysis_pts', action='store_const', const='all', default=None, help='Hide boxplot points during analysis')
    parser.add_argument('--analysis-no-plots-per-app', dest='analysis_no_plots_per_app', action='store_true', default=False, help='Do not plot images per-app during analysis')
    parser.add_argument('--analysis-no-checkpoint', dest='analysis_no_checkpoint', action='store_true', default=False, help='Override saved checkpoint file during analysis')
    parser.add_argument('--analysis-no-categories', dest='analysis_no_categories', action='store_true', default=False, help='Do not apply per-category analysis')
    parser.add_argument('--analysis-images-dir', type=str, dest='analysis_images_dir', default='images', help='Name of directory to put analysis plots in')

    # FORCE
    parser.add_argument('--force-generate', dest='force_generate', action='store_true', default=False, help='Redo generation even if done already')
    parser.add_argument('--force-sample', dest='force_sample', action='store_true', default=False, help='Redo sampling even if done already')
    parser.add_argument('--force-categorize', dest='force_categorize', action='store_true', default=False, help='Redo categorization even if done already')
    parser.add_argument('--force-analyze', dest='force_analyze', action='store_true', default=False, help='Redo analysis even if done already')

    mutex = parser.add_mutually_exclusive_group()
    mutex.add_argument('-S', '--skip', type=str, nargs='+',
                       choices=['generate', 'execute', 'sample', 'execute-samples', 'coverage-xml', 'categorize', 'analyze'],
                       dest='skip', default=None, help='Skip those steps')
    mutex.add_argument('-O', '--only', type=str, nargs='+',
                       choices=['generate', 'execute', 'sample', 'execute-samples', 'coverage-xml', 'categorize', 'analyze'],
                       dest='only', default=None, help='Execute only those steps')

    parser.add_argument('-H', '--histogramize', dest='histogramize', action='store_true', default=False, help='Output a histogram for the categories in all apps')
    parser.add_argument('-C', '--cumulate-coverage', dest='cumulate_coverage', action='store_true', default=False, help='Output accumulated coverage data for all apps')

    args = parser.parse_args()

    sections = dict(histogramize=args.histogramize, cumulate_coverage=args.cumulate_coverage)
    global_sections = sections['histogramize'] or sections['cumulate_coverage']
    make_true = not args.only and not global_sections
    for k in ['generate', 'execute',
              'sample', 'execute-samples',
              'coverage-xml', 'categorize', 'analyze']:
        sections[k] = make_true

    if not global_sections:
        if args.only:
            for step in args.only:
                sections[step] = True
        elif args.skip:
            for step in args.skip:
                sections[step] = False

    combs = ['ctd-amplified', 'evosuite', 'randoop'] \
        + ['ctdamplified-evosuite-randoop', 'evosuite-randoop'] \
        + [f'ctd-amplified-sampled-{sampling_trial}' for sampling_trial in range(args.sampling_trials)] \
        + [f'evosuite-sampled-{sampling_trial}' for sampling_trial in range(args.sampling_trials)] \
        + [f'randoop-sampled-{sampling_trial}' for sampling_trial in range(args.sampling_trials)]

    if args.apps:
        apps = args.apps
    else:
        # take all ok_app except those package-less
        apps = [app for app in apps_list.ok_app if app in apps_list.SF110_apps and app not in
                ['9_falselight', '26_jipa', '47_dvd-homevideo', '50_biff', '82_gaj', '95_celwars2009', '100_jgaap']]

    parsed_config = dict(general=dict(apps=apps,
                                      time_limits=args.time_limits,
                                      levels=args.levels,
                                      trials=range(args.num_trials),
                                      sampling_trials=args.sampling_trials,
                                      output_dir=args.output_dir,
                                      apps_dir=args.apps_dir,
                                      categorizer_output=args.categorizer_output,
                                      combs=combs),
                         verbosity=dict(verbose=not args.silent,
                                        verbose_execute=not args.silent_execute,
                                        precision=args.precision),
                         generate=dict(reuse_base_tests=args.reuse_base_tests,
                                       generate_standalone=args.generate_standalone,
                                       force=args.force_generate),
                         # execute=dict(),
                         sample=dict(clean_sampling=args.sampling_clean,
                                     skip_generate_xml=args.sampling_skip_generate_xml,
                                     force=args.force_sample),
                         categorize=dict(threshold=args.threshold,
                                         target_coverage=args.target_coverage,
                                         force=args.force_categorize),
                         analyze=dict(width=args.analysis_width,
                                      rng_y=args.analysis_rng_y,
                                      pts=args.analysis_pts,
                                      plots_per_app=not args.analysis_no_plots_per_app,
                                      delete_intermediate=args.analysis_no_checkpoint,
                                      analyze_categories=not args.analysis_no_categories,
                                      images_dir=args.analysis_images_dir,
                                      force=args.force_analyze),
                         sections=sections,
                         )

    with open('parsed_config.toml', 'w') as f:
        toml.dump(parsed_config, f)

    main(config=parsed_config)
