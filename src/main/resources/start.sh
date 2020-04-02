#!/bin/sh
#pid=`ps aux | grep java | grep myblog.jar | awk '{print $2}'`
pid=`jps -l | grep myblog.jar | cut -d " " -f 1`
if [ "$pid" != "" ]; then
  echo "myblog.jar is running!"
else
  java -Xms2048M -Xmx4096M -jar myblog.jar &
fi