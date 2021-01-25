#!/bin/sh
echo 10240 > /proc/sys/net/core/somaxconn

cp -r /data/* /tmp
mkdir -p /app/logs/${name}
