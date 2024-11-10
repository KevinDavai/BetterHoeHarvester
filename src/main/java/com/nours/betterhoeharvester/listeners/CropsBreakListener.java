package com.nours.betterhoeharvester.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import com.nours.betterhoeharvester.BetterHoeHarvester;
import com.nours.betterhoeharvester.Utils.ConstantUtils;
import com.nours.betterhoeharvester.Utils.WorldGuardUtils;
import com.nours.betterhoeharvester.farmingzones.FarmingBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
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

        if(!ConstantUtils.cropMaterials.containsValue(block.getType())) {
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
            actualSelectedMaterial = plugin.getFarmingBlockManager().addPlayerSelectedFarmingBlock(player, block.getType());
        }

        try {
            plugin.getProtocolManager().sendServerPacket(player, packet);
            plugin.getFarmingBlockManager().addBrokenBlock(block.getLocation(), new FarmingBlock(actualSelectedMaterial, player.getUniqueId()));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        Particle breakParticle = plugin.getConfigManager().getBasicConfig().getBreakParticle();
        if(breakParticle != null) {
            Location location = block.getLocation();
            location.getWorld().spawnParticle(breakParticle, location.add(0.5, 0.5, 0.5), 1, 0.3, 0.3, 0.3, 0.02);
        }

        Sound breakSound = plugin.getConfigManager().getBasicConfig().getBreakSound();
        if(breakSound != null) {
            // Play sound for the player only
            float volume = plugin.getConfigManager().getBasicConfig().getBreakVolume();
            float pitch = plugin.getConfigManager().getBasicConfig().getBreakPitch();
            player.playSound(player.getLocation(), breakSound, volume, pitch);
        }

        plugin.getCustomDropManager().handleCustomDrops(actualSelectedMaterial, player, block.getLocation());
    }
}
