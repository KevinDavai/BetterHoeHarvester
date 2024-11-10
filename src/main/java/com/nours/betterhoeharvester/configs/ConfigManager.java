package com.nours.betterhoeharvester.configs;

import com.nours.betterhoeharvester.BetterHoeHarvester;
import com.nours.betterhoeharvester.configs.cfg.BasicConfig;
import com.nours.betterhoeharvester.configs.cfg.CustomDropConfig;

public class ConfigManager {
    private final BasicConfig basicConfig;
    private final CustomDropConfig customDropConfig;

    public ConfigManager(BetterHoeHarvester plugin) {
        basicConfig = new BasicConfig(plugin, "config.yml");
        customDropConfig = new CustomDropConfig(plugin, "customdrop.yml");
    }

    public void loadAllConfigs() {
        basicConfig.loadConfig();
        customDropConfig.loadConfig();
    }

    public void reloadAllConfigs() {
        loadAllConfigs();
    }

    public BasicConfig getBasicConfig() {
        return basicConfig;
    }

    public CustomDropConfig getCustomDropConfig() {
        return customDropConfig;
    }
}
