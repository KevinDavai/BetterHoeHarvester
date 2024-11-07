package com.nours.betterhoeharvester.configs;

import com.nours.betterhoeharvester.BetterHoeHarvester;
import com.nours.betterhoeharvester.configs.cfg.BasicConfig;
import com.nours.betterhoeharvester.configs.cfg.CropsConfig;

public class ConfigManager {
    private final BasicConfig basicConfig;
    private final CropsConfig cropsConfig;

    public ConfigManager(BetterHoeHarvester plugin) {
        basicConfig = new BasicConfig(plugin, "config.yml", null);
        cropsConfig = new CropsConfig(plugin, "crops.yml", null);
    }

    public void loadAllConfigs() {
        basicConfig.loadConfig();
        cropsConfig.loadConfig();
    }

    public void reloadAllConfigs() {
        loadAllConfigs();
    }

    public BasicConfig getBasicConfig() {
        return basicConfig;
    }

    public CropsConfig getCropsConfig() {
        return cropsConfig;
    }
}
