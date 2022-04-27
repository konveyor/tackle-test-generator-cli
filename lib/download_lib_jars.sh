#!/bin/sh
rm -r ~/.m2/repository/org/konveyor/tackle-test-generator-unit/
rm -r ~/.m2/repository/org/konveyor/tackle-test-generator-ui/
mvn -s ./settings.xml -q download:wget@get-randoop-jar download:wget@get-replacecall-jar download:wget@get-evosuite-jar download:wget@get-evosuite-runtime-jar
mvn -s ./settings.xml -q dependency:copy-dependencies -DoutputDirectory=./download
