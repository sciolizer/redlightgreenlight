package com.sciolizer.redlightgreenlight;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.WeakHashMap;

public class RedLightGreenLight extends JavaPlugin {

    protected WeakHashMap<Entity, Location> entityLocations = new WeakHashMap<Entity, Location>();
    protected boolean atLeastOnePlayerActedLastTick = false;
    protected boolean atLeastOnePlayerActedThisTick = false;

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

        registerPlayerEvents();

        pm.registerEvents(new Listener() {
            @EventHandler
            public void onVehicleEnterEvent(VehicleEnterEvent event) {
                processVehicleEvent(event);
            }

            @EventHandler
            public void onVehicleMoveEvent(VehicleMoveEvent event) {
                processVehicleEvent(event);
            }

            @EventHandler
            public void onVehicleExitEvent(VehicleExitEvent event) {
                processVehicleEvent(event);
            }
        }, this);

        pm.registerEvents(new Listener() {
            @EventHandler
            public void onEntityBreakDoorEvent(EntityBreakDoorEvent event) {
                processEntityEvent(event);
            }

            @EventHandler
            public void onEntityCreateProtalEvent(EntityCreatePortalEvent event) {
                processEntityEvent(event);
            }

            @EventHandler
            public void onEntityCombustByEntityEvent(EntityCombustByEntityEvent event) {
                processEntityEvent(event); //  (unsure if self?)
            }

            @EventHandler
            public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
                processEntityEvent(event); // (towards other? or duplicates attack event?)
            }

            @EventHandler
            public void onEntityInteractEvent(EntityInteractEvent event) {
                processEntityEvent(event);
            }

            @EventHandler
            public void onEntityShootBowEvent(EntityShootBowEvent event) {
                processEntityEvent(event);
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
                        if (atLeastOnePlayerActedThisTick || !entityLocations.containsKey(entity)) {
                            entityLocations.put(entity, entity.getLocation());
                        } else {
                            entity.teleport(entityLocations.get(entity));
                        }
                    }
                }
                atLeastOnePlayerActedLastTick = atLeastOnePlayerActedThisTick;
                atLeastOnePlayerActedThisTick = false;
            }
        }.runTaskTimer(this, 1, 1);
    }

    private void registerPlayerEvents() {
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(new Listener() {
            @EventHandler(priority = EventPriority.MONITOR)
            public void onPlayerMoveEvent(PlayerMoveEvent event) {
                if (!samePosition(event.getFrom(), event.getTo())) {
                    setPlayerActed();
                }
            }
        }, this);

        pm.registerEvents(new Listener() {
            @EventHandler(priority = EventPriority.MONITOR)
            public void onPlayerBedEnterEvent(PlayerBedEnterEvent event) {
                setPlayerActed();
            }
        }, this);

        pm.registerEvents(new Listener() {
            @EventHandler(priority = EventPriority.MONITOR)
            public void onPlayerBedLeaveEvent(PlayerBedLeaveEvent event) {
                setPlayerActed();
            }
        }, this);

        pm.registerEvents(new Listener() {
            @EventHandler(priority = EventPriority.MONITOR)
            public void onPlayerBucketEmptyEvent(PlayerBucketEmptyEvent event) {
                setPlayerActed();
            }
        }, this);

        pm.registerEvents(new Listener() {
            @EventHandler(priority = EventPriority.MONITOR)
            public void onPlayerBucketFillEvent(PlayerBucketFillEvent event) {
                setPlayerActed();
            }
        }, this);

        pm.registerEvents(new Listener() {
            @EventHandler(priority = EventPriority.MONITOR)
            public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
                setPlayerActed();
            }
        }, this);

        pm.registerEvents(new Listener() {
            @EventHandler(priority = EventPriority.MONITOR)
            public void onPlayerEditBookEvent(PlayerEditBookEvent event) {
                setPlayerActed();
            }
        }, this);

        pm.registerEvents(new Listener() {
            @EventHandler(priority = EventPriority.MONITOR)
            public void onPlayerEggThrowEvent(PlayerEggThrowEvent event) {
                setPlayerActed();
            }
        }, this);

        pm.registerEvents(new Listener() {
            @EventHandler(priority = EventPriority.MONITOR)
            public void onPlayerFishEvent(PlayerFishEvent event) {
                setPlayerActed();
            }
        }, this);

        pm.registerEvents(new Listener() {
            @EventHandler(priority = EventPriority.MONITOR)
            public void onPlayerInteractEvent(PlayerInteractEvent event) {
                setPlayerActed();
            }
        }, this);

        pm.registerEvents(new Listener() {
            @EventHandler(priority = EventPriority.MONITOR)
            public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent event) {
                setPlayerActed();
            }
        }, this);

        pm.registerEvents(new Listener() {
            @EventHandler(priority = EventPriority.MONITOR)
            public void onPlayerItemHeldEvent(PlayerItemHeldEvent event) {
                setPlayerActed();
            }
        }, this);

        pm.registerEvents(new Listener() {
            @EventHandler(priority = EventPriority.MONITOR)
            public void onPlayerPickupItemEvent(PlayerPickupItemEvent event) {
                setPlayerActed();
            }
        }, this);

        pm.registerEvents(new Listener() {
            @EventHandler(priority = EventPriority.MONITOR)
            public void onPlayerPortalEvent(PlayerPortalEvent event) {
                setPlayerActed();
            }
        }, this);

        pm.registerEvents(new Listener() {
            @EventHandler(priority = EventPriority.MONITOR)
            public void onPlayerShearEntityEvent(PlayerShearEntityEvent event) {
                setPlayerActed();
            }
        }, this);

        pm.registerEvents(new Listener() {
            @EventHandler(priority = EventPriority.MONITOR)
            public void onPlayerLeashEntityEvent(PlayerLeashEntityEvent event) {
                setPlayerActed();
            }
        }, this);

        pm.registerEvents(new Listener() {
            @EventHandler(priority = EventPriority.MONITOR)
            public void onPlayerUnleashEntityEvent(PlayerUnleashEntityEvent event) {
                setPlayerActed();
            }
        }, this);
    }

    protected void setPlayerActed() {
        atLeastOnePlayerActedThisTick = true;
    }

    protected boolean samePosition(Location l1, Location l2) {
        return
                Double.doubleToLongBits(l1.getX()) == Double.doubleToLongBits(l2.getX()) &&
                        Double.doubleToLongBits(l1.getY()) == Double.doubleToLongBits(l2.getY()) &&
                        Double.doubleToLongBits(l1.getZ()) == Double.doubleToLongBits(l2.getZ());
    }

    protected void processVehicleEvent(VehicleEvent event) {
        // todo: this one is trickier because we need to cancel vehicle move events associated with
        // monsters, but not vehicle move events associated with players, and the VehicleMoveEvent
        // does not contain an entity, so we need to track the entities ourselves
    }

    protected <T extends EntityEvent & Cancellable> void processEntityEvent(T event) {
        Entity entity = event.getEntity();
        if (entity instanceof HumanEntity) {
            setPlayerActed();
        } else if (!atLeastOnePlayerActedLastTick) {
            event.setCancelled(true);
        }
    }
}
