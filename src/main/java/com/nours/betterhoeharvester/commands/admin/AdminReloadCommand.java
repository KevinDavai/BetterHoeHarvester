package com.nours.betterhoeharvester.commands.admin;

import com.nours.betterhoeharvester.BetterHoeHarvester;
import com.nours.betterhoeharvester.commands.CommandHandler;
import com.nours.betterhoeharvester.commands.TabHelper;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import javax.annotation.Nullable;
import java.util.List;

public class AdminReloadCommand implements CommandHandler {

    private final BetterHoeHarvester plugin;

    public AdminReloadCommand(BetterHoeHarvester plugin) {
        this.plugin = plugin;
    }

    @Override
    public void handle(CommandSender sender, String[] args) {
        sender.sendMessage(ChatColor.GRAY + "Reloading BetterHoeHarvester configuration...");
        plugin.getConfigManager().reloadAllConfigs();

        plugin.getLogger().info("Basic config " + plugin.getConfigManager().getBasicConfig().getExampleSetting());
        plugin.getLogger().info("Crops config " + plugin.getConfigManager().getCropsConfig().getExampleSetting());
        sender.sendMessage(ChatColor.GREEN + "BetterHoeHarvester configuration successfully reloaded.");
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return TabHelper.EMPTY_LIST;  // No tab completion needed for reload
    }

    @Override
    public @Nullable String getPermission() {
        return "betterhoeharvester.admin";
    }
}