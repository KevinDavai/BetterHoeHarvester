package com.nours.betterhoeharvester.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseCommandManager implements CommandHandler {

    protected final Map<String, CommandHandler> subcommands = new HashMap<>();
    protected final Map<String, String> aliases = new HashMap<>();
    private final int subcommandIndex;

    public BaseCommandManager(int subcommandIndex) {
        this.subcommandIndex = subcommandIndex;
    }

    @Override
    public void handle(CommandSender sender, String[] args) {
        if (args.length > subcommandIndex) {
            String subcommand = args[subcommandIndex].toLowerCase();
            CommandHandler handler = subcommands.getOrDefault(subcommand, subcommands.get(aliases.get(subcommand)));
            if (handler != null) {
                if (handler.getPermission() == null || sender.hasPermission(handler.getPermission())) {
                    handler.handle(sender, args);
                } else {
                    sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
                }
                return;
            }
        }
        showHelp(sender);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length > subcommandIndex + 1) {
            String subcommand = args[subcommandIndex].toLowerCase();
            CommandHandler handler = subcommands.getOrDefault(subcommand, subcommands.get(aliases.get(subcommand)));
            if (handler != null && (handler.getPermission() == null || sender.hasPermission(handler.getPermission()))) {
                return handler.tabComplete(sender, args);
            }
        } else if (args.length == subcommandIndex + 1) {
            List<String> availableCommands = new ArrayList<>();
            for (Map.Entry<String, CommandHandler> command : subcommands.entrySet()) {
                String permission = command.getValue().getPermission();
                if (permission == null || sender.hasPermission(permission)) {
                    availableCommands.add(command.getKey());
                }
            }
            return TabHelper.matchTabComplete(args[subcommandIndex], availableCommands);
        }
        return TabHelper.EMPTY_LIST;
    }

    // Method to display help information
    public abstract void showHelp(CommandSender sender);

}