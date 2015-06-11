#!/bin/bash

javac -d bin/ -verbose @<(find net/ -name *.java)
