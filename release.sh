#!/bin/bash
#
# Release a new version of the rbr-server
# Put the artifact into artifactory

mvn clean
mvn -B release:prepare
mvn -B release:perform
