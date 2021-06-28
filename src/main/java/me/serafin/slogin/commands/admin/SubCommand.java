package me.serafin.slogin.commands.admin;

import me.serafin.slogin.models.Lang;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface SubCommand {

    String getName();

    String getDescription(Lang lang);

    String getSyntax();

    List<String> getAliases();

    void perform(@NotNull CommandSender sender, String[] args);

}
