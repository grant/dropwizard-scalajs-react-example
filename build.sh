#!/usr/bin/env bash
echo 'Starting build.sh...'

sbt "project server" assembly

echo 'Ending build.sh...'
