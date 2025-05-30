#!/bin/bash

NEXT=$(git rev-list -n1 HEAD~1)

git clean -f
git checkout -f "$NEXT"
