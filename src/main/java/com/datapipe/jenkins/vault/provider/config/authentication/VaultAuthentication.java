package com.datapipe.jenkins.vault.provider.config.authentication;

import hudson.model.AbstractDescribableImpl;
import hudson.util.Secret;

public abstract class VaultAuthentication extends
    AbstractDescribableImpl<VaultAuthentication> {

    public abstract Secret getVaultToken();
}
