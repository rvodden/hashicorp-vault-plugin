package com.datapipe.jenkins.vault.provider.authentication;

import hudson.model.AbstractDescribableImpl;
import hudson.util.Secret;

public interface VaultAuthenticator {
    public abstract Secret getToken();
}
