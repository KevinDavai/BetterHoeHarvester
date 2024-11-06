package com.nours.betterhoeharvester.commands;

import org.bukkit.command.CommandSender;

import javax.annotation.Nullable;
import java.util.List;

public interface CommandHandler {
    void handle(CommandSender sender, String[] args);

    List<String> tabComplete(CommandSender sender, String[] args);

    @Nullable  String getPermission();
}
