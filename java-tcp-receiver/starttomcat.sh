

#!/bin/sh

# If argument is '2', only run Tomcat and index.html checks, then exit
if [ "$1" = "2" ]; then
  sleep 2
  if pgrep -f 'org.apache.catalina.startup.Bootstrap' > /dev/null; then
    echo "Tomcat is running."
    if [ -f /opt/tomcat/webapps/java-webapp/index.html ]; then
      echo "index.html is present in the webapp directory."
      status=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/java-webapp/index.html)
      if [ "$status" -eq 200 ]; then
        echo "Tomcat is serving index.html (HTTP 200)."
      else
        echo "Tomcat is NOT serving index.html (HTTP $status)."
      fi
    else
      echo "index.html is NOT present in the webapp directory!"
    fi
  else
    echo "Tomcat failed to start."
  fi
  exit 0
fi

# Default behavior: start Tomcat, run checks, then start receiverapp.jar and tail logs
/opt/tomcat/bin/catalina.sh start
sleep 10
if pgrep -f 'org.apache.catalina.startup.Bootstrap' > /dev/null; then
  echo "Tomcat is running."
  if [ -f /opt/tomcat/webapps/java-webapp/index.html ]; then
    echo "index.html is present in the webapp directory."
    status=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/java-webapp/index.html)
    if [ "$status" -eq 200 ]; then
      echo "Tomcat is serving index.html (HTTP 200)."
    else
      echo "Tomcat is NOT serving index.html (HTTP $status)."
    fi
  else
    echo "index.html is NOT present in the webapp directory!"
  fi
else
  echo "Tomcat failed to start."
fi


#tail -f /opt/tomcat/logs/catalina.out