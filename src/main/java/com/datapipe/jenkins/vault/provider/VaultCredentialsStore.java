package com.datapipe.jenkins.vault.provider;

import com.cloudbees.plugins.credentials.Credentials;
import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.cloudbees.plugins.credentials.CredentialsStore;
import com.cloudbees.plugins.credentials.CredentialsStoreAction;
import com.cloudbees.plugins.credentials.domains.Domain;
import hudson.model.ModelObject;
import hudson.security.ACL;
import hudson.security.Permission;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import jenkins.model.Jenkins;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import org.acegisecurity.Authentication;
import org.kohsuke.stapler.export.ExportedBean;

/**
 * This is the User Interface code which exposes System level Vault Credential
 * operations to the user.
 */
public class VaultCredentialsStore extends CredentialsStore {

    private final VaultCredentialsProvider provider;
    private final VaultCredentialsStoreAction action = new VaultCredentialsStoreAction(this);

    public VaultCredentialsStore(VaultCredentialsProvider vaultCredentialsProvider) {
        super(VaultCredentialsProvider.class);
        this.provider = vaultCredentialsProvider;
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public ModelObject getContext() {
        // TODO: support more than the GLOBAL scope.
        return Objects.requireNonNull(Jenkins.getInstanceOrNull());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasPermission(@NonNull Authentication authentication, @NonNull Permission permission) {
        return CredentialsProvider.VIEW.equals(permission)
            && Jenkins.getInstanceOrNull().getACL().hasPermission(authentication, permission);
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public List<Credentials> getCredentials(@NonNull Domain domain) {
        // TODO: support more than just the Global domain
        if (Domain.global().equals(domain)
            && Jenkins.getInstanceOrNull().hasPermission(CredentialsProvider.VIEW)) {
            return provider.getCredentials(Credentials.class, Jenkins.getInstanceOrNull(), ACL.SYSTEM);
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addCredentials(@NonNull Domain domain, @NonNull Credentials credentials) {
        throw new UnsupportedOperationException("Jenkins may not add credentials to Vault.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeCredentials(@NonNull Domain domain, @NonNull Credentials credentials) {
        throw new UnsupportedOperationException("Jenkins may not remove credentials from Vault.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean updateCredentials(@NonNull Domain domain, @NonNull Credentials current,
        @NonNull Credentials replacement) {
        throw new UnsupportedOperationException("Jenkins may not update credentials in Vault.");
    }

    @Nullable
    @Override
    public CredentialsStoreAction getStoreAction() {
        return action;
    }

    /**
     * This is used to store the (not secret) parts of the credentials
     */
    public static class VaultCredentialsProperty extends

    /**
     * Expose the store.
     */
    @ExportedBean
    public static class VaultCredentialsStoreAction extends CredentialsStoreAction {

        private static final String ICON_CLASS = "icon-vault-credentials-store";

        private final VaultCredentialsStore vaultCredentialsStore;

        private VaultCredentialsStoreAction(VaultCredentialsStore vaultCredentialsStore) {
            this.vaultCredentialsStore = vaultCredentialsStore;
            addIcons();
        }

        private void addIcons() {
            // TODO: add icons
        }

        @Override
        @NonNull
        public CredentialsStore getStore() {
            return getStore();
        }

        @Override
        public String getIconClassName() {
            return isVisible() ? ICON_CLASS : null;
        }

        @Override
        public String getDisplayName() { return Messages.vault(); }
    }
}
