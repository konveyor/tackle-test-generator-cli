TEST_CONFIG_FILE1=./__test_config1.toml
TEST_CONFIG_FILE2=./__test_config2.toml

# setup commands run befeore execution of tests in file
setup_file() {
    rm -f $TEST_CONFIG_FILE1 $TEST_CONFIG_FILE2
}

teardown_file() {
    rm -f $TEST_CONFIG_FILE1 $TEST_CONFIG_FILE2
}

@test "Test 01: CLI main help" {
    run docker-compose run --rm tkltest-cli --help
    [ $status -eq 0 ]
}

@test "Test 02: CLI config command help" {
    run docker-compose run --rm tkltest-cli config --help
    [ $status -eq 0 ]
}

@test "Test 03: CLI config subcommands help" {
    run docker-compose run --rm tkltest-cli config init --help
    [ $status -eq 0 ]

    run docker-compose run --rm tkltest-cli config list --help
    [ $status -eq 0 ]
}

@test "Test 04: CLI config init" {
    run docker-compose run --rm tkltest-cli config init
    [ $status -eq 0 ]

    # run "config init" with file name specified
    run docker-compose run --rm tkltest-cli config init --file $TEST_CONFIG_FILE2
    [ $status -eq 0 ]

    # assert that config file is created
    [ -f $TEST_CONFIG_FILE2 ]
}

@test "Test 05: CLI config list" {
    run docker-compose run --rm tkltest-cli config list
    [ $status -eq 0 ]
}

@test "Test 06: CLI generate command help" {
    run docker-compose run --rm tkltest-cli generate --help
    [ $status -eq 0 ]
}

@test "Test 07: CLI execute command help" {
    run docker-compose run --rm tkltest-cli execute --help
    [ $status -eq 0 ]
}

@test "Test 08: CLI \"generate ctd-amplified\" command help" {
    run docker-compose run --rm tkltest-cli generate ctd-amplified --help
    [ $status -eq 0 ]
}

@test "Test 09: CLI \"generate evosuite\" command help" {
    run docker-compose run --rm tkltest-cli generate evosuite --help
    [ $status -eq 0 ]
}

@test "Test 10: CLI \"generate randoop\" command help" {
    run docker-compose run --rm tkltest-cli generate randoop --help
    [ $status -eq 0 ]
}
