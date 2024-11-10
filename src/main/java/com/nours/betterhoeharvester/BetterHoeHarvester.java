package com.nours.betterhoeharvester;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.nours.betterhoeharvester.commands.basic.BasicCommandHandler;
import com.nours.betterhoeharvester.configs.ConfigManager;
import com.nours.betterhoeharvester.customdrops.CustomDropManager;
import com.nours.betterhoeharvester.farmingzones.FarmingBlockManager;
import com.nours.betterhoeharvester.farmingzones.FarmingZoneThread;
import com.nours.betterhoeharvester.listeners.BlockChangePacketListener;
import com.nours.betterhoeharvester.listeners.ChunkLoadPacketListener;
import com.nours.betterhoeharvester.listeners.CropsBreakListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class BetterHoeHarvester extends JavaPlugin implements Listener {

    private static BetterHoeHarvester plugin;
    private ProtocolManager protocolManager;

    private FarmingBlockManager farmingBlockManager;
    private CustomDropManager customDropManager;

    private final BasicCommandHandler commandManager = new BasicCommandHandler(this);

    private ConfigManager configManager;


    @Override
    public void onLoad() {
        plugin = this;
        protocolManager = ProtocolLibrary.getProtocolManager();
    }

    @Override
    public void onEnable() {
        log("******** ENABLE BetterHoeHarvester ********");

        farmingBlockManager = new FarmingBlockManager(this);
        farmingBlockManager.collectWheatBlocksInRegion("BetterHoeHarvester");

        customDropManager = new CustomDropManager(this);

        configManager = new ConfigManager(this);
        configManager.loadAllConfigs();

        FarmingZoneThread farmingZoneThread = new FarmingZoneThread(farmingBlockManager, this);

        getServer().getPluginManager().registerEvents(new CropsBreakListener(this), this);
        protocolManager.addPacketListener(new BlockChangePacketListener(this));
        protocolManager.addPacketListener(new ChunkLoadPacketListener(this));

        this.commandManager.loadCommand();

        farmingZoneThread.runTaskTimer(this, 0, 20L);
    }


    @Override
    public void onDisable() {
        //PacketEvents.getAPI().terminate();
    }

    public static void log(String message) {
        message = ChatColor.translateAlternateColorCodes('&', message);
        boolean colored = message.contains(ChatColor.COLOR_CHAR + "");
        String lastColor = colored ? ChatColor.getLastColors(message.substring(0, 2)) : "";
        for (String line : message.split("\n")) {
            if (colored)
                Bukkit.getConsoleSender().sendMessage(lastColor + "[" + plugin.getDescription().getName() + "] " + line);
            else
                plugin.getLogger().info(line);
        }
    }

    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    public FarmingBlockManager getFarmingBlockManager() {
        return farmingBlockManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public CustomDropManager getCustomDropManager() {
        return customDropManager;
    }
}
