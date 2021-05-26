#!/bin/bash

mvn download:wget@get-randoop-jar download:wget@get-replacecall-jar
mvn dependency:copy-dependencies -DoutputDirectory=./download
