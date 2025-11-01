package com.example.keyvault;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * Simple example that reads secrets mounted by the Azure Key Vault CSI driver.
 *
 * By default the CSI driver mounts secrets under /mnt/secrets-store/<secret-name>
 * or as files under the mount path. This example reads all files under the
 * mount path and prints their names and values.
 */
public class KeyVaultCsiExample {

    private static final String DEFAULT_MOUNT_PATH = "/mnt/secrets-store";

    public static void main(String[] args) {
        String mountPath = args.length > 0 ? args[0] : DEFAULT_MOUNT_PATH;
        System.out.println("Reading secrets from mount path: " + mountPath);
        try {
            readAndPrintSecrets(mountPath);
        } catch (IOException e) {
            System.err.println("Failed to read secrets: " + e.getMessage());
            System.exit(2);
        }
    }

    private static void readAndPrintSecrets(String mountPath) throws IOException {
        Path base = Paths.get(mountPath);
        if (!Files.exists(base) || !Files.isDirectory(base)) {
            System.err.println("Mount path does not exist or is not a directory: " + mountPath);
            return;
        }

        var files = Files.list(base)
                .filter(Files::isRegularFile)
                .collect(Collectors.toList());

        if (files.isEmpty()) {
            System.out.println("No secrets found at mount path.");
            return;
        }

        for (Path p : files) {
            String name = p.getFileName().toString();
            String value = Files.readString(p).trim();
            System.out.printf("Secret: %s = %s\n", name, value);
        }
    }
}
