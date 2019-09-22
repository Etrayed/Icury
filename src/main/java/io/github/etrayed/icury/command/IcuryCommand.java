package io.github.etrayed.icury.command;

import io.github.etrayed.icury.Icury;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Etrayed
 */
public class IcuryCommand implements CommandExecutor, TabCompleter {

    private static final String FULL_PERMISSION = "icury.*";

    public IcuryCommand() {
        SubCommand.registerCommands();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!commandSender.hasPermission(FULL_PERMISSION) && !commandSender.hasPermission("*")) {
            SubCommand.sendMessage(commandSender, Icury.getConfigurationInterpreter().getNoPermissionMessage(FULL_PERMISSION));

            return false;
        }

        SubCommand subCommand;

        if(strings.length == 0 || (subCommand = SubCommand.getSubCommand(strings[0])) == null) {
            SubCommand.sendMessage(commandSender, "§7Available SubCommands:");

            if(commandSender instanceof Player) {
                for (String name : SubCommand.allNames()) {
                    TextComponent nameComponent = new TextComponent("§5" + name);
                    TextComponent descriptionComponent = new TextComponent(TextComponent.fromLegacyText(" §8- §7"
                            + SubCommand.getSubCommand(name).description));

                    nameComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/icury " + name));
                    nameComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            TextComponent.fromLegacyText("§5Click §7to insert \"§5/icury " + name + "§7\"")));

                    nameComponent.addExtra(descriptionComponent);

                    ((Player) commandSender).spigot().sendMessage(nameComponent);
                }
            } else {
                for (String name : SubCommand.allNames()) {
                    SubCommand.sendMessage(commandSender, name + " - "
                            + SubCommand.getSubCommand(name).description);
                }
            }

            return false;
        }

        if(!subCommand.permission.isEmpty() && !commandSender.hasPermission("icury." + subCommand.permission)
                && !commandSender.hasPermission("*") && !commandSender.hasPermission(FULL_PERMISSION)) {
            SubCommand.sendMessage(commandSender, Icury.getConfigurationInterpreter().getNoPermissionMessage(subCommand.permission));

            return false;
        }

        subCommand.execute(commandSender, Arrays.copyOfRange(strings, 1, strings.length));

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        SubCommand subCommand;

        if(strings.length == 0 || (subCommand = SubCommand.getSubCommand(strings[0])) == null) {
            return new ArrayList<>(SubCommand.allNames());
        }

        return subCommand.tabComplete(commandSender, Arrays.copyOfRange(strings, 1, strings.length));
    }
}
