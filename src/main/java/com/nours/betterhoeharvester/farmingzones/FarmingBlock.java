package com.nours.betterhoeharvester.farmingzones;

import org.bukkit.Material;

import java.util.UUID;

public class FarmingBlock {
    private final Material material;
    private final UUID player;
    private final long destroyTime;

    public FarmingBlock(Material material, UUID player) {
        this.material = material;
        this.player = player;
        this.destroyTime = System.currentTimeMillis();
    }

    public Material getMaterial() {
        return material;
    }

    public UUID getPlayer() {
        return player;
    }

    public long getDestroyTime() {
        return destroyTime;
    }
}
