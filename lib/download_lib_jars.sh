#!/bin/sh

rm -r ~/.m2/repository/com/github/konveyor/tackle-test-generator-core/
#mvn --no-transfer-progress download:wget@get-randoop-jar download:wget@get-replacecall-jar download:wget@get-evosuite-jar download:wget@get-evosuite-runtime-jar
mvn --no-transfer-progress download:wget@get-replacecall-jar
mvn --no-transfer-progress dependency:copy-dependencies -DoutputDirectory=./download
mv download/tackle-test-generator-unit-main-*.jar download/tackle-test-generator-unit-main-SNAPSHOT.jar
mvn --no-transfer-progress -f pom_ui.xml package
