package com.nours.betterhoeharvester.Utils;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {

    private Material material;
    private int amount = 1;
    private String name;
    private List<String> lore = new ArrayList<>();
    private int customModelData = -1;
    private boolean glow;

    /**
     * Load item properties from a ConfigurationSection.
     */
    public ItemBuilder fromConfigValue(ConfigurationSection section) {
        // Material
        String materialString = section.getString("material");
        if(materialString == null) {
            throw new IllegalArgumentException("Missing material in item configuration.");
        }

        this.material = Material.getMaterial(materialString.toUpperCase());
        if (this.material == null) {
            throw new IllegalArgumentException("Invalid material in item configuration.");
        }

        // Amount (default to 1)
        this.amount = section.getInt("amount", 1);

        // Name
        this.name = section.getString("name");

        // Lore
        this.lore = section.getStringList("lore");

        // Custom Model Data (default to -1 if not present)
        this.customModelData = section.contains("custom-model-data") ? section.getInt("custom-model-data") : -1;

        // Glow (true/false)
        this.glow = section.getBoolean("glow", false);

        return this;
    }

    /**
     * Builds the final ItemStack based on the stored values.
     */
    public ItemStack build() {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return item;
        }

        // Set display name
        if (name != null) {
            meta.setDisplayName(ColorUtils.color(name));
        }

        // Set lore
        if (!lore.isEmpty()) {
            meta.setLore(ColorUtils.color(lore));
        }

        // Set custom model data if specified
        if (customModelData != -1) {
            meta.setCustomModelData(customModelData);
        }

        // Apply glow if requested and no enchantments are present
        if (glow) {
            meta.addEnchant(Enchantment.LUCK_OF_THE_SEA, 1, true);
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
        }

        item.setItemMeta(meta);
        return item;
    }
}