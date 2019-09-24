package com.datapipe.jenkins.vault.credentials;

import com.cloudbees.plugins.credentials.CredentialsScope;
import com.cloudbees.plugins.credentials.common.StandardCertificateCredentials;
import com.cloudbees.plugins.credentials.impl.BaseStandardCredentials;
import hudson.util.Secret;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.security.KeyStore;

public class VaultProvidedCertificateCredentials extends BaseStandardCredentials implements StandardCertificateCredentials, VaultProvidedCredentials {

  private VaultCredential vaultCredential;

  public VaultProvidedCertificateCredentials(@CheckForNull String id,
                                             @CheckForNull String description,
                                             @CheckForNull String keyStorePath,
                                             @CheckForNull String keyStoreField,
                                             @CheckForNull String passwordPath,
                                             @CheckForNull String passwordField,
                                             @CheckForNull VaultCredential vaultCredential) {
    this(null,id,description,keyStorePath,keyStoreField,passwordPath,passwordField,vaultCredential);
  }

  public VaultProvidedCertificateCredentials(@CheckForNull CredentialsScope scope,
                                             @CheckForNull String id,
                                             @CheckForNull String description,
                                             @CheckForNull String keyStorePath,
                                             @CheckForNull String keyStoreField,
                                             @CheckForNull String passwordPath,
                                             @CheckForNull String passwordField,
                                             @CheckForNull VaultCredential vaultCredential) {
    super(scope, id, description);
    this.vaultCredential = vaultCredential;
  }

  @Nonnull
  @Override
  public KeyStore getKeyStore() {
    return null;
  }

  @Nonnull
  @Override
  public Secret getPassword() {
    return null;
  }

}
