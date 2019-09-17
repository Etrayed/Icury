package io.github.etrayed.icury;

import com.j256.ormlite.logger.LocalLog;
import com.j256.ormlite.logger.LoggerFactory;

import io.github.etrayed.icury.util.ConfigurationInterpreter;
import io.github.etrayed.icury.util.LibraryLoader;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Etrayed
 */
public class IcuryPlugin extends JavaPlugin {

    @Override
    public void onLoad() {
        this.getDataFolder().mkdir();

        this.getConfig().options().copyDefaults(true);

        System.setProperty(LocalLog.LOCAL_LOG_LEVEL_PROPERTY, LocalLog.Level.TRACE.name());
        System.setProperty(LoggerFactory.LOG_TYPE_SYSTEM_PROPERTY, LoggerFactory.LogType.LOCAL.name());

        Icury.setLogger(this.getLogger());
        Icury.setConfigurationInterpreter(new ConfigurationInterpreter(this.getConfig()));

        LibraryLoader.loadAll();
    }

    @Override
    public void onEnable() {
        Icury.setInstance(new Icury());
    }

    @Override
    public void onDisable() {

    }
}
