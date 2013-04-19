set -v

mvn -DskipTests clean package

TOMCAT=/Users/pwebb/projects/websockets/code/tomcat/trunk/output/build

#/home/rossen/dev/sources/apache/tomcat/trunk/output/build

rm -rf $TOMCAT/webapps/spring-websocket-test*
cp target/spring-websocket-test.war $TOMCAT/webapps/
$TOMCAT/bin/catalina.sh jpda run
