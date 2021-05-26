DAYTRADER_CONFIG_FILE=./test/data/daytrader7/tkltest_config.toml
DAYTRADER_PARTITIONS_FILE=./test/data/daytrader7/refactored/PartitionsFile.json
DAYTRADER_CTD_TEST_PLAN_FILE=./daytrader7_ctd_models_and_test_plans.json
DAYTRADER_TESTGEN_SUMMARY_FILE=./daytrader7_test_generation_summary.json

DAYTRADER_CTD_AMPLIFIED_TESTDIR=./daytrader7-ctd-amplified-tests
DAYTRADER_TEST_REPORTS_DIR=./daytrader7-tkltest-reports

# setup commands run befeore execution of tests in file
setup_file() {
    echo "# setup_file: removing daytrader tests, test reports, and test plan" >&3
    rm -rf $DAYTRADER_CTD_AMPLIFIED_TESTDIR $DAYTRADER_TEST_REPORTS_DIR
    rm -f $DAYTRADER_CTD_TEST_PLAN_FILE $DAYTRADER_TESTGEN_SUMMARY_FILE
}

@test "Test 01: CLI generate [target-class-list] ctd-amplified --base-test-generator randoop daytrader" {   
    run tkltest \
        --config-file $DAYTRADER_CONFIG_FILE \
        --test-directory $DAYTRADER_CTD_AMPLIFIED_TESTDIR \
        generate ctd-amplified --base-test-generator randoop
    [ $status -eq 0 ]

    # assert over test reports dir
    [ -d ./$DAYTRADER_TEST_REPORTS_DIR/ctd-report ]

    # assert over test plan file
    [ -f $DAYTRADER_CTD_TEST_PLAN_FILE ]
    class_count=`jq '.models_and_test_plans.monolithic | keys | length' $DAYTRADER_CTD_TEST_PLAN_FILE`
    [ $class_count -eq 2 ]

    # assert over test generation report
    [ -f $DAYTRADER_TESTGEN_SUMMARY_FILE ]
    [ `jq .building_block_sequences_info.bb_sequences $DAYTRADER_TESTGEN_SUMMARY_FILE` -gt 0 ]
    [ `jq .building_block_sequences_info.parsed_sequences $DAYTRADER_TESTGEN_SUMMARY_FILE` -gt 0 ]
    [ `jq .extended_sequences_info.generated_sequences $DAYTRADER_TESTGEN_SUMMARY_FILE` -gt 0 ]
    [ `jq .extended_sequences_info.executed_sequences $DAYTRADER_TESTGEN_SUMMARY_FILE` -gt 0 ]
    [ `jq .extended_sequences_info.failing_sequences $DAYTRADER_TESTGEN_SUMMARY_FILE` -eq 0 ]
    [ `jq .extended_sequences_info.final_sequences $DAYTRADER_TESTGEN_SUMMARY_FILE` -gt 0 ]
    [ `jq .test_plan_coverage_info.test_plan_rows $DAYTRADER_TESTGEN_SUMMARY_FILE` -gt 0 ]
    [ `jq .test_plan_coverage_info.rows_covered_full $DAYTRADER_TESTGEN_SUMMARY_FILE` -gt 0 ]
    [ `jq .test_plan_coverage_info.rows_covered_bb_sequences $DAYTRADER_TESTGEN_SUMMARY_FILE` -gt 0 ]

    # assert over generated test cases
    [ -d $DAYTRADER_CTD_AMPLIFIED_TESTDIR ]
    test_count=`find $DAYTRADER_CTD_AMPLIFIED_TESTDIR -name *.java -exec grep "@Test" {} \; | wc -l`
    echo "# test_count=$test_count" >&3
    [ $test_count -gt 0 ]
}

@test "Test 02: CLI execute mono daytrader" {
    run tkltest --log-level INFO \
        --config-file $DAYTRADER_CONFIG_FILE \
        --test-directory $DAYTRADER_CTD_AMPLIFIED_TESTDIR \
        execute mono
    [ $status -eq 0 ]

        # assert over reports dirs and instrumented classes dir
    [ -d ./$DAYTRADER_TEST_REPORTS_DIR/jacoco-reports ]
    [ -d ./$DAYTRADER_TEST_REPORTS_DIR/junit-reports ]
}

@test "Test 03: CLI generate --partitions-file ctd-amplified daytrader" {   
    rm -rf $DAYTRADER_CTD_AMPLIFIED_TESTDIR $DAYTRADER_TEST_REPORTS_DIR
    rm -f $DAYTRADER_CTD_TEST_PLAN_FILE $DAYTRADER_TESTGEN_SUMMARY_FILE
    run tkltest \
        --config-file $DAYTRADER_CONFIG_FILE \
        --test-directory $DAYTRADER_CTD_AMPLIFIED_TESTDIR \
        generate --partitions-file $DAYTRADER_PARTITIONS_FILE ctd-amplified
    [ $status -eq 0 ]

    # assert over test reports dir
    [ -d ./$DAYTRADER_TEST_REPORTS_DIR/ctd-report ]

    # assert over test plan file
    [ -f $DAYTRADER_CTD_TEST_PLAN_FILE ]
    class_count=`jq '.models_and_test_plans | keys | length' $DAYTRADER_CTD_TEST_PLAN_FILE`
    [ $class_count -eq 4 ]

    # assert over test generation report
    [ -f $DAYTRADER_TESTGEN_SUMMARY_FILE ]
    [ `jq .extended_sequences_info.final_sequences $DAYTRADER_TESTGEN_SUMMARY_FILE` -gt 0 ]
    [ `jq .test_plan_coverage_info.test_plan_rows $DAYTRADER_TESTGEN_SUMMARY_FILE` -gt 0 ]
    [ `jq .test_plan_coverage_info.rows_covered_full $DAYTRADER_TESTGEN_SUMMARY_FILE` -gt 0 ]
    [ `jq .test_plan_coverage_info.rows_covered_bb_sequences $DAYTRADER_TESTGEN_SUMMARY_FILE` -gt 0 ]

    # assert over generated test cases
    [ -d $DAYTRADER_CTD_AMPLIFIED_TESTDIR ]
    test_count=`find $DAYTRADER_CTD_AMPLIFIED_TESTDIR -name *.java -exec grep "@Test" {} \; | wc -l`
    echo "# test_count=$test_count" >&3
    [ $test_count -gt 0 ]
}

@test "Test 04: CLI execute mono daytrader" {
    run tkltest --log-level INFO \
        --config-file $DAYTRADER_CONFIG_FILE \
        --test-directory $DAYTRADER_CTD_AMPLIFIED_TESTDIR \
        execute mono
    [ $status -eq 0 ]

        # assert over reports dirs and instrumented classes dir
    [ -d ./$DAYTRADER_TEST_REPORTS_DIR/jacoco-reports ]
    [ -d ./$DAYTRADER_TEST_REPORTS_DIR/junit-reports ]
}
