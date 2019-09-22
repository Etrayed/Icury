package io.github.etrayed.icury.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.github.etrayed.icury.Icury;
import io.github.etrayed.icury.IcuryPlugin;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.apache.ApacheClient;

import org.apache.http.impl.client.HttpClients;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * @author Etrayed
 */
public class Updater {

    private static final JsonParser PARSER = new JsonParser();

    private static Version latestVersion;

    static {
        Unirest.config().httpClient(new ApacheClient(HttpClients.createDefault(), Unirest.config()));
    }

    public static void check() {
        Icury.getLogger().info("Checking for updates...");

        HttpResponse<String> response = Unirest.get("http://check.etrayed.de").asString();

        if(response.getStatus() != 200) {
            Icury.getLogger().severe("Failed to check for updates: " + response.getStatus() + " - "
                    + response.getStatusText());

            return;
        }

        JsonObject responseObject = PARSER.parse(response.getBody()).getAsJsonObject().getAsJsonObject("icury");

        setupUpdate(latestVersion = new Version(responseObject.get("version").getAsString(), responseObject.get("url").getAsString()));
    }

    private static void setupUpdate(Version version) {
        if(!version.hasChanged) {
            Icury.getLogger().info("Plugin is UP-TO-DATE.");
            return;
        }

        Icury.getLogger().info("Downloading the newer version " + version.version + "....");

        long timestamp = System.currentTimeMillis();

        Bukkit.getUpdateFolderFile().mkdir();

        HttpResponse<File> response = Unirest.get(version.url).asFile(new File(Bukkit.getUpdateFolderFile(),
                JavaPlugin.getPlugin(IcuryPlugin.class).ownFile.getName()).getPath());

        if(response.getStatus() != 200) {
            Icury.getLogger().severe("Failed to download the newer version: " + response.getStatus() + " - "
                    + response.getStatusText());

            return;
        }

        Icury.getLogger().info("Update completed! Took " + (System.currentTimeMillis()  - timestamp) + "ms.");
        Icury.getLogger().info("Restart (or reload) the server to activate changes.");
    }

    public static Version getLatestVersion() {
        return latestVersion;
    }

    public static final class Version {

        private final String version, url;

        private final boolean hasChanged;

        private Version(String version, String url) {
            this.version = version;
            this.url = url;
            this.hasChanged = !version.equals(Icury.getVersion());
        }

        public String getVersion() {
            return version;
        }

        public boolean hasChanged() {
            return hasChanged;
        }
    }
}
