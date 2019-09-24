package com.datapipe.jenkins.vault.credentials;

import com.cloudbees.plugins.credentials.Credentials;
import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.cloudbees.plugins.credentials.domains.Domain;
import com.cloudbees.plugins.credentials.domains.DomainCredentials;
import com.cloudbees.plugins.credentials.domains.DomainRequirement;
import hudson.ExtensionList;
import hudson.XmlFile;
import hudson.model.ItemGroup;
import hudson.model.User;
import hudson.security.ACL;
import hudson.util.CopyOnWriteMap;
import jenkins.model.Jenkins;
import org.acegisecurity.Authentication;
import org.acegisecurity.providers.anonymous.AnonymousAuthenticationToken;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class VaultCredentialsProvider extends CredentialsProvider {
  /**
   * Our logger.
   */
  private static final Logger LOGGER = Logger.getLogger(VaultCredentialsProvider.class.getName());

  /**
   * In memory store of the (non-secrets parts of the) credentials
   */
  private Map<Domain, List<VaultProvidedCredentials>> domainCredentialsMap = new CopyOnWriteMap.Hash<Domain, List<VaultProvidedCredentials>>();

  public VaultCredentialsProvider() {
    try {
      XmlFile xml = getConfigFile();
      if (xml.exists()) {
        xml.unmarshal(this);
      }
    } catch (IOException ioException) {
      LOGGER.log(Level.SEVERE, "Failed to read the existing credentials", ioException);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public <C extends Credentials> List<C> getCredentials(
      @Nonnull Class<C> type,
      @Nullable ItemGroup itemGroup,
      @Nullable Authentication authentication) {
    return getCredentials(type, itemGroup, authentication, Collections.<DomainRequirement> emptyList());
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public <C extends Credentials> List<C> getCredentials(
      @Nonnull Class<C> aClass,
      @Nullable ItemGroup itemGroup,
      @Nullable Authentication authentication,
      @Nonnull List<DomainRequirement> domainRequirements) {

      return (List<C>) domainCredentialsMap.get(domainRequirements);
  }

  /**
   * Gets the singleton instance.
   *
   * @return the singleton instance.
   */
  public static VaultCredentialsProvider getInstance() {
    return ExtensionList.lookup(VaultCredentialsProvider.class).get(VaultCredentialsProvider.class);
  }


}
