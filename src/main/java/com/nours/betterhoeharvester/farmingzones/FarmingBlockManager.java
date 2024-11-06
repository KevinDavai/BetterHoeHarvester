package com.nours.betterhoeharvester.farmingzones;

import com.nours.betterhoeharvester.BetterHoeHarvester;
import com.nours.betterhoeharvester.Utils.ConstantUtils;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class FarmingBlockManager {
    private final BetterHoeHarvester plugin;

    private final ConcurrentHashMap<Location, List<FarmingBlock>> brokenBlocks = new ConcurrentHashMap<>();
    private final Map<Player, Material> playerSelectedFarmingBlock = new HashMap<>();
    private final Map<Chunk, Set<BlockState>> chunkBlockStates = new HashMap<>();

    public FarmingBlockManager(BetterHoeHarvester plugin) {
        this.plugin = plugin;
    }


    public void addBrokenBlock(Location location, FarmingBlock farmingBlock) {
        brokenBlocks.computeIfAbsent(location, loc -> new ArrayList<>())
                .add(farmingBlock);
    }

    public Material getPlayerSelectedFarmingBlock(Player player) {
        return playerSelectedFarmingBlock.get(player);
    }

    public void addPlayerSelectedFarmingBlock(Player player, Material material) {
        playerSelectedFarmingBlock.put(player, material);
    }

    public List<FarmingBlock> getBlocksAtLocation(Location location) {
        return brokenBlocks.getOrDefault(location, new ArrayList<>());
    }

    public void removeBrokenBlock(Location location, FarmingBlock farmingBlock) {
        List<FarmingBlock> farmingBlocks = brokenBlocks.get(location);
        if (farmingBlocks != null) {
            farmingBlocks.remove(farmingBlock);

            if (farmingBlocks.isEmpty()) {
                brokenBlocks.remove(location);
            }
        }
    }

    public ConcurrentHashMap<Location, List<FarmingBlock>> getBrokenBlocks() {
        return brokenBlocks;
    }

    public void collectWheatBlocksInRegion(String regionName) {
        for (World world : plugin.getServer().getWorlds()) {
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionManager regions = container.get(BukkitAdapter.adapt(world));

            if (regions == null) {
                continue; // Skip this world if it doesn't have a region manager
            }

            // Check if the region exists in this world
            if (!regions.hasRegion(regionName)) {
                continue; // Skip to the next world if the region doesn't exist here
            }

            // Get the region object
            ProtectedRegion region = regions.getRegion(regionName);

            // Iterate through the blocks in the region's bounding box
            for (int x = region.getMinimumPoint().getBlockX(); x <= region.getMaximumPoint().getBlockX(); x++) {
                for (int y = region.getMinimumPoint().getBlockY(); y <= region.getMaximumPoint().getBlockY(); y++) {
                    for (int z = region.getMinimumPoint().getBlockZ(); z <= region.getMaximumPoint().getBlockZ(); z++) {
                        Location location = new Location(world, x, y, z);
                        Block block = location.getBlock();

                        // Check if the block is Wheat
                        if (block.getType() == Material.WHEAT) {
                            BlockState blockState = block.getState();  // Get the block's state
                            Chunk chunk = block.getChunk();

                            // Add the blockState to the map of chunkBlockStates
                            chunkBlockStates.computeIfAbsent(chunk, k -> new HashSet<>()).add(blockState);
                        }
                    }
                }
            }

            plugin.getLogger().info("Collected " + chunkBlockStates.size() + " Wheat blocks in region " + regionName + " in world " + world.getName());
        }
    }

    public void processCropChanges(Material cropType, Player player) {
        BlockData blockData = cropType.createBlockData();

        // Vérification des types de cultures (Ageable, Coraux, Fleurs)
        if (ConstantUtils.ageableCrops.contains(cropType) && blockData instanceof Ageable) {
            updateBlockForCrop((Ageable) blockData, player, null);
        } else if (ConstantUtils.corals.contains(cropType) && blockData instanceof Waterlogged) {
            updateBlockForCrop((Waterlogged) blockData, player, null);
        } else if (ConstantUtils.flowers.contains(cropType)) {
            updateBlockForCrop(blockData, player, null);
        } else {
            // Si le type de culture n'est pas pris en charge
            Bukkit.getScheduler().runTask(plugin, () -> {
                player.sendMessage(ChatColor.RED + "This crop type is not supported.");
            });
        }
    }

    public void processCropChangesInChunk(Material cropType, Player player, Chunk chunk) {
        BlockData blockData = cropType.createBlockData();

        // Vérification des types de cultures (Ageable, Coraux, Fleurs)
        if (ConstantUtils.ageableCrops.contains(cropType) && blockData instanceof Ageable) {
            updateBlockForCrop((Ageable) blockData, player, chunk);
        } else if (ConstantUtils.corals.contains(cropType) && blockData instanceof Waterlogged) {
            updateBlockForCrop((Waterlogged) blockData, player, chunk);
        } else if (ConstantUtils.flowers.contains(cropType)) {
            updateBlockForCrop(blockData, player, chunk);
        } else {
            // Si le type de culture n'est pas pris en charge
            Bukkit.getScheduler().runTask(plugin, () -> {
                player.sendMessage(ChatColor.RED + "This crop type is not supported.");
            });
        }
    }

    private void updateBlockForCrop(BlockData blockData, Player player, Chunk chunk) {
        if (chunk != null) {
            plugin.getFarmingBlockManager().updateBlocksInChunk(chunk, blockData, player);
        } else {
            plugin.getFarmingBlockManager().updateBlocksInRegion(blockData, player);
        }
    }

    private void updateBlockForCrop(Ageable ageableData, Player player, Chunk chunk) {
        // Mettre à jour l'âge du crop
        ageableData.setAge(ageableData.getMaximumAge());
        updateBlockForCrop((BlockData) ageableData, player, chunk);
    }

    private void updateBlockForCrop(Waterlogged waterloggedData, Player player, Chunk chunk) {
        // Désactiver l'état "waterlogged" des coraux
        waterloggedData.setWaterlogged(false);
        updateBlockForCrop((BlockData) waterloggedData, player, chunk);
    }

    private void updateBlocksInRegion(BlockData updatedBlockData, Player player) {
        // List to store the block states that need to be updated
        List<BlockState> blockStatesToUpdate = new ArrayList<>();

        // Get the player's current chunk
        Chunk playerChunk = player.getLocation().getChunk();

        // Loop through the chunkBlockStates map, which stores BlockState objects by chunk
        for (Map.Entry<Chunk, Set<BlockState>> entry : chunkBlockStates.entrySet()) {
            Chunk chunk = entry.getKey();
            Set<BlockState> blockStatesInChunk = entry.getValue();

            // Check if the chunk is in range of the player
            if (playerChunk.equals(chunk) || isChunkInRange(player, chunk)) {
                // Loop through block states in this chunk
                for (BlockState state : blockStatesInChunk) {
                    // Set the updated block data for each block state
                    state.setBlockData(updatedBlockData);
                    blockStatesToUpdate.add(state); // Add the updated block state to the list
                }
            }
        }

        // Send the block changes to the player if there are any updates
        if (!blockStatesToUpdate.isEmpty()) {
            player.sendBlockChanges(blockStatesToUpdate, true); // Send block updates to the player
        }
    }

    // Helper method to determine if a chunk is within range of the player
    private boolean isChunkInRange(Player player, Chunk chunk) {
        int viewDistance = player.getServer().getViewDistance() + 2; // + 2 to be sure that the client has loaded the chunk
        int chunkX = chunk.getX();
        int chunkZ = chunk.getZ();
        int playerChunkX = player.getLocation().getChunk().getX();
        int playerChunkZ = player.getLocation().getChunk().getZ();

        // Check if the chunk is within a certain distance from the player's current chunk
        return Math.abs(chunkX - playerChunkX) <= viewDistance && Math.abs(chunkZ - playerChunkZ) <= viewDistance;
    }

    private void updateBlocksInChunk(Chunk targetChunk, BlockData updatedBlockData, Player player) {
        // Retrieve the set of block states for the target chunk
        Set<BlockState> blockStatesToUpdate = chunkBlockStates.get(targetChunk);

        // If no block states exist for this chunk, exit early
        if (blockStatesToUpdate == null || blockStatesToUpdate.isEmpty()) {
            return;
        }

        // Update all block states in the chunk
        for (BlockState state : blockStatesToUpdate) {
            state.setBlockData(updatedBlockData);
        }

        // Send the block changes to the player
        player.sendBlockChanges(blockStatesToUpdate, true);  // Send the block updates to the player
    }
}
