package com.datapipe.jenkins.vault.provider;

import com.bettercloud.vault.Vault;
import com.bettercloud.vault.VaultConfig;
import com.bettercloud.vault.VaultException;
import com.cloudbees.plugins.credentials.Credentials;
import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.cloudbees.plugins.credentials.CredentialsScope;
import com.cloudbees.plugins.credentials.CredentialsStore;
import com.cloudbees.plugins.credentials.common.IdCredentials;
import com.datapipe.jenkins.vault.provider.config.PluginConfiguration;
import com.datapipe.jenkins.vault.provider.config.VaultConfiguration;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import hudson.model.ItemGroup;
import hudson.model.ModelObject;
import hudson.model.Saveable;
import hudson.security.ACL;
import java.util.function.Supplier;
import jenkins.model.GlobalConfiguration;
import jenkins.model.Jenkins;
import org.acegisecurity.Authentication;

import java.util.*;
import java.util.logging.Logger;

/**
 * This is the heart of the plugin. This class glues the Jenkins concepts to
 * Vault concepts.
 */
public class VaultCredentialsProvider extends CredentialsProvider implements Saveable {
    /**
     * Our logger.
     */
    private static final Logger LOGGER = Logger.getLogger(VaultCredentialsProvider.class.getName());

    private final VaultCredentialsStore store = new VaultCredentialsStore(this);

    @Nullable
    private Map<String,Vault> vaults;

    // TODO: support more then just GLOBAL scope
    private final Set<CredentialsScope> SCOPES = Collections.singleton(CredentialsScope.GLOBAL);

    private final Supplier<Collection<IdCredentials>> credentialSupplier = CredentialsSupplierFactory.create();

    private static PluginConfiguration getPluginConfiguration() {
        return GlobalConfiguration.all().get(PluginConfiguration.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<CredentialsScope> getScopes(ModelObject object) {
        // TODO: support more than just GLOBAL scope.
        // Given we only support GLOBAL scope, that scope will be the same for all objects.
        return SCOPES;
    }

    @Override
    public CredentialsStore getStore(ModelObject object) {
        // TODO: support more then just GLOBAL scope.
        // Given we only support GLOBAL scope, this store will always be null unless we're at the top level.
        return object == Jenkins.getInstanceOrNull() ? store : null;
    }

    Vault getVault(String name) {
        VaultConfiguration vaultConfiguration = getPluginConfiguration().getVaultConfiguration(name);
        if(! vaults.containsKey(name))
            return null;
        if(vaults.get(name) == null) {
            try {
                final VaultConfig vaultConfig = new VaultConfig()
                    .address(vaultConfiguration.getVaultUrl())
                    .token(
                        vaultConfiguration.getVaultAuthenticationConfiguration().getVaultAuthentication().getVaultToken()
                            .getPlainText())
                    .build();
                vaults.put(name, new Vault(vaultConfig));
            } catch (VaultException vaultException) {
                throw new RuntimeException("Unable to initialize vault" + vaultException.getMessage(), vaultException);
            }
        }
        return vaults.get(name);
    }

    @NonNull
    @Override
    public <C extends Credentials> List<C> getCredentials(@NonNull Class<C> type,
        @Nullable ItemGroup itemGroup, @Nullable Authentication authentication) {

        if (ACL.SYSTEM.equals(authentication)) {
            final ArrayList<C> list = new ArrayList<>();
            for (IdCredentials credential : credentialSupplier.get()) {
                if(type.isAssignableFrom(credential.getClass())) {
                    list.add(type.cast(credential));
                }
            }
        }

        return Collections.emptyList();
    }

    @Override
    public String getIconClassName() {
        return "icon-vault-credentials-store";
    }
}
