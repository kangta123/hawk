#!/bin/bash

check(){
  buildRet=$1
  echo -n "=@@$2 ${buildRet} $3@@@"
}
package(){
  cp -rf /node/node_modules .
  npm install
  cp -rf node_modules /node/
  eval ${BUILD_COMMAND}
  check $? "Build"
}

build_docker(){

  IMAGE_NAME="${IMAGE_PREFIX}${GROUP}_${PROJECT_NAME}"

  tmp=$(mktemp dockerfile.XXXXXX)
  echo "FROM ${BASE_IMAGE}" > ${tmp}

  mkdir docker 2>/dev/null
  docker  build \
        --build-arg PROJECT=${BUILD_OUT}  \
        -t ${IMAGE_NAME}:${TAG} . -f - < ${tmp}

  check $? "DockerBuild" ${IMAGE_NAME}

  docker  push ${IMAGE_NAME}:${TAG}
  check $? "DockerPush"

}

check 0 "Start"
rm -fr app
git clone --depth 1 -b ${REMOTE_BRANCH} ${GIT_URL}  app  && cd app
check $? "GitClone"

package
build_docker

check 0 "End"
