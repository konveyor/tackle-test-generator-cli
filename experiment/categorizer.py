import csv
import json
import numpy as np
import os
import pandas as pd
import plotly.express as px
import re
import sys
import xml.etree.ElementTree as ElementTree
from pathlib import Path

import apps_list


def parse_xml(file_name, names, names_with_counters, target_coverage, verbose=False):
    target_coverage = str(target_coverage).upper()
    tree = ElementTree.parse(file_name)
    classes = tree.getroot().iter('class')
    i = 1
    for cls in classes:
        cls_name, cls_src = cls.attrib['name'], cls.attrib['sourcefilename']
        methods = cls.findall('method')
        for method in methods:
            desc, name = method.attrib['desc'], method.attrib['name']
            line = method.attrib['line'] if 'line' in method.attrib else None  # only `clinit` does not have line
            # XML inserts V (void) after the constructor, whereas JSON does not; so remove it
            fixed_name = re.sub(r'^(<init>.*\))V', r'\1', f'{name}{desc}')
            formal_name = (fixed_name, cls_name.replace(os.sep, '.'))
            i += 1
            coverage_counter = [counter for counter in method if counter.attrib['type'] == target_coverage]
            if len(coverage_counter) == 1:
                if verbose:
                    print(f'{i}. Caught method {name} ({desc}) at line {line} of class {cls_name} ({cls_src}) '
                          f'[{formal_name}]')
                coverage_counter = coverage_counter[0]
                names.append(formal_name)
                names_with_counters.append((*formal_name, coverage_counter.attrib['covered'],
                                            coverage_counter.attrib['missed'], line))
            else:
                if verbose:
                    print(f'{i}. Passed over method {name} ({desc}) at line {line} of class {cls_name} ({cls_src}) '
                          f'[{formal_name}]')
    return names, names_with_counters


def parse_json(file_name, threshold, names, cat_1, cat_under, cat_over, cat_col, verbose=False):
    with open(file_name) as f:
        plans = json.load(f)

    plans = plans['models_and_test_plans']['monolithic']
    i = 1
    for cls in plans:
        methods = plans[cls]
        for name in methods:
            attrib, test_plan = methods[name]['attributes'], methods[name]['test_plan']
            has_collection = False
            for attr in attrib:
                attr_name = str(attr['attribute_name'])
                has_collection |= (not attr_name[-1].isdigit())
            test_plan_len = len(test_plan)
            formal_name = (str(name), cls)
            if has_collection:
                if verbose:
                    print(f'{i}. Method {name} has a collection as parameter [{formal_name}]')
                i += 1
                cat_col.append(formal_name)
            else:
                if verbose:
                    print(f'{i}. Method {name} has {test_plan_len} test plan(s) [{formal_name}]')
                i += 1
                if test_plan_len == 1:
                    cat_1.append(formal_name)
                elif test_plan_len <= threshold:
                    cat_under.append(formal_name)
                else:
                    cat_over.append(formal_name)
            names.append(formal_name)

    if verbose:
        print(f'Counters (1,under,over,col) = {len(cat_1), len(cat_under), len(cat_over), len(cat_col)}')
    return names, cat_1, cat_under, cat_over, cat_col


def intersect(list1, list2, list3=None):
    if not list3:
        return [value for value in list1 if value in list2]
    if len(list3) == len(list1):
        return [list3[i] for i in range(len(list1)) if list1[i] in list2]
    if len(list3) == len(list2):
        return [list3[i] for i in range(len(list2)) if list2[i] in list1]
    return None


def subtract(list1, list2):
    return [value for value in list1 if value not in list2]


def histogramize(config):
    apps, time_limits, levels, trials, output_dir, categorizer_output = \
        config['general']['apps'], config['general']['time_limits'], config['general']['levels'], \
        config['general']['trials'], config['general']['output_dir'], config['general']['categorizer_output']
    verbose, precision = config['verbosity']['verbose'], config['verbosity']['precision']
    threshold, target_coverage = config['categorize']['threshold'], config['categorize']['target_coverage']

    if verbose:
        print(f'Running histogramizer with threshold = {threshold} and target coverage = {target_coverage}')

    header = ['App', 'Combination', 'Interaction Level']
    for target in target_coverage:
        target = target.title()
        header.extend([f'{target} coverage ({cat})' for cat in ['0', '1', 'under', 'over', 'col', 'total']])

    result = [header]
    stats = [[title.replace('coverage', 'counter') for title in header]]
    print('(r)', result[0])
    print('(s)', stats[0])

    cat_count = len(['0', '1', 'under', 'over', 'col', 'total'])
    total_counters = {target: [0] * cat_count for target in target_coverage}
    total_percents = {target: [0] * cat_count for target in target_coverage}
    level_percents = {level: None for level in levels}

    for level in levels:
        level_counters = {target: [0] * cat_count for target in target_coverage}
        level_percents[level] = {target: [0] * cat_count for target in target_coverage}
        for app in apps:
            app_counters = {target: None for target in target_coverage}
            app_percents = {target: None for target in target_coverage}
            any_conf = f'{app}_{time_limits[0]}s_{level}l_{trials[0]}'
            combination = 'ctd-amplified'
            if verbose:
                print(f'HISTOGRAMIZER: app={app}, any_conf={any_conf}, combination={combination}')
            names_json, names_cat_1, names_cat_under, names_cat_over, names_cat_col = [], [], [], [], []
            file_name_json = os.path.join(output_dir, f'{app}/{any_conf}/{any_conf}_ctd_models_and_test_plans.json')
            if not os.path.exists(file_name_json):
                print(f'[!!] ERROR: No test plans were found for app {app} (level:{level}), skipping.', file=sys.stderr)
                continue

            parse_json(file_name_json, threshold, names_json,
                       names_cat_1, names_cat_under, names_cat_over, names_cat_col, verbose & False)
            file_name_xml = os.path.join(output_dir, f'{app}/{any_conf}/{any_conf}-{combination}-tests/merged_jacoco.xml')
            if not os.path.exists(file_name_xml):
                if verbose:
                    print(f'Skipping combination {combination} for app {app} (either dir or jacoco do not exist)')
                continue

            line = [app, combination, level]
            line_stats = [app, combination, level]
            for target in target_coverage:
                names_xml, names_with_counters = [], []
                parse_xml(file_name_xml, names_xml, names_with_counters, target, verbose & False)

                names_cat_0 = subtract(names_xml, names_json)
                categories = {'0': names_cat_0, '1': names_cat_1,
                              'under': names_cat_under, 'over': names_cat_over, 'col': names_cat_col}

                cat_sums = {'0': [0, 0], '1': [0, 0], 'under': [0, 0], 'over': [0, 0], 'col': [0, 0]}

                for method_name, cls_name, covered, missed, _ in names_with_counters:
                    for cat in categories:
                        if (method_name, cls_name) in categories[cat]:
                            cat_sums[cat][0] += int(covered)
                            cat_sums[cat][1] += int(missed)

                cat_coverage = dict()
                covered, total = 0, 0
                for cat in cat_sums:
                    if cat_sums[cat][0] + cat_sums[cat][1] > 0:
                        covered += cat_sums[cat][0]
                        total += cat_sums[cat][0] + cat_sums[cat][1]
                        cat_coverage[cat] = round(float(cat_sums[cat][0]) /
                                                  float(cat_sums[cat][0] + cat_sums[cat][1]), precision)
                    else:
                        cat_coverage[cat] = np.nan

                if total > 0:
                    total_coverage = round(float(covered) / total, precision) if total > 0 else np.nan
                else:
                    total_coverage = np.nan
                line.extend([*cat_coverage.values(), total_coverage])
                line_stats.extend([*cat_sums.values(), total])

                app_counters[target] = [u + v for u, v in cat_sums.values()] + [total]
                total_counters[target] = [app_counters[target][i] + total_counters[target][i] for i in range(cat_count)]
                level_counters[target] = [app_counters[target][i] + level_counters[target][i] for i in range(cat_count)]
                app_percents[target] = [((float(app_counters[target][i]) / app_counters[target][cat_count - 1])
                                        if app_counters[target][cat_count - 1] > 0 else 0) for i in range(cat_count)]
                total_percents[target] = [app_percents[target][i] + total_percents[target][i] for i in range(cat_count)]
                level_percents[level][target] = [app_percents[target][i] + level_percents[level][target][i] for i in range(cat_count)]

            result.append(line)
            stats.append(line_stats)
            print('(r)', result[-1])
            print('(s)', stats[-1])

            stats.append([app, '*', level,
                          *[f'{app_counters[target][i]} ({app_percents[target][i] * 100:.2f}%)'
                            for i in range(cat_count) for target in target_coverage]])
            print('(s)', stats[-1])

        for target in target_coverage:
            if level_percents[level][target][cat_count - 1] > 0:
                level_percents[level][target] = [percent / level_percents[level][target][cat_count - 1] for percent in level_percents[level][target]]
            stats.append(['*', '*', level, *[f'{level_counters[target][i]} ({level_percents[level][target][i] * 100:.2f}%)' for i in range(cat_count)]])
            print('(s)', stats[-1])
            if total_percents[target][cat_count - 1] > 0:
                total_percents[target] = [percent / total_percents[target][cat_count - 1]
                                          for percent in total_percents[target]]
        stats.append(
            ['*', '*', '*', *[f'{total_counters[target][i]} ({total_percents[target][i] * 100:.2f}%)'
                              for i in range(cat_count) for target in target_coverage]])
        print('(s)', stats[-1])

    # histogram (bar graph) for per-level percents
    for target in target_coverage:
        granularity = target.lower()
        granularity = granularity + ('es' if granularity == 'branch' else 's')
        all_levels = []
        for level in levels:
            all_levels.extend(zip([level] * (cat_count - 1), level_percents[level][target][:cat_count - 1]))
        df = pd.DataFrame(all_levels)
        df.columns = ['Interaction Level', 'Percentage']
        df['Category'] = ['0', '1', 'under', 'over', 'collection'] * len(levels)
        bars = px.bar(df.astype({'Interaction Level': 'str'}),
                      # title=f'Percentage of {granularity} per category',
                      x='Category', y='Percentage', color='Interaction Level').update_layout(
            font=dict(
                size=18,
            ),
            barmode='group',
            showlegend=False,
            yaxis=dict(
                tickmode='linear',
                tick0=0,
                dtick=0.05,
                tickformat=',.1%',
                # range=[0, 1],
            ),
        ).update_traces(
            texttemplate='%{y}',
            textposition='auto',  # or 'outside'
        )
        # df.to_csv(os.path.join(output_dir, f'{categorizer_output}_histogram_{target.lower()}.csv'))
        bars.write_image(os.path.join(output_dir, 'images', f'{categorizer_output}_histogram_{target.lower()}.png'), format='png')
        print(f"Written histogram image {os.path.join(output_dir, 'images', f'{categorizer_output}_histogram_{target.lower()}.png')}")

    with open(os.path.join(output_dir, categorizer_output + '_histogram.csv'), 'w', newline='') as f:
        writer = csv.writer(f)
        writer.writerows(result)

    with open(os.path.join(output_dir, categorizer_output + '_histogram_stats.csv'), 'w', newline='') as f:
        writer = csv.writer(f)
        writer.writerows(stats)

    return result


def categorize_separate(app, config):
    time_limits, levels, trials, output_dir, categorizer_output, combs = \
        config['general']['time_limits'], config['general']['levels'], \
        config['general']['trials'], config['general']['output_dir'], config['general']['categorizer_output'], \
        config['general']['combs']
    verbose, precision = config['verbosity']['verbose'], config['verbosity']['precision']
    threshold, target_coverage = config['categorize']['threshold'], config['categorize']['target_coverage']

    if verbose:
        print(f'Running categorizer ***per-app*** with threshold = {threshold} and target coverage = {target_coverage}')

    header = ['App', 'Combination', 'Interaction Level']
    for target in target_coverage:
        target = target.title()
        header.extend([f'{target} coverage ({cat})' for cat in ['0', '1', 'under', 'over', 'col', 'total']])

    result = [header]
    stats = [[title.replace('coverage', 'counter') for title in header]]
    print(result[0])

    cat_count = len(['0', '1', 'under', 'over', 'col', 'total'])

    for level in levels:
        level_counters, level_percents = [0] * cat_count, [0] * cat_count
        app_counters = None
        for combination in combs:
            any_conf = f'{app}_{time_limits[0]}s_{level}l_{trials[0]}'
            if verbose:
                print(f'CATEGORIZER: app={app}, any_conf={any_conf}, combination={combination}')
            names_json, names_cat_1, names_cat_under, names_cat_over, names_cat_col = [], [], [], [], []
            file_name_json = os.path.join(output_dir, f'{app}/{any_conf}/{any_conf}_ctd_models_and_test_plans.json')
            if not os.path.exists(file_name_json):
                print(f'[!!] ERROR: No test plans were found for app {app}, skipping.', file=sys.stderr)
                return None
            parse_json(file_name_json, threshold, names_json,
                       names_cat_1, names_cat_under, names_cat_over, names_cat_col, verbose & False)

            line = [app, combination, level]
            line_stats = [app, combination, level]

            file_name_xml = os.path.join(output_dir, f'{app}/{any_conf}/{any_conf}-{combination}-tests/merged_jacoco.xml')
            if not os.path.exists(file_name_xml):
                if verbose:
                    print(f'Skipping combination {combination} (either dir or jacoco do not exist)')
                continue
            for target in target_coverage:
                names_xml, names_with_counters = [], []
                parse_xml(file_name_xml, names_xml, names_with_counters, target, verbose & False)

                # if not len(names_xml):
                #     continue
                names_cat_0 = subtract(names_xml, names_json)
                categories = {'0': names_cat_0, '1': names_cat_1,
                              'under': names_cat_under, 'over': names_cat_over, 'col': names_cat_col}

                cat_sums = {'0': [0, 0], '1': [0, 0], 'under': [0, 0], 'over': [0, 0], 'col': [0, 0]}

                for method_name, cls_name, covered, missed, _ in names_with_counters:
                    for cat in categories:
                        if (method_name, cls_name) in categories[cat]:
                            cat_sums[cat][0] += int(covered)
                            cat_sums[cat][1] += int(missed)

                cat_coverage = dict()
                covered, total = 0, 0
                for cat in cat_sums:
                    if cat_sums[cat][0] + cat_sums[cat][1] > 0:
                        covered += cat_sums[cat][0]
                        total += cat_sums[cat][0] + cat_sums[cat][1]
                        cat_coverage[cat] = round(float(cat_sums[cat][0]) /
                                                  float(cat_sums[cat][0] + cat_sums[cat][1]), precision)
                    else:
                        cat_coverage[cat] = np.nan

                if total > 0:
                    total_coverage = round(float(covered) / total, precision)
                else:
                    total_coverage = np.nan
                line.extend([*cat_coverage.values(), total_coverage])
                line_stats.extend([*cat_sums.values(), total])

                if app_counters is None:
                    app_counters = [u + v for u, v in cat_sums.values()] + [total]

            result.append(line)
            stats.append(line_stats)
            print(result[-1])

        if app_counters is not None:
            level_counters = [app_counters[i] + level_counters[i] for i in range(cat_count)]
            app_percents = [((float(app_counters[i]) / app_counters[cat_count - 1])
                             if app_counters[cat_count - 1] > 0 else 0) for i in range(cat_count)]
            level_percents = [app_percents[i] + level_percents[i] for i in range(cat_count)]
            stats.append([app, '*', level, *[f'{app_counters[i]} ({app_percents[i] * 100:.2f}%)' for i in range(cat_count)]])

        if level_percents[cat_count - 1] > 0:
            level_percents = [percent / level_percents[cat_count - 1] for percent in level_percents]
        stats.append(['*', '*', level, *[f'{level_counters[i]} ({level_percents[i] * 100:.2f}%)' for i in range(cat_count)]])

    with open(os.path.join(output_dir, app, f'{app}_{categorizer_output}.csv'), 'w', newline='') as f:
        writer = csv.writer(f)
        writer.writerows(result)

    with open(os.path.join(output_dir, app, f'{app}_{categorizer_output}_stats.csv'), 'w', newline='') as f:
        writer = csv.writer(f)
        writer.writerows(stats)

    return result


if __name__ == '__main__':
    apps = sys.argv[1:]
    if apps:
        for app in apps:
            categorize_separate(app, {})
    histogramize({})
