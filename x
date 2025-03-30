#!/bin/bash
cp build_apk.sh README.md /tmp/
cd ..
rm -rf user-workspace
git clone https://github.com/enriquefnt/blackboxai-1742564595470.git user-workspace
cd user-workspace
rm -rf *
cp /tmp/build_apk.sh .
cp /tmp/README.md .
chmod +x build_apk.sh
git config --global user.email "enriquefnt@gmail.com"
git config --global user.name "enriquefnt"
git add .
git commit -m "Clean"
git push -f