#!/bin/bash

set -e

FROM_JIRA=$1
TO_REPO=$2

java -jar quarkus-run.jar --from-jira --to-repo=${TO_REPO}
