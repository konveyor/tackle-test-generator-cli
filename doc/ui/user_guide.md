# TackleTest-UI User Guide

1. [Overview](#overview)
2. [Test Generation](#test-generation)
    - [Generation Output Artifacts](#generated-artifacts)
3. [Test Execution](#test-execution)
4. [Best Practices and Troubleshooting Tips](#best-practices-and-troubleshooting-tips)
5. [Configuration Options](tkltest_ui_config_options.md)

## Overview

*TackleTest-UI* supports automatic generation of end-to-end test cases for web applications. These test cases
exercise the application under test via its user interface and drive execution through different application
tiers. Some of the features of TackleTest-UI include

- Model-based generation of test cases for web applications: infers state-transition model of the webapp under test and generated test cases from paths in the model 
- Integration with the state-of-the-art web-crawling tool [Crawljax](https://github.com/crawljax/crawljax)
- Generation two test suites using the TestNG framework: one using Crawljax API and the other using Selenium API

The web crawler randomly traverses navigation paths through the app's web UI; although it mimics user interaction with the application, the crawling may not 
necessarily be similar to the way a user would navigate the application, i.e., via meaningful functional flows or use cases.

``` 
usage: tkltest-ui [-h] [-u APP_URL] [-cf CONFIG_FILE]
                  [-l {CRITICAL,ERROR,WARNING,INFO,DEBUG}]
                  [-td TEST_DIRECTORY] [-vb] [-v]
                  {config,generate,execute} ...

Command-line interface for generating and executing UI test cases for web
applications

positional arguments:
  {config,generate,execute}
    config              Initialize configuration file or list configuration
                        options
    generate            Generate UI test cases on the application under test
    execute             Execute generated UI tests on the application under
                        test

optional arguments:
  -h, --help            show this help message and exit
  -u APP_URL, --app-url APP_URL
                        URL where application under test is deployed
  -cf CONFIG_FILE, --config-file CONFIG_FILE
                        path to TOML file containing configuration options
  -l {CRITICAL,ERROR,WARNING,INFO,DEBUG}, --log-level {CRITICAL,ERROR,WARNING,INFO,DEBUG}
                        logging level for printing diagnostic messages
  -td TEST_DIRECTORY, --test-directory TEST_DIRECTORY
                        name of root test directory containing the generated
                        JUnit test classes
  -vb, --verbose        run in verbose mode printing detailed status messages
  -v, --version         print CLI version number
``` 

## Test Generation

Test generation is supported by the `tkltest-ui generate` command. Currently, this command invokes CrawlJax
which performs model-based test generation. CrawlJax automatically crawls the application via its DOM interface, 
and creates a state transition model based on the crawled paths. The test cases are derived from the model by
enumerating the model paths. The resulting test cases are using the TestNG framework. They are exported in 
two different versions: using CrawlJax API and using Selenium API only. Test cases in Selenium API will be generated in a folder named `tkltest-output-ui-<app-name>/<app-name>_localhost_<time-limit>mins/localhost/crawl##/selenium-api-tests/src`. 
Each `crawl##` folder contains output of a different run. Your latest run will be in the `crawl` folder with the largest number. 
These test cases depend on Selenium only, and not on CrawlJax. In addition, test cases in CrawlJax API will be generated in `tkltest-output-ui-<app-name>/<app-name>_localhost_<time-limit>mins/localhost/crawl##/src`

To perform test generation, the starting point is creation of the configuration file, containing options and values for configuring the behavior of test generation. 
This includes specifying the app under test, specifying input data, and specifying time limit for test generation.

``` 
usage: tkltest-ui generate [-h] [-b {chrome,chrome_headless}] [-t TIME_LIMIT]

optional arguments:
  -h, --help            show this help message and exit
  -b {chrome,chrome_headless}, --browser {chrome,chrome_headless}
                        browser on which to launch app under test for crawling
                        and test generation; default is chrome_headless
  -t TIME_LIMIT, --time-limit TIME_LIMIT
                        maximum crawl time (in minutes); default is 5 min
``` 

### Specifying the app under test

The specification of the app under test is provided using the following configuration parameters:

1. `app_name`: The name of the application being tested. The value provided for `app_name` is used as prefix in the names of files and directories created by the test generator. 

2. `app_url`: URL where application under test is deployed. Note that you will need to have a running instance of the application 
   at the provided url.

For details of additional parameters for configuring test generation, see [main configuration options](./tkltest_ui_config_options.md#main-configuration-options).
   
### Specifying input data

Specification of input data is optional. If you do not specify input data, random data will 
be used. However, in such cases, the crawling will not be able to progress
forme that perform login/authentication, or will otherwise get error responses by submitting
forms with invalid input values. This can limit the exploration that the crawler can perform.
Therefoire, it is advised  to specify form data, at the very least for forms that are critical for
proceeding in navigation, such as login forms.

Form data is specified in a separate configuration file in TOML format. The location of the 
form data specification file should be provided in the `form_data_spec_file` option in the 
main configuration file.

For details on how to specify form data, see [form data specification](./tkltest_ui_config_options.md#Form-data-specification).

For an example specification, see the [form data spec for Petclinic webapp](../../test/ui/data/petclinic/tkltest_ui_formdata_config.toml).
 
### Specifying constraints on clickable elements

Clickables are web elements that can be clicked to trigger actions that transform the 
web application from one state to another. Complex applications typically have a huge 
space of navigation options, some of them may be irrelevant to the functionality being tested (for 
example, headers and footers). In such cases, it is advised to constraint 
the navigation space by specifying which clickable elements encountered during crawling should be avoided.
Clickables specification can be also required in the other direction: some html tags (such as `<div>`) 
are not automatically recognized as clickables by CrawlJax, but in some applications may actually trigger 
navigation actions via embedded JavaScript code. Hence, they need to be specified in order for CrawlJax to 
navigate through them.
Specification of clickables is done in a separate configuration file, as in the case of data form specification.
The location of the clickables specification file should be given in the config option `clickables_spec_file`
in the main configuration file. 

For details on how to specify clickables, see [clickables specification](./tkltest_ui_config_options.md#clickables-specification).
 
## Generated artifacts      

The main artifacts created by the `tkltest-ui generate` command are the two test suites, one using Selenium API
and the other using Crawljax API. In addition to these, a number of other artifacts are created from the automated
crawling in the `tkltest-output-ui-<app-name>/<app-name>_localhost_<time-limit>mins/localhost/crawl##` directory.
These latter artifacts describe information related to the internal model CrawlJax built, and can be used for 
advanced debugging or for deep diving into the crawling results from which the test suites are derived.

- The Selenium API tests, the Maven build file, and the TestNG config file appear under directory `selenium-api-tests`.

- The Crawljax API tests, along with JSON files containing data required by Crawljax test helper methods, reside
in the `src/` directory at the top-level. The top-level directory contains the Maven build file and the TestNG
config file for these tests.

- The index.html file provides visualization of the crawl model as well as statistics about the crawl.

- The JSON files at the top level contain information about crawl paths traversed and states reached, some of which are used by Crawljax test helper methods.

- The `css/`, `img/`, `js/`, and `lib/` directories contain JavaScript and CSS code and images to support the crawl model visualization.

- The `doms/`, `screenshots/`, and `states/` directories contain information about the states reached (HTML, captured images, etc.)

The following tree illustrates the created `crawl##` directory structure:

<img width="309" alt="image" src="https://user-images.githubusercontent.com/2688521/157508099-e59eea34-028e-4221-b506-f3efb6f2ec31.png">

## Test Execution

As mentioned [here](#test-generation), two test suites are generated from CrawlJax output: one using Selenium API 
and one using CrawlJax API. Corresponding `pom.xml` build files are generated for each of the test
suites. Test execution can be performed in three different ways:

1. Running the command `tkltest-ui execute`
2. Running the command `mvn test` in the folder where the generated `pom.xml` is located
3. Running the `pom.xml` via an IDE 

In the following, we provide more details about the `tkltest-ui execute` command. It takes 
one optional parameter:

``` 
usage: tkltest-ui execute [-h] [-at {crawljax,selenium}]

optional arguments:
  -h, --help            show this help message and exit
  -at {crawljax,selenium}, --api-type {crawljax,selenium}
                        library API type used by the generated (Java) test
                        cases: Selenium or Crawljax; default is Selenium API
``` 


**Note:** since the test cases are using the Selenium API, you need to install a browser WebDriver 
on the machine in which they are being executed. More details on installing can be found [here](https://www.selenium.dev/documentation/webdriver/getting_started/install_drivers/).

When test execution completes, a Maven Surfire report is generated, summarizing all test case results.
The report is generated in the folder `crawl##/selenium-api-tests/test-results` for Selenium API tests;
it is the standard TestNG test execution report, as illustrated in this image:

<img width="500" alt="image" src="https://user-images.githubusercontent.com/2688521/157544658-fc20455a-93e9-441a-b9ef-f549d7efe0c1.png">

For the Crawljax API tests, a custom test report showing visualization of the executed tests with screenshots
is created in the folder `crawl##/test-results`. The following image is an illustrative example of the report:

<img width="500" alt="image" src="https://user-images.githubusercontent.com/2688521/157544777-a50f26a4-4a18-4811-86d4-51c154d2f7cc.png">

Each execution of the Crawljax API test suite generates a new execution report created in a subdirectory under `crawl##/test-results`
(the subdirectory name is an integer value, starting at `0` and incremented for each execution).

## Best Practices and Troubleshooting Tips

1. Be aware that the crawler will typically update the persistent state of the application under test as it traverses various application states and submits input forms. So it is advised to perform test generation in a development or test environment only where pertsistent state updates do not have undesired effects.
2. Ensuring that the application (persistent) state is reset between test generation and test execution and consecutive test executions will help avoid unexpected (and false) test failures. To avoid such failures, to the extent possible, test executions should be done with the same persistent state in which crawling and test generation were done.
3. If test generation ends immediately without loading the app URL, perform the following checks: all [TackleTest-UI prerequisites](../installation.md#prerequisites-for-tackletest-ui) are installed, the app URL is not malformed, and the app is in fact available at the given URL.
4. To help with failure diagnosis, capturing the crawl logs can help; run test generation with the `--verbose` option to redirect crawl logs to a file.
5. To check whether a crawl and test-generation run completed successfully, check for the generated artifacts. Among the [generated artifacts](#generated-artifacts), the contents of `doms/` and `screenshots/` directories are updated dynamically as new states and discovered during crawling. `css/`, `img/`, `js/`, and `lib/` directories contain static content that is initialized before crawling starts. The remaining artifacts, including the two test suites, are created after crawling has completed.

[comment]: <> (5. Advanced: Check that the screenshots and DOMs are correctly captured for specific states TBD)
[comment]: <> (6. Advanced: Check test-execution reports and correlate with captured application model if necessary TBD)
