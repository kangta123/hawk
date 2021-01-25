#!/bin/sh

source jvm.sh

sh startup.sh

java ${JAVA_OPTS} -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}  -jar /app/jar/app.jar
