package com.nours.betterhoeharvester.commands.basic;

import com.nours.betterhoeharvester.BetterHoeHarvester;
import com.nours.betterhoeharvester.commands.BaseCommandManager;
import com.nours.betterhoeharvester.commands.admin.AdminCommandHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;


public class BasicCommandHandler extends BaseCommandManager implements TabExecutor {

    private final BetterHoeHarvester plugin;

    public BasicCommandHandler(BetterHoeHarvester plugin) {
        super(0);  // 0 means the subcommand comes after "/betterhoe"

        this.plugin = plugin;

        // Register subcommands
        subcommands.put("admin", new AdminCommandHandler(plugin));

        // Register condense subcommand
        subcommands.put("changecrops", new BasicChangeCropsCommand(plugin));
    }



    @Override
    public void showHelp(CommandSender sender) {
        // Header with alternating dashes
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7- &8- &7- &8- &7- &8- &7- &8- [&e BetterHoeHarvester &7] &8- &7- &8- &7- &8- &7- &8-"));

        // Available commands header
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Available commands:"));

        // Command listings with alternating dash colors
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8 * " + ChatColor.YELLOW + "/betterhoe admin reload " + ChatColor.translateAlternateColorCodes('&', "&7") + ": reload BetterHoeHarvester configuration."));
    }

    @Override
    public @Nullable String getPermission() {
        return null;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        super.handle(sender, args);
        return true;
    }


    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        return super.tabComplete(sender, args);
    }

    public void loadCommand() {
        // Register the command
        this.plugin.getCommand("betterhoe").setExecutor(this);
        this.plugin.getCommand("betterhoe").setTabCompleter(this);
    }

}