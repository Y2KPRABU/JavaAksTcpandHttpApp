# Azure Key Vault Integration

This repository contains configuration for integrating Azure Key Vault with Kubernetes using the CSI driver.

## Architecture

```
┌─────────────────┐     ┌──────────────┐     ┌───────────────┐
│  GitHub Actions │────>│ AKS Cluster  │────>│  Azure        │
│  - Secrets      │     │ - CSI Driver │     │  Key Vault    │
│  - Workflows    │     │ - K8s Secrets│     │               │
└─────────────────┘     └──────────────┘     └───────────────┘
```

## Security Features

- No credentials in code
- Secrets managed through GitHub Secrets
- Service Principal authentication
- Kubernetes RBAC integration
- CSI driver secure mounting

## Directory Structure

```
.
├── .github/
│   └── workflows/
│       └── deploy-keyvault.yml    # GitHub Actions workflow
└── java-keyvault/
    └── k8s/
        ├── README.md              # Detailed setup instructions
        ├── secretproviderclass-sp.yaml
        ├── pod-secret-test-sp.yaml
        ├── service-account-token.yaml
        └── setup-secrets.ps1      # Local deployment script
```

## Quick Start

1. Fork this repository
2. Set up GitHub Secrets (see k8s/README.md)
3. Enable GitHub Actions
4. Trigger the deployment workflow

## Local Development

See `java-keyvault/k8s/README.md` for manual deployment instructions.

## Contributing

1. Create a feature branch
2. Make your changes
3. Create a pull request

Never commit sensitive information or credentials to the repository.