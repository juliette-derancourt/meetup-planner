#!/bin/bash

CURRENT=$(git rev-list -n1 HEAD)
PREVIOUS=$(git rev-list --all --parents | grep " ${CURRENT}" | cut -d" " -f1)

git checkout -f "$PREVIOUS"