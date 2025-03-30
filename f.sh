#!/bin/bash

# Backup
cp build_apk.sh README.md /tmp/

# Clean everything
cd ..
rm -rf user-workspace
mkdir user-workspace
cd user-workspace

# Init new repo
git init
git remote add origin https://github.com/enriquefnt/blackboxai-1742564595470.git

# Restore only needed files
cp /tmp/build_apk.sh .
cp /tmp/README.md .
chmod +x build_apk.sh

# Push clean repo
git add .
git commit -m "Clean repo"
git branch -M main
git push -f origin main

echo "Done. Only essential files remain:"
ls -la