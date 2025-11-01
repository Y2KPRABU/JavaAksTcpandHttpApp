# Dockerfile for java-tcp-receiver
FROM registry.access.redhat.com/ubi9/ubi-minimal:latest



# Install OpenJDK 8 JRE, Apache HTTP Server, and Tomcat
RUN microdnf install -y java-1.8.0-openjdk-headless httpd && \
    microdnf install -y wget tar 

RUN  wget https://archive.apache.org/dist/tomcat/tomcat-8/v8.5.100/bin/apache-tomcat-8.5.100.tar.gz -O /tmp/tomcat.tar.gz
RUN  mkdir -p /opt 
RUN  tar xzf /tmp/tomcat.tar.gz -C /opt 
RUN    mv /opt/apache-tomcat-8.5.100 /opt/tomcat
# Clean up the downloaded Tomcat tarball    
RUN  rm -rf /tmp/tomcat.tar.gz 
RUN microdnf install -y net-tools
RUN microdnf install -y iproute
RUN microdnf install -y nmap-ncat
RUN  microdnf clean all

WORKDIR /app
COPY build/libs/java-tcp-receiver-1.0-all.jar receiverapp.jar
COPY java-webapp/build/libs/java-webapp.war /opt/tomcat/webapps/java-webapp.war
EXPOSE 12345 9878 8080

RUN microdnf install -y procps-ng

# Use starttomcat.sh to run Tomcat in the background and receiverapp.jar in the foreground
COPY starttomcat.sh /app/starttomcat.sh
RUN chmod +x /app/starttomcat.sh
#CMD ["/app/starttomcat.sh"]
# COMMENTING THE BELOW SINCE WE HAVE A TAIL TASK IN THE YAML FOR PODS
#ENTRYPOINT ["java", "-jar", "/app/receiverapp.jar", "1"]

# To run receiverapp.jar manually, use:
# docker exec -it <container_name> java -jar /app/receiverapp.jar

# To start Apache HTTPD inside the container, you can add:
# RUN systemctl enable httpd
# or start it manually in a shell: httpd -k start
#
# run it like this with cmd below
# docker run -it --name java8tcpserver -p 12345:12345 java-tcp-receiver:latest