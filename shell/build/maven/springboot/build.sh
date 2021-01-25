#!/usr/bin/env bash
export MAVEN_OPTS="-Xmx512m"
mode=${MODE:-single}

cp *.jar $JAVA_HOME/jre/lib/ext

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

package_assign_project(){
   array=(${SUB_PATH//,/ })
   for var in ${array[@]}
   do
     for f in `find $var  -name "pom.xml"`
     do
         do_package $f
     done
   done

  check $? "Mvn"
}

build_docker(){
  JAR_FILE=$1
  pomFile=`dirname ${JAR_FILE}`
  groupId=${GROUP}
  name=`mvn help:evaluate -Dexpression=project.artifactId -f ${pomFile}/../ |grep -v "^\[" | grep -v "^Download" | grep -v "^Progress" `
  name=`echo $name | awk '{print tolower($0)}'`
  IMAGE_NAME="${IMAGE_PREFIX}${groupId}_${name}"

  tmp=$(mktemp dockerfile.XXXXXX)
  echo "FROM ${BASE_IMAGE}" > ${tmp}

  mkdir docker 2>/dev/null
  docker  build \
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

size=`find . -name "pom.xml"|wc -l`
if [ ${size} -eq '0' ]; then
  echo "Can not found file pom.xml"
  exit
fi

test -z $SUB_PATH && package || package_assign_project

find . -regex ".*/target/[^/]*\.*[j|w]ar" |grep -v "sources\.jar"

for f in `find . -regex ".*/target/[^/]*\.*[j|w]ar" |grep -v "sources\.jar"`
do
  echo "build source ${f}"
  unzip -l ${f} |grep "application.*\.properties\|application.*\.yml\|bootstrap.*\.yml\|bootstrap.*\.properties"   > /dev/null
  if [ $? -eq "0" ]; then
    echo "build file ${f} `pwd`"
    build_docker ${f}
  fi
done

check 0 "End"
