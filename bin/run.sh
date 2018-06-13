#!/bin/bash

#进入src目录执行

#编译
javac -Djava.ext.dirs=../lib/ ./*/*.java

#执行
java -Djava.ext.dirs=../lib/ main/Main
