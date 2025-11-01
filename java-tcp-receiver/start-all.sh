#!/bin/sh
# Start Tomcat in silent mode as a background process
/opt/tomcat/bin/catalina.sh start > /dev/null 2>&1 &
# Start receiverapp.jar in the foreground
java -jar /app/receiverapp.jar
