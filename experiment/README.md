
# Experiments

This README describes the experiment scripts required to run the TackleTest tool on the SF110 benchmark.

## Prerequisites

The evaluation scripts are part of the [TackleTest CLI repo](https://github.com/konveyor/tackle-test-generator-cli), 
and it is required as a whole to run these experiments.

In the directory containing the benchmark apps, it is expected that every subdirectory is for an app, and both a `classes` subdirectory and a `classpath.txt` file should exist in each app directory.

## Cycles

Experimentation works in 7 cycles:

1. **Generation** of test suites (CTD-guided, EvoSuite, Randoop)
2. **Execution** of test suites
3. **Sampling** to get to equal-sized test suites
4. **Execution** of sampled test suites
5. JaCoCo **Coverage XML** file generation
6. **Categorization** of coverage results by method categories
7. **Analysis** and plotting coverage results for each app

Once data for enough apps is collected, 2 more steps can then be appended:

1. Histogram generation of method categories (**histogramization**)
2. Box-plotting coverage results for all data (**coverage accumulation**)

## Usage

The script `tkltest_entry.py` is the main script.
It can be run from the command-line with different options and configurations, as follows.

If we wish to run the app `1_tullibee` 
with time limit `60` and interaction level `3`, 
repeating the trial `5` times and sampling (only one trial) `10` times, 
we would use:
```shell
python3 tkltest_entry.py -t 60 -l 3 -n 5 -s 10 -a path/to/apps 1_tullibee
```

If we desire to run only part of the above cycles, or skip specific cycles, 
we can use the (mutually exclusive) flags `-O (--only)` and `-S (--skip)`,
which take values from the set
`{generate|execute|sample|execute-samples|coverage-xml|categorize|analyze}`.
For example, we can run:
```shell
python3 tkltest_entry.py 1_tullibee -O generate sample
```

Here you can read about all the other possible flags and the default values
(alternatively, you can run`python3 tkltest_entry.py --help`):

    usage: tkltest_entry.py [-h] [-n NUM_TRIALS] [-t TIME_LIMITS [TIME_LIMITS ...]] [-l LEVELS [LEVELS ...]] [-o OUTPUT_DIR] [-a APPS_DIR] [-s SAMPLING_TRIALS] [--categorizer-output CATEGORIZER_OUTPUT] [-q] [-Q]
                            [--precision PRECISION] [-R] [--generate_standalone] [--sampling-clean] [--sampling-skip-generate-xml] [--threshold THRESHOLD]
                            [--target-coverage {instruction,branch,method} [{instruction,branch,method} ...]] [--analysis-wide-images] [--analysis-full-yaxis] [--analysis-no-boxplot-pts] [--analysis-no-plots-per-app]
                            [--analysis-no-checkpoint] [--analysis-no-categories] [--analysis-images-dir ANALYSIS_IMAGES_DIR] [--force-generate] [--force-sample] [--force-categorize] [--force-analyze]
                            [-S {generate,execute,sample,execute-samples,coverage-xml,categorize,analyze} [{generate,execute,sample,execute-samples,coverage-xml,categorize,analyze} ...] | -O
                            {generate,execute,sample,execute-samples,coverage-xml,categorize,analyze} [{generate,execute,sample,execute-samples,coverage-xml,categorize,analyze} ...]] [-H] [-C]
                            [apps [apps ...]]
    
    Run TackleTest Experiments
    
    positional arguments:
      apps                  Apps to run on (default: None)
    
    optional arguments:
      -h, --help            show this help message and exit
      -n NUM_TRIALS, --trials NUM_TRIALS
                            Number of trials for each configuration (default: 5)
      -t TIME_LIMITS [TIME_LIMITS ...], --time-limits TIME_LIMITS [TIME_LIMITS ...]
                            Time limits to run with (default: [10, 30, 60])
      -l LEVELS [LEVELS ...], --levels LEVELS [LEVELS ...]
                            Interaction levels to run with (default: [1, 2, 3])
      -o OUTPUT_DIR, --output-dir OUTPUT_DIR
                            Output directory to move results into (default: output)
      -a APPS_DIR, --apps-dir APPS_DIR
                            Directory where the apps lie (classes dir and classpath file must be right under every app name) (default: tmp_apps)
      -s SAMPLING_TRIALS, --sampling-trials SAMPLING_TRIALS
                            Number of sampling trials (default: 10)
      --categorizer-output CATEGORIZER_OUTPUT
                            Name of categorizer output file (default: categories)
      -q, --silent          Less verbose messages (default: False)
      -Q, --silent-execute  No verbose during execution (default: False)
      --precision PRECISION
                            Number of decimal places in figures (default: 3)
      -R, --reuse-base-tests
                            Reuse base tests during generation (default: False)
      --generate_standalone
                            Generate standalone EvoSuite and Randoop test suites (default: False)
      --sampling-clean      Clean existing sampled test suites before sampling (default: False)
      --sampling-skip-generate-xml
                            Do not generate JaCoCo XML summary file for the sampled test suites (default: False)
      --threshold THRESHOLD
                            Threshold between 'under' (incl.) and 'over' categories (default: 3)
      --target-coverage {instruction,branch,method} [{instruction,branch,method} ...]
                            Coverage type to gather categorized data for (default: ['instruction', 'branch', 'method'])
      --analysis-wide-images
                            Make output images wide during analysis (default: None)
      --analysis-full-yaxis
                            Make y-axis values [0, 1] during analysis (default: None)
      --analysis-no-boxplot-pts
                            Hide boxplot points during analysis (default: None)
      --analysis-no-plots-per-app
                            Do not plot images per-app during analysis (default: False)
      --analysis-no-checkpoint
                            Override saved checkpoint file during analysis (default: False)
      --analysis-no-categories
                            Do not apply per-category analysis (default: False)
      --analysis-images-dir ANALYSIS_IMAGES_DIR
                            Name of directory to put analysis plots in (default: images)
      --force-generate      Redo generation even if done already (default: False)
      --force-sample        Redo sampling even if done already (default: False)
      --force-categorize    Redo categorization even if done already (default: False)
      --force-analyze       Redo analysis even if done already (default: False)
      -S {generate,execute,sample,execute-samples,coverage-xml,categorize,analyze} [{generate,execute,sample,execute-samples,coverage-xml,categorize,analyze} ...], --skip {generate,execute,sample,execute-samples,coverage-xml,categorize,analyze} [{generate,execute,sample,execute-samples,coverage-xml,categorize,analyze} ...]
                            Skip those steps (default: None)
      -O {generate,execute,sample,execute-samples,coverage-xml,categorize,analyze} [{generate,execute,sample,execute-samples,coverage-xml,categorize,analyze} ...], --only {generate,execute,sample,execute-samples,coverage-xml,categorize,analyze} [{generate,execute,sample,execute-samples,coverage-xml,categorize,analyze} ...]
                            Execute only those steps (default: None)
      -H, --histogramize    Output a histogram for the categories in all apps (default: False)
      -C, --cumulate-coverage
                            Output accumulated coverage data for all apps (default: False)

## Results

The test suites should be generated in the output directory of your choice 
(`-o (--output-dir)`, defaulting to `output`). 
There, too, in a subdirectory (specified by `--analysis-images-dir`, defaulting to `images`),
you can find all the evaluation plots.
The plots are split into 3 sets:
1. Combinations (show to what measure the CTD-guided tests amplify code coverage) at `images/combinations`
2. Singles (compare the test-suites separately) at `images/singles`
3. Samples (compare the equal-sized sampled test-suites) at `images/samples`

By default, in each of these sets, there are both `per-app` line plots and `all-apps` box-plots, 
each in a separate subdirectory (e.g. `images/samples/all-apps`).
Finally, in every such subdirectory, plots for the different method categories exist too (unless `--analysis-no-categories` is set).
For instance, the plot for instruction coverage of methods in category '1' of app `1_tullibee` is found in `output/images/*/per-app/1_tullibee_instruction_coverage_1.png`. 

For more information on method categorization, please refer to our paper,
*TackleTest: A Tool for Amplifying Test Generation via Type-Based Combinatorial Coverage*.

### Interaction coverage and exceptions

Unless the CLI option `no_ctd_coverage` is set (it is not, by default), a report for **CTD interaction coverage** will be generated for each app configuration.
To generate a plot describing interaction coverage for all apps, 
please use the method `plot_oc(time_limit, trial, config)` in the script `tkl_coverage_analysis.py` 
(you will first need to run the main script `tkltest_entry.py` once to generate a global `parsed_config.toml` file that is required for those methods).

For generating information on the **exceptions encountered during generation**, an initial step is required: 
We have supplied a Bash script called `fetch_bb_exceptions.sh`. This generates a CSV file called `exceptions_summary.csv`, 
which contains all exceptions encountered in the EvoSuite and Randoop test suites.
This file can then be given to methods in `tkl_coverage_analysis.py` to plot the histogram of exceptions.

## The SF110 Benchmark

To try out the benchmark used in the paper, the SF110 benchmark, first please download the binaries from the [EvoSuite website here](https://www.evosuite.org/experimental-data/sf110/).
We have supplied two Bash scripts, `tkltest_run_59.sh` and `tkltest_run_35.sh`.
The first script runs the evaluation on 59 apps (called _SF110<sub>59</sub>_ in the paper) with 3 time limits, 3 interaction levels, and 5 trials each.
The second script runs the evaluation on 35 apps (_SF110<sub>35</sub>_) with 3 time limits, 3 interaction levels and 5 trials, where one trial is sampled 10 times.

You can find the app names used for _SF110<sub>59</sub>_ and _SF110<sub>35</sub>_ inside the respective Bash scripts.

The reasons for passing over the other apps vary: some apps were developed without a package structure, 
on which Randoop fails to run; some do not compile; some face execution errors, most notably errors triggered by Abstract Window Toolkit (\texttt{java.awt}); 
some take too much time during generation; and some are incompatible with TackleTest for other reasons.
