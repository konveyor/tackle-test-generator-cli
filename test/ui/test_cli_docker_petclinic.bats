load './petclinic_setenv'

# setup commands run befeore execution of tests in file
setup_file() {
    echo "# setup_file: building webapp image" >&3
    cd test/ui/data/webapps/petclinic && ./deploy_app.sh build && cd ../../../../..
    echo "# setup_file: deleting output dirs" >&3
    echo "#   - deleting $PETCLINIC_OUTPUT_DIR" >&3
    rm -rf $PETCLINIC_OUTPUT_DIR
}

teardown_file() {
    echo "# teardown_file: stopping webapp" >&3
    cd test/ui/data/webapps/petclinic && ./deploy_app.sh stop && cd ../../../../..
}

setup() {
    echo "# setup: deploying webapp" >&3
    cd test/ui/data/webapps/petclinic && ./deploy_app.sh start && cd ../../../../..
}

@test "Test 01: tkltest-ui docker generate petclinic" {
    # generate test cases for petclinic app
    run docker-compose run --rm tkltest-cli \
        tkltest-ui --verbose --log-level INFO \
        --config-file $PETCLINIC_CONFIG_FILE \
        --test-directory $PETCLINIC_OUTPUT_DIR \
        generate
    [ $status -eq 0 ]

    # assert that the crawl directory is created
    [ -d ./$PETCLINIC_CRAWL_DIR ]

    # assert that pom.xml and test class crawljax API tests are generated
    [ -f ./$PETCLINIC_CRAWL_DIR/pom.xml ]
    [ -f ./$PETCLINIC_CRAWLJAX_API_TEST_FILE ]

    # assert over number of generated crawljax API tests
    test_count=`grep @Test $PETCLINIC_CRAWLJAX_API_TEST_FILE | wc -l`
    echo "# crawljax_api_test_count=$test_count" >&3
    [ $test_count -gt 0 ]

    # assert that pom.xml and test class for selenium API tests are generated
    [ -f ./$PETCLINIC_SELENIUM_API_TEST_DIR/pom.xml ]
    [ -f ./$PETCLINIC_SELENIUM_API_TEST_FILE ]

    # assert over number of generated selenium API tests
    test_count=`grep @Test $PETCLINIC_SELENIUM_API_TEST_FILE | wc -l`
    echo "# selenium_api_test_count=$test_count" >&3
    [ $test_count -gt 0 ]
}

@test "Test 02: tkltest-ui docker execute [api_type=selenium] petclinic" {
    # execute test cases for petclinic app
    run docker-compose run --rm tkltest-cli \
        tkltest-ui --verbose --log-level INFO \
        --config-file $PETCLINIC_CONFIG_FILE \
        --test-directory $PETCLINIC_OUTPUT_DIR \
        execute
    # [ $status -eq 0 ]

    # assert that test report is created
    [ -f ./$PETCLINIC_SELENIUM_API_TEST_REPORT ]
}

@test "Test 03: tkltest-ui docker execute [api_type=crawljax] petclinic" {
    # execute test cases for petclinic app
    run docker-compose run --rm tkltest-cli \
        tkltest-ui --verbose --log-level INFO \
        --config-file $PETCLINIC_CONFIG_FILE \
        --test-directory $PETCLINIC_OUTPUT_DIR \
        execute --api-type crawljax
    # [ $status -eq 0 ]

    # assert that test report is created
    [ -f ./$PETCLINIC_CRAWLJAX_API_TEST_REPORT ]
}
