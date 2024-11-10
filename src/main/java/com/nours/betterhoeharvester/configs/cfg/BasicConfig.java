package com.nours.betterhoeharvester.configs.cfg;

import com.nours.betterhoeharvester.BetterHoeHarvester;
import com.nours.betterhoeharvester.configs.AbstractConfig;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class BasicConfig extends AbstractConfig {
    private int cropsRespawnDelay;

    private Particle breakParticle;
    private Particle respawnParticle;

    private Sound breakSound;
    private float breakVolume = 1.0f; // Default volume
    private float breakPitch = 1.0f; // Default pitch

    private boolean straightToInventory;
    private boolean dropItemsWhenFull;

    private static final String[] ignoredSections = new String[]{};

    public BasicConfig(BetterHoeHarvester plugin, String configFileName) {
        super(plugin, configFileName, ignoredSections);
    }

    @Override
    protected void loadConfigValues() {
        cropsRespawnDelay = config.getInt("crops-respawn-delay", 4);

        straightToInventory = config.getBoolean("straight-to-inventory", true);

        dropItemsWhenFull = config.getBoolean("drop-items-if-full-inventory", false);

        if (config.getBoolean("particles.break-crops.enabled", false)) {
            String particleName = config.getString("particles.break-crops.particle", "HAPPY_VILLAGER").toUpperCase();
            try {
                breakParticle = Particle.valueOf(particleName);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Invalid particle type for break-crops: " + particleName);
                breakParticle = null;
            }
        } else {
            breakParticle = null;
        }

        // Load particle for respawning crops
        if (config.getBoolean("particles.respawn-crops.enabled", false)) {
            String particleName = config.getString("particles.respawn-crops.particle", "HAPPY_VILLAGER").toUpperCase();
            try {
                respawnParticle = Particle.valueOf(particleName);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Invalid particle type for respawn-crops: " + particleName);
                respawnParticle = null;
            }
        } else {
            respawnParticle = null;
        }

        // Load sound for breaking crops
        if (config.getBoolean("sounds.break-crops.enabled", false)) {
            String soundName = config.getString("sounds.break-crops.sound", "BLOCK_NOTE_BLOCK_PLING").toUpperCase();
            try {
                breakSound = Sound.valueOf(soundName);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Invalid sound type for break-crops: " + soundName);
                breakSound = null;
            }
            breakVolume = (float) Math.max(0.0, Math.min(1.0, config.getDouble("sounds.break-crops.volume", 1.0)));
            breakPitch = (float) Math.max(0.5, Math.min(2.0, config.getDouble("sounds.break-crops.pitch", 1.0)));
        } else {
            breakSound = null;
        }
    }

    public Particle getBreakParticle() {
        return breakParticle;
    }

    public Particle getRespawnParticle() {
        return respawnParticle;
    }

    public Sound getBreakSound() {
        return breakSound;
    }

    public float getBreakVolume() {
        return breakVolume;
    }

    public float getBreakPitch() {
        return breakPitch;
    }

    public int getCropsRespawnDelay() {
        return cropsRespawnDelay;
    }

    public boolean isStraightToInventory() {
        return straightToInventory;
    }

    public boolean isDropItemsWhenFull() {
        return dropItemsWhenFull;
    }
}
