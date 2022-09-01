IRS_OUTPUT_DIR=./tkltest-output-unit-irs
IRS_CONFIG_FILE=./test/data/irs/tkltest_config.toml
IRS_CONFIG_FILE2=./test/data/irs/tkltest_config2.toml
IRS_CTD_TEST_PLAN_FILE=$IRS_OUTPUT_DIR/irs_ctd_models_and_test_plans.json
IRS_TESTGEN_SUMMARY_FILE=$IRS_OUTPUT_DIR//irs_test_generation_summary.json

IRS_CTD_AMPLIFIED_TESTDIR=./irs-ctd-amplified-tests
IRS_RANDOOP_TESTDIR=./irs-randoop-standalone-tests
IRS_EVOSUITE_TESTDIR=./irs-evosuite-standalone-tests
IRS_TEST_REPORTS_DIR=$IRS_OUTPUT_DIR//irs-tkltest-reports
IRS_TEST_BUILD_DIR=$IRS_OUTPUT_DIR//irs-tkltest-build-artifacts
IRS_INSTR_CLASSES_DIR=$IRS_OUTPUT_DIR/irs-instrumented-classes

# setup commands run before execution of tests in file
setup_file() {
    echo "# setup_file: removing irs tests, test reports, and test plan" >&3
    rm -rf $IRS_CTD_AMPLIFIED_TESTDIR $IRS_RANDOOP_TESTDIR $IRS_EVOSUITE_TESTDIR \
        $IRS_TEST_REPORTS_DIR $IRS_INSTR_CLASSES_DIR
    rm -f $IRS_CTD_TEST_PLAN_FILE
    rm -rf ./irs-randoop-standalone-tests
}

@test "Test 01: CLI generate [all-classes] --no-diff-assertions ctd-amplified irs" {
    run tkltest-unit --log-level INFO \
        --config-file $IRS_CONFIG_FILE \
        --test-directory $IRS_CTD_AMPLIFIED_TESTDIR \
        generate --no-diff-assertions ctd-amplified --no-augment-coverage
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
    [ -f $IRS_TEST_BUILD_DIR/build.xml ]
}

@test "Test 02: CLI execute irs" {
    rm -rf $IRS_TEST_REPORTS_DIR
    run tkltest-unit --log-level INFO \
        --config-file $IRS_CONFIG_FILE \
        --test-directory $IRS_CTD_AMPLIFIED_TESTDIR \
        --offline-instrumentation \
        execute
    [ $status -eq 0 ]

    # assert over reports dirs and instrumented classes dir
    [ -d ./$IRS_TEST_REPORTS_DIR/jacoco-reports ]
    [ -d ./$IRS_TEST_REPORTS_DIR/junit-reports ]
    [ -d $IRS_INSTR_CLASSES_DIR ]

    # assert over test failures and errors
    partition_rep_dir=$IRS_TEST_REPORTS_DIR/junit-reports/$IRS_CTD_AMPLIFIED_TESTDIR/raw

    employee_test_errors_count=`xmllint $partition_rep_dir/TEST-irs.irs_Employee_Test.xml -xpath 'string(testsuite/@errors)'`
    echo "# employee_test_errors_count=$employee_test_errors_count" >&3
    
    employee_test_failures_count=`xmllint $partition_rep_dir/TEST-irs.irs_Employee_Test.xml -xpath 'string(testsuite/@failures)'`
    echo "# employee_test_failures_count=$employee_test_failures_count" >&3

    [ $employee_test_errors_count -eq 0 ]
    [ $employee_test_failures_count -eq 0 ]
    [ `xmllint $partition_rep_dir/TEST-irs.irs_Employer_Test.xml -xpath 'string(testsuite/@errors)'` -eq 0 ]
    [ `xmllint $partition_rep_dir/TEST-irs.irs_Employer_Test.xml -xpath 'string(testsuite/@failures)'` -eq 0 ]
    [ `xmllint $partition_rep_dir/TEST-irs.irs_IRS_Test.xml -xpath 'string(testsuite/@errors)'` -eq 0 ]
    [ `xmllint $partition_rep_dir/TEST-irs.irs_IRS_Test.xml -xpath 'string(testsuite/@failures)'` -eq 0 ]
    [ `xmllint $partition_rep_dir/TEST-irs.irs_Salary_Test.xml -xpath 'string(testsuite/@errors)'` -eq 0 ]
    [ `xmllint $partition_rep_dir/TEST-irs.irs_Salary_Test.xml -xpath 'string(testsuite/@failures)'` -eq 0 ]
    [ `xmllint $partition_rep_dir/TEST-irs.irs_BusinessProcess_Test.xml -xpath 'string(testsuite/@errors)'` -eq 0 ]
    [ `xmllint $partition_rep_dir/TEST-irs.irs_BusinessProcess_Test.xml -xpath 'string(testsuite/@failures)'` -eq 0 ]
}


@test "Test 03: CLI execute --online-instrumentation irs" {
    rm -rf $IRS_TEST_REPORTS_DIR
    run tkltest-unit --log-level INFO \
        --config-file $IRS_CONFIG_FILE \
        --test-directory $IRS_CTD_AMPLIFIED_TESTDIR \
        execute
    [ $status -eq 0 ]

    # assert over reports dirs and instrumented classes dir
    [ -d ./$IRS_TEST_REPORTS_DIR/jacoco-reports ]
    [ -d ./$IRS_TEST_REPORTS_DIR/junit-reports ]
}

@test "Test 04: CLI generate [all-classes] ctd-amplified [diff-assertions] irs" {
    rm -rf $IRS_CTD_TEST_PLAN_FILE $IRS_CTD_AMPLIFIED_TESTDIR $IRS_TEST_REPORTS_DIR
    run tkltest-unit --log-level INFO \
        --config-file $IRS_CONFIG_FILE \
        --test-directory $IRS_CTD_AMPLIFIED_TESTDIR \
        generate ctd-amplified --no-augment-coverage
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
}

@test "Test 05: CLI execute irs" {
    rm -rf $IRS_TEST_REPORTS_DIR
    run tkltest-unit --log-level INFO \
        --config-file $IRS_CONFIG_FILE \
        --test-directory $IRS_CTD_AMPLIFIED_TESTDIR \
        execute
    [ $status -eq 0 ]

    # assert over reports dirs
    [ -d ./$IRS_TEST_REPORTS_DIR/jacoco-reports ]
    [ -d ./$IRS_TEST_REPORTS_DIR/junit-reports ]

    # assert over test failures and errors
    partition_rep_dir=$IRS_TEST_REPORTS_DIR/junit-reports/$IRS_CTD_AMPLIFIED_TESTDIR/raw

    employee_test_errors_count=`xmllint $partition_rep_dir/TEST-irs.irs_Employee_Test.xml -xpath 'string(testsuite/@errors)'`
    echo "# employee_test_errors_count=$employee_test_errors_count" >&3
    
    employee_test_failures_count=`xmllint $partition_rep_dir/TEST-irs.irs_Employee_Test.xml -xpath 'string(testsuite/@failures)'`
    echo "# employee_test_failures_count=$employee_test_failures_count" >&3
    
    employer_test_errors_count=`xmllint $partition_rep_dir/TEST-irs.irs_Employer_Test.xml -xpath 'string(testsuite/@errors)'`
    echo "# employer_test_errors_count=$employer_test_errors_count" >&3
    
    employer_test_failures_count=`xmllint $partition_rep_dir/TEST-irs.irs_Employer_Test.xml -xpath 'string(testsuite/@failures)'`
    echo "# employer_test_failures_count=$employer_test_failures_count" >&3

    [ $employee_test_errors_count -eq 0 ]
    [ $employee_test_failures_count -eq 0 ]
    [ $employer_test_errors_count -eq 0 ]
    [ $employer_test_failures_count -eq 0 ]
    [ `xmllint $partition_rep_dir/TEST-irs.irs_IRS_Test.xml -xpath 'string(testsuite/@errors)'` -eq 0 ]
    [ `xmllint $partition_rep_dir/TEST-irs.irs_IRS_Test.xml -xpath 'string(testsuite/@failures)'` -eq 0 ]
    [ `xmllint $partition_rep_dir/TEST-irs.irs_Salary_Test.xml -xpath 'string(testsuite/@errors)'` -eq 0 ]
    [ `xmllint $partition_rep_dir/TEST-irs.irs_Salary_Test.xml -xpath 'string(testsuite/@failures)'` -eq 0 ]
    [ `xmllint $partition_rep_dir/TEST-irs.irs_BusinessProcess_Test.xml -xpath 'string(testsuite/@errors)'` -eq 0 ]
    [ `xmllint $partition_rep_dir/TEST-irs.irs_BusinessProcess_Test.xml -xpath 'string(testsuite/@failures)'` -eq 0 ]
}

@test "Test 051: CLI [build_type=maven] generate [all-classes] ctd-amplified [reuse_base_tests] irs" {
    run tkltest-unit --log-level INFO \
        --config-file $IRS_CONFIG_FILE \
        --build-type maven \
        --test-directory $IRS_CTD_AMPLIFIED_TESTDIR \
        generate ctd-amplified --reuse-base-tests
    [ $status -eq 0 ]
}

@test "Test 06: CLI generate execute [all-classes] randoop irs" {
    run tkltest-unit --log-level INFO \
        --config-file $IRS_CONFIG_FILE \
        --test-directory $IRS_RANDOOP_TESTDIR \
        generate randoop
    [ $status -eq 0 ]

    # assert over generated test cases
    [ -d $IRS_RANDOOP_TESTDIR ]
    test_count=`find $IRS_RANDOOP_TESTDIR -name *.java -exec grep "@Test" {} \; | wc -l`
    echo "# test_count=$test_count" >&3
    [ $test_count -gt 0 ]

    # assert build file is generated
    [ -f $IRS_TEST_BUILD_DIR/build.xml ]

    # execute generated tests
    run tkltest-unit --log-level INFO \
        --config-file $IRS_CONFIG_FILE \
        --test-directory $IRS_RANDOOP_TESTDIR \
        execute
    [ $status -eq 0 ]
}

@test "Test 07: CLI generate execute [all-classes] evosuite irs" {
    run tkltest-unit --log-level INFO \
        --config-file $IRS_CONFIG_FILE \
        --test-directory $IRS_EVOSUITE_TESTDIR \
        generate evosuite
    [ $status -eq 0 ]

    # assert over generated test cases
    [ -d $IRS_EVOSUITE_TESTDIR ]
    test_count=`find $IRS_EVOSUITE_TESTDIR -name *.java -exec grep "@Test" {} \; | wc -l`
    echo "# test_count=$test_count" >&3
    [ $test_count -gt 0 ]

    # assert build file is generated
    [ -f $IRS_TEST_BUILD_DIR/build.xml ]

    # execute generated tests
    run tkltest-unit --log-level INFO \
        --config-file $IRS_CONFIG_FILE \
        --test-directory $IRS_EVOSUITE_TESTDIR \
        execute
    [ $status -eq 0 ]
}

@test "Test 08: CLI generate execute [target-class-list] ctd-amplified irs" {
    rm -rf $IRS_CTD_TEST_PLAN_FILE $IRS_CTD_AMPLIFIED_TESTDIR $IRS_TEST_REPORTS_DIR
    run tkltest-unit --log-level INFO \
        --config-file $IRS_CONFIG_FILE2 \
        --test-directory $IRS_CTD_AMPLIFIED_TESTDIR \
        generate --no-diff-assertions ctd-amplified --no-augment-coverage
    [ $status -eq 0 ]

    # assert over test reports dir
    [ -d ./$IRS_TEST_REPORTS_DIR/ctd-report ]

    # assert over test plan file
    [ -f $IRS_CTD_TEST_PLAN_FILE ]
    class_count=`jq '.models_and_test_plans.monolithic | keys | length' $IRS_CTD_TEST_PLAN_FILE`
    [ $class_count -eq 2 ]

    # assert over generated test cases
    [ -d $IRS_CTD_AMPLIFIED_TESTDIR ]
    test_count=`find $IRS_CTD_AMPLIFIED_TESTDIR -name *.java -exec grep "@Test" {} \; | wc -l`
    echo "# test_count=$test_count" >&3
    [ $test_count -gt 0 ]

    # assert build file is generated
    [ -f $IRS_TEST_BUILD_DIR/pom.xml ]

    # execute generated tests
    run tkltest-unit --log-level INFO \
        --config-file $IRS_CONFIG_FILE2 \
        --test-directory $IRS_CTD_AMPLIFIED_TESTDIR \
        execute
    [ $status -eq 0 ]
}

@test "Test 09: CLI generate [target-class-list] randoop irs" {
    rm -rf $IRS_RANDOOP_TESTDIR
    run tkltest-unit --log-level INFO \
        --config-file $IRS_CONFIG_FILE2 \
        --test-directory $IRS_RANDOOP_TESTDIR \
        generate randoop
    [ $status -eq 0 ]

    # assert over generated test cases
    [ -d $IRS_RANDOOP_TESTDIR ]
    test_count=`find $IRS_RANDOOP_TESTDIR -name *.java -exec grep "@Test" {} \; | wc -l`
    echo "# test_count=$test_count" >&3
    [ $test_count -gt 0 ]

    # execute generated tests
    run tkltest-unit --log-level INFO \
        --config-file $IRS_CONFIG_FILE2 \
        --test-directory $IRS_RANDOOP_TESTDIR \
        execute
    [ $status -eq 0 ]
}

@test "Test 10: CLI generate [target-class-list] evosuite irs" {
    rm -rf $IRS_EVOSUITE_TESTDIR
    run tkltest-unit --log-level INFO \
        --config-file $IRS_CONFIG_FILE2 \
        --test-directory $IRS_EVOSUITE_TESTDIR \
        generate evosuite
    [ $status -eq 0 ]

    # assert over generated test cases
    [ -d $IRS_EVOSUITE_TESTDIR ]
    test_count=`find $IRS_EVOSUITE_TESTDIR -name *.java -exec grep "@Test" {} \; | wc -l`
    echo "# test_count=$test_count" >&3
    [ $test_count -gt 0 ]

    # execute generated tests
    run tkltest-unit --log-level INFO \
        --config-file $IRS_CONFIG_FILE2 \
        --test-directory $IRS_EVOSUITE_TESTDIR \
        execute
    [ $status -eq 0 ]
}

@test "Test 11: CLI generate [all-classes] ctd-amplified --base-test-generator randoop irs" {
    skip
    rm -rf $IRS_CTD_TEST_PLAN_FILE $IRS_CTD_AMPLIFIED_TESTDIR $IRS_TEST_REPORTS_DIR
    run tkltest-unit --log-level INFO \
        --config-file $IRS_CONFIG_FILE \
        --test-directory $IRS_CTD_AMPLIFIED_TESTDIR \
        generate --no-diff-assertions ctd-amplified --base-test-generator randoop --no-augment-coverage
    [ $status -eq 0 ]

    # assert over test reports dir
    [ -d ./$IRS_TEST_REPORTS_DIR/ctd-report ]

    # assert over test plan file
    [ -f $IRS_CTD_TEST_PLAN_FILE ]
    class_count=`jq '.models_and_test_plans.monolithic | keys | length' $IRS_CTD_TEST_PLAN_FILE`
    [ $class_count -eq 5 ]

    # assert over generated test cases
    [ -d $IRS_CTD_AMPLIFIED_TESTDIR ]
    test_count=`find $IRS_CTD_AMPLIFIED_TESTDIR -name *.java -exec grep "@Test" {} \; | wc -l`
    echo "# test_count=$test_count" >&3
    [ $test_count -gt 0 ]
}

@test "Test 12: CLI generate [all-classes] ctd-amplified --base-test-generator evosuite irs" {
    skip
    rm -rf $IRS_CTD_TEST_PLAN_FILE $IRS_CTD_AMPLIFIED_TESTDIR $IRS_TEST_REPORTS_DIR
    run tkltest-unit --log-level INFO \
        --config-file $IRS_CONFIG_FILE \
        --test-directory $IRS_CTD_AMPLIFIED_TESTDIR \
        generate --no-diff-assertions ctd-amplified --base-test-generator evosuite --no-augment-coverage
    [ $status -eq 0 ]

    # assert over test reports dir
    [ -d ./$IRS_TEST_REPORTS_DIR/ctd-report ]

    # assert over test plan file
    [ -f $IRS_CTD_TEST_PLAN_FILE ]
    class_count=`jq '.models_and_test_plans.monolithic | keys | length' $IRS_CTD_TEST_PLAN_FILE`
    [ $class_count -eq 5 ]

    # assert over generated test cases
    [ -d $IRS_CTD_AMPLIFIED_TESTDIR ]
    test_count=`find $IRS_CTD_AMPLIFIED_TESTDIR -name *.java -exec grep "@Test" {} \; | wc -l`
    echo "# test_count=$test_count" >&3
    [ $test_count -gt 0 ]
}

@test "Test 13: CLI generate [all-classes] ctd-amplified --base-test-generator evosuite [augment-coverage] irs" {
    rm -rf $IRS_CTD_TEST_PLAN_FILE $IRS_CTD_AMPLIFIED_TESTDIR $IRS_TEST_REPORTS_DIR
    run tkltest-unit --log-level INFO \
        --config-file $IRS_CONFIG_FILE \
        --test-directory $IRS_CTD_AMPLIFIED_TESTDIR \
        generate ctd-amplified --base-test-generator evosuite
    [ $status -eq 0 ]

    # assert over test reports dir
    [ -d ./$IRS_TEST_REPORTS_DIR/ctd-report ]

    # assert over test plan file
    [ -f $IRS_CTD_TEST_PLAN_FILE ]
    class_count=`jq '.models_and_test_plans.monolithic | keys | length' $IRS_CTD_TEST_PLAN_FILE`
    [ $class_count -eq 5 ]

    # assert over generated test cases
    [ -d $IRS_CTD_AMPLIFIED_TESTDIR ]
    test_class_count=`find $IRS_CTD_AMPLIFIED_TESTDIR -name *.java | wc -l`
    echo "# test_class_count=$test_class_count" >&3
    [ $test_class_count -gt 5 ]
    test_case_count=`find $IRS_CTD_AMPLIFIED_TESTDIR -name *.java -exec grep "@Test" {} \; | wc -l`
    echo "# test_case_count=$test_case_count" >&3
    [ $test_case_count -gt 20 ]
}
