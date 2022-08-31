IRS_CONFIG_FILE_ERR=./test/data/irs/tkltest_config_err.toml
IRS_CONFIG_FILE_ERR2=./test/data/irs/tkltest_config_err2.toml
IRS_CONFIG_FILE_ERR3=./test/data/irs/tkltest_config_err3.toml
IRS_CONFIG_FILE=./test/data/irs/tkltest_config.toml
IRS_PARTITIONS_FILE=./test/data/irs/refactored/PartitionsFile.json
IRS_CTD_AMPLIFIED_TESTDIR=./irs-ctd-amplified-tests

IRS_OUTPUT_DIR=./tkltest-output-unit-irs
IRS_TEST_BUILD_DIR=$IRS_OUTPUT_DIR//irs-tkltest-build-artifacts
IRS_GENERATE_CONFIG_FILE=$IRS_TEST_BUILD_DIR/.tkltest_generate.toml
TEST_CONFIG_FILE1=./__test_unit_config1.toml
TEST_CONFIG_FILE2=./__test_unit_config2.toml
TKLTEST_CLI_VERSION=`cat ./tkltest/_version.py | cut -d '=' -f 2 | xargs`

# setup commands run befeore execution of tests in file
setup_file() {
    rm -f $TEST_CONFIG_FILE1 $TEST_CONFIG_FILE2
}

setup() {
    load "../test_helper/bats-assert/load"
    load "../test_helper/bats-support/load"
}

teardown_file() {
    rm -f $TEST_CONFIG_FILE1 $TEST_CONFIG_FILE2
}

@test "Test 00: CLI main no args" {
    run tkltest-unit
    assert_success
    assert_output --partial 'usage: tkltest-unit [-h] [-cf CONFIG_FILE]'
}

@test "Test 01: CLI main help" {
    run tkltest-unit --help
    assert_success
    assert_output --partial 'usage: tkltest-unit [-h] [-cf CONFIG_FILE]'
}

@test "Test 02: CLI config command help" {
    run tkltest-unit config --help
    assert_success
    assert_output --partial 'usage: tkltest-unit config [-h] {init,list} ...'
}

@test "Test 03: CLI config subcommands help" {
    run tkltest-unit config init --help
    assert_success
    assert_output --partial 'usage: tkltest-unit config init [-h] [-f FILE]'

    run tkltest-unit config list --help
    assert_success
    assert_output --partial 'usage: tkltest-unit config list [-h]'
}

@test "Test 04: CLI config init" {
    run tkltest-unit config init
    assert_success

    # write captured output to file
    # printf "%s\n" "${lines[@]}" > $TEST_CONFIG_FILE1

    # run "config init" with file name specified
    run tkltest-unit config init --file $TEST_CONFIG_FILE2
    assert_success

    # assert that config file is created
    [ -f $TEST_CONFIG_FILE2 ]

    # remove blank lines from file and assert that the two files have the same content
    # grep . $TEST_CONFIG_FILE2 | tee $TEST_CONFIG_FILE2
    # diff $TEST_CONFIG_FILE1 $TEST_CONFIG_FILE2
    # [ $? -eq 0 ]
}

@test "Test 05: CLI config list" {
    run tkltest-unit config list
    assert_success
}

@test "Test 06: CLI generate command help" {
    run tkltest-unit generate --help
    assert_success
    assert_output --partial 'usage: tkltest-unit generate [-h] [-nda] [-bp]'
}

@test "Test 07: CLI execute command help" {
    run tkltest-unit execute --help
    assert_success
    assert_output --partial 'usage: tkltest-unit execute [-h] [-nbf] [-cc]'
}

@test "Test 08: CLI \"generate ctd-amplified\" command help" {
    run tkltest-unit generate ctd-amplified --help
    assert_success
    assert_output --partial 'usage: tkltest-unit generate ctd-amplified [-h]'
}

@test "Test 09: CLI \"generate evosuite\" command help" {
    run tkltest-unit generate evosuite --help
    assert_success
    assert_output --partial 'usage: tkltest-unit generate evosuite [-h]'
}

@test "Test 10: CLI \"generate randoop\" command help" {
    run tkltest-unit generate randoop --help
    assert_success
    assert_output --partial 'usage: tkltest-unit generate randoop [-h]'
}

@test "Test 11: CLI --version" {
    run tkltest-unit --version
    assert_success
    assert_output "$TKLTEST_CLI_VERSION"
}

@test "Test 12: CLI generate ctd-amplified invalid spec in toml" {
    run tkltest-unit --config-file $IRS_CONFIG_FILE_ERR generate ctd-amplified
    assert_failure 1
    assert_line --index 1 --partial "Warning: Unsupported flag in toml file"
    assert_line --index 2 --partial "Warning: Unsupported flag in toml file"
    assert_line --index 3 --partial "ERROR: configuration options validation failed:"
    assert_line --index 4 --partial "Missing required options for \"general\": ['app_name']"
    assert_line --index 5 --partial "Missing conditionally required option for \"general\": monolith_app_path (required if \"app_build_files\" is not specified)"
    assert_line --index 6 --partial "Value for option \"build_type\" must be one of ['ant', 'maven', 'gradle']: cpp"
    assert_line --index 7 --partial "Missing conditionally required option for \"generate\": app_build_files (required if \"monolith_app_path\" is not specified)"
    assert_line --index 8 --partial "Value for option \"base_test_generator\" must be one of ['combined', 'evosuite', 'randoop']: combine"
}

@test "Test 13: CLI generate ctd-amplified no config" {
    run tkltest-unit generate ctd-amplified
    assert_failure 1
    assert_line --index 0 --partial 'ERROR: No config file specified'
}

@test "Test 14: CLI generate ctd-amplified invalid spec in toml" {
    run tkltest-unit --config-file $IRS_CONFIG_FILE_ERR generate ctd-amplified
    assert_failure 1
    assert_line --index 1 --partial "Warning: Unsupported flag in toml file"
    assert_line --index 2 --partial "Warning: Unsupported flag in toml file"
    assert_line --index 3 --partial "ERROR: configuration options validation failed:"
    assert_line --index 4 --partial "Missing required options for \"general\": ['app_name']"
    assert_line --index 5 --partial "Missing conditionally required option for \"general\": monolith_app_path (required if \"app_build_files\" is not specified)"
    assert_line --index 6 --partial "Value for option \"build_type\" must be one of ['ant', 'maven', 'gradle']: cpp"
    assert_line --index 7 --partial "Missing conditionally required option for \"generate\": app_build_files (required if \"monolith_app_path\" is not specified)"
    assert_line --index 8 --partial "Value for option \"base_test_generator\" must be one of ['combined', 'evosuite', 'randoop']: combine"
}

@test "Test 15: CLI execute invalid spec in toml" {
    run tkltest-unit --config-file $IRS_CONFIG_FILE_ERR execute
    assert_failure 1
    assert_line --index 1 --partial "Warning: Unsupported flag in toml file"
    assert_line --index 2 --partial "Warning: Unsupported flag in toml file"
    assert_line --index 3 --partial "ERROR: configuration options validation failed:"
    assert_line --index 4 --partial "Missing required options for \"general\": ['app_name']"
    assert_line --index 5 --partial "Missing conditionally required option for \"general\": monolith_app_path (required if \"app_build_files\" is not specified)"
    assert_line --index 6 --partial "Value for option \"build_type\" must be one of ['ant', 'maven', 'gradle']: cpp"
    assert_line --index 7 --partial "Missing required options for \"execute\": ['app_packages']"

}

@test "Test 16: CLI execute missing generate config" {
    if [ ! -d $IRS_CTD_AMPLIFIED_TESTDIR ];  then
        echo "# creating $IRS_CTD_AMPLIFIED_TESTDIR" >&3
        mkdir $IRS_CTD_AMPLIFIED_TESTDIR
    fi
    if [ -f $IRS_GENERATE_CONFIG_FILE ]; then
        echo "# removing $IRS_GENERATE_CONFIG_FILE" >&3
        rm $IRS_GENERATE_CONFIG_FILE
    fi
    run tkltest-unit --config-file $IRS_CONFIG_FILE \
        --test-directory $IRS_CTD_AMPLIFIED_TESTDIR execute
    assert_failure 1
    assert_line --index 1 --partial "Warning: Unsupported flag in toml file"
    assert_line --index 2 --partial "Warning: Unsupported flag in toml file"
    assert_line --index 3 --partial "Warning: Unsupported flag in toml file"
    assert_line --index 4 --partial "Executing tests for app irs using config file"
    assert_line --index 5 --partial "Generate config file not found:"
    assert_line --index 6 --partial "To execute tests in ../$IRS_CTD_AMPLIFIED_TESTDIR, the file created by the generate command must be available"
}

@test "Test 17: CLI generate ctd-amplified parameter constraint violation" {
    run tkltest-unit --config-file $IRS_CONFIG_FILE generate ctd-amplified \
        --base-test-generator randoop
    assert_failure 1
    assert_line --index 1 --partial "Warning: Unsupported flag in toml file"
    assert_line --index 2 --partial "Warning: Unsupported flag in toml file"
    assert_line --index 3 --partial "Warning: Unsupported flag in toml file"
    assert_line --index 4 --partial "ERROR: configuration options validation failed:"
    assert_line --index 5 --partial "Violated parameter constraint: For coverage augmentation, base test generator must be \"combined\" or \"evosuite\""
}

@test "Test 18: CLI generate ctd-amplified invalid build/classpath spec in toml" {
    run tkltest-unit --config-file $IRS_CONFIG_FILE_ERR2 generate ctd-amplified
    assert_failure 1
    assert_output --partial 'app_classpath_file (required if "app_build_files" is not specified)'
    assert_output --partial 'app_build_files (required if "app_classpath_file" is not specified)'
}

@test "Test 19: CLI generate ctd-amplified invalid build/classpath spec in toml" {
    run tkltest-unit --config-file $IRS_CONFIG_FILE_ERR3 generate ctd-amplified
    assert_failure 1
    assert_output --partial 'app_classpath_file (required if "app_build_files" is not specified)'
    assert_output --partial 'app_build_files (required if "app_classpath_file" is not specified)'
}
