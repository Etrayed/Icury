package io.github.etrayed.icury;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Etrayed
 */
public class Icury {

    private static Icury instance;

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
}