TEST_CONFIG_FILE1=./__test_ui_config1.toml
TEST_CONFIG_FILE2=./__test_ui_config2.toml
TKLTEST_CLI_VERSION=`cat ./tkltest/_version.py | cut -d '=' -f 2 | xargs`

# setup commands run befeore execution of tests in file
# setup_file() {
# }

setup() {
    load "../test_helper/bats-assert/load"
    load "../test_helper/bats-support/load"
}

teardown_file() {
    rm -f $TEST_CONFIG_FILE
}

@test "Test 00: CLI main no args" {
    run tkltest-ui
    assert_success
    assert_output --partial 'usage: tkltest-ui [-h] [-au APP_URL] [-cf CONFIG_FILE]'
}

@test "Test 01: CLI main help" {
    run tkltest-ui --help
    assert_success
    assert_output --partial 'usage: tkltest-ui [-h] [-au APP_URL] [-cf CONFIG_FILE]'
}

@test "Test 02: CLI config command help" {
    run tkltest-ui config --help
    assert_success
    assert_output --partial 'usage: tkltest-ui config [-h] {init,list} ...'
}

@test "Test 03: CLI config subcommands help" {
    run tkltest-ui config init --help
    assert_success
    assert_output --partial 'usage: tkltest-ui config init [-h] [-f FILE]'

    run tkltest-ui config list --help
    assert_success
    assert_output --partial 'usage: tkltest-ui config list [-h]'
}

@test "Test 04: CLI config init" {
    run tkltest-ui config init
    assert_success

    # write captured output to file
    printf "%s\n" "${lines[@]}" > $TEST_CONFIG_FILE1

    # run "config init" with file name specified
    run tkltest-ui config init --file $TEST_CONFIG_FILE2
    assert_success

    # assert that config file is created
    [ -f $TEST_CONFIG_FILE2 ]

    # remove blank lines from file and assert that the two files have the same content
    grep . $TEST_CONFIG_FILE2 | tee $TEST_CONFIG_FILE2
    diff $TEST_CONFIG_FILE1 $TEST_CONFIG_FILE2
    [ $? -eq 0 ]
}

@test "Test 05: CLI config list" {
    run tkltest-ui config list
    assert_success
}

@test "Test 06: CLI config file load" {
    run tkltest-ui -cf ./test/ui/tkltest_ui_config.toml -l DEBUG
    assert_success
}
