echo "Set registry and image names
SET ACR_NAME="y2kpr.azurecr.io"
SET SENDER_IMAGE="java-tcp-sender:v7"

REM Build images
docker build -t "%ACR_NAME%/%SENDER_IMAGE%" ../java-tcp-sender

REM Login to Azure Container Registry
REM docker login %ACR_NAME%
REM az acr login --name y2kpr

REM Push images
docker push "%ACR_NAME%/%SENDER_IMAGE%"

REM Delete old pod
kubectl delete pod java-tcp-sender-dev

REM Deploy to AKS
kubectl apply -f ../java-tcp-sender/deploymentsender.yaml

echo "Deployment complete."
