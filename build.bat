@echo off
if not exist bin mkdir bin
dir /s /b src\main\java\*.java > sources.txt
javac -d bin @sources.txt
del sources.txt
echo.
echo Build OK -^> bin\
echo Console:  java -cp bin com.unifor.simulator.Main
echo GUI:      java -cp bin com.unifor.simulator.Main --gui
