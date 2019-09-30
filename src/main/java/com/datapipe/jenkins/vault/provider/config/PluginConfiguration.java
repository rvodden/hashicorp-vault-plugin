package com.datapipe.jenkins.vault.provider.config;

import edu.umd.cs.findbugs.annotations.Nullable;
import hudson.Extension;
import java.util.List;
import jenkins.model.GlobalConfiguration;
import net.sf.json.JSONObject;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.StaplerRequest;

@Extension
@Symbol("vaultCredentialsProvider")
public class PluginConfiguration  extends GlobalConfiguration {

    private List<VaultConfiguration> vaultConfigurations;

    public PluginConfiguration() {
        load();
    }

    public List<VaultConfiguration> getVaultConfigurations() {
        return vaultConfigurations;
    }

    @DataBoundSetter
    public void setVaultConfigurations(
        List<VaultConfiguration> vaultConfigurations) {
        this.vaultConfigurations = vaultConfigurations;
    }

    @Nullable
    public VaultConfiguration getVaultConfiguration(String name) {
        List<VaultConfiguration> vaultConfigurations = getVaultConfigurations();
        for( VaultConfiguration vaultConfiguration : vaultConfigurations) {
            if(vaultConfiguration.getVaultName() == name) {
                return vaultConfiguration;
            }
        }
        return null;
    }
    @Override
    public synchronized boolean configure(StaplerRequest req, JSONObject json) {
        // This method is unnecessary, except to apply the following workaround.
        // Workaround: Set any optional struct fields to null before binding configuration.
        // https://groups.google.com/forum/#!msg/jenkinsci-dev/MuRJ-yPRRoo/AvoPZAgbAAAJ

        req.bindJSON(this, json);
        save();
        return true;
    }
}
