FROM  openresty/openresty:centos

ADD nginx/start.sh /start.sh
ADD nginx/nginx.conf /usr/local/openresty/nginx/conf/nginx.conf

RUN chmod +x /start.sh

ENTRYPOINT /start.sh

WORKDIR /usr/local/openresty/nginx/html/

