FROM rtfpessoa/ubuntu-jdk8

ENV GRADLE_HOME /opt/gradle
ENV GRADLE_USER_HOME /gradle

RUN apt-get update \
    && apt-get install --yes --no-install-recommends \
        fontconfig \
        unzip \
        wget \
        \
        bzr \
        git \
        mercurial \
        openssh-client \
        subversion \
    && rm -rf /var/lib/apt/lists/*

ENV GRADLE_VERSION 4.8.1
RUN set -o errexit -o nounset \
    && echo "Downloading Gradle" \
    && wget --no-verbose --output-document=gradle.zip "https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip" \
    \
    && echo "Installing Gradle" \
    && unzip gradle.zip \
    && rm gradle.zip \
    && mv "gradle-${GRADLE_VERSION}" "${GRADLE_HOME}/" \
    && ln --symbolic "${GRADLE_HOME}/bin/gradle" /usr/bin/gradle \
    && echo "Testing Gradle installation" \
    && gradle --version

ONBUILD ARG REMOTE_BRANCH
ONBUILD ARG GIT_URL
ONBUILD ARG BASE_IMAGE
ONBUILD ARG IMAGE
ONBUILD ARG TAG

ADD build.sh /
ADD lib/*  /
ADD config.json /root/.docker/
RUN chmod 755 build.sh
ENTRYPOINT /build.sh
