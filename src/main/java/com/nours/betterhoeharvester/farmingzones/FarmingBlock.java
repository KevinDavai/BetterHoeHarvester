package com.nours.betterhoeharvester.farmingzones;

import org.bukkit.Material;

import java.util.UUID;

public class FarmingBlock {
    private final Material material;
    private final UUID player;
    private long destroyTime;

    public FarmingBlock(Material material, UUID player) {
        System.out.println("material  " + material);
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

    public void setDestroyTime(long destroyTime) {
        this.destroyTime = destroyTime;
    }
}
