#!/usr/bin/env bash

export MAVEN_OPTS="-Xmx512m"
mode=${MODE:-single}

check(){
  buildRet=$1
  echo "=@@$2 ${buildRet} $3@@@"
}

do_package(){
    mvn install -Dfindbugs.skip -Dcheckstyle.skip -Dpmd.skip=true -Denforcer.skip -Dmaven.javadoc.skip -Dmaven.test.skip.exec -Dlicense.skip=true -Drat.skip=true  -DskipTests -Dsonar.skip=true -Dpmd.skip=true -f $1
}

upload_app(){
    for f in `find . -name "pom.xml"`
    do
        arId=`mvn help:evaluate -Dexpression=project.artifactId -f $f | sed -n '/^\[/!p' | sed -n '/^Download/!p'`
        echo "=@@subProject@$PROJECT_ID@$REMOTE_BRANCH@$arId@${f%pom.xml*}@@@"
        echo ""
    done
}

package(){
    upload_app

    if [ ${mode} == 'single' ]; then
        f=`find . -name pom.xml | awk -F "/" '{print NF,$0}' | sort -n | head -n1 | awk '{print $2}'`
        do_package $f
    else
        for f in `find . -name "pom.xml"`
        do
            do_package $f
        done
    fi
    check $? "Build"
}

build_docker(){
    mkdir docker 2>/dev/null
    tmp=$(mktemp dockerfile.XXXXXX)
    echo "FROM ${BASE_IMAGE}"> $tmp
    groupId=${GROUP}
    IMAGE_NAME=$(echo ${IMAGE_PREFIX}${groupId}_$1 | awk '{print tolower($0)}')
    check $? "DockerBuild" ${IMAGE_NAME}
    docker  build \
        --build-arg PROJECT=$1 \
        -t ${IMAGE_NAME}:${TAG} . -f - < ${tmp}
    docker push ${IMAGE_NAME}:${TAG}
    check $? "DockerPush"
}


check 0 "Start"
rm -fr app
git clone --depth 1 -b ${REMOTE_BRANCH} ${GIT_URL}  app  && cd app
check $? "GitClone"

size=`find . -name "pom.xml"|wc -l`
if [ ${size} -eq '0' ]; then
  echo "Can not found file pom.xml"
  exit
fi

package

target=`find . -name "*.war" | head -1`
echo "target is ${target}"
targetFolder=`dirname ${target}`
cd ${targetFolder}

targetFile=`ls *.war | head -1`

build_docker "${targetFile%.*}"

check 0 "End"
