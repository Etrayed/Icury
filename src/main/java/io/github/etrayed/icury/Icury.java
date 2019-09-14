package io.github.etrayed.icury;

/**
 * @author Etrayed
 */
public class Icury {

    private static Icury instance;

    Icury() {

    }

    public static Icury getInstance() {
        return instance;
    }

    static Icury setInstance(Icury instance) {
        return Icury.instance = instance;
    }
}