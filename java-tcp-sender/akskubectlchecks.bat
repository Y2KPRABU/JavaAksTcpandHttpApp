@echo off
REM Batch file to run kubectl checks for pod status and TCP/HTTP connections

set POD_NAME=java-tcp-sender-dev
REM Update POD_NAME if needed

REM Get pod status
echo --- Pod Status ---
kubectl get pod %POD_NAME% -o wide

REM Wait for a few seconds
pause 5

REM Describe pod
echo --- Pod Description ---
kubectl describe pod %POD_NAME%

pause 

REM Get pod logs
echo --- Pod Logs ---
kubectl logs %POD_NAME%

pause 

REM Exec into pod and check TCP connections (using ss)
echo --- TCP Connections ---
kubectl exec %POD_NAME% -- ss -tulnp

REM Exec into pod and check HTTP connection to localhost:8080
REM echo --- HTTP Connection Test ---
REM kubectl exec %POD_NAME% -- curl http://localhost:8080/

pause
