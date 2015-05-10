package com.sciolizer.redlightgreenlight;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.WeakHashMap;

public class RedLightGreenLight extends JavaPlugin {

    protected WeakHashMap<Entity, Location> spawnLocations = new WeakHashMap<Entity, Location>();
    protected WeakHashMap<Player,Location> previousPlayerLocations = new WeakHashMap<Player, Location>();

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
                boolean atLeastOnePlayerMoved = false;
                for (World world : Bukkit.getServer().getWorlds()) {
                    for (Entity entity : world.getEntities()) {
                        if (entity instanceof Player) {
                            Player player = (Player) entity;
                            Location previousLocation = previousPlayerLocations.get(player);
                            if (previousLocation == null) {
                                previousPlayerLocations.put(player, player.getLocation());
                            } else {
                                Location newLocation = player.getLocation();
                                if (!atLeastOnePlayerMoved && !previousLocation.equals(newLocation)) {
                                    atLeastOnePlayerMoved = true;
                                }
                                previousPlayerLocations.put(player, newLocation);
                            }
                        }
                    }
                }
                for (World world : Bukkit.getServer().getWorlds()) {
                    for (Entity entity : world.getEntities()) {
                        if (entity instanceof HumanEntity) {
                            continue;
                        }
                        if (atLeastOnePlayerMoved || !spawnLocations.containsKey(entity)) {
                            spawnLocations.put(entity, entity.getLocation());
                        } else {
                            entity.teleport(spawnLocations.get(entity));
                        }
                    }
                }
            }
        }.runTaskTimer(this, 1, 1);
	}
}
