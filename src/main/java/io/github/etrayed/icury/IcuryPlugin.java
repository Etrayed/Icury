package io.github.etrayed.icury;

import com.j256.ormlite.logger.LocalLog;
import com.j256.ormlite.logger.LoggerFactory;

import io.github.etrayed.icury.command.IcuryCommand;
import io.github.etrayed.icury.util.ConfigurationInterpreter;
import io.github.etrayed.icury.util.LibraryLoader;
import io.github.etrayed.icury.util.Updater;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

/**
 * @author Etrayed
 */
public class IcuryPlugin extends JavaPlugin {

    static final boolean DEBUG = Boolean.getBoolean("io.github.etrayed.icury.debug");

    public final File ownFile = getFile();

    private final IcuryCommand icuryCommand = new IcuryCommand();

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

        PluginCommand pluginCommand = this.getCommand("icury");

        pluginCommand.setExecutor(icuryCommand);
        pluginCommand.setTabCompleter(icuryCommand);
    }

    @Override
    public void onDisable() {
        Icury.getInstance().shutdown();
    }
}
