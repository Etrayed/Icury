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

import java.util.logging.Logger;

/**
 * @author Etrayed
 */
public class Icury {

    private static Icury instance;

    private static Logger logger;

    private static ConfigurationInterpreter configurationInterpreter;

    private final Gson gson;

    private final Storage storage;

    Icury() {
        this.gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

        this.storage = storageByType();
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

    public Gson getGson() {
        return gson;
    }

    public Storage getStorage() {
        return storage;
    }

    public static Icury getInstance() {
        return instance;
    }

    static Icury setInstance(Icury instance) {
        return Icury.instance = instance;
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
}