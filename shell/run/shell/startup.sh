#!/bin/sh

if [[ ${SSH} == 1 ]]; then
  echo "root:${SSHPASSWORD}" | chpasswd
  /usr/sbin/sshd -D &
fi


for i in $(seq 30); do
    echo "Waiting network connection available. ${i}"
     curl -s http://${HAWK_GATEWAY} 1>/dev/null
    if [[ $? -eq "0" ]]; then
        break
    fi
    sleep 1
done

if [ -n "${HAWK_GATEWAY}" ];then
  curl "http://${HAWK_GATEWAY}/api/container/file?serviceName=${SERVICE_NAME}&namespace=${POD_NAMESPACE}" 2>/dev/null | sh
else
  >&2 echo "empty env value HAWK_GATEWAY"
fi

