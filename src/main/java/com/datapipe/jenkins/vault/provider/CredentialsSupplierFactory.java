package com.datapipe.jenkins.vault.provider;

import com.bettercloud.vault.Vault;
import com.bettercloud.vault.VaultConfig;
import com.bettercloud.vault.VaultException;
import com.bettercloud.vault.response.LogicalResponse;
import com.cloudbees.plugins.credentials.common.IdCredentials;
import com.datapipe.jenkins.vault.provider.config.PluginConfiguration;
import com.datapipe.jenkins.vault.provider.config.VaultConfiguration;
import com.datapipe.jenkins.vault.provider.credentials.VaultStringCredentialsImpl;
import com.datapipe.jenkins.vault.provider.util.Memoizer;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import jenkins.model.GlobalConfiguration;


/**
 * this class will be used if we implement secret searching - isn't used otherwise
 */
final class CredentialsSupplierFactory {

    // prevent instantiation
    private CredentialsSupplierFactory() {

    }

    static Supplier<Collection<IdCredentials>> create() {
        final Supplier<Collection<IdCredentials>> baseSupplier = new LazyVaultCredentialsSupplier(CredentialsSupplierFactory::getPluginConfiguration);
        return Memoizer.memoizeWithExpiration(baseSupplier, Duration.ofMinutes(5L));
    }

    private static PluginConfiguration getPluginConfiguration() {
        return GlobalConfiguration.all().get(PluginConfiguration.class);
    }

    private static class LazyVaultCredentialsSupplier implements Supplier<Collection<IdCredentials>> {

        private static final Logger LOG = Logger.getLogger(LazyVaultCredentialsSupplier.class.getName());

        private final Supplier<PluginConfiguration> configurationSupplier;

        private LazyVaultCredentialsSupplier( Supplier<PluginConfiguration> configurationSupplier ) {
            this.configurationSupplier = configurationSupplier;
        }

        @Override
        // TODO: make this lazy
        public Collection<IdCredentials> get() {
            final PluginConfiguration pluginConfiguration = configurationSupplier.get();
            final VaultConfiguration vaultConfiguration = pluginConfiguration.getVaultConfigurations().get(0);
            final Vault vault;
            try {
                final VaultConfig vaultConfig = new VaultConfig()
                    .address(vaultConfiguration.getVaultUrl())
                    .token(
                        vaultConfiguration.getVaultAuthenticationConfiguration().getVaultAuthentication().getVaultToken()
                            .getPlainText())
                    .build();
                vault = new Vault(vaultConfig);
            } catch (VaultException vaultException) {
                throw new RuntimeException("Unable to initialize vault" + vaultException.getMessage(), vaultException);
            }
            return new VaultCredentialsSupplier(vault).get();
        }
    }

    private static class VaultCredentialsSupplier implements Supplier<Collection<IdCredentials>> {

        private static final Logger LOG = Logger.getLogger(VaultCredentialsSupplier.class.getName());

        private Vault vault;

        private VaultCredentialsSupplier(Vault vault) {
            this.vault = vault;
        }

        @Override
        public Collection<IdCredentials> get() {
            LOG.log(Level.FINE,"Retrieve paths from Vault");

            final LogicalResponse logicalResponse;
            // TODO: make root path configurable
            // TODO: implement tree walk
            try {
                logicalResponse = vault.logical().list("secret");
            } catch ( VaultException vaultException ) {
                LOG.log(Level.INFO,"Listing vault secrets failed: " + vaultException.getMessage());
                LOG.log(Level.INFO,vaultException.getStackTrace().toString());
                return Collections.emptyList();
            }

            final List<String> pathList = logicalResponse.getListData();
            final List<IdCredentials> credentials = pathList.stream().map(
                    path -> new VaultStringCredentialsImpl(path, path, "value", vault)
                ).collect(Collectors.toList());
            return credentials;
        }
    }

}
