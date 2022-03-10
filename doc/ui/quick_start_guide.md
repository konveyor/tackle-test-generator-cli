## TackleTest-UI Quick Start Guide

We list the minimal steps required to use the tool for its two main functions: generating UI tests
and executing them. More detailed description is available in the [CLI user guide](./user_guide.md).

1. Create an empty configuration file, named `tkltest_config.toml`, by running the command
   ```
   tkltest-ui config init --file tkltest_config.toml
   ````
   `tkltest_config.toml` will be created in the working directory.

2. Assign values to the following configuration options in the configuration file
   (details on all configuration options are available [here](./tkltest_ui_config_options.md)):
   
   - `app_name`: name of the app under test (this name is used as prefix of file/directories created
     during test generation)
     
   - `app_url`: URL where application under test is deployed. Note that you will need to have a running instance of the application 
   at the provided url
     
3. To generate test cases, run the command
   ```
   tkltest-ui --verbose generate 
   ```
   The  `--verbose` option redirects crawl logs to a file called `<app-nane>_crawljax_runner.log`. You can view the progress of crawling via the command `tail -f <logfile>`. Test cases in Selenium API will be generated in a folder named `tkltest-output-ui-<app-name>/<app-name>_localhost_<time-limit>mins/localhost/crawl##/selenium-api-tests/src`.
   Each `crawl##` folder contains output of a different run. Your latest run will be in the `crawl` folder with the largest number.
   These test cases depend on Selenium only, and not on CrawlJax. In addition, test cases in CrawlJax API will be generated in `tkltest-output-ui-<app-name>/<app-name>_localhost_<time-limit>mins/localhost/crawl##/src`

4. To execute the generated tests on the legacy app, run the command
   ```
   tkltest-ui --verbose execute
   ```
   Test results report will be created in  `tkltest-output-unit-<app-name>/<app-name>_localhost_<time-limit>mins/localhost/crawl##/selenium-api-tests/test-output`.
   By default, the Selenium API test cases are executed. If you want to execute the CrawlJax API test cases, set the config option `api_type` to `crawljax`. 
 
Note that, if the `--config-file` option is not specified on the command line (as in the commands above),
the CLI uses by default `./tkltest_config.toml` as the configuration file.

Note also that, typically, test data such as login information and various data forms input will also need to be provided in order to generate meaningful 
tests. These can be optionally given in another configuration file, whose location should be specified in the `form_data_spec_file` config option.
More details about input data specification is available [here](./tkltest_ui_config_options.md#form-data-specification).