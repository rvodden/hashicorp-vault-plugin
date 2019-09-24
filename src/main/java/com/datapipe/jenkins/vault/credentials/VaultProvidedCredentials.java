package com.datapipe.jenkins.vault.credentials;

import com.cloudbees.plugins.credentials.CredentialsNameProvider;
import com.cloudbees.plugins.credentials.NameWith;
import com.cloudbees.plugins.credentials.common.StandardCredentials;

import javax.annotation.Nonnull;
import java.io.Serializable;

@NameWith(VaultProvidedCredentials.NameProvider.class)
public interface VaultProvidedCredentials extends StandardCredentials, Serializable {
  class NameProvider extends CredentialsNameProvider<VaultProvidedCredentials> {

    @Nonnull
    public String getName(@Nonnull VaultProvidedCredentials vaultProvidedCredentials) {
      return vaultProvidedCredentials.getDescription();
    }
  }
}
