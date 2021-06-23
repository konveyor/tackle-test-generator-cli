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



Explain execution options using the CLI: build type, code coverage, offline instrumentation,
app packages, reports path, test class

Illustrate generated reports.
