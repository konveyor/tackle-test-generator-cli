#!/usr/bin/env bash

PETCLINIC_CONFIG_FILE=./test/ui/data/petclinic/tkltest_ui_config.toml
PETCLINIC_CONFIG_FILE_NONFRAG=./test/ui/data/petclinic/tkltest_ui_config_nonfrag.toml
PETCLINIC_OUTPUT_DIR=./__tkltest-output-ui-petclinic
PETCLINIC_CRAWL_DIR=$PETCLINIC_OUTPUT_DIR/localhost/crawl0
PETCLINIC_CRAWLJAX_API_TEST_FILE=$PETCLINIC_CRAWL_DIR/src/test/java/generated/GeneratedTests.java
PETCLINIC_CRAWLJAX_API_TEST_REPORT=$PETCLINIC_CRAWL_DIR/test-results/0/TestResults.html
PETCLINIC_SELENIUM_API_TEST_DIR=$PETCLINIC_CRAWL_DIR/selenium-api-tests
PETCLINIC_SELENIUM_API_TEST_FILE=$PETCLINIC_SELENIUM_API_TEST_DIR/src/test/java/generated/GeneratedTests.java
PETCLINIC_SELENIUM_API_TEST_REPORT=$PETCLINIC_SELENIUM_API_TEST_DIR/test-results/index.html
