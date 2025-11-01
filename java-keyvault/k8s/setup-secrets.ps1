param(
    [Parameter(Mandatory=$false)]
    [string]$TenantId = "b41b72d0-4e9f-4c26-8a69-f949f367c91d",
    
    [Parameter(Mandatory=$false)]
    [string]$ClientId = "240ee637-227a-4d6b-a256-8012d068ced0",
    
    [Parameter(Mandatory=$false)]
    [string]$ClientSecret = "u6b8Q~X2vhue3Zfpsl1EW7N.LU5FB6YbSt8TAbam",
    
    [Parameter(Mandatory=$false)]
    [string]$Namespace = "default",

    [Parameter(Mandatory=$false)]
    [string]$KeyVaultName = "kv-aks-sundar"
)

Write-Host "Using configuration:"
Write-Host "Tenant ID: $TenantId"
Write-Host "Client ID: $ClientId"
Write-Host "Key Vault: $KeyVaultName"
Write-Host "Namespace: $Namespace"

Write-Host "Creating Azure service principal secret..."
kubectl create secret generic azure-sp-secret `
    --from-literal=clientid=$ClientId `
    --from-literal=clientsecret=$ClientSecret `
    --from-literal=tenantid=$TenantId `
    --namespace $Namespace `
    --dry-run=client -o yaml | kubectl apply -f -

Write-Host "Creating service account for Key Vault access..."
kubectl apply -f service-account-token.yaml --namespace $Namespace

Write-Host "Creating SecretProviderClass..."

# Create SecretProviderClass YAML with current values
$secretProviderYaml = @"
apiVersion: secrets-store.csi.x-k8s.io/v1
kind: SecretProviderClass
metadata:
  name: azure-kv-sp
spec:
  provider: azure
  parameters:
    usePodIdentity: "false"
    useVMManagedIdentity: "false"
    cloudName: AzurePublicCloud
    tenantId: "$TenantId"
    clientId: "$ClientId"
    clientSecret: "$ClientSecret"
    keyvaultName: "$KeyVaultName"
    objects: |
      array:
        - |
          objectName: test1
          objectType: secret
        - |
          objectName: testname1
          objectType: secret
  secretObjects:
  - data:
    - key: test1
      objectName: test1
    - key: testname1
      objectName: testname1
    secretName: kvmounted-secrets
    type: Opaque
"@

$secretProviderYaml | kubectl apply -f - --namespace $Namespace

Write-Host "Creating pod with Key Vault integration..."

# Create pod YAML with current values
$podYaml = @"
apiVersion: v1
kind: Pod
metadata:
  name: kv-demo-sp
spec:
  serviceAccountName: keyvault-sa
  containers:
  - name: java-keyvault
    image: openjdk:latest
    command:
    - java
    - -cp
    - /app/java-keyvault.jar
    - com.example.keyvault.KeyVaultCsiExample
    - /mnt/secrets-store
    resources:
      requests:
        memory: "64Mi"
        cpu: "250m"
      limits:
        memory: "128Mi"
        cpu: "500m"
    volumeMounts:
    - name: secrets-store-inline
      mountPath: "/mnt/secrets-store"
      readOnly: true
    - name: app-jar
      mountPath: "/app"
    env:
    - name: TEST1
      valueFrom:
        secretKeyRef:
          name: kvmounted-secrets
          key: test1
    - name: TESTNAME1
      valueFrom:
        secretKeyRef:
          name: kvmounted-secrets
          key: testname1
    - name: AZURE_CLIENT_ID
      valueFrom:
        secretKeyRef:
          name: azure-sp-secret
          key: clientid
    - name: AZURE_CLIENT_SECRET
      valueFrom:
        secretKeyRef:
          name: azure-sp-secret
          key: clientsecret
    - name: AZURE_TENANT_ID
      valueFrom:
        secretKeyRef:
          name: azure-sp-secret
          key: tenantid
    - name: AZURE_AUTHORITY_HOST
      value: "https://login.microsoftonline.com"
    - name: AAD_POD_IDENTITY_AUTHORITY_HOST
      value: "https://login.microsoftonline.com"
    - name: AZURE_AUTHORITY_HOST_CSI
      value: "https://login.microsoftonline.com"
  volumes:
    - name: secrets-store-inline
      csi:
        driver: secrets-store.csi.k8s.io
        readOnly: true
        volumeAttributes:
          secretProviderClass: azure-kv-sp
        nodePublishSecretRef:
          name: secrets-store-creds
    - name: app-jar
      configMap:
        name: keyvault-app
"@

$podYaml | kubectl apply -f - --namespace $Namespace

Write-Host "`nSetup complete! Checking resources..."
Write-Host "`nChecking secrets:"
kubectl get secrets -n $Namespace | Select-String "azure-sp-secret|kvmounted-secrets"

Write-Host "`nChecking SecretProviderClass:"
kubectl get secretproviderclass -n $Namespace

Write-Host "`nWaiting for pod to start..."
Start-Sleep -Seconds 5
kubectl get pods kv-demo-sp -n $Namespace

Write-Host "`nUse these commands to check status:"
Write-Host "kubectl get pods kv-demo-sp -n $Namespace"
Write-Host "kubectl describe pod kv-demo-sp -n $Namespace"
Write-Host "kubectl logs kv-demo-sp -n $Namespace"