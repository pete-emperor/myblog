#!/bin/sh
#pid=`ps aux | grep java | grep  myblog.jar | awk '{print $2}'`
pid=`jps -l | grep  myblog.jar | cut -d " " -f 1`
if [ "$pid" != "" ]; then
  kill -9 $pid
  echo "stopping  myblog.jar..."
else
  echo " myblog.jar not run!"
fi 