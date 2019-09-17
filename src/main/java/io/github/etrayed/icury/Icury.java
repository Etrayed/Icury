package io.github.etrayed.icury;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

    Icury() {
        this.gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
    }

    public Gson getGson() {
        return gson;
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