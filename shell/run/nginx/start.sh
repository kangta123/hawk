#!/bin/bash

curl -fsSL http://${gateway}/container/file/nginx/config\?serviceName\=${SERVICE_NAME}  2>/dev/null > local

sed -i '86 r local' /usr/local/openresty/nginx/conf/nginx.conf

/usr/bin/openresty -g 'daemon off;'
