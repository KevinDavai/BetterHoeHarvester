package com.nours.betterhoeharvester.configs;

import com.nours.betterhoeharvester.BetterHoeHarvester;

import java.io.File;
import java.util.logging.Level;

public abstract class AbstractConfig {

    protected final BetterHoeHarvester plugin;
    protected CommentedConfiguration config;
    protected final String configFileName;
    protected final String[] ignoredSections;

    public AbstractConfig(BetterHoeHarvester plugin, String configFileName, String[] ignoredSections) {
        this.plugin = plugin;
        this.configFileName = configFileName;
        this.ignoredSections = ignoredSections;
    }

    public void loadConfig() {
        File file = new File(plugin.getDataFolder(), configFileName);

        if (!file.exists()) {
            plugin.saveResource(configFileName, false);
        }

        config = CommentedConfiguration.loadConfiguration(file);

        try {
            config.syncWithConfig(file, plugin.getResource(configFileName), ignoredSections);
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Error while syncing the config file " + configFileName, e);
        }

        loadConfigValues();
    }

    protected abstract void loadConfigValues();

    public CommentedConfiguration getConfig() {
        return config;
    }
}
