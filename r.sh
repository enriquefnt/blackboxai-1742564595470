#!/bin/bash

# Backup
cp build_apk.sh /tmp/
cp README.md /tmp/

# Clean
git rm -rf .
git clean -fdx

# Restore
cp /tmp/build_apk.sh .
cp /tmp/README.md .
chmod +x build_apk.sh

# Push
git add .
git commit -m "Clean"
git push -f origin main

ls