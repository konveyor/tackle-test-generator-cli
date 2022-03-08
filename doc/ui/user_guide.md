# TackleTest-UI User Guide

1. [Overview](#overview)
2. [Test Generation](#test-generation)
3. [Test Execution](#test-execution)
4. [Best Practices and Troubleshooting Tips](#best-practices-and-troubleshooting-tips)
5. [Configuration Options](tkltest_ui_config_options.md)

## Overview

*TackleTest-UI** supports automatic generation of end-to-end test cases for web applications. These test cases
exercise the application under test via its user interface and drive execution through different application
tiers. Some of the features of TackleTest-UI include

- Model-based generation of test cases for web applications: infers state-transition model of the webapp under test and generated test cases from paths in the model 
- Integration with the state-of-the-art web-crawling tool [Crawljax](https://github.com/crawljax/crawljax)
- Generation two test suites using the TestNG framework: one using Crawljax API and the other using Selenium API

The web crawler randomly crawls applications paths; while it mimics user interaction with the application, the crawling may not 
necessarily be similar to the way a user would navigate the application, i.e., via meaningful functional paths.

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
two different versions: using CrawlJax API, and using Selenium API only. Test cases in Selenium API will be generated in a folder named `tkltest-output-ui-<app-name>/<app-name>_localhost_<time-limit>mins/localhost/crawl##/selenium-api-tests/src`. 
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
   
### Specifying input data

Specification of input data is optional. If you do not specify input data, random data will 
be used. However, in such a case there is a high chance that the crawling will not be able to progress
beyond initial login attempt, or otherwise fail early due to invalid input, Hence, it is advised
to specify form data, to the very least for forms that are critical for proceeding in navigation, such as login forms.
Form data is specified in a separate configuration file in toml format. The location of the 
form data specification file should be provided in the `form_data_spec_file` option in the 
main configuration file. For an example of a form data specification file see `test/ui/data/petclinic/tkltest_ui_formdata_config.toml`.

For each form, you need to specify a table with the form name `[forms.<form name>]`. In the 
table of each form, you specify a list of input data for different fields. For each field,
you need to specify (1) `input_type`, (2) `identification`, and (3) `input_value`.  
1. `input type` is either `text`, `select`, `checkbox`, `radio`, `email`, `textarea`, `password`, or `number`.
2. `identification` consists of two parts:

        a. `how`: enum with choices `name`, `id`, `tag`, `text`, `xpath`, `partial_text`
        b. `value`: string value for how
3. `input_value` is the string value to be used as input. 

In addition, for each form, you need to specify one `before_click` element to identify the web element that is 
clicked to submit the form. It consists of two parts:

1. `tag_name`: string specifying HTML tag
2. one of `with_attribute`, `with_text`, or `under_xpath`, taking the following structure
        `with_attribute` = { attr_name = "", attr_value = ""}
        `with_text` = "<text>"
        `under_xpath` = "<xpath>"

Follow the provided example for more details on data form specification.
  
### Specifying constraints on clickable elements

Clickables are web elements that can be clicked to trigger actions that transform the 
web application from one state to another. For some applications, there is a huge 
space of navigation options, some of them irrelevant to the functionality being tested (for 
example, buttons navigating to external sites). In such cases, it is advised to restrict 
the navigation space by restricting the Crawler on which elements encountered during crawling should be clicked, 
and which ones should be avoided.
Specification of constraints on clickables is done in a separate configuration file, as in the case of data form specification.
The location of the clickables specification file should be given in the config option `clickables_spec_file`
in the main configuration file. 
    

## Test Execution

TBD

## Best Practices and Troubleshooting Tips

TBD
