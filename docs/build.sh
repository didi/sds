#!/bin/bash
source /etc/profile

BRANCH=token_bucket
REPO=SDS
ENV=test

if [ "$ENV" =  ""  ]; then
  ENV=stable
fi

rootPath=/root/$ENV

rm -rf $rootPath/$REPO

if [ ! -d "$rootPath" ]; then
  mkdir -p "$rootPath"
fi

cd ${rootPath}

if [ "A$BRANCH" = "A" ]; then
        echo "BRANCH is null. Use the master branch instead"
        BRANCH=master
fi

if [ "A$REPO" = "A" ]; then
        echo " REPO is null. Use the sds "
        GITREPO="SDS"
fi

echo "BRANCH is $BRANCH "
echo "REPO   is $REPO "
echo "ENV is $ENV "
echo "clone command :git clone -b $BRANCH --depth=1 git@git.xiaojukeji.com:daijiaServer/${REPO}.git "

git clone -b $BRANCH --depth=1 git@git.xiaojukeji.com:didi-java-middleware/${REPO}.git
cd $rootPath/$REPO/ && mvn clean install -U -Dmaven.test.skip

echo "unzip $rootPath/$REPO/sds-assembly/target/$ENV-sds-assembly-1.0.1-SNAPSHOT.zip"
unzip $rootPath/$REPO/sds-assembly/target/$ENV-sds-assembly-1.0.1-SNAPSHOT.zip -d $rootPath/$REPO/sds-assembly/target/output &&


killLabel=${rootPath}/$REPO

if [ `ps -ef |grep "java" |grep -c $killLabel` -gt 0 ];then

   kill -9 `ps -ef |grep "java" |grep  $killLabel | awk '{print $2}'`
   echo "stop success"
   sleep 3
else
   echo "$REPO $ENV $ is stopped"
fi

sh $rootPath/$REPO/sds-assembly/target/output/bin/start.sh