package me.serafin.slogin.commands.admin;

import me.serafin.slogin.SLogin;
import me.serafin.slogin.managers.LangManager;
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

public class SLoginCommand implements CommandExecutor, TabCompleter {

    ArrayList<SubCommand> commands = new ArrayList<>();

    SLogin plugin;
    LangManager lang;

    public SLoginCommand(SLogin plugin){
        this.plugin = plugin;
        this.lang = plugin.getLangManager();

        commands.add(new PlayerInfoSubCommand(plugin));
        commands.add(new ForceLoginSubCommand(plugin));
        commands.add(new RegisterSubCommand(plugin));
        commands.add(new ChangePasswordSubCommand(plugin));
        commands.add(new UnRegisterCommand(plugin));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (args.length != 0) {
            for (SubCommand subCommand : commands) {
                if (subCommand.getAliases().contains(args[0].toLowerCase())) {
                    subCommand.perform(sender, args);
                    return true;
                }
            }
        }

        sender.sendMessage("");
        sender.sendMessage(Utils.format("&7___________/ &eCOMMAND LIST &7\\___________"));
        sender.sendMessage("");

        for(SubCommand subCommand : commands){
            if(Utils.isHigherVersion("1.11", Utils.getServerVersion())) {
                sender.spigot().sendMessage(Utils.sendCommandSuggest(
                        Utils.format("&e" + subCommand.getSyntax() + " &7- " + subCommand.getDescription()),
                        Utils.format("&e" + subCommand.getName().toUpperCase() + "\n&7" + subCommand.getDescription()),
                        "/sl " + subCommand.getName() + " "));
            }
            else {
                sender.sendMessage(Utils.format("&e" + subCommand.getSyntax() + " &7- " + subCommand.getDescription()));
            }
        }

        sender.sendMessage("");

        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        List<String> sub = new ArrayList<>();

        if(args.length == 1)
            for(SubCommand subCommand : commands)
                sub.add(subCommand.getName());
        else
            for(Player online : Bukkit.getOnlinePlayers())
                sub.add(online.getName());

        return sub;
    }
}
