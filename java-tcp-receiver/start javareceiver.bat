@echo off
REM Start the java8tcpreceiver container (if not already running)
docker start java8tcpreceiver

REM Open a shell inside the running container
docker exec -it java8tcpreceiver bash

REM (Optional) If you want to run the JAR manually inside the shell, use:
REM java -jar /app/receiverapp.jar

