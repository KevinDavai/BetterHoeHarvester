package com.nours.betterhoeharvester.Utils;

import org.bukkit.ChatColor;

import java.util.List;
import java.util.stream.Collectors;

public class ColorUtils {
    public static String color(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static List<String> color(List<String> lore){
        return lore.stream().map(ColorUtils::color).collect(Collectors.toList());
    }
}
