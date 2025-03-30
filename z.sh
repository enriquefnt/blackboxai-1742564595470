#!/bin/bash

# Backup
cp build_apk.sh README.md /tmp/

# Reset repo
rm -rf .git
git init
git remote add origin https://github.com/enriquefnt/blackboxai-1742564595470.git

# Clean all
rm -rf *
rm -f .*

# Restore
cp /tmp/build_apk.sh /tmp/README.md .
chmod +x build_apk.sh

# Push
git add .
git commit -m "Clean"
git branch -M main
git push -f origin main