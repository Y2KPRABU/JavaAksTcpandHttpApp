# Dockerfile for java-tcp-sender

FROM registry.access.redhat.com/ubi9/ubi-minimal:latest

# Install OpenJDK 8 only if not already installed
RUN microdnf install -y java-1.8.0-openjdk-headless; \
   echo "Java 1.8.0 OpenJDK Headless installed." 
RUN microdnf install -y net-tools
RUN microdnf install -y iproute
RUN microdnf clean all; 

WORKDIR /app
COPY build/libs/java-tcp-sender-1.0-all.jar senderapp.jar
ENV RECEIVER_HOST=java-tcp-receiver-service
# cOMMENTING THIS AS ADDING A TAIL TASK IN THE YAML MANIFEST FOR AKS PODS
#ENTRYPOINT ["java", "-jar", "/app/senderapp.jar", "1" ]


# run it like this with cmd below
 #docker run -it --name java8tcpsender --network tcp-net-sundar java-tcp-sender:latest
 #run as below to run the user as root
 #docker run -it --user root --name java8tcpsender --network tcp-net-sundar java-tcp-sender:latest
#below command start the existing container created above
 #docker start java8tcpsender
 #open a bash script
 #docker exec -it java8tcpserver bash
 #run the jar file
 