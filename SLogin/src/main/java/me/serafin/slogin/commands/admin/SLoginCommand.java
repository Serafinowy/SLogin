package me.serafin.slogin.commands.admin;

import me.serafin.slogin.SLogin;
import me.serafin.slogin.managers.LangManager;
import me.serafin.slogin.models.Lang;
import me.serafin.slogin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class SLoginCommand implements CommandExecutor, TabCompleter {

    private final ArrayList<SubCommand> commands = new ArrayList<>();
    private final LangManager langManager;

    public SLoginCommand(SLogin plugin) {
        this.langManager = plugin.getLangManager();

        commands.add(new PlayerInfoSubCommand(plugin));
        commands.add(new ForceLoginSubCommand(plugin));
        commands.add(new RegisterSubCommand(plugin));
        commands.add(new ChangePasswordSubCommand(plugin));
        commands.add(new UnRegisterSubCommand(plugin));
        commands.add(new ReloadSubCommand(plugin));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        Lang lang = langManager.getLang("default");
        if (sender instanceof Player)
            lang = langManager.getLang(((Player) sender).getLocale());

        if (args.length != 0) {
            for (SubCommand subCommand : commands) {
                if (subCommand.getName().equals(args[0].toLowerCase()) || subCommand.getAliases().contains(args[0].toLowerCase())) {
                    subCommand.perform(sender, args);
                    return true;
                }
            }
        }

        sender.sendMessage("");
        sender.sendMessage(lang.admin_commandList_title);
        sender.sendMessage("");

        for (SubCommand subCommand : commands) {
            sender.spigot().sendMessage(Utils.sendCommandSuggest(
                    lang.admin_commandList_chatFormat
                            .replace("{COMMAND}", subCommand.getSyntax())
                            .replace("{DESCRIPTION}", subCommand.getDescription(lang)),
                    lang.admin_commandList_hoverFormat
                            .replace("{COMMAND}", subCommand.getName())
                            .replace("{DESCRIPTION}", subCommand.getDescription(lang)),
                    "/sl " + subCommand.getName() + " "));
        }

        sender.sendMessage("");

        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        List<String> sub = new ArrayList<>();

        if (args.length == 1)
            for (SubCommand subCommand : commands)
                sub.add(subCommand.getName());
        else
            for (Player online : Bukkit.getOnlinePlayers())
                sub.add(online.getName());

        return sub;
    }
}