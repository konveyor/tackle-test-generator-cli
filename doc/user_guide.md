# Tackle-Test CLI User Guide

The core capability provided by the CLI is automated generation of unit test cases for Java classes. The tests can be generated using different tools/strategies, and with or without assertions. The generated tests can be executed in different ways: (1) via the CLI, (2) using `ant` or `maven`, or (3) in an IDE.

In this guide, we provide detailed description of the test-generation and test-execution capabilities, along with explanation of various configuration options that control the behavior of the CLI and the core test-generation engine.

## Test Generation

To perform test generation, the starting point is creation of the configuration file, containing options and values for configuring the behavior of test generation. This includes specifying the app under test, selecting the test-generation strategy, specifying assertion generation, and specifying time limits for test generation.

### Specifying the app under test

The specification of the app under test is provided using the following configuration parameters:

1. `app_name`: The name of the application being tested. The value provided for `app_name` is used as prefix in the names of files and directories created by the test generator. For example, the directories containing the generated tests and test reports have the prefix `<app_name>-` and the CTD test-plan file has the prefix `<app_name>_`. The app name should thus not contain characters that are invalid in file/directory names.

2. `monolith_app_path`: A list of absolute or relative paths to directories containing application classes. The specified paths are used as Java `CLASSPATH` for purposes of loading application classes for analysis and test generation.
   
3. `app_classpath_file`. The name of a text file containing all library dependencies of the app under test. The file should contain a list of jar files (using relative or absolute paths). For example, see the [irs classpath file](../test/data/irs/irsMonoClasspath.txt).

### Selecting the test-generation strategy

The CLI implements three strategies for test generation: CTD-guided test generation, test generation using EvoSuite standalone, and test generation using Randoop standalone; these strategies are selected via the `generate` subcommands `ctd-amplified`, `evosuite`, and `randoop`, respectively.

CTD-guided test generation constructs a CTD model of each public method in the specified application classes, and generates a test plan from the model, where each row in the test plan---specifying a vector of types for the method parameters---becomes a coverage goal for test generation. Applying CTD at the method level, thus, results in a set of test plans that guide test generation. The CTD model for a method consists of a set of types for each formal parameter of the method; these types are subtypes of the declared parameter type and identified statically via type inference. Test generation operates in two steps. First, the test-generation engine runs EvoSuite and/or Randoop to create a set of base test cases from which it mines "building-block" test sequences and adds them to a sequence pool. Next, it iterates over the CTD test plans, and attempts to synthesize a covering test sequence for each row of a test plan by creating objects/values of the types specified in a row, and reusing sequences from the sequence pool.

CTD-guided test generation can be configured using the following parameters:

1. `base_test_generator`: The base test generator to use for creating the building-block test sequences: the values can be `evosuite` (EvoSuite only), `randoop` (Randoop only), or `combined` (both Evosuite and Randoop).

2. `ctd_coverage`: A boolean flag indicating whether to create the CTD coverage report during test generation. The coverage report shows for each test-plan row, whether it is covered, partially covered, or uncovered. A test-plan row is _covered_ if the test-generation engine is able to create a sequence calling the target method with parameter types specified in the row. Conversely, a test-plan row is uncovered if the engine is unable to create a sequence for the row. Partial coverage can occur in cases where one of the types in a row (i.e., a formal parameter of the target method) is a collection, map, or array type. In such cases, test plan also specifies a set of types to be added to the collection/map/array. If the test-generation engine is able to create only a subset of these specified types, the synthesized test sequence _partially covers_ the test-plan row.
   
3. `interaction_level`: CTD interaction level for test-plan generation. This option specifies the value of _n_ for _n-way_ interaction coverage. For example, the value `2` for `interaction_level` results in pair-wise testing, in which each pair of parameter types are included in the test plan.

To generate tests using EvoSuite in a standalone manner, the CLI provides the `generate evosuite` command. In this case, tests are generated directly by EvoSuite (without any CTD modeling), and test generation can be configured on the coverage criteria to be used by EvoSuite:

`criterion`: A list of coverage criteria to be used by EvoSuite; possible values are `LINE`, `BRANCH`, `EXCEPTION`, `WEAKMUTATION`, `OUTPUT`, `METHOD`, `METHODNOEXCEPTION`, `CBRANCH`, and `ALL`. For more details on EvoSuite, please see [EvoSuite documentation](https://github.com/EvoSuite/evosuite/wiki).

To generate tests using Randoop in a standalone manner, the CLI provides the `generate randoop` command. In this case, tests are generated directly by Randoop (without any CTD modeling), and test generation can be configured on whether error-revealing tests are generated:

`no_error_revealing_tests`: A boolean flag indicating that error-revealing tests should be generated by Randoop. For details on error-revealing tests, please see the [Randoop user manual](https://randoop.github.io/randoop/manual/index.html#error_revealing_tests).

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

## Best Practices and Troubleshooting Tips

1. Use the `-vb/--verbose` option, especially for test generation, because this prints out status messages that show progress during test generation/execution.

2. The CLI provides `-l/--log-level` option that enables detailed log messages to be printed at different logging levels (e.g., `INFO`, `WARNING`, `ERROR`). Enabling this option can help with troubleshooting in cases where the test generation or execution fails.

3. For large applications, it might be a good practice to start with a limited scope of a few classes to get a feel for the tool before performing test generation on all application classes. Limited-scope testing is also useful in cases where testing has to be focused on certain application classes, e.g., for validating changes made to those classes.
