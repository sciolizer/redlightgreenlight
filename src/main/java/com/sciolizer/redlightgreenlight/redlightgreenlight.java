package com.sciolizer.redlightgreenlight;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.WeakHashMap;

public class RedLightGreenLight extends JavaPlugin {

    protected WeakHashMap<Entity, Location> spawnLocations = new WeakHashMap<Entity, Location>();

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

        new BukkitRunnable() {
            @Override
            public void run() {
                for (World world : Bukkit.getServer().getWorlds()) {
                    for (Entity entity : world.getEntities()) {
                        if (entity instanceof HumanEntity) {
                            continue;
                        }
                        if (!spawnLocations.containsKey(entity)) {
                            spawnLocations.put(entity, entity.getLocation());
                        }
                        Location location = spawnLocations.get(entity);
                        if (location != null) { // always true?
                            entity.teleport(location);
                        }
                    }
                }
            }
        }.runTaskTimer(this, 1, 1);
	}
}
