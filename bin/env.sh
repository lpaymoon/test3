###!/bin/bash

echo "### set ENV ###"

CP=.

#for jarfile in `ls -1 ./lib/*.jar`

jarfiles=$(ls ./lib/*.jar)
for jarfile in $jarfiles
do
        CP="$CP:$jarfile"
done

SERVER_NAME=upRestBusi
export DATE=`date +'%F.%T'`
export CLASSPATH=$CP
export LANG=zh_CN.GB18030
export APP=com.umpay.hfrestbusi.core.Server
export TAG=upRestBusi

ulimit -s 128
ulimit -n 1024

echo "### set ulimit -s `ulimit -s`"
echo "### set ulimit -n `ulimit -n`"
echo "### set LANG=$LANG"
echo "### set CLASSPATH=$CLASSPATH"
echo "### set ENV ### END"


