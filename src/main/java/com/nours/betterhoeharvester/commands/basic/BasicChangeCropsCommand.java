package com.nours.betterhoeharvester.commands.basic;

import com.nours.betterhoeharvester.BetterHoeHarvester;
import com.nours.betterhoeharvester.Utils.ConstantUtils;
import com.nours.betterhoeharvester.Utils.WorldGuardUtils;
import com.nours.betterhoeharvester.commands.CommandHandler;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.util.*;

public class BasicChangeCropsCommand implements CommandHandler {
    private final BetterHoeHarvester plugin;

    public BasicChangeCropsCommand(BetterHoeHarvester plugin) {
        this.plugin = plugin;
    }

    @Override
    public void handle(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be executed by a player.");
            return;
        }

        if (args.length == 1) {
            return;
        }

        if(!WorldGuardUtils.isPlayerInTheRegion(player, "BetterHoeHarvester")) {
            sender.sendMessage(ChatColor.RED + "You must be in the BetterHoeHarvester region to use this command.");
            return;
        }

        String cropName = args[1].toLowerCase();
        Material cropMaterial = ConstantUtils.cropMaterials.get(cropName);

        // If there's a second argument, check if carrot / potato / beetroot / nether_wart
        if (cropMaterial != null) {
            processCropInRegion(player, cropMaterial);
        } else {
            sender.sendMessage(ChatColor.RED + "Usage: /betterhoe changecrops <wheats|carrots|potatoes|beetroots|nether_wart>");
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return new ArrayList<>(ConstantUtils.cropMaterials.keySet());
    }

    private void processCropInRegion(Player player, Material cropType) {
        World world = player.getWorld();
        RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world));
        if (regionManager == null) return;

        ProtectedRegion region = regionManager.getRegion("BetterHoeHarvester");
        if (region == null) return;


        plugin.getFarmingBlockManager().addPlayerSelectedFarmingBlock(player, cropType);

        // Run the crop processing in a separate thread
        new BukkitRunnable() {
            @Override
            public void run() {
                plugin.getFarmingBlockManager().processCropChanges(cropType, player);
            }
        }.runTaskAsynchronously(plugin); // Schedule the task to run asynchronously
    }


    @Override
    public @Nullable String getPermission() {
        return "betterhoeharvester.changecrops";
    }
}
