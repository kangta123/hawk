#!/bin/bash

JDK8=`java -version 2>&1 | grep version |grep 1.8`
JDK11=`java -version 2>&1 | grep version |grep 11`

LOGDIR="/app/logs"

APPID="app"

JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}')

ulimit -c unlimited


MEM_OPTS="-Xms${XMX} -Xmx${XMX} -XX:NewRatio=1"

MEM_OPTS="$MEM_OPTS -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=256m"

MEM_OPTS="$MEM_OPTS -XX:+AlwaysPreTouch"

MEM_OPTS="$MEM_OPTS -Xss${XSS}"


if [ -d /dev/shm/ ]; then
    GC_LOG_FILE=/dev/shm/gc-${APPID}.log
else
	GC_LOG_FILE=${LOGDIR}/gc-${APPID}.log
fi

if [ -f ${GC_LOG_FILE} ]; then
  GC_LOG_BACKUP=${LOGDIR}/gc-${APPID}-$(date +'%Y%m%d_%H%M%S').log
  echo "saving gc log ${GC_LOG_FILE} to ${GC_LOG_BACKUP}"
  mv ${GC_LOG_FILE} ${GC_LOG_BACKUP}
fi

OPTIMIZE_OPTS="-XX:-UseBiasedLocking -XX:AutoBoxCacheMax=20000 -Djava.security.egd=file:/dev/./urandom"

SHOOTING_OPTS="-XX:+PrintCommandLineFlags -XX:-OmitStackTraceInFastThrow -XX:ErrorFile=${LOGDIR}/hs_err_%p.log"

OTHER_OPTS=""
## For jdk8 ##
if [ -n "${JDK8}" ]; then
    OTHER_OPTS="$OTHER_OPTS -Djava.net.preferIPv4Stack=true -Dfile.encoding=UTF-8 -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap"
    GC_OPTS="$GC_OPTS -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=75 -XX:+UseCMSInitiatingOccupancyOnly -XX:+ExplicitGCInvokesConcurrent"
    GC_OPTS="$GC_OPTS -XX:+ParallelRefProcEnabled -XX:+CMSParallelInitialMarkEnabled"
    GC_OPTS="$GC_OPTS -XX:+CMSScavengeBeforeRemark"
    GC_OPTS="$GC_OPTS -XX:MaxTenuringThreshold=3"
    GC_OPTS="$GC_OPTS -XX:+UnlockDiagnosticVMOptions -XX:ParGCCardsPerStrideChunk=1024"

    GCLOG_OPTS="-Xloggc:${GC_LOG_FILE} -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintPromotionFailure -XX:+PrintGCApplicationStoppedTime"
fi

if [ -n "${JDK11}" ];then
    GCLOG_OPTS="-Xlog:gc*,safepoint:gc.log:time,uptime:filecount=100,filesize=128K"
fi

export JAVA_OPTS="$MEM_OPTS $GC_OPTS $GCLOG_OPTS $OPTIMIZE_OPTS $SHOOTING_OPTS $JMX_OPTS $OTHER_OPTS"
