#!/bin/sh
rm -f ~/.m2/repository/org/konveyor/tackle-test-generator-core/1.0.0-SNAPSHOT/tackle-test-generator-core-1.0.0-SNAPSHOT.jar
mvn -s ./settings.xml download:wget@get-randoop-jar download:wget@get-replacecall-jar
mvn -s ./settings.xml dependency:copy-dependencies -DoutputDirectory=./download
