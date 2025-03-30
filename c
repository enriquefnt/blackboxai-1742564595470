#!/bin/bash
mkdir -p /tmp/backup
cp build_apk.sh /tmp/backup/
cp README.md /tmp/backup/
cd ..
rm -rf user-workspace
mkdir user-workspace
cd user-workspace
git init
git remote add origin https://github.com/enriquefnt/blackboxai-1742564595470.git
cp /tmp/backup/* .
rm -rf /tmp/backup
chmod +x build_apk.sh
git config --global user.email "enriquefnt@gmail.com"
git config --global user.name "enriquefnt"
git add .
git commit -m "Clean"
git branch -M main
git push -f origin main