#!/bin/bash

export orig_epbase=http://trelx51x:8080/
export target_epbase=http://127.0.0.1:8180/

export rfiles=`grep -lsr $orig_epbase {arcusys-portlet,intalio} |grep -v svn`

for i in $rfiles
do
  sed -i'' -e "s,$orig_epbase,$target_epbase," $i
done

