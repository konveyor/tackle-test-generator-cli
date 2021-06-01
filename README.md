# Tackle-Test CLI

This repository contains a Python-based Command-line interface (CLI), `tkltest`, for the Tackle-Test
tooling for performing automated test generation and differential testing on two application versions
(in the context of application modernization). Currently, `tlktest` provides this capability for Java unit
testing, where unit test cases can be automatically generated on a given application version (the _base_ version)
and executed against a modified version to detect differences. 

The tool is integrated with two automated unit test-generation tools for Java: 
[Randoop](https://randoop.github.io/randoop/) and [EvoSuite](https://www.evosuite.org/). So tests can be
generated using either of these tools standalone or in combination with CTD modeling, which generates
test cases guided by a different coverage criterion that exercises methods based on combinations of different
types of method parameters. CTD-guided test generation creates a CTD model for each method by identifying
possible subtypes of each method parameter type using class-hierarchy analysis and rapid type analysis. From this
model, a CTD test plan is generated for a given interaction level or strength; the rows in the test plan become
coverage goals for the test generator. This type of test generation puts emphasis on type interactions
(or type-based testing), and applying CTD modeling enables the interaction levels to be controlled as well
as optimizes the number of coverage goals (and resulting test cases) at an interaction level.

`tkltest` also lets tests to be generated with or without assertions. In the case of EvoSuite and Randoop,
the assertions are generated by those tools. In the case of CTD amplification, differential assertions are
added by the test generator on states of objects observed during in-memory execution of test sequences.
Addition of assertions to tests makes the tests stronger in detecting behavioral differences between
two application versions, but it can also result in some false failures when expected differences in program state
occur between the versions. If assertions are not added, the only differences detected by the test cases
are those that cause the application to fail with runtime exceptions.

## Installation Prerequisites and Dependencies

1. Install JDK 8. The JDK home directory has to be specified as a configuration option;
   see the section [Configuration Options](#configuration-options).
   
2. Install Ant. The Ant executable must be in the path. Along with generating JUnit test cases,
   the CLI generates an Ant `build.xml` that can be used for building and running the generated tests.
   
3. Install Maven and download Java libraries using the script:
    ```buildoutcfg
    cd lib; ./download_lib_jars.sh 
    ```
    
    Windows users should run:
    
      ```buildoutcfg
    cd lib; download_lib_jars.sh 
    ```
    
    This downloads the Java libraries required by the CLI into the `lib/download` directory.

## Developer Installation of the CLI

To install the CLI command `tkltest` in a virtual environment, follow these steps:
```
python3 -m venv venv
source venv/bin/activate
pip install --editable .
```

Windows users should run:

```
python3 -m venv venv
venv\Scripts\activate.bat
pip install --editable .
```

This will load the command from the current folder so that you can continue to develop it and
make changes and just run the command without having to package and re-install it.

## Usage

`tkltest` provides different commands, along with options, for generating and running test cases.
`tkltest --help` shows the available commands and options.

```
usage: tkltest [-h] [-cf CONFIG_FILE] [-l {CRITICAL,ERROR,WARNING,INFO,DEBUG}]
               [-td TEST_DIRECTORY] [-vb] [-v]
               {config,generate,execute} ...

Command-line interface for generating and executing test cases on two
application versions and performing differential testing (currently supporting
Java unit testing)

positional arguments:
  {config,generate,execute}
    config              Initialize configuration file or list configuration
                        options
    generate            Generate test cases on the monolithic app version
    execute             Execute generated tests on the mono or micro app
                        version

optional arguments:
  -h, --help            show this help message and exit
  -cf CONFIG_FILE, --config-file CONFIG_FILE
                        path to TOML file containing configuration options
  -l {CRITICAL,ERROR,WARNING,INFO,DEBUG}, --log-level {CRITICAL,ERROR,WARNING,INFO,DEBUG}
                        logging level for printing diagnostic messages
  -td TEST_DIRECTORY, --test-directory TEST_DIRECTORY
                        directory containing generated test classes(space-
                        separated fully qualified class names)
  -vb, --verbose        run in verbose mode printing detailed status messages
  -v, --version         print CLI version number

```

To see the CLI in action on a sample Java application, set JAVA_HOME to the JDK installation
and run the command
```
tkltest --config-file ./test/data/irs/tkltest_config.toml --verbose generate ctd-amplified
```

## Quick Start Guide

We list the minimal steps required to use the tool for its two main functions: generating unit tests
and executing them.

1. Created an empty configuration file by running the command
   ```
   tkltest config init
   ````
   A file named `tkltest_config.toml` will be created in the working directory.

2. Assign values to the following configuration options in the configuration file
   (details on all configuration options are available [here](#configuration-options)):
   
   - `app_name`: name of the app under test (this name is used as prefix of file/directories created
     during test generation)
   
   - `app_classpath_file`: relative or absolute path to a text file containing library 
     dependencies of the app under test. For example, see [irs classpath file](test/data/irs/irsMonoClasspath.txt)
     
   - `monolith_app_path`: a list of paths (relative or absolute) to directories containing 
     app classes (jar files cannot be specified here). For example, see
     [daytrader toml spec](test/data/daytrader7/tkltest_config.toml#L6)
     
   - `app_packages`: a list of app package prefixes, with wildcards allowed. For example, see
     [daytrader toml spec](test/data/daytrader7/tkltest_config.toml#L63)

3. To generate test cases, run the command
   ```
   tkltest --verbose generate ctd-amplified
   ```
   The unit test cases will be generated in a folder named `<app-name>-ctd-amplified-tests/monolith`.
   A CTD coverage report will be created as well  in a folder named `<app-name>-tkltest-reports`, showing
   the CTD test plan row coverage achieved by the generated tests.

4. To execute the generated unit tests on the legacy app, run the command
   ```
   tkltest --verbose --test-directory <app-name>-ctd-amplified-tests execute mono
   ```
   JUnit reports and Jacoco code coverage reports will be created in  `<app-name>-tkltest-reports`.
 
Note that, if the `--config-file` option is not specified on the command line (as in the commands above),
the CLI uses by default `./tkltest_config.toml` as the configuration file.

## Generate Command

Generates JUnit test cases on the application. Currently, the supported sub-command of `generate`
is `ctd-amplified`, which performs CTD modeling and optimization over application classes to
compute coverage goals, and generates test cases to cover those  goals. CTD-guided test
generation can leverage either Randoop or EvoSuite for generating  initial or building-block
test sequences that are then extended for covering rows in the  CTD test plan.

By default, this sub-command generates diff assertions and adds them to the generated test cases.
To avoid adding assertions, use the `-nda/--no-diff-assertions` option.

``` 
usage: tkltest generate [-h] [-pf PARTITIONS_FILE]
                        {ctd-amplified,evosuite,randoop} ...

positional arguments:
  {ctd-amplified,evosuite,randoop}
    ctd-amplified       Use CTD for computing coverage goals
    evosuite            Use EvoSuite for generating a test suite
    randoop             Use Randoop for generating a test suite

optional arguments:
  -h, --help            show this help message and exit
  -pf PARTITIONS_FILE, --partitions-file PARTITIONS_FILE
                        path to file containing specification of partitions
```

## Execute Command

Executes generated JUnit test cases in the legacy or the refactored application. These two execution
modes are supported via the `execute mono` and `execute micro` sub-commands.

```
usage: tkltest execute [-h] [-cc] [-jr] [-ofli] [-rp REPORTS_PATH]
                       [-tc TEST_CLASS]
                       {mono,micro} ...

positional arguments:
  {mono,micro}
    mono                Execute tests on monolithic app version
    micro               Execute tests on refactored (microservice) app version

optional arguments:
  -h, --help            show this help message and exit
  -cc, --code-coverage  generate code coverage report with JaCoCo agent
  -jr, --junit-report   generate JUnit test results report
  -ofli, --offline-instrumentation
                        perform offline instrumentation of app classes for
                        measuring code coverage (default: app classes are
                        instrumented dynamically during class loading)
  -rp REPORTS_PATH, --reports-path REPORTS_PATH
                        path to the reports directory
  -tc TEST_CLASS, --test-class TEST_CLASS
                        path to a test class file (.java) to compile and run
```

`execute mono` requires specifications of the legacy app classes as well as the library
dependencies of the app.

`execute micro` requires the app library dependencies to be specified along with the
classpaths for each partition of the refactored app. `execute micro` also requires the
partitions to be running for test execution because the execution of a unit test on
a proxy class results in service calls being made across partitions. To support this,
`execute micro` requires specification of the docker-compose file for the transformed
app, along with information about environment variables that need to be set for
test execution on each partition. `execute micro` can run tests on all partitions or
on a specified list of partitions. Also, the command can optionally start partition
containers before test execution and optionally stop partition containers after test
execution.

## Known Tool Issues

1. On apps with native UI (e.g., swing), the tool can sometimes get stuck during sequence execution
   (even though it uses a Java agent for replacing calls to GUI components); as a workaround,
   users can exclude UI-related classes from the set of test targets.

2. Coverage in JEE apps could be low because of limited JEE mocking support.

## Configuration Options

All configuration options for `tkltest` commands  can be specified in a [toml](https://toml.io/en/)
file, containing sections for different commands and subcommands.

A subset of the configuration options can also be specified on the command line, as the
examples above illustrate. If an option is specified both on the command line
and in the configuration file, the command-line value overrides the configuration-file value.

The configuration file can be initialized via `tkltest config init`. `tkltest config list` lists
all available configuration options with information about each option: the option's TOML name,
the option's command-line short/long names (if it supported in the CLI), whether the option is
required, and the option description).

| TOML name ("*"=req, "^"=CLI-only)   | CLI name                         | Description                                                                                                                                         |
|-------------------------------------|----------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------|
| general                             |                                  |                                                                                                                                                     |
| app_name*                           |                                  | name of the application being tested                                                                                                                |
| app_classpath_file*                 |                                  | file containing paths to jar files that represent the library dependencies of app                                                                   |
| config_file^                        | -cf/--config-file                | path to TOML file containing configuration options                                                                                                  |
| log_level^                          | -l/--log-level                   | logging level for printing diagnostic messages                                                                                                      |
| monolith_app_path*                  |                                  | list of paths to application classes                                                                                                                |
| java_jdk_home*                      |                                  | root directory for JDK installation (must be JDK; JRE will not suffice); can be set as environment variable JAVA_HOME                               |
| test_directory                      | -td/--test-directory             | name of root test directory containing the generated JUnit test classes                                                                             |
| verbose                             | -vb/--verbose                    | run in verbose mode printing detailed status messages                                                                                               |
| version^                            | -v/--version                     | print CLI version number                                                                                                                            |
|                                     |                                  |                                                                                                                                                     |
| config                              |                                  | Initialize configuration file or list configuration options                                                                                         |
|                                     |                                  |                                                                                                                                                     |
| config.init                         |                                  | Initialize configuration options and print (in TOML format) to file or stdout                                                                       |
| file^                               | -f/--file                        | name of TOML file to create with initialized configuration options                                                                                  |
|                                     |                                  |                                                                                                                                                     |
| config.list                         |                                  | List all configuration options with description                                                                                                     |
|                                     |                                  |                                                                                                                                                     |
| generate                            |                                  | Generate test cases on the monolithic app version                                                                                                   |
| add_assertions                      |                                  | add assertions in evosuite/randoop-generated tests                                                                                                  |
| jee_support                         |                                  | add support JEE mocking in generated tests cases                                                                                                    |
| partitions_file                     | -pf/--partitions-file            | path to file containing specification of partitions                                                                                                 |
| target_class_list                   |                                  | list of target classes to perform test generation on                                                                                                |
| time_limit                          |                                  | time limit (in seconds) for evosuite/randoop test generation                                                                                        |
|                                     |                                  |                                                                                                                                                     |
| generate.ctd_amplified              |                                  | Use CTD for computing coverage goals                                                                                                                |
| base_test_generator                 | -btg/--base-test-generator       | base test generator to use for creating building-block test sequences                                                                               |
| ctd_coverage                        | -ctd/--ctd-coverage              | generate CTD coverage report                                                                                                                        |
| no_diff_assertions                  | -nda/--no-diff-assertions        | do not add assertions for differential testing to the generated tests                                                                               |
| interaction_level                   |                                  | CTD interaction level (strength) for test-plan generation                                                                                           |
| num_seq_executions                  |                                  | number of executions to perform to determine pass/fail status of generated sequences                                                                |
| refactored_app_path_prefix*         |                                  | path prefix to root directory of refactored app version                                                                                             |
| refactored_app_path_suffix*         |                                  | list of paths to refactored app classes                                                                                                             |
|                                     |                                  |                                                                                                                                                     |
| generate.evosuite                   |                                  | Use EvoSuite for generating a test suite                                                                                                            |
| criterion                           |                                  | coverage criterion for evosuite                                                                                                                     |
|                                     |                                  |                                                                                                                                                     |
| generate.randoop                    |                                  | Use Randoop for generating a test suite                                                                                                             |
| no_error_revealing_tests            |                                  | do not generate error-revealing tests with randoop                                                                                                  |
|                                     |                                  |                                                                                                                                                     |
| execute                             |                                  | Execute generated tests on the mono or micro app version                                                                                            |
| app_packages*                       |                                  | list of app packages (wildcard allowed in names)                                                                                                    |
| code_coverage                       | -cc/--code-coverage              | generate code coverage report with JaCoCo agent                                                                                                     |
| junit_report                        | -jr/--junit-report               | generate JUnit test results report                                                                                                                  |
| offline_instrumentation             | -ofli/--offline-instrumentation  | perform offline instrumentation of app classes for measuring code coverage (default: app classes are instrumented dynamically during class loading) |
| reports_path                        | -rp/--reports-path               | path to the reports directory                                                                                                                       |
| test_class                          | -tc/--test-class                 | path to a test class file (.java) to compile and run                                                                                                |
|                                     |                                  |                                                                                                                                                     |
| execute.mono                        |                                  | Execute tests on monolithic app version                                                                                                             |
|                                     |                                  |                                                                                                                                                     |
| execute.micro                       |                                  | Execute tests on refactored (microservice) app version                                                                                              |
| docker_compose_file*                | -dcf/--docker-compose-file       | docker compose file for running the partitions of the refactored app                                                                                |
| partitions                          | -p/--partitions                  | refactored app partitions to run tests on (space-separated partition names); if omitted, tests are run on all partitions                            |
| run_partition_containers            | -rpc/--run-partition-containers  | run partition containers before running tests                                                                                                       |
| stop_partition_containers           | -spc/--stop-partition-containers | stop partition containers after running tests                                                                                                       |
|                                     |                                  |                                                                                                                                                     |
