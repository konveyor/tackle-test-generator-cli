TEST_CONFIG_FILE1=./__test_ui_config1.toml

# setup commands run befeore execution of tests in file
# setup_file() {
# }

teardown_file() {
    rm -f $TEST_CONFIG_FILE1
}

@test "Test 01: tkltest-ui docker help" {
    run docker-compose run --rm tkltest-ui --help
    [ $status -eq 0 ]
}

@test "Test 02: tkltest-ui docker config command help" {
    run docker-compose run --rm tkltest-ui config --help
    [ $status -eq 0 ]
}

@test "Test 03: tkltest-ui docker config subcommands help" {
    run docker-compose run --rm tkltest-ui config init --help
    [ $status -eq 0 ]

    run docker-compose run --rm tkltest-ui config list --help
    [ $status -eq 0 ]
}

@test "Test 04: tkltest-ui docker config init" {
    run docker-compose run --rm tkltest-ui config init
    [ $status -eq 0 ]

    # run "config init" with file name specified
    run docker-compose run --rm tkltest-ui config init --file $TEST_CONFIG_FILE1
    [ $status -eq 0 ]

    # assert that config file is created
    [ -f $TEST_CONFIG_FILE1 ]
}

@test "Test 05: tkltest-ui docker config list" {
    run docker-compose run --rm tkltest-ui config list
    [ $status -eq 0 ]
}

@test "Test 06: tkltest-ui docker generate command help" {
    run docker-compose run --rm tkltest-ui generate --help
    [ $status -eq 0 ]
}

@test "Test 07: tkltest-ui docker execute command help" {
    run docker-compose run --rm tkltest-ui execute --help
    [ $status -eq 0 ]
}
