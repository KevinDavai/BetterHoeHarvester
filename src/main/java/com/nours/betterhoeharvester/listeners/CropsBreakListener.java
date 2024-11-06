package com.nours.betterhoeharvester.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import com.nours.betterhoeharvester.BetterHoeHarvester;
import com.nours.betterhoeharvester.Utils.WorldGuardUtils;
import com.nours.betterhoeharvester.farmingzones.FarmingBlock;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class CropsBreakListener implements Listener {

    private final BetterHoeHarvester plugin;

    public CropsBreakListener(BetterHoeHarvester plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();

        if(!WorldGuardUtils.isPlayerInTheRegion(player, "BetterHoeHarvester")) {
            return;
        }

        event.setCancelled(true);

        PacketContainer packet = new PacketContainer(PacketType.Play.Server.BLOCK_CHANGE);
        WrappedBlockData blockType = WrappedBlockData.createData(Material.AIR);
        BlockPosition position = new BlockPosition(block.getLocation().toVector());

        packet.getBlockPositionModifier().write(0, position);
        packet.getBlockData().write(0,blockType);


        Material actualSelectedMaterial = plugin.getFarmingBlockManager().getPlayerSelectedFarmingBlock(player);
        if(actualSelectedMaterial == null) {
            actualSelectedMaterial = block.getType();
        }

        try {
            plugin.getProtocolManager().sendServerPacket(player, packet);
            plugin.getFarmingBlockManager().addBrokenBlock(block.getLocation(), new FarmingBlock(actualSelectedMaterial, player.getUniqueId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
