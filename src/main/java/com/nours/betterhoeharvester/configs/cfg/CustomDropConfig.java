package com.nours.betterhoeharvester.configs.cfg;

import com.nours.betterhoeharvester.BetterHoeHarvester;
import com.nours.betterhoeharvester.Utils.ConstantUtils;
import com.nours.betterhoeharvester.Utils.ItemBuilder;
import com.nours.betterhoeharvester.configs.AbstractConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CustomDropConfig extends AbstractConfig {

    private boolean customDropsEnabled;
    private final Map<String, Map<ItemStack, Double>> cropsDrop = new HashMap<>();

    private static final String[] ignoredSections = new String[]{
            "Custom-Drops.crops"
    };

    public CustomDropConfig(BetterHoeHarvester plugin, String configFileName) {
        super(plugin, configFileName, ignoredSections);
    }

    @Override
    protected void loadConfigValues() {
        customDropsEnabled = config.getBoolean("Custom-Drops.enabled", false);

        if(customDropsEnabled) {
            loadCropDrops();
        }
    }

    private void loadCropDrops() {
        // Iterate over each crop type in the config
        ConfigurationSection cropsSection = config.getConfigurationSection("Custom-Drops.crops");
        this.cropsDrop.clear();
        if (cropsSection != null) {
            for (String cropKey : cropsSection.getKeys(false)) {
                if(!ConstantUtils.cropMaterials.containsKey(cropKey.toLowerCase())) {
                    plugin.getLogger().warning("Invalid crop type in config: " + cropKey);
                    continue;
                }

                ConfigurationSection cropDropSection = cropsSection.getConfigurationSection(cropKey);
                if(cropDropSection == null) {
                    continue;
                }

                Map<ItemStack, Double> cropItems = new HashMap<>();

                for(String dropKey : cropDropSection.getKeys(false)) {
                    ConfigurationSection dropSection = cropDropSection.getConfigurationSection(dropKey);

                    if(dropSection == null) {
                        continue;
                    }

                    try {
                        ItemStack dropItem = new ItemBuilder().fromConfigValue(dropSection).build();
                        cropItems.put(dropItem, dropSection.getDouble("drop-chance", 1.0));
                    } catch (IllegalArgumentException e) {
                        plugin.getLogger().warning("Invalid drop item for crop " + cropKey + ", drop item: " + dropKey + " - " + e.getMessage());
                    }
                }

                if(!cropItems.isEmpty()) {
                    cropsDrop.put(cropKey, cropItems);
                }
            }
        }
    }

    public boolean isCustomDropsEnabled() {
        return customDropsEnabled;
    }

    public Map<String, Map<ItemStack, Double>> getCropsDrop() {
        return cropsDrop;
    }

    public Set<ItemStack> getDropItems(String cropType) {
        return cropsDrop.getOrDefault(cropType, new HashMap<>()).keySet();
    }

    public double getDropChance(String cropType, ItemStack dropItem) {
        return cropsDrop.getOrDefault(cropType, new HashMap<>()).getOrDefault(dropItem, 0.0);
    }
}
