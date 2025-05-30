#!/bin/bash

CURRENT=$(git rev-list -n1 HEAD)

git revert --no-commit "$CURRENT"