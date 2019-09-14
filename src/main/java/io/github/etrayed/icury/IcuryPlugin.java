package io.github.etrayed.icury;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Etrayed
 */
public class IcuryPlugin extends JavaPlugin {

    @Override
    public void onLoad() {
        Icury.setInstance(new Icury());
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
