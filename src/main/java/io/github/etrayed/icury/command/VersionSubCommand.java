package io.github.etrayed.icury.command;

import io.github.etrayed.icury.Icury;
import io.github.etrayed.icury.util.Updater;

import org.bukkit.command.CommandSender;

/**
 * @author Etrayed
 */
class VersionSubCommand extends SubCommand {

    VersionSubCommand() {
        super("Displays the current version");
    }

    @Override
    void execute(CommandSender commandSender, String[] args) {
        sendMessage(commandSender, "Icury " + Icury.getVersion() + " by Etrayed");

        if(Updater.getLatestVersion().hasChanged()) {
            sendMessage(commandSender, "§aNewer version available: §l" + Updater.getLatestVersion().getVersion());
        }
    }
}
