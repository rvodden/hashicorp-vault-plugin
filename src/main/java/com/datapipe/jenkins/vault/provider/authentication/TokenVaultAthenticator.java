package com.datapipe.jenkins.vault.provider.authentication;

import com.bettercloud.vault.Vault;
import com.bettercloud.vault.VaultConfig;
import com.bettercloud.vault.VaultException;
import hudson.Extension;
import hudson.model.Descriptor;
import hudson.util.Secret;
import org.jenkinsci.plugins.plaincredentials.StringCredentials;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

public class TokenVaultAthenticator implements VaultAuthenticator {

    private StringCredentials vaultToken;

    public TokenVaultAthenticator(StringCredentials vaultToken) {
        this.vaultToken = vaultToken;
    }

    @Override
    public Secret getToken() {
        return vaultToken.getSecret();
    }
}
