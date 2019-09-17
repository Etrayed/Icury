package io.github.etrayed.icury.util;

import io.github.etrayed.icury.storage.StorageType;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author Etrayed
 */
public class ConfigurationInterpreter {

    private final FileConfiguration instance;

    public ConfigurationInterpreter(FileConfiguration instance) {
        this.instance = instance;
    }

    public StorageType getStorageType() {
        return StorageType.valueOf(instance.getString("storageType").toUpperCase());
    }

    public DatabaseCredentials getDatabaseCredentials() {
        ConfigurationSection credentialsSection = instance.getConfigurationSection("databaseCredentials");

        return new DatabaseCredentials(credentialsSection.getString("hostname"), credentialsSection.getString("databaseName"),
                credentialsSection.getString("username"), credentialsSection.getString("password"),
                credentialsSection.getString("customUrl"), credentialsSection.getInt("port"));
    }

    public static final class DatabaseCredentials {

        public final String hostname, databaseName, username, password, customUrl;

        public final int port;

        private DatabaseCredentials(String hostname, String databaseName, String username, String password, String customUrl, int port) {
            this.hostname = hostname;
            this.databaseName = databaseName;
            this.username = username;
            this.password = password;
            this.customUrl = customUrl;
            this.port = port;
        }
    }
}
