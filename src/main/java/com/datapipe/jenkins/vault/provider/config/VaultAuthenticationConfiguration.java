package com.datapipe.jenkins.vault.provider.config;

import com.datapipe.jenkins.vault.provider.Messages;
import com.datapipe.jenkins.vault.provider.config.authentication.VaultAuthentication;
import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import java.util.List;
import jenkins.model.Jenkins;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

public class VaultAuthenticationConfiguration extends AbstractDescribableImpl<VaultAuthenticationConfiguration> {

    private VaultAuthentication vaultAuthentication;

    @DataBoundConstructor
    public VaultAuthenticationConfiguration(VaultAuthentication vaultAuthentication) {
        this.vaultAuthentication = vaultAuthentication;
    }

    @Override
    public Descriptor<VaultAuthenticationConfiguration> getDescriptor() {
        return null;
    }

    public VaultAuthentication getVaultAuthentication() {
        return vaultAuthentication;
    }

    @DataBoundSetter
    public void setVaultAuthentication(
        VaultAuthentication vaultAuthentication) {
        this.vaultAuthentication = vaultAuthentication;
    }

    @Extension
    @Symbol("vaultAuthenticationConfiguration")
    public static class DescriptorImpl extends Descriptor<VaultAuthenticationConfiguration> {
        public String getDisplayName() {
            return Messages.vaultAuthenticationConfiguration();
        }

        public List<Descriptor<VaultAuthentication>> getAuthenticationDescriptors() {
            return Jenkins.getInstanceOrNull().getDescriptorList(VaultAuthentication.class);
        }
    }
}
