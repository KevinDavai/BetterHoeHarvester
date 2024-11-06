package com.nours.betterhoeharvester.commands.admin;

import com.nours.betterhoeharvester.commands.BaseCommandManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import javax.annotation.Nullable;

public class AdminCommandHandler extends BaseCommandManager {

    public AdminCommandHandler() {
        super(1);

        // Register admin-related subcommands
        subcommands.put("reload", new AdminReloadCommand());
    }

    @Override
    public void showHelp(CommandSender sender) {
        // Header with alternating dashes
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7- &8- &7- &8- &7- &8- &7- &8- [&e BetterHoeHarvester Admin &7] &8- &7- &8- &7- &8- &7- &8-"));

        // Available commands header
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Available commands:"));

        // Command listings with alternating dash colors
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8 * " + ChatColor.YELLOW + "/betterhoe admin reload " + ChatColor.translateAlternateColorCodes('&', "&7") + ": reload BetterHoeHarvester configuration."));

    }

    @Override
    public @Nullable String getPermission() {
        return "betterhoeharvester.admin";
    }
}
