#!/bin/sh
root=`pwd`
imageReg=registry.cn-qingdao.aliyuncs.com/kangta123/hawk/

cd ${root}

# echo "build jdk8"
# cd run && docker build -t ${imageReg}hawk_jdk8:v4.0 -f Dockerfile_JDK8 .
# docker push ${imageReg}hawk_jdk8:v4.0
# cd ${root}

echo "build jdk11"
cd run && docker build -t ${imageReg}hawk_jdk11:v4.0 -f Dockerfile_JDK11 .
docker push ${imageReg}hawk_jdk11:v4.0
cd ${root}

# echo "run hawk_nginx "
# cd run && docker build -t ${imageReg}hawk_nginx:v4.0 -f nginx/Dockerfile .
# docker push ${imageReg}hawk_nginx:v4.0
# cd ${root}
#
#
# echo "init npm"
# cd run/init && docker build -t ${imageReg}hawk_init_npm:v4.0 -f Dockerfile_NPM .
# docker push ${imageReg}hawk_init_npm:v4.0
# cd ${root}
#
# echo "init springboot"
# cd run/init && docker build -t ${imageReg}hawk_init_springboot:v4.0 -f Dockerfile_SPRINGBOOT .
# docker push ${imageReg}hawk_init_springboot:v4.0
# cd ${root}
#
# echo "init tomcat"
# cd run/init && docker build -t ${imageReg}hawk_init_tomcat:v4.0 -f Dockerfile_TOMCAT .
# docker push ${imageReg}hawk_init_tomcat:v4.0
# cd ${root}
#
#
#
# echo "build tomcat"
# cd build/maven && docker build -t ${imageReg}hawk_tomcat_build:v4.0 -f tomcat/Dockerfile .
# docker push ${imageReg}hawk_tomcat_build:v4.0
# cd ${root}
#
# echo "build springboot"
# cd build/maven && docker build -t ${imageReg}hawk_springboot_build:v4.0.1 -f springboot/Dockerfile .
# docker push ${imageReg}hawk_springboot_build:v4.0.1
# cd ${root}
#
# echo "build gradle"
# cd build/gradle && docker build -t ${imageReg}hawk_gradle_build:v4.0 -f Dockerfile .
# docker push ${imageReg}hawk_gradle_build:v4.0
# cd ${root}
#
# echo "build npm"
# cd build/npm && docker build -t ${imageReg}hawk_npm_build:v4.0 -f Dockerfile .
# docker push ${imageReg}hawk_npm_build:v4.0
# cd ${root}
#
# echo "run tomcat 8"
# cd run && docker build -t ${imageReg}hawk_tomcat8:v4.0 -f tomcat/Dockerfile .
# docker push ${imageReg}hawk_tomcat8:v4.0
# cd ${root}
# echo "run hawk_springboot "
# cd run && docker build -t ${imageReg}hawk_springboot8:v4.0 -f springboot/Dockerfile .
# docker push ${imageReg}hawk_springboot8:v4.0
# cd ${root}

