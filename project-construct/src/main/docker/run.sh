#!/bin/sh

echo "********************************************************"
echo "Waiting for the consul server to start on port $CONSULSERVER_HOST : $CONSULSERVER_PORT"
echo "********************************************************"
while ! `nc -z $CONSULSERVER_HOST  $CONSULSERVER_PORT`; do sleep 3; done
echo "******* Consul Server has started"


echo "********************************************************"
echo "Starting Project Service                           "
echo "********************************************************"
java -Djava.security.egd=file:/dev/./urandom -Dserver.port=$SERVER_PORT   \
     -Dspring.cloud.consul.port=$CONSULSERVER_PORT  \
     -Dspring.cloud.consul.host=$CONSULSERVER_HOST               \
     -Dspring.profiles.active=$PROFILE                                   \
     -jar /usr/local/app/@project.build.finalName@.jar