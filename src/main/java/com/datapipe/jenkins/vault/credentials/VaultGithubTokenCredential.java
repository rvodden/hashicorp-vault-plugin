package com.datapipe.jenkins.vault.credentials;

import com.bettercloud.vault.Vault;
import com.bettercloud.vault.VaultException;
import com.cloudbees.plugins.credentials.CredentialsScope;
import com.datapipe.jenkins.vault.exception.VaultPluginException;
import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.util.Secret;
import org.kohsuke.stapler.DataBoundConstructor;

public class VaultGithubTokenCredential extends AbstractVaultTokenCredential {

    // https://www.vaultproject.io/docs/auth/github.html#generate-a-github-personal-access-token
    private final @NonNull
    Secret accessToken;

    @DataBoundConstructor
    public VaultGithubTokenCredential(@CheckForNull CredentialsScope scope,
        @CheckForNull String id,
        @CheckForNull String description,
        @NonNull Secret accessToken) {
        super(scope, id, description);
        this.accessToken = accessToken;
    }

    public Secret getAccessToken() {
        return accessToken;
    }

    @Override
    public String getToken(Vault vault) {
        try {
            return vault.auth().loginByGithub(Secret.toString(accessToken)).getAuthClientToken();
        } catch (VaultException e) {
            throw new VaultPluginException("could not log in into vault", e);
        }
    }

    @Extension
    public static class DescriptorImpl extends BaseStandardCredentialsDescriptor {

        @NonNull
        @Override
        public String getDisplayName() {
            return "(Old) Vault Github Token Credential";
        }
    }
}
