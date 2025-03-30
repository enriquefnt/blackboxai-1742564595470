#!/bin/bash

# Backup essential files
mkdir -p /tmp/backup
cp build_apk.sh /tmp/backup/
cp README.md /tmp/backup/

# Clean repository
cd ..
rm -rf user-workspace
git clone https://github.com/enriquefnt/blackboxai-1742564595470.git user-workspace
cd user-workspace

# Remove everything
rm -rf *
rm -f .*

# Restore essential files
cp /tmp/backup/* .
rm -rf /tmp/backup
chmod +x build_apk.sh

# Configure git
git config --global user.email "enriquefnt@gmail.com"
git config --global user.name "enriquefnt"

# Push clean repository
git add .
git commit -m "Clean repository"
git push -f origin main

echo "Repository cleaned. Only essential files remain:"
ls -la