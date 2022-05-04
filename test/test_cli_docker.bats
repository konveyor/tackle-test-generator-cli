TEST_CONFIG_FILE1=./__test_config1.toml
TEST_CONFIG_FILE2=./__test_config2.toml

IRS_OUTPUT_DIR=./tkltest-output-unit-irs
IRS_CONFIG_FILE=./test/data/irs/tkltest_config.toml
IRS_CTD_TEST_PLAN_FILE=$IRS_OUTPUT_DIR/irs_ctd_models_and_test_plans.json
IRS_TESTGEN_SUMMARY_FILE=$IRS_OUTPUT_DIR/irs_test_generation_summary.json
IRS_CTD_AMPLIFIED_TESTDIR=$IRS_OUTPUT_DIR/irs-ctd-amplified-tests
IRS_TEST_REPORTS_DIR=$IRS_OUTPUT_DIR/irs-tkltest-reports

# setup commands run befeore execution of tests in file
setup_file() {
    rm -f $TEST_CONFIG_FILE1 $TEST_CONFIG_FILE2
    rm -rf $IRS_CTD_AMPLIFIED_TESTDIR $IRS_TEST_REPORTS_DIR $IRS_CTD_TEST_PLAN_FILE
}

teardown_file() {
    rm -f $TEST_CONFIG_FILE1 $TEST_CONFIG_FILE2
}

@test "Test 00: entrypoint help" {
    run docker-compose run --rm tkltest-cli --help
    [ $status -eq 0 ]
}

@test "Test 01: CLI main help" {
    run docker-compose run --rm tkltest-cli tkltest-unit --help
    [ $status -eq 0 ]
}

@test "Test 02: CLI config command help" {
    run docker-compose run --rm tkltest-cli tkltest-unit config --help
    [ $status -eq 0 ]
}

@test "Test 03: CLI config subcommands help" {
    run docker-compose run --rm tkltest-cli tkltest-unit config init --help
    [ $status -eq 0 ]

    run docker-compose run --rm tkltest-cli tkltest-unit config list --help
    [ $status -eq 0 ]
}

@test "Test 04: CLI config init" {
    run docker-compose run --rm tkltest-cli tkltest-unit config init
    [ $status -eq 0 ]

    # run "config init" with file name specified
    run docker-compose run --rm tkltest-cli tkltest-unit config init --file $TEST_CONFIG_FILE2
    [ $status -eq 0 ]

    # assert that config file is created
    [ -f $TEST_CONFIG_FILE2 ]
}

@test "Test 05: CLI config list" {
    run docker-compose run --rm tkltest-cli tkltest-unit config list
    [ $status -eq 0 ]
}

@test "Test 06: CLI generate command help" {
    run docker-compose run --rm tkltest-cli tkltest-unit generate --help
    [ $status -eq 0 ]
}

@test "Test 07: CLI execute command help" {
    run docker-compose run --rm tkltest-cli tkltest-unit execute --help
    [ $status -eq 0 ]
}

@test "Test 08: CLI \"generate ctd-amplified\" command help" {
    run docker-compose run --rm tkltest-cli tkltest-unit generate ctd-amplified --help
    [ $status -eq 0 ]
}

@test "Test 09: CLI \"generate evosuite\" command help" {
    run docker-compose run --rm tkltest-cli tkltest-unit generate evosuite --help
    [ $status -eq 0 ]
}

@test "Test 10: CLI \"generate randoop\" command help" {
    run docker-compose run --rm tkltest-cli tkltest-unit generate randoop --help
    [ $status -eq 0 ]
}

@test "Test 11: CLI [build_type=ant] generate [all-classes] ctd-amplified irs" {
    run docker-compose run --rm tkltest-cli tkltest-unit \
        --config-file $IRS_CONFIG_FILE \
        --test-directory $IRS_CTD_AMPLIFIED_TESTDIR \
        generate ctd-amplified
    [ $status -eq 0 ]

    # assert over test reports dir
    [ -d ./$IRS_TEST_REPORTS_DIR/ctd-report ]

    # assert over test plan file
    [ -f $IRS_CTD_TEST_PLAN_FILE ]
    class_count=`jq '.models_and_test_plans.monolithic | keys | length' $IRS_CTD_TEST_PLAN_FILE`
    [ $class_count -eq 5 ]

    # assert over test generation report
    [ -f $IRS_TESTGEN_SUMMARY_FILE ]
    [ `jq .building_block_sequences_info.base_sequences $IRS_TESTGEN_SUMMARY_FILE` -gt 0 ]
    [ `jq .building_block_sequences_info.parsed_base_sequences_full $IRS_TESTGEN_SUMMARY_FILE` -gt 0 ]
    [ `jq .building_block_sequences_info.parsed_base_sequences_partial $IRS_TESTGEN_SUMMARY_FILE` -eq 0 ]
    [ `jq .building_block_sequences_info.method_sequence_pool_keys $IRS_TESTGEN_SUMMARY_FILE` -gt 0 ]
    [ `jq .building_block_sequences_info.class_sequence_pool_keys $IRS_TESTGEN_SUMMARY_FILE` -gt 0 ]
    [ `jq .extended_sequences_info.generated_sequences $IRS_TESTGEN_SUMMARY_FILE` -gt 0 ]
    [ `jq .extended_sequences_info.executed_sequences $IRS_TESTGEN_SUMMARY_FILE` -gt 0 ]
    [ `jq .extended_sequences_info.failing_sequences $IRS_TESTGEN_SUMMARY_FILE` -eq 2 ]
    [ `jq .extended_sequences_info.final_sequences $IRS_TESTGEN_SUMMARY_FILE` -gt 0 ]
    [ `jq .test_plan_coverage_info.test_plan_rows $IRS_TESTGEN_SUMMARY_FILE` -eq 22 ]
    [ `jq .test_plan_coverage_info.rows_covered_full $IRS_TESTGEN_SUMMARY_FILE` -gt 0 ]
    [ `jq .test_plan_coverage_info.rows_covered_bb_sequences $IRS_TESTGEN_SUMMARY_FILE` -gt 0 ]

    # assert over generated test cases
    [ -d $IRS_CTD_AMPLIFIED_TESTDIR ]
    test_count=`find $IRS_CTD_AMPLIFIED_TESTDIR -name *.java -exec grep "@Test" {} \; | wc -l`
    echo "# test_count=$test_count" >&3
    [ $test_count -gt 0 ]

    # assert build file is generated
    [ -f $IRS_CTD_AMPLIFIED_TESTDIR/build.xml ]
}

@test "Test 12: CLI [build_type=maven] generate [all-classes] ctd-amplified [reuse_base_tests] irs" {
    run docker-compose run --rm tkltest-cli tkltest-unit \
        --config-file $IRS_CONFIG_FILE \
        --build-type maven \
        --test-directory $IRS_CTD_AMPLIFIED_TESTDIR \
        generate ctd-amplified --reuse-base-tests
    [ $status -eq 0 ]

    # assert over test reports dir
    [ -d ./$IRS_TEST_REPORTS_DIR/ctd-report ]

    # assert over test plan file
    [ -f $IRS_CTD_TEST_PLAN_FILE ]
    class_count=`jq '.models_and_test_plans.monolithic | keys | length' $IRS_CTD_TEST_PLAN_FILE`
    [ $class_count -eq 5 ]

    # assert over test generation report
    [ -f $IRS_TESTGEN_SUMMARY_FILE ]
    [ `jq .building_block_sequences_info.base_sequences $IRS_TESTGEN_SUMMARY_FILE` -gt 0 ]
    [ `jq .building_block_sequences_info.parsed_base_sequences_full $IRS_TESTGEN_SUMMARY_FILE` -gt 0 ]
    [ `jq .building_block_sequences_info.parsed_base_sequences_partial $IRS_TESTGEN_SUMMARY_FILE` -eq 0 ]
    [ `jq .building_block_sequences_info.method_sequence_pool_keys $IRS_TESTGEN_SUMMARY_FILE` -gt 0 ]
    [ `jq .building_block_sequences_info.class_sequence_pool_keys $IRS_TESTGEN_SUMMARY_FILE` -gt 0 ]
    [ `jq .extended_sequences_info.generated_sequences $IRS_TESTGEN_SUMMARY_FILE` -gt 0 ]
    [ `jq .extended_sequences_info.executed_sequences $IRS_TESTGEN_SUMMARY_FILE` -gt 0 ]
    [ `jq .extended_sequences_info.failing_sequences $IRS_TESTGEN_SUMMARY_FILE` -eq 2 ]
    [ `jq .extended_sequences_info.final_sequences $IRS_TESTGEN_SUMMARY_FILE` -gt 0 ]
    [ `jq .test_plan_coverage_info.test_plan_rows $IRS_TESTGEN_SUMMARY_FILE` -eq 22 ]
    [ `jq .test_plan_coverage_info.rows_covered_full $IRS_TESTGEN_SUMMARY_FILE` -gt 0 ]
    [ `jq .test_plan_coverage_info.rows_covered_bb_sequences $IRS_TESTGEN_SUMMARY_FILE` -gt 0 ]

    # assert over generated test cases
    [ -d $IRS_CTD_AMPLIFIED_TESTDIR ]
    test_count=`find $IRS_CTD_AMPLIFIED_TESTDIR -name *.java -exec grep "@Test" {} \; | wc -l`
    echo "# test_count=$test_count" >&3
    [ $test_count -gt 0 ]

    # assert build file is generated
    [ -f $IRS_CTD_AMPLIFIED_TESTDIR/pom.xml ]
}

@test "Test 13: CLI [build_type=gradle] generate [all-classes] ctd-amplified [reuse_base_tests] irs" {
    run docker-compose run --rm tkltest-cli tkltest-unit \
        --config-file $IRS_CONFIG_FILE \
        --build-type gradle \
        --test-directory $IRS_CTD_AMPLIFIED_TESTDIR \
        generate ctd-amplified --reuse-base-tests
    [ $status -eq 0 ]

    # assert over test reports dir
    [ -d ./$IRS_TEST_REPORTS_DIR/ctd-report ]

    # assert over test plan file
    [ -f $IRS_CTD_TEST_PLAN_FILE ]
    class_count=`jq '.models_and_test_plans.monolithic | keys | length' $IRS_CTD_TEST_PLAN_FILE`
    [ $class_count -eq 5 ]

    # assert over test generation report
    [ -f $IRS_TESTGEN_SUMMARY_FILE ]
    [ `jq .building_block_sequences_info.base_sequences $IRS_TESTGEN_SUMMARY_FILE` -gt 0 ]
    [ `jq .building_block_sequences_info.parsed_base_sequences_full $IRS_TESTGEN_SUMMARY_FILE` -gt 0 ]
    [ `jq .building_block_sequences_info.parsed_base_sequences_partial $IRS_TESTGEN_SUMMARY_FILE` -eq 0 ]
    [ `jq .building_block_sequences_info.method_sequence_pool_keys $IRS_TESTGEN_SUMMARY_FILE` -gt 0 ]
    [ `jq .building_block_sequences_info.class_sequence_pool_keys $IRS_TESTGEN_SUMMARY_FILE` -gt 0 ]
    [ `jq .extended_sequences_info.generated_sequences $IRS_TESTGEN_SUMMARY_FILE` -gt 0 ]
    [ `jq .extended_sequences_info.executed_sequences $IRS_TESTGEN_SUMMARY_FILE` -gt 0 ]
    [ `jq .extended_sequences_info.failing_sequences $IRS_TESTGEN_SUMMARY_FILE` -eq 2 ]
    [ `jq .extended_sequences_info.final_sequences $IRS_TESTGEN_SUMMARY_FILE` -gt 0 ]
    [ `jq .test_plan_coverage_info.test_plan_rows $IRS_TESTGEN_SUMMARY_FILE` -eq 22 ]
    [ `jq .test_plan_coverage_info.rows_covered_full $IRS_TESTGEN_SUMMARY_FILE` -gt 0 ]
    [ `jq .test_plan_coverage_info.rows_covered_bb_sequences $IRS_TESTGEN_SUMMARY_FILE` -gt 0 ]

    # assert over generated test cases
    [ -d $IRS_CTD_AMPLIFIED_TESTDIR ]
    test_count=`find $IRS_CTD_AMPLIFIED_TESTDIR -name *.java -exec grep "@Test" {} \; | wc -l`
    echo "# test_count=$test_count" >&3
    [ $test_count -gt 0 ]

    # assert build file is generated
    [ -f $IRS_CTD_AMPLIFIED_TESTDIR/build.gradle ]
}
