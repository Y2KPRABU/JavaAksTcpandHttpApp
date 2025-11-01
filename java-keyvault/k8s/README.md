# Key Vault CSI Driver Setup Guide

This guide helps you set up Azure Key Vault integration with Kubernetes using the CSI driver.

## Deployment Options

You can deploy this integration in two ways:
1. Using GitHub Actions (recommended)
2. Manual deployment using local scripts

## GitHub Actions Setup

1. In your GitHub repository, go to Settings > Secrets and Variables > Actions
2. Add the following secrets:

   ```
   AZURE_CREDENTIALS         # JSON output from az ad sp create-for-rbac
   AZURE_TENANT_ID          # Your Azure tenant ID
   AZURE_CLIENT_ID          # Service Principal client ID
   AZURE_CLIENT_SECRET      # Service Principal client secret
   KEYVAULT_NAME           # Your Azure Key Vault name
   AKS_CLUSTER_NAME        # Your AKS cluster name
   AKS_RESOURCE_GROUP      # Resource group containing your AKS cluster
   ```

3. Create Azure service principal and get credentials:
   ```powershell
   az ad sp create-for-rbac --name "github-actions-sp" --role contributor \
     --scopes /subscriptions/<subscription-id>/resourceGroups/<resource-group> \
     --sdk-auth
   ```
   Copy the entire JSON output to the `AZURE_CREDENTIALS` secret.

4. Trigger the deployment:
   - Push to main branch, or
   - Go to Actions > Deploy Key Vault Integration > Run workflow

The GitHub Action will:
- Log in to Azure
- Connect to your AKS cluster
- Install the CSI driver if needed
- Create all necessary Kubernetes resources
- Verify the deployment

## Manual Deployment Prerequisites

1. Azure CLI installed and logged in
2. kubectl configured with your Kubernetes cluster
3. Azure Key Vault CSI Driver installed in your cluster
4. Service Principal with access to Key Vault

## Installation Steps

### 1. Install Azure Key Vault CSI Driver (if not already installed)

```powershell
helm repo add csi-secrets-store-provider-azure https://azure.github.io/secrets-store-csi-driver-provider-azure/charts
helm install csi csi-secrets-store-provider-azure/csi-secrets-store-provider-azure --namespace kube-system
```

### 2. Create Service Principal (if not already created)

```powershell
# Login to Azure
az login

# Create Service Principal
$SP=$(az ad sp create-for-rbac --name "keyvault-sp" --skip-assignment)
$CLIENT_ID=$(echo $SP | jq -r '.appId')
$CLIENT_SECRET=$(echo $SP | jq -r '.password')
$TENANT_ID=$(echo $SP | jq -r '.tenant')

# Assign Key Vault permissions
az keyvault set-policy -n <your-keyvault-name> --secret-permissions get --spn $CLIENT_ID
```

### 3. Run Setup Script

The setup script will create all necessary Kubernetes resources:

```powershell
.\setup-secrets.ps1 -TenantId <your-tenant-id> -ClientId <your-client-id> -ClientSecret <your-client-secret>
```

Optional: Specify a different namespace:
```powershell
.\setup-secrets.ps1 -TenantId <your-tenant-id> -ClientId <your-client-id> -ClientSecret <your-client-secret> -Namespace my-namespace
```

### 4. Verify Setup

Check if the pod is running:
```powershell
kubectl get pods kv-demo-sp
```

View pod logs:
```powershell
kubectl logs kv-demo-sp
```

## Troubleshooting

1. If pod is not starting, check events:
```powershell
kubectl describe pod kv-demo-sp
```

2. Check CSI driver logs:
```powershell
kubectl logs -n kube-system -l app=secrets-store-csi-driver
```

3. Check Azure provider logs:
```powershell
kubectl logs -n kube-system -l app=csi-secrets-store-provider-azure
```

## Security Notes

- Keep your Service Principal credentials secure
- Don't commit secrets to source control
- Use managed identities in production when possible
- Rotate Service Principal secrets regularly

## Cleaning Up

To remove all created resources:

```powershell
kubectl delete pod kv-demo-sp
kubectl delete secret azure-sp-secret
kubectl delete secretproviderclass azure-kv-sp
kubectl delete secret keyvault-sa-token
```