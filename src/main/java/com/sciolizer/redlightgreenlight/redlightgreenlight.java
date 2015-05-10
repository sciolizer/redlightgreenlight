package com.sciolizer.redlightgreenlight;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class RedLightGreenLight extends JavaPlugin {

	public void onDisable() {
	}

	public void onEnable() { 
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(new Listener() {
            @EventHandler
            public void onBlockPlace(BlockPlaceEvent event) {
                Bukkit.getServer().broadcastMessage(event.getBlock().getType() + " placed at " + event.getBlock().getLocation() + "by player " + event.getPlayer().getName());
            }
        }, this);
	}
}
