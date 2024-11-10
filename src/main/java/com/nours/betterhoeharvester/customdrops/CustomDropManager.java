package com.nours.betterhoeharvester.customdrops;

import com.nours.betterhoeharvester.BetterHoeHarvester;
import com.nours.betterhoeharvester.configs.cfg.CustomDropConfig;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Random;

public class CustomDropManager {
    private final Random random = new Random();
    private final BetterHoeHarvester plugin;

    public CustomDropManager(BetterHoeHarvester plugin) {
        this.plugin = plugin;
    }

    /**
     * Handles giving custom drops to the player when they break a crop block.
     */
    public void handleCustomDrops(Material cropType, Player player, Location location) {
        CustomDropConfig dropConfig = plugin.getConfigManager().getCustomDropConfig();

        if (!dropConfig.isCustomDropsEnabled()) return;

        String cropKey = cropType.name();

        // Check if there are custom drops configured for this crop
        if (!dropConfig.getCropsDrop().containsKey(cropKey)) return;

        Map<ItemStack, Double> cropDrops = dropConfig.getCropsDrop().get(cropKey);

        // Iterate through each custom drop item for the crop type
        for (Map.Entry<ItemStack, Double> entry : cropDrops.entrySet()) {
            ItemStack dropItem = entry.getKey();
            double dropChance = entry.getValue();

            // Check if the item should drop based on the configured drop chance
            if (random.nextDouble() <= dropChance) {
                // Drop the item at the block location or add it to the player's inventory
                giveItemToPlayerOrDrop(player, dropItem, location);
            }
        }
    }

    /**
     * Attempts to add an item to the player's inventory. Drops the item at the block location if inventory is full.
     */
    private void giveItemToPlayerOrDrop(Player player, ItemStack item, Location location) {
        boolean straightToInventory = plugin.getConfigManager().getBasicConfig().isStraightToInventory();
        boolean dropItemsWhenFull = plugin.getConfigManager().getBasicConfig().isDropItemsWhenFull();

        if(straightToInventory) {
            // Add the item to the player's inventory
            Map<Integer, ItemStack> excessItems = player.getInventory().addItem(item);
            if (!excessItems.isEmpty() && dropItemsWhenFull) {
                // If player's inventory is full, drop the item at the block's location
                excessItems.values().forEach(droppedItem ->
                      player.getWorld().dropItem(player.getLocation(), droppedItem)
                );
            }
            return;
        }

        // Drop the item at the block location
        location.getWorld().dropItem(location, item);
    }
}
