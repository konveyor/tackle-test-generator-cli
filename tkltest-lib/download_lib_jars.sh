#!/bin/sh

rm -r ~/.m2/repository/com/github/konveyor/tackle-test-generator-core/
#rm tackle-test-generator-unit-main-SNAPSHOT.jar
#mvn --no-transfer-progress download:wget@get-randoop-jar download:wget@get-replacecall-jar download:wget@get-evosuite-jar download:wget@get-evosuite-runtime-jar
#mvn --no-transfer-progress download:wget@get-replacecall-jar
#mvn --no-transfer-progress dependency:copy-dependencies -DoutputDirectory=./
#mv tackle-test-generator-unit-main-*.jar tackle-test-generator-unit-main-SNAPSHOT.jar
#mvn --no-transfer-progress -f pom_ui.xml package
./download_lib_jars_unit.sh
./download_lib_jars_ui.sh
