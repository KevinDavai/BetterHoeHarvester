package com.nours.betterhoeharvester.configs.cfg;

import com.nours.betterhoeharvester.BetterHoeHarvester;
import com.nours.betterhoeharvester.configs.AbstractConfig;

public class CropsConfig extends AbstractConfig {

    private String exampleSetting;

    public CropsConfig(BetterHoeHarvester plugin, String configFileName, String[] ignoredSections) {
        super(plugin, configFileName, ignoredSections);
    }

    @Override
    protected void loadConfigValues() {
        exampleSetting = config.getString("example-crops-config", "default-value");
    }

    public String getExampleSetting() {
        return exampleSetting;
    }
}
