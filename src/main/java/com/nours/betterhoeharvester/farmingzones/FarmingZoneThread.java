package com.nours.betterhoeharvester.farmingzones;

import com.nours.betterhoeharvester.BetterHoeHarvester;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FarmingZoneThread extends BukkitRunnable {

    private final FarmingBlockManager farmingBlockManager;
    private final BetterHoeHarvester plugin;

    public FarmingZoneThread(FarmingBlockManager farmingBlockManager, BetterHoeHarvester plugin) {
        this.farmingBlockManager = farmingBlockManager;
        this.plugin = plugin;
    }

    @Override
    public void run() {
        long currentTime = System.currentTimeMillis();

        for(Map.Entry<Location, List<FarmingBlock>> entry : farmingBlockManager.getBrokenBlocks().entrySet()) {
            Location location = entry.getKey();
            List<FarmingBlock> farmingBlocks = entry.getValue();

            // Using interator to avoid ConcurrentModificationException
            Iterator<FarmingBlock> iterator = farmingBlocks.iterator();
            while (iterator.hasNext()) {
                FarmingBlock farmingBlock = iterator.next();

                if (currentTime - farmingBlock.getDestroyTime() >= 4000) {
                    Material material = farmingBlock.getMaterial();

                    Player p = Bukkit.getPlayer(farmingBlock.getPlayer());

                    BlockData blockData = material.createBlockData();

                    if(blockData instanceof Ageable ageable) {
                        ageable.setAge(ageable.getMaximumAge());
                    } else if(blockData instanceof Waterlogged waterlogged) {
                        waterlogged.setWaterlogged(false);
                    }

                    if (p != null) {
                        p.sendBlockChange(location, blockData);
                    }

                    iterator.remove();
                    farmingBlockManager.removeBrokenBlock(location, farmingBlock);
                }
            }
        }
    }
}
