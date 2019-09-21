package io.github.etrayed.icury;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.github.etrayed.icury.storage.Storage;
import io.github.etrayed.icury.storage.json.JsonStorage;
import io.github.etrayed.icury.storage.mongodb.MongoStorage;
import io.github.etrayed.icury.storage.mysql.MySQLStorage;
import io.github.etrayed.icury.storage.xml.XmlStorage;
import io.github.etrayed.icury.storage.yaml.YamlStorage;
import io.github.etrayed.icury.util.ConfigurationInterpreter;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author Etrayed
 */
public class Icury {

    private static Icury instance;

    private static Logger logger;

    private static String version;

    private static ConfigurationInterpreter configurationInterpreter;

    private Gson gson;

    private Storage storage;

    private Icury() {}

    @SuppressWarnings("unchecked")
    private void init() {
        this.gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

        this.storage = storageByType();
        this.storage.load();
    }

    private Storage storageByType() {
        switch (Icury.getConfigurationInterpreter().getStorageType()) {
            case JSON:
                return new JsonStorage();
            case MONGODB:
                return new MongoStorage();
            case MYSQL:
                return new MySQLStorage();
            case XML:
                return new XmlStorage();
            case YAML:
                return new YamlStorage();

            default:
                throw new IllegalArgumentException("Invalid storageType: "
                        + Icury.getConfigurationInterpreter().getStorageType());
        }
    }

    void shutdown() {
        try {
            this.storage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Gson getGson() {
        return gson;
    }

    public Storage getStorage() {
        return storage;
    }

    public static Icury getInstance() {
        return instance;
    }

    static void setInstance() {
        (Icury.instance = new Icury()).init();
    }

    public static Logger getLogger() {
        return logger;
    }

    static void setLogger(Logger logger) {
        Icury.logger = logger;
    }

    static void setConfigurationInterpreter(ConfigurationInterpreter configurationInterpreter) {
        Icury.configurationInterpreter = configurationInterpreter;
    }

    public static ConfigurationInterpreter getConfigurationInterpreter() {
        return configurationInterpreter;
    }

    public static String getVersion() {
        return version;
    }

    static void setVersion(String version) {
        Icury.version = version;
    }
}