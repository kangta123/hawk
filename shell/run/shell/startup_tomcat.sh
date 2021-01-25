#!/bin/sh

source /app/bin/jvm.sh

sh /app/bin/startup.sh

sh $CATALINA_HOME/bin/catalina.sh run
