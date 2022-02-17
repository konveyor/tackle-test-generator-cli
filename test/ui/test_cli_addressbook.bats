ADDRESSBOOK_CONFIG_FILE=./test/ui/data/addressbook/tkltest_ui_config.toml
ADDRESSBOOK_OUTPUT_DIR=./__tkltest-output-ui-addressbook
ADDRESSBOOK_CRAWL_DIR=$ADDRESSBOOK_OUTPUT_DIR/localhost/crawl0
ADDRESSBOOK_TEST_FILE=$ADDRESSBOOK_CRAWL_DIR/src/test/java/generated/GeneratedTests.java

# setup commands run before execution of tests in file
setup_file() {
    echo "# setup_file: deleting output dirs" >&3
    echo "#   - deleting $ADDRESSBOOK_OUTPUT_DIR" >&3
    rm -rf $ADDRESSBOOK_OUTPUT_DIR
}

teardown_file() {
    echo "# teardown_file: stopping webapp" >&3
    cd test/ui/data/webapps/addressbook && ./deploy_app.sh stop && cd ../../../../..
}

setup() {
    echo "# setup: deploying webapp" >&3
    cd test/ui/data/webapps/addressbook && ./deploy_app.sh start && cd ../../../../..
}

@test "Test 01: CLI generate addressbook" {
    # generate test cases for addressbook app
    run tkltest-ui --verbose \
        --config-file $ADDRESSBOOK_CONFIG_FILE \
        --test-directory $ADDRESSBOOK_OUTPUT_DIR \
        generate
    [ $status -eq 0 ]

    # assert that the crawl directory is created
    [ -d ./$ADDRESSBOOK_CRAWL_DIR ]

    # assert that pom.xml and test class are generated
    [ -f ./$ADDRESSBOOK_CRAWL_DIR/pom.xml ]
    [ -f ./$ADDRESSBOOK_TEST_FILE ]

    # assert over number of generated tests
    test_count=`grep @Test $ADDRESSBOOK_TEST_FILE | wc -l`
    echo "# test_count=$test_count" >&3
    [ $test_count -gt 0 ]
}

@test "Test 02: CLI execute addressbook" {
    # execute test cases for addressbook app
    run tkltest-ui --verbose \
        --config-file $ADDRESSBOOK_CONFIG_FILE \
        --test-directory $ADDRESSBOOK_OUTPUT_DIR \
        execute
    [ $status -eq 0 ]
}
