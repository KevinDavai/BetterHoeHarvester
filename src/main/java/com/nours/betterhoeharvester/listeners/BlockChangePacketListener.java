package com.nours.betterhoeharvester.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import com.nours.betterhoeharvester.BetterHoeHarvester;
import com.nours.betterhoeharvester.farmingzones.FarmingBlock;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.List;

public class BlockChangePacketListener extends PacketAdapter {

        private final BetterHoeHarvester plugin;

        public BlockChangePacketListener(BetterHoeHarvester plugin) {
            super(plugin, ListenerPriority.HIGH, PacketType.Play.Server.BLOCK_CHANGE);
            this.plugin = plugin;
        }

        @Override
        public void onPacketSending(PacketEvent event) {

            WrappedBlockData blockData = event.getPacket().getBlockData().read(0);

            BlockPosition blockPosition = event.getPacket().getBlockPositionModifier().read(0);
            Location bukkitLocation = new Location(event.getPlayer().getWorld(), blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());

            // Retrieve the list of FarmingBlock objects at this location from the FarmingBlockManager
            List<FarmingBlock> blocksAtLocation = plugin.getFarmingBlockManager().getBlocksAtLocation(bukkitLocation);


            // Check if any FarmingBlock at this location hasn't respawned yet
            for (FarmingBlock farmingBlock : blocksAtLocation) {
                if(farmingBlock.getPlayer() == event.getPlayer().getUniqueId()) {
                    int respawnDelay = plugin.getConfigManager().getBasicConfig().getCropsRespawnDelay();
                    if (System.currentTimeMillis() < farmingBlock.getDestroyTime() + respawnDelay * 1000L) {
                        // Cancel the event if the respawn time has not ended
                        event.setCancelled(true);
                        return; // Exit early since we found a block that hasn't respawned
                    } else {
                        event.setCancelled(false);
                        return;
                    }
                }
            }

            if(blockData.getType().equals(Material.WHEAT)
                    && plugin.getFarmingBlockManager().getPlayerSelectedFarmingBlock(event.getPlayer()) != Material.WHEAT)
            {
                event.setCancelled(true);
            }
        }
}

