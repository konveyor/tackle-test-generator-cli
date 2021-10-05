IRS_CONFIG_FILE_ERR=./test/data/irs/tkltest_config_err.toml
IRS_CONFIG_FILE=./test/data/irs/tkltest_config.toml
IRS_PARTITIONS_FILE=./test/data/irs/refactored/PartitionsFile.json
IRS_CTD_AMPLIFIED_TESTDIR=./irs-ctd-amplified-tests
IRS_GENERATE_CONFIG_FILE=$IRS_CTD_AMPLIFIED_TESTDIR/.tkltest_generate.toml
TEST_CONFIG_FILE1=./__test_config1.toml
TEST_CONFIG_FILE2=./__test_config2.toml
TKLTEST_CLI_VERSION=`cat ./tkltest/_version.py | cut -d '=' -f 2 | xargs`

# setup commands run befeore execution of tests in file
setup_file() {
    rm -f $TEST_CONFIG_FILE1 $TEST_CONFIG_FILE2
}

teardown_file() {
    rm -f $TEST_CONFIG_FILE1 $TEST_CONFIG_FILE2
}

@test "Test 01: CLI main help" {
    run tkltest --help
    [ $status -eq 0 ]
    [ "${lines[0]}" = "usage: tkltest [-h] [-cf CONFIG_FILE] [-l {CRITICAL,ERROR,WARNING,INFO,DEBUG}]" ]
}

@test "Test 02: CLI config command help" {
    run tkltest config --help
    [ $status -eq 0 ]
    [ "${lines[0]}" = "usage: tkltest config [-h] {init,list} ..." ]
}

@test "Test 03: CLI config subcommands help" {
    run tkltest config init --help
    [ $status -eq 0 ]
    [ "${lines[0]}" = "usage: tkltest config init [-h] [-f FILE]" ]

    run tkltest config list --help
    [ $status -eq 0 ]
    [ "${lines[0]}" = "usage: tkltest config list [-h]" ]
}

@test "Test 04: CLI config init" {
    run tkltest config init
    [ $status -eq 0 ]

    # write captured output to file
    # printf "%s\n" "${lines[@]}" > $TEST_CONFIG_FILE1

    # run "config init" with file name specified
    run tkltest config init --file $TEST_CONFIG_FILE2
    [ $status -eq 0 ]

    # assert that config file is created
    [ -f $TEST_CONFIG_FILE2 ]

    # remove blank lines from file and assert that the two files have the same content
    # grep . $TEST_CONFIG_FILE2 | tee $TEST_CONFIG_FILE2
    # diff $TEST_CONFIG_FILE1 $TEST_CONFIG_FILE2
    # [ $? -eq 0 ]
}

@test "Test 05: CLI config list" {
    run tkltest config list
    [ $status -eq 0 ]
}

@test "Test 06: CLI generate command help" {
    run tkltest generate --help
    [ $status -eq 0 ]
    [ "${lines[0]}" = "usage: tkltest generate [-h] [-nda] [-pf PARTITIONS_FILE]" ]
}

@test "Test 07: CLI execute command help" {
    run tkltest execute --help
    [ $status -eq 0 ]
    [ "${lines[0]}" = "usage: tkltest execute [-h] [-bt {ant,maven}] [-nbf] [-cc] [-onli]" ]
}

@test "Test 08: CLI \"generate ctd-amplified\" command help" {
    run tkltest generate ctd-amplified --help
    [ $status -eq 0 ]
    [ "${lines[0]}" = "usage: tkltest generate ctd-amplified [-h] [-btg {combined,evosuite,randoop}]" ]
}

@test "Test 09: CLI \"generate evosuite\" command help" {
    run tkltest generate evosuite --help
    [ $status -eq 0 ]
    [ "${lines[0]}" = "usage: tkltest generate evosuite [-h]" ]
}

@test "Test 10: CLI \"generate randoop\" command help" {
    run tkltest generate randoop --help
    [ $status -eq 0 ]
    [ "${lines[0]}" = "usage: tkltest generate randoop [-h]" ]
}

@test "Test 11: CLI --version" {
    run tkltest --version
    [ $status -eq 0 ]
    [ "${lines[0]}" = "$TKLTEST_CLI_VERSION" ]
}

@test "Test 12: CLI generate ctd-amplified invalid spec in toml" {
    run tkltest --config-file $IRS_CONFIG_FILE_ERR generate ctd-amplified
    [ $status -eq 1 ]
    echo "# ${lines[@]}" >&3
    [[ "${lines[1]}" == *"ERROR: configuration options validation failed:"* ]]
    [[ "${lines[2]}" == *"Missing required options for \"general\": ['app_name', 'app_classpath_file', 'monolith_app_path']"* ]]
    [[ "${lines[3]}" == *"Value for option \"base_test_generator\" must be one of ['combined', 'evosuite', 'randoop']: combine"* ]]
}

@test "Test 13: CLI generate ctd-amplified invalid spec in toml" {
    run tkltest --config-file $IRS_CONFIG_FILE_ERR \
        generate --partitions-file $IRS_PARTITIONS_FILE ctd-amplified
    [ $status -eq 1 ]
    echo "# ${lines[@]}" >&3
    [[ "${lines[1]}" == *"ERROR: configuration options validation failed:"* ]]
    [[ "${lines[2]}" == *"Missing required options for \"general\": ['app_name', 'app_classpath_file', 'monolith_app_path']"* ]]
    [[ "${lines[3]}" == *"Missing required options for \"generate ctd-amplified\": ['refactored_app_path_prefix', 'refactored_app_path_suffix']"* ]]
    [[ "${lines[4]}" == *"Value for option \"base_test_generator\" must be one of ['combined', 'evosuite', 'randoop']: combine"* ]]
}

@test "Test 14: CLI execute invalid spec in toml" {
    run tkltest --config-file $IRS_CONFIG_FILE_ERR execute
    [ $status -eq 1 ]
    echo "# ${lines[@]}" >&3
    [[ "${lines[1]}" == *"ERROR: configuration options validation failed:"* ]]
    [[ "${lines[2]}" == *"Missing required options for \"general\": ['app_name', 'app_classpath_file', 'monolith_app_path']"* ]]
    [[ "${lines[3]}" == *"Missing required options for \"execute\": ['app_packages']"* ]]
    [[ "${lines[4]}" == *"Value for option \"build_type\" must be one of ['ant', 'maven']: gradle"* ]]
}

@test "Test 15: CLI execute missing generate config" {
    if [ ! -d $IRS_CTD_AMPLIFIED_TESTDIR ];  then
        echo "# creating $IRS_CTD_AMPLIFIED_TESTDIR" >&3
        mkdir $IRS_CTD_AMPLIFIED_TESTDIR
    fi
    if [ -f $IRS_GENERATE_CONFIG_FILE ]; then
        echo "# removing $IRS_GENERATE_CONFIG_FILE" >&3
        rm $IRS_GENERATE_CONFIG_FILE
    fi
    run tkltest --config-file $IRS_CONFIG_FILE \
        --test-directory $IRS_CTD_AMPLIFIED_TESTDIR execute
    [ $status -eq 1 ]
    echo "# ${lines[@]}" >&3
    [[ "${lines[1]}" == *"Generate config file not found:"* ]]
    [[ "${lines[2]}" == *"To execute tests in ../../$IRS_CTD_AMPLIFIED_TESTDIR, the file created by the generate command must be available"* ]]
    [[ "${lines[2]}" == *"To execute tests in ../../$IRS_CTD_AMPLIFIED_TESTDIR, the file created by the generate command must be available"* ]]
}

@test "Test 16: CLI generate ctd-amplified parameter constraint violation" {
    run tkltest --config-file $IRS_CONFIG_FILE generate ctd-amplified \
        --base-test-generator randoop --augment-coverage
    [ $status -eq 1 ]
    echo "# ${lines[@]}" >&3
    [[ "${lines[1]}" == *"ERROR: configuration options validation failed:"* ]]
    [[ "${lines[2]}" == *"Violated parameter constraint: To use option \"-ac/--augment-coverage\", base test generator must be \"combined\" or \"evosuite\""* ]]
}
