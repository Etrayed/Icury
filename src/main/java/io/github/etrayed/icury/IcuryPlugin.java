package io.github.etrayed.icury;

import com.j256.ormlite.logger.LocalLog;
import com.j256.ormlite.logger.LoggerFactory;

import io.github.etrayed.icury.util.ConfigurationInterpreter;
import io.github.etrayed.icury.util.LibraryLoader;
import io.github.etrayed.icury.util.Updater;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

/**
 * @author Etrayed
 */
public class IcuryPlugin extends JavaPlugin {

    public final File ownFile = getFile();

    @Override
    public void onLoad() {
        this.getDataFolder().mkdir();

        this.getConfig().options().copyDefaults(true);
        this.saveConfig();

        configOrmliteLog();

        Icury.setLogger(this.getLogger());
        Icury.setVersion(this.getDescription().getVersion());

        LibraryLoader.loadAll();

        Icury.setConfigurationInterpreter(new ConfigurationInterpreter(this.getConfig()));

        if(Icury.getConfigurationInterpreter().isAutoUpdate()) {
            Updater.check();
        }
    }

    private void configOrmliteLog() {
        File logFile = new File(this.getDataFolder(), "sqlActions.log");

        if(!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.setProperty(LocalLog.LOCAL_LOG_FILE_PROPERTY, logFile.getAbsolutePath());
        System.setProperty(LoggerFactory.LOG_TYPE_SYSTEM_PROPERTY, "LOCAL");
    }

    @Override
    public void onEnable() {
        Icury.setInstance();
    }

    @Override
    public void onDisable() {
        Icury.getInstance().shutdown();
    }
}
