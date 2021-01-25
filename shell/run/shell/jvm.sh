#!/bin/bash

JVM_FILE="/app/bin/jvm-options-${PERFORMANCE_LEVEL:-NORMAL}.sh"

source ${JVM_FILE}

export LD_LIBRARY_PATH=/app/shared/jprofiler11.0.1/bin/linux-x64

JAVA_OPTS="${JAVA_OPTS} -javaagent:/app/lib/jmx_prometheus_javaagent.jar=9001:/app/lib/config.yaml"

if [[ ${JPROFILER:-0} == "1" ]]; then
  JAVA_OPTS="${JAVA_OPTS} -agentpath:/app/shared/jprofiler11.0.1/bin/linux-x64/libjprofilerti.so=port=8849,nowait"
fi

if [[ ${REMOTE_DEBUG:-0} == "1" ]]; then
  JAVA_OPTS="${JAVA_OPTS} -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"
fi

if [[ -n "${JAVA_PROPS}" ]]; then
  JAVA_PROPS=$(echo "${JAVA_PROPS}" | tr -d '"')
  JAVA_OPTS="${JAVA_PROPS} ${JAVA_OPTS}"
fi

JAVA_OPTS="-DserviceName=${SERVICE_NAME} ${JAVA_OPTS} "
