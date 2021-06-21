package me.serafin.slogin.commands.admin;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface SubCommand {

    String getName();

    String getDescription();

    String getSyntax();

    List<String> getAliases();

    void perform(@NotNull CommandSender sender, String[] args);

}
