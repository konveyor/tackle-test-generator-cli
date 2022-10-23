#!/bin/sh

rm -f tackle-test-generator-unit-main-SNAPSHOT.jar
mvn --no-transfer-progress download:wget@get-replacecall-jar
mvn --no-transfer-progress dependency:copy-dependencies -DoutputDirectory=./
mv tackle-test-generator-unit-main-*.jar tackle-test-generator-unit-main-SNAPSHOT.jar
