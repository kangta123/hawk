FROM rtfpessoa/ubuntu-jdk8

ARG MAVEN_VERSION=3.6.3
ARG USER_HOME_DIR="/root"

RUN mkdir -p /usr/share/maven /usr/share/maven/ref \
  && curl -fsSL -o /tmp/apache-maven.tar.gz https://mirrors.bfsu.edu.cn/apache/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz \
  && tar -xzf /tmp/apache-maven.tar.gz -C /usr/share/maven --strip-components=1 \
  && rm -f /tmp/apache-maven.tar.gz \
  && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn \
  && apt-get update \
  && apt-get install -y git unzip  curl procps \
  && rm -rf /var/lib/apt/lists/*

ENV MAVEN_HOME /usr/share/maven
ENV MAVEN_CONFIG "$USER_HOME_DIR/.m2"

ONBUILD ARG REMOTE_BRANCH
ONBUILD ARG GIT_URL
ONBUILD ARG BASE_IMAGE
ONBUILD ARG IMAGE
ONBUILD ARG TAG


ADD springboot/build.sh /

ADD springboot/lib/*.jar  /

ADD config.json /root/.docker/

RUN chmod 755 build.sh

ENTRYPOINT /build.sh



