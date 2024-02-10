#!/bin/bash

MVN_ARG=$1
replace () {
INPUT=$1
OLD=$2
NEW=$3
OUTPUT=$(echo $INPUT | sed "s/$OLD/$NEW/")
echo "$OUTPUT"
}
replaceInFile() {
INPUT=$1
OLD=$2
NEW=$3
echo INPUT=$INPUT
echo OLD=$OLD
echo NEW=$NEW
sed -i 's/$OLD/$NEW/g' $INPUT
cat $INPUT
}

echo ... 1. Building
mvn clean install $MVN_ARG
#Use mvn clean install -o to build offline

if [ $? -eq 0 ]
then
    echo "Build was finished"
else
    echo "Build failed. Exiting"
    exit
fi

echo ... 2. Deleting dist directory
rm ./dist/*.jar

echo ... 3. Detecting version
cd modules/time-calc-app/target
VERSION=`ls time-calc-app-*-jar-with-all-dependencies.jar`
VERSION=`replace $VERSION "-jar-with-all-dependencies.jar" ""`
VERSION=`replace $VERSION "time-calc-app-" ""`
ORIG_VERSION=$VERSION
BUILD_TIMESTAMP=`date +'%Y%m%d_%H%M%S'`
if [[ `ls time-calc-app-*-jar-with-all-dependencies.jar` == *"SNAPSHOT"* ]];
then
    VERSION=`echo $VERSION-$BUILD_TIMESTAMP`
else
    echo "Release is in progress."
fi
echo version=$VERSION
cd ../../..
mkdir dist
echo $VERSION>./dist/VERSION

echo ... 4. Moving new jar file to dist directory
mv ./modules/time-calc-app/target/time-calc-app-$ORIG_VERSION-jar-with-all-dependencies.jar ./dist/time-calc-$VERSION.jar
rm ./modules/time-calc-app/target/time-calc-app-$ORIG_VERSION.jar

cp ./dist/time-calc-$VERSION.jar C:/Users/Robert Vokac/Desktop/rv


