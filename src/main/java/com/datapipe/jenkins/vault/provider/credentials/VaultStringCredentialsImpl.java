package com.datapipe.jenkins.vault.provider.credentials;

import com.bettercloud.vault.Vault;
import com.bettercloud.vault.VaultException;
import com.bettercloud.vault.response.LogicalResponse;
import com.cloudbees.plugins.credentials.CredentialsUnavailableException;
import com.cloudbees.plugins.credentials.impl.BaseStandardCredentials;
import com.datapipe.jenkins.vault.provider.Messages;
import hudson.Extension;
import hudson.util.Secret;
import java.io.IOException;
import javax.annotation.Nonnull;
import org.antlr.v4.runtime.misc.NotNull;

public class VaultStringCredentialsImpl extends BaseStandardCredentials implements VaultStringCredentials {

    private static final long serialVersionID = 1L;

    private final transient Vault vault;

    /**
     *  THe name of the Vault instance in which the secret is stored
     */
    private String name;

    /**
     * THe path at which the secret is stored in vault
     */
    private String path;

    /**
     * The key under which the secret is stored in vault.
     */
    private String key;

    public VaultStringCredentialsImpl(String id, String description, String name, String path, String key, Vault vault) {
        super(id, description);
        this.name = name;
        this.path = path;
        this.key = key;
        this.vault = vault;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Nonnull
    @Override
    public Secret getSecret() {
        final String id = this.getId();

        final String data;
        try {
            data = getSecretValue(id);
        } catch ( IOException | InterruptedException exception ) {
            final String msg = Messages.couldNotRetrieveSecretError(id);
            throw new CredentialsUnavailableException("message", msg, exception);

        }
        return Secret.fromString(data);
    }

    private String getSecretValue(String secretPath) throws IOException, InterruptedException {
        // TODO: make engineVersion configurable
        LogicalResponse response;
        try {
            response = vault.logical().read(secretPath);
        } catch (VaultException vaultException ) {
            // TODO: if this is a timeout, it should throw InterruptedException
            throw new IOException("Could not retrieve secret at path: " + secretPath ,vaultException);
        }
        // TODO: make key (or field ) configurable.
        return response.getData().get("value");
    }

    @Extension
    public static class DescriptorImpl extends BaseStandardCredentialsDescriptor {
        @Override
        @NotNull
        public String getDisplayName() {
            return Messages.vaultStringCredentials();
        }
    }
}
