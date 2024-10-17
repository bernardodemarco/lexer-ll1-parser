#!/bin/bash

javac -d ./out $(find . -name "*.java")    
java -cp ./out Main
