package com.nours.betterhoeharvester.configs.cfg;

import com.nours.betterhoeharvester.BetterHoeHarvester;
import com.nours.betterhoeharvester.configs.AbstractConfig;

public class BasicConfig extends AbstractConfig {
    private String exampleSetting;

    public BasicConfig(BetterHoeHarvester plugin, String configFileName, String[] ignoredSections) {
        super(plugin, configFileName, ignoredSections);
    }

    @Override
    protected void loadConfigValues() {
        exampleSetting = config.getString("example-config-yml", "default-value");
    }

    public String getExampleSetting() {
        return exampleSetting;
    }
}
