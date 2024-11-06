package com.nours.betterhoeharvester.Utils;

import org.bukkit.Material;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ConstantUtils {
    public static final Set<Material> ageableCrops = EnumSet.of(Material.WHEAT, Material.CARROTS, Material.POTATOES, Material.BEETROOTS, Material.NETHER_WART);
    public static final Set<Material> corals = EnumSet.of(Material.TUBE_CORAL, Material.BRAIN_CORAL, Material.BUBBLE_CORAL, Material.FIRE_CORAL, Material.HORN_CORAL);
    public static final Set<Material> flowers = EnumSet.of(Material.PEONY, Material.LILAC, Material.ROSE_BUSH, Material.WARPED_ROOTS, Material.CRIMSON_ROOTS);

    public static final Map<String, Material> cropMaterials = new HashMap<>();
    static {
        ageableCrops.forEach(crop -> cropMaterials.put(crop.name().toLowerCase(), crop));
        corals.forEach(coral -> cropMaterials.put(coral.name().toLowerCase(), coral));
        flowers.forEach(flower -> cropMaterials.put(flower.name().toLowerCase(), flower));
    }
}
