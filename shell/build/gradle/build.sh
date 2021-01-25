#!/usr/bin/env bash

cp /*.jar $JAVA_HOME/jre/lib/ext

check(){
  buildRet=$1
  echo "==========@@$2 ${buildRet} $3@@@"
}

package(){
  eval ${BUILD_COMMAND}
  check $? "Build"
}

build_docker(){
  JAR_FILE=$1
  pomFile=`dirname ${JAR_FILE}`
  groupId=${GROUP}
  name=`echo $JAR_FILE|rev |cut -d '/' -f1 |rev | sed 's/\-[0-9].*//'`

  IMAGE_NAME="${IMAGE_PREFIX}${groupId}_${name}"

  tmp=$(mktemp dockerfile.XXXXXX)
  echo "FROM ${BASE_IMAGE}" > ${tmp}

  mkdir docker 2>/dev/null
  docker build \
        --build-arg JAR_FILE=${JAR_FILE}  \
        -t ${IMAGE_NAME}:${TAG} . -f - < ${tmp}

  check $? "DockerBuild" ${IMAGE_NAME}

  docker push ${IMAGE_NAME}:${TAG}
  check $? "DockerPush"

}

check 0 "Start"
rm -fr app
git clone --depth 1 -b ${REMOTE_BRANCH} ${GIT_URL}  app  && cd app
check $? "GitClone"

package
for f in `find . -regex ".*/target/[^/]*\.jar" |grep -v "sources\.jar"`
do
  unzip -l ${f} |grep "application.*\.properties\|application.*\.yml\|bootstrap.*\.yml\|bootstrap.*\.properties"
  if [ $? -eq "0" ]; then
    build_docker ${f}
  fi
done

check 0 "End"
