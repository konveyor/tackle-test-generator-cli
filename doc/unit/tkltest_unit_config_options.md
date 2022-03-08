## TackleTest-Unit Configuration Options

All configuration options for `tkltest-unit` commands  can be specified in a [TOML](https://toml.io/en/)
file, containing sections for different commands and subcommands.

A subset of the configuration options can also be specified on the command line.
If an option is specified both on the command line
and in the configuration file, the command-line value overrides the configuration-file value.

The configuration file can be initialized via `tkltest-unit config init`. `tkltest-unit config list` lists
all available configuration options with information about each option: the option's TOML name,
the option's command-line short/long names (if it supported in the CLI), whether the option is
required, and the option description).

| TOML name ("*"=req, "^"=CLI-only)   | CLI name                           | Description                                                                                                                             |
|-------------------------------------|------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------|
| general                             |                                    |                                                                                                                                         |
| app_name*                           |                                    | name of the application being tested                                                                                                    |
| app_classpath_file                  |                                    | file containing paths to jar files that represent the library dependencies of app. Required only if app_build_files is not given.       |
| config_file^                        | -cf/--config-file                  | path to TOML file containing configuration options                                                                                      |
| log_level^                          | -l/--log-level                     | logging level for printing diagnostic messages; options are CRITICAL, ERROR, WARNING, INFO, DEBUG                                       |
| monolith_app_path                   |                                    | list of paths to application classes.Required only if app_build_files is not given.                                                     |
| java_jdk_home*                      |                                    | root directory for JDK installation (must be JDK; JRE will not suffice); alternatively, can be set as environment variable JAVA_HOME    |
| test_directory                      | -td/--test-directory               | name of root test directory containing the generated JUnit test classes                                                                 |
| reports_path                        | -rp/--reports-path                 | path to the reports directory                                                                                                           |
| verbose                             | -vb/--verbose                      | run in verbose mode printing detailed status messages                                                                                   |
| version^                            | -v/--version                       | print CLI version number                                                                                                                |
| offline_instrumentation             | -offli/--offline-instrumentation   | perform offline instrumentation of app classes for measuring code coverage (default: app classes are instrumented online)               |
| build_type                          | -bt/--build-type                   | build file type for compiling and running the tests: ant, maven, or gradle                                                              |
| max_memory_for_coverage             | -mam/--maximal-memory-for-coverage | maximal heap size (in MB) used for obtaining coverage data                                                                              |
|                                     |                                    |                                                                                                                                         |
| config                              |                                    | Initialize configuration file or list configuration options                                                                             |
|                                     |                                    |                                                                                                                                         |
| config.init                         |                                    | Initialize configuration options and print (in TOML format) to file or stdout                                                           |
| file^                               | -f/--file                          | name of TOML file to create with initialized configuration options                                                                      |
|                                     |                                    |                                                                                                                                         |
| config.list                         |                                    | List all configuration options with description                                                                                         |
|                                     |                                    |                                                                                                                                         |
| generate                            |                                    | Generate test cases on the application under test                                                                                       |
| jee_support                         |                                    | add support JEE mocking in generated tests cases                                                                                        |
| no_diff_assertions                  | -nda/--no-diff-assertions          | do not add assertions for differential testing to the generated tests                                                                   |
| partitions_file                     | -pf/--partitions-file              | path to file containing specification of partitions                                                                                     |
| target_class_list                   |                                    | list of target classes or packages to perform test generation on; packages must end with a wildcard                                     |
| excluded_class_list                 |                                    | list of classes or packages to exclude from test generation; packages must end with a wildcard                                          |
| time_limit                          |                                    | time limit per class (in seconds) for evosuite/randoop test generation                                                                  |
| app_build_type                      |                                    | build type for collecting app dependencies: ant, maven, or gradle                                                                       |
| app_build_files                     |                                    | list of paths to app build files for the specified app build type                                                                       |
| app_build_settings_files            |                                    | list of paths to app build settings files or property files for the specified app build type                                            |
| app_build_target                    |                                    | Name of the Ant target that is being used to build the app from the build file; required only for apps that use an Ant build file       |
| bad_path                            | -bp/--bad-path                     | Generate also bad path tests; assertions will validate that the exception observed during generation is thrown also during execution    |
|                                     |                                    |                                                                                                                                         |
| generate.ctd_amplified              |                                    | Use CTD for computing coverage goals                                                                                                    |
| base_test_generator                 | -btg/--base-test-generator         | base test generator to use for creating building-block test sequences                                                                   |
| no_augment_coverage                 | -nac/--no-augment-coverage         | do not augment CTD-guided tests with coverage-increasing base tests                                                                     |
| no_ctd_coverage                     | -nctd/--no-ctd-coverage            | do not generate CTD coverage report                                                                                                     |
| interaction_level                   |                                    | CTD interaction level (strength) for test-plan generation                                                                               |
| num_seq_executions                  |                                    | number of executions to perform to determine pass/fail status of generated sequences                                                    |
| refactored_app_path_prefix          |                                    | path prefix to root directory of refactored app version                                                                                 |
| refactored_app_path_suffix          |                                    | list of paths to refactored app classes                                                                                                 |
| reuse_base_tests                    | -rbt/--reuse-base-tests            | reuse existing base test cases                                                                                                          |
|                                     |                                    |                                                                                                                                         |
| generate.evosuite                   |                                    | Use EvoSuite for generating a test suite                                                                                                |
| criterion                           |                                    | coverage criterion for evosuite                                                                                                         |
|                                     |                                    |                                                                                                                                         |
| generate.randoop                    |                                    | Use Randoop for generating a test suite                                                                                                 |                                                                                    |
|                                     |                                    |                                                                                                                                         |
| execute                             |                                    | Execute generated tests on the application version under test                                                                           |
| app_packages*                       |                                    | list of app packages; must end with a wildcard                                                                                          |
| create_build_file                   | -nbf/--no-build-file-creation      | whether to generate build files; if set to false, a build file (of type set in build_type option) should already exist and will be used |
| code_coverage                       | -cc/--code-coverage                | generate code coverage report with JaCoCo agent                                                                                         |
| test_class                          | -tc/--test-class                   | path to a test class file (.java) to compile and run                                                                                    |
| combine_modules_coverage_reports    |                                    | when test suites are generated per module, create a combined coverage report                                                            |
|                                     |                                    |                                                                                                                                         |
| dev_tests                           |                                    | information about developer-written test suite                                                                                          |
| build_type*                         |                                    | build type for compiling and running the developer-written test suite: ant, maven, or gradle                                            |
| build_file*                         |                                    | path to build file for compiling and running the developer-written test suite                                                           |
| build_targets*                      |                                    | list of build targets for compiling and running the developer-written test suite                                                        |
| coverage_exec_file                  |                                    | the path to the Jacoco coverage .exec file, generated by the developer-written build file                                               |
| compare_code_coverage               |                                    | create a code coverage report that compares between the automatically generated test suite and the developer-written test suite         |
| use_for_augmentation                |                                    | when augmenting with evosuite tests, consider developer-written test suite coverage                                                     |
| coverage_threshold                  |                                    | classes with developer-written instruction coverage percentage higher than the threshold are excluded from test generation              |
