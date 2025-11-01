# Set registry and image names
$ACR_NAME = "y2kpr.azurecr.io"
$RECEIVER_IMAGE = "java-tcp-receiver:v1.6"

# Build images
docker build -t "$ACR_NAME/$RECEIVER_IMAGE" ../java-tcp-receiver

# Login to Azure Container Registry
#docker login $ACR_NAME
#az acr login --name y2kpr

# Push images
docker push "$ACR_NAME/$RECEIVER_IMAGE"

# Remove old pod
kubectl delete pod java-tcp-receiver-dev
# Remove old deployment
#kubectl delete -f ../java-tcp-receiver/deployment.yaml
# Deploy to AKS
kubectl apply -f ../java-tcp-receiver/deployment.yaml

Write-Host "Deployment complete."
