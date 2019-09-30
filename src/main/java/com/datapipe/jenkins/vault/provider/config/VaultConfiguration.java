package com.datapipe.jenkins.vault.provider.config;

import com.datapipe.jenkins.vault.provider.Messages;
import com.google.inject.internal.asm.$ClassWriter;
import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import javax.annotation.Nonnull;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;


public class VaultConfiguration extends AbstractDescribableImpl<VaultConfiguration> {

    private String vaultName;
    private String vaultUrl;
    private VaultAuthenticationConfiguration vaultAuthenticationConfiguration;

    @DataBoundConstructor
    public VaultConfiguration(
        String vaultName,
        String vaultUrl,
        VaultAuthenticationConfiguration vaultAuthenticationConfiguration
    ) {
        this.vaultName = vaultName;
        this.vaultUrl = vaultUrl;
        this.vaultAuthenticationConfiguration = vaultAuthenticationConfiguration;
    }

    public String getVaultName() {
        return vaultName;
    }

    @DataBoundSetter
    public void setVaultName(String vaultName) {
        this.vaultName = vaultName;
    }

    public String getVaultUrl() {
        return vaultUrl;
    }

    @DataBoundSetter
    public void setVaultUrl(String vaultUrl) {
        this.vaultUrl = vaultUrl;
    }

    public VaultAuthenticationConfiguration getVaultAuthenticationConfiguration() {
        return vaultAuthenticationConfiguration;
    }

    @DataBoundSetter
    public void setVaultAuthenticationConfiguration(
        VaultAuthenticationConfiguration vaultAuthenticationConfiguration) {
        this.vaultAuthenticationConfiguration = vaultAuthenticationConfiguration;
    }

    @Extension
    @Symbol("vaultConfiguration")
    public static class DescriptorImpl extends Descriptor<VaultConfiguration> {

        @Nonnull
        @Override
        public String getDisplayName() {
            return Messages.vaultConfiguration();
        }
    }

}
