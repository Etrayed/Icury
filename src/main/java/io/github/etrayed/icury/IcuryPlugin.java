package io.github.etrayed.icury;

import io.github.etrayed.icury.util.LibraryLoader;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Etrayed
 */
public class IcuryPlugin extends JavaPlugin {

    @Override
    public void onLoad() {
        this.getDataFolder().mkdir();

        LibraryLoader.loadAll();

        Icury.setInstance(new Icury());
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
