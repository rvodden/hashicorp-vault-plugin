package com.datapipe.jenkins.vault.provider.config.authentication;

import com.cloudbees.plugins.credentials.CredentialsMatchers;
import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.cloudbees.plugins.credentials.common.StandardListBoxModel;
import com.datapipe.jenkins.vault.jcasc.secrets.VaultAuthenticator;
import com.datapipe.jenkins.vault.provider.Messages;
import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.Item;
import hudson.security.ACL;
import hudson.util.ListBoxModel;
import hudson.util.Secret;
import java.util.Collections;
import javax.annotation.Nonnull;
import jenkins.model.Jenkins;
import org.antlr.v4.runtime.misc.NotNull;
import org.jenkinsci.Symbol;
import org.jenkinsci.plugins.plaincredentials.StringCredentials;
import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

import static hudson.Util.fixEmptyAndTrim;

public class TokenVaultAuthentication extends VaultAuthentication {

    private String vaultTokenCredentialId;

    @DataBoundConstructor
    public TokenVaultAuthentication(
        String vaultTokenCredential
    ) {
        this.vaultTokenCredentialId = vaultTokenCredential;
    }

    public String getVaultTokenCredentialId() {
        return vaultTokenCredentialId;
    }

    @Override
    public Secret getVaultToken() {
        return getCredentialById(getVaultTokenCredentialId()).getSecret();
    }

    @DataBoundSetter
    public void setVaultTokenCredentialId(String vaultTokenCredentialId) {
        this.vaultTokenCredentialId = fixEmptyAndTrim(vaultTokenCredentialId);
    }

    public VaultAuthenticator getAuthenticator() {
        return null;
    }

    private StringCredentials getCredentialById(@NotNull String id) {
        return CredentialsMatchers.firstOrNull(
            CredentialsProvider.lookupCredentials(
                StringCredentials.class,
                (Item) null, ACL.SYSTEM, Collections.emptyList())
            ,
            CredentialsMatchers.withId(id)
        );
    }

    @Extension
    @Symbol("vaultTokenCredential")
    public static class DescriptorImpl extends Descriptor<VaultAuthentication> {

        @Nonnull
        @Override
        public String getDisplayName() {
            return Messages.tokenVaultAuthentication();
        }

        public ListBoxModel doFillVaultTokenCredentialIdItems(@AncestorInPath Item item,
            @QueryParameter String credentialsId) {
            // This is needed for folders: credentials bound to a folder are
            // realized through domain requirements

            StandardListBoxModel standardListBoxModel = new StandardListBoxModel();

            if (item == null) {
                if (!Jenkins.get().hasPermission(Jenkins.ADMINISTER)) {
                    return standardListBoxModel.includeCurrentValue(credentialsId);
                }
            } else {
                if (!Jenkins.get().hasPermission(Item.EXTENDED_READ) &&
                    ! item.hasPermission(CredentialsProvider.USE_ITEM)) {
                    return standardListBoxModel.includeCurrentValue(credentialsId);
                }
            }

            return standardListBoxModel.includeCurrentValue(credentialsId)
                .includeMatchingAs(
                        ACL.SYSTEM,
                        item,
                        StringCredentials.class,
                        Collections.emptyList(),
                        null
                );
        }
    }

}
