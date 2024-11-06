package com.nours.betterhoeharvester.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.nours.betterhoeharvester.BetterHoeHarvester;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.*;
import org.bukkit.entity.Player;

public class ChunkLoadPacketListener extends PacketAdapter {

    private final BetterHoeHarvester plugin;

    public ChunkLoadPacketListener(BetterHoeHarvester plugin) {
        super(plugin, ListenerPriority.NORMAL, PacketType.Play.Server.MAP_CHUNK);
        this.plugin = plugin;
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        Player player = event.getPlayer();

        // Vérifier le type de culture sélectionné par le joueur
        Material cropType = plugin.getFarmingBlockManager().getPlayerSelectedFarmingBlock(player);
        if (cropType == null) {
            return; // Pas de culture sélectionnée, pas de modification nécessaire
        }

        World world = player.getWorld();
        RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world));

        if (regionManager == null) return;

        // Obtenir les coordonnées du chunk
        int chunkX = event.getPacket().getIntegers().read(0);
        int chunkZ = event.getPacket().getIntegers().read(1);
        Chunk chunk = world.getChunkAt(chunkX, chunkZ);

        // Récupérer la région `BetterHoeHarvester` à vérifier
        ProtectedRegion region = regionManager.getRegion("BetterHoeHarvester");
        if (region == null) return;


        // Do this in a delayedTask because the chunk might not be fully loaded yet for the CLIENT to receive the packet
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getFarmingBlockManager().processCropChangesInChunk(cropType, player, chunk),20);

    }

}
