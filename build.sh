#!/bin/bash
IMAGE_PREFIX=registry.cn-qingdao.aliyuncs.com/kangta123/hawk/
DATA_BASE_IMAGE=hawk_init_springboot:v4.0
BASE_IMAGE=${IMAGE_PREFIX}${DATA_BASE_IMAGE}
TAG=${1:-"latest"}
folder=${2}
GROUP=hawk
ROOT=`pwd`
export MAVEN_OPTS="-Xmx512m"

package() {
    ./mvnw clean install -DskipTests -Dsonar.skip=true -Dpmd.skip=true
    cd ${folder:-"."}
}

build_docker() {
  JAR_FILE=$1
  pomFile=$(dirname ${JAR_FILE})
  pwd
  name=$($ROOT/mvnw help:evaluate -Dexpression=project.artifactId -f ${pomFile}/../ | grep -v "^\[" | grep -v "^Download" | grep -v "^Progress")
  name=$(echo $name | awk '{print tolower($0)}')
  IMAGE_NAME="${IMAGE_PREFIX}${GROUP}_${name}"

  tmp=$(mktemp dockerfile.XXXXXX)
  echo "FROM ${BASE_IMAGE}" >${tmp}

  mkdir docker 2>/dev/null
  docker  build \
    --build-arg JAR_FILE=${JAR_FILE} \
    -t ${IMAGE_NAME}:${TAG} . -f - <${tmp}

  docker push ${IMAGE_NAME}:${TAG}

  rm -fr ${tmp}
}


package

for f in $(find . -regex ".*/target/[^/]*\.jar" | grep -v "sources\.jar"); do
  unzip -l ${f} | grep "application.*\.properties\|application.*\.yml\|bootstrap.*\.yml\|bootstrap.*\.properties"
  if [ $? -eq "0" ]; then
    echo "build file ${f} $(pwd)"
    build_docker ${f}
  fi
done

