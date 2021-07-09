#!/bin/sh

mvn -s ./settings.xml download:wget@get-randoop-jar download:wget@get-replacecall-jar
mvn -s ./settings.xml dependency:copy-dependencies -DoutputDirectory=./download
