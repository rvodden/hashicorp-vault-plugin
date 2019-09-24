package com.datapipe.jenkins.vault.credentials;

import com.cloudbees.plugins.credentials.CredentialsDescriptor;
import com.cloudbees.plugins.credentials.CredentialsScope;
import com.cloudbees.plugins.credentials.common.StandardUsernamePasswordCredentials;
import com.cloudbees.plugins.credentials.impl.BaseStandardCredentials;
import hudson.util.Secret;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

public class VaultProvidedUsernamePasswordCredentials extends BaseStandardCredentials implements StandardUsernamePasswordCredentials, VaultProvidedCredentials {

  private String username;

  public VaultProvidedUsernamePasswordCredentials(
      @CheckForNull CredentialsScope scope,
      @CheckForNull String id,
      @CheckForNull String description,
      @CheckForNull String username,
      @CheckForNull String passwordPath,
      @CheckForNull String passwordField,
      @CheckForNull VaultCredential vaultCredential) {
    super(scope, id, description);
  }

  @Nonnull
  @Override
  public String getDescription() {
    return null;
  }

  @Nonnull
  @Override
  public String getId() {
    return null;
  }

  @Nonnull
  @Override
  public Secret getPassword() {
    return null;
  }

  @Nonnull
  @Override
  public String getUsername() { return username; }

  @Override
  public CredentialsScope getScope() {
    return null;
  }

  @Nonnull
  @Override
  public CredentialsDescriptor getDescriptor() {
    return null;
  }
}
