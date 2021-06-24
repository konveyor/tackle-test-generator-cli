# Tackle-Test CLI User Guide

The core capability provided by the CLI is automated generation of unit test cases for Java classes. The tests can be generated using different tools/strategies, and with or without assertions. The generated tests can be executed in different ways: (1) via the CLI, (2) using `ant` or `maven`, or (3) in an IDE.

In this guide, we provide detailed description of the test-generation and test-execution capabilities, along with explanation of various configuration options that control the behavior of the CLI and the core test-generation engine.

## Test Generation

To perform test generation, the starting point is creation of the configuration file, containing options and values for configuring the behavior of test generation. This includes specifying the app under test, selecting the test-generation strategy, specifying assertion generation, and specifying time limits for test generation.

### Specifying the app under test

App name, classpath, and app library dependencies

For large apps, it might be a good practice to start with a limited scope of a few classes to get a feel for the tool

### Selecting the test-generation strategy

ctd-amplified subcommand with options: base test generator, ctd coverage, interaction level

randoop subcommand with options

evosuite subcommand with options

### Specifying assertion generation

There are two general types of assertions that can be automatically generated and added to the junit test cases. 

The first assertion type, used in the CTD-guided testing strategy, is differential (diff) assertions. Diff assertion generation 
records the created object states for each executed statement of the test case on the legacy app. It then adds them as assertions right after the statement. The final junit test cases hence contain `assertEquals`  
statements after each original statement of the test case that resulted in the creation of new objects. Diff assertion generation
is activated by default for CTD-guided testing and can be deactivated via the toml file option `generate.ctd_amplified.no_diff_assertions` 
or the flag `--no-diff-assertions` (short name: `-nda`).

The second assertion type, used in Randoop and EvoSuite testing strategies, is regression assertions. 
These assertions reflect the current behavior of the app, including exceptions occurred during test execution.
They are not activated by default, and are controlled via the toml file option `generate.add_assertions`.

### Controlling the time spent in test generation

`time-limit` option, `num_seq_execution` option

## Test Execution

The CLI `execute` command automatically creates a build script to compile the test cases, execute them, 
and create junit and (optionally) code coverage reports. Different configuration options exist to control the behavior of this command.

1. `build_type` - can be either `ant` (default) or `maven`. Indicates the type of build script that will be generated.
2. `code_coverage` - whether to create Jacoco code coverage reports for the executed test cases. Default is false.
3. `offline_instrumentation` - whether to use offline instrumentation for the code coverage collection. Default is false, 
                                meaning that if code coverage is collected, instrumentation will occur at class load using a Java agent.
4. `app_packages` - a list of prefixes of the app under test, to be used by Jacoco so that it reports coverage of the app 
                        under test only rather than also third party code
5. `test_class` - a class name to be tested, in case the user wants to execute a specific class only. 
Empty by default, in which case all classes targeted during test generation are executed.

Illustrate generated reports.
