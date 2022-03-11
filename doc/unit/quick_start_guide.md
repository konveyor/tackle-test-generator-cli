## TackleTest-Unit Quick Start Guide

We list the minimal steps required to use the tool for its two main functions: generating unit tests
and executing them. More detailed description is available in the [CLI user guide](./user_guide.md).

1. Created an empty configuration file, named `tkltest_config.toml`, by running the command
   ```
   tkltest-unit config init --file tkltest_config.toml
   ````
   `tkltest_config.toml` will be created in the working directory.

2. Assign values to the following configuration options in the configuration file
   (details on all configuration options are available [here](./tkltest_unit_config_options.md)):
   
   - `app_name`: name of the app under test (this name is used as prefix of file/directories created
     during test generation)
     
   - `app_build_files`: one or more build files for the application. TackleTest will automatically extract the following information 
         from the provided build files: (1) the paths to the application classes, (2) the external dependencies of the application, 
         and (3) the identity of its modules, in case of a multi-module application
  
   - `app_build_type`: either maven, gradle or ant
     
3. To generate test cases, run the command
   ```
   tkltest-unit --verbose generate ctd-amplified
   ```
   The unit test cases will be generated in a folder named `tkltest-output-unit-<app-name>/<app-name>-ctd-amplified-tests/monolith`.
   A CTD coverage report will be created as well  in a folder named `tkltest-output-unit-<app-name>/<app-name>-tkltest-reports`, showing
   the CTD test plan row coverage achieved by the generated tests.

4. To execute the generated unit tests on the legacy app, run the command
   ```
   tkltest-unit --verbose --test-directory tkltest-output-unit-<app-name>/<app-name>-ctd-amplified-tests execute
   ```
   JUnit reports and Jacoco code coverage reports will be created in  `tkltest-output-unit-<app-name>/<app-name>-tkltest-reports`.
 
Note that, if the `--config-file` option is not specified on the command line (as in the commands above),
the CLI uses by default `./tkltest_config.toml` as the configuration file.
