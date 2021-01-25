#!/bin/sh
root=`pwd`
imageReg=registry.cn-qingdao.aliyuncs.com/kangta123/hawk/
echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" registry.cn-qingdao.aliyuncs.com --password-stdin

echo "build npm business data image"
cd run/init && docker build -t ${imageReg}hawk_init_npm:v4.0 -f Dockerfile_NPM .
docker push ${imageReg}hawk_init_npm:v4.0
cd ${root}

echo "build springboot business data image"
cd run/init && docker build -t ${imageReg}hawk_init_springboot:v4.0 -f Dockerfile_SPRINGBOOT .
docker push ${imageReg}hawk_init_springboot:v4.0
cd ${root}

echo "build tomcat business data image"
cd run/init && docker build -t ${imageReg}hawk_init_tomcat:v4.0 -f Dockerfile_TOMCAT .
docker push ${imageReg}hawk_init_tomcat:v4.0
cd ${root}

echo "build jdk8 base image"
cd run && docker build -t ${imageReg}hawk_jdk8:v4.0 -f Dockerfile_JDK8 .
docker push ${imageReg}hawk_jdk8:v4.0
cd ${root}


echo "build tomcat building runtime image"
cd build/maven && docker build -t ${imageReg}hawk_tomcat_build:v4.0 -f tomcat/Dockerfile .
docker push ${imageReg}hawk_tomcat_build:v4.0
cd ${root}

echo "build springboot building runtime image"
cd build/maven && docker build -t ${imageReg}hawk_springboot_build:v4.0.1 -f springboot/Dockerfile .
docker push ${imageReg}hawk_springboot_build:v4.0.1
cd ${root}

echo "build gradle building runtime image"
cd build/gradle && docker build -t ${imageReg}hawk_gradle_build:v4.0 -f Dockerfile .
docker push ${imageReg}hawk_gradle_build:v4.0
cd ${root}

echo "build npm nginx image"
cd build/npm && docker build -t ${imageReg}hawk_npm_build:v4.0 -f Dockerfile .
docker push ${imageReg}hawk_npm_build:v4.0
cd ${root}

echo "build tomcat8 runtime image"
cd run && docker build -t ${imageReg}hawk_tomcat8:v4.0 -f tomcat/Dockerfile .
docker push ${imageReg}hawk_tomcat8:v4.0
cd ${root}

echo "build springboot runtime image"
cd run && docker build -t ${imageReg}hawk_springboot8:v4.0 -f springboot/Dockerfile .
docker push ${imageReg}hawk_springboot8:v4.0
cd ${root}

echo "build nginx business runtime image"
cd run && docker build -t ${imageReg}hawk_nginx:v4.0 -f nginx/Dockerfile .
docker push ${imageReg}hawk_nginx:v4.0
cd ${root}
