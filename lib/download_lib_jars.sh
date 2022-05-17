#!/bin/sh

rm -r ~/.m2/repository/org/konveyor/tackle-test-generator-unit/
rm -r ~/.m2/repository/org/konveyor/tackle-test-generator-ui/
#mvn --no-transfer-progress download:wget@get-randoop-jar download:wget@get-replacecall-jar download:wget@get-evosuite-jar download:wget@get-evosuite-runtime-jar
mvn --no-transfer-progress download:wget@get-replacecall-jar
mvn --no-transfer-progress dependency:copy-dependencies -DoutputDirectory=./download
mvn --no-transfer-progress -f pom_ui.xml package
