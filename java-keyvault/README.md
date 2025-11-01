# java-keyvault

Sample Java code that reads secrets mounted by the Azure Key Vault CSI driver.

Usage
-----

Build with Maven:
```powershell
mvn -f java-keyvault/pom.xml package
```

Run locally (pass mount path if different):

```powershell
java -cp java-keyvault/target/java-keyvault-1.0-SNAPSHOT.jar com.example.keyvault.KeyVaultCsiExample /mnt/secrets-store
```

Kubernetes example
------------------

Install the Azure Key Vault CSI driver and create a `SecretProviderClass`. Below is a minimal `SecretProviderClass` and Pod snippet that mounts secrets to `/mnt/secrets-store`.

```yaml
apiVersion: secrets-store.csi.x-k8s.io/v1
kind: SecretProviderClass
metadata:
  name: azure-kvname
spec:
  provider: azure
  parameters:
    usePodIdentity: "false"
    keyvaultName: "<your-keyvault-name>"
    objects:  |
      array:
        - objectName: secret1
          objectType: secret
        - objectName: secret2
          objectType: secret
    tenantId: "<your-tenant-id>"

---

apiVersion: v1
kind: Pod
metadata:
  name: kv-demo
spec:
  containers:
  - name: java-keyvault
    image: openjdk:11-jre-slim
    command: ["java", "-cp", "/app/java-keyvault.jar", "com.example.keyvault.KeyVaultCsiExample", "/mnt/secrets-store"]
    volumeMounts:
    - name: secrets-store-inline
      mountPath: /mnt/secrets-store
  volumes:
    - name: secrets-store-inline
      csi:
        driver: secrets-store.csi.k8s.io
        readOnly: true
        volumeAttributes:
          secretProviderClass: "azure-kvname"
```

Notes
-----
- The CSI driver will mount each secret as a file under the mount path. This sample reads all files in the mount path and prints their contents.
- For production use, handle secrets carefully (avoid printing them in logs).
