package io.github.etrayed.icury.command;

import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.*;

/**
 * @author Etrayed
 */
abstract class SubCommand {

    private static final Map<String, SubCommand> REGISTERED_COMMANDS = new HashMap<>();

    final String description, permission;

    SubCommand(String description) {
        this(description, "");
    }

     SubCommand(String description, String permission) {
        this.description = description;
        this.permission = permission;
    }

    abstract void execute(CommandSender commandSender, String[] args);

    List<String> tabComplete(CommandSender commandSender, String[] args) {
        return new ArrayList<>();
    }

    List<String> playerCompleter(CommandSender commandSender, String prefix) {
        List<String> compatibleResults = new ArrayList<>();

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if((!(commandSender instanceof Player) || ((Player) commandSender).canSee(onlinePlayer))
                    && StringUtil.startsWithIgnoreCase(onlinePlayer.getName(), prefix)) {
                compatibleResults.add(onlinePlayer.getName());
            }
        }

        compatibleResults.sort(String.CASE_INSENSITIVE_ORDER);

        return compatibleResults;
    }

    static void registerCommands() {
        REGISTERED_COMMANDS.put("version", new VersionSubCommand());
    }

    static SubCommand getSubCommand(String name) {
        for (String s : REGISTERED_COMMANDS.keySet()) {
            if(s.equalsIgnoreCase(name)) {
                return REGISTERED_COMMANDS.get(s);
            }
        }

        return null;
    }

    static Set<String> allNames() {
        return REGISTERED_COMMANDS.keySet();
    }

    static void sendMessage(CommandSender commandSender, String message) {
        if(commandSender instanceof Player) {
            ((Player) commandSender).spigot().sendMessage(TextComponent.fromLegacyText(message));
        } else {
            commandSender.sendMessage(removeAllColorCodes(message));
        }
    }

    private static String removeAllColorCodes(String message) {
        StringBuilder stringBuilder = new StringBuilder(message);

        int lastIndex = -1;

        while ((lastIndex = stringBuilder.indexOf("§", lastIndex + 1)) != -1) {
            ChatColor chatColor = ChatColor.getByChar(stringBuilder.charAt(lastIndex + 1));

            int end = lastIndex + 2;

            if(chatColor == null) {
                end--;
            }

            stringBuilder.delete(lastIndex, end);
        }

        return new String(stringBuilder);
    }
}
