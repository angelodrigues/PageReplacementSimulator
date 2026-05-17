#!/usr/bin/env bash
set -e
mkdir -p bin
find src/main/java -name "*.java" > sources.txt
javac -d bin @sources.txt
rm sources.txt
echo "Build OK -> bin/"
echo "Console:  java -cp bin com.unifor.simulator.Main"
echo "GUI:      java -cp bin com.unifor.simulator.Main --gui"
