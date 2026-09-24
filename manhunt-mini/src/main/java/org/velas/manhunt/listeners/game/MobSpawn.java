package org.velas.manhunt.listeners.game;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.velas.manhunt.Manhunt;
import org.velas.manhunt.config.Settings;
import org.velas.manhunt.game.ManhuntGame;

import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

public class MobSpawn extends BukkitRunnable implements Listener {

    private static final int SPAWN_RADIUS = 32;
    private static final int MIN_DISTANCE = 12;

    private static final int MAX_BLAZES = 7;
    private static final int MAX_ENDERMEN = 5;

    private BukkitRunnable spawner = null;

    @EventHandler
    public void onMobSpawn(CreatureSpawnEvent event) {
        if(spawner!=null) return;
        if (!ManhuntGame.getCurrentGame().isRunning()) return;

        World world = event.getLocation().getWorld();
        if(world==null) return;
        if(world.getEnvironment()!= World.Environment.NETHER) return;
        if(!world.getName().equals(Settings.NETHER_WORLD_NAME)) return;
        if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.NATURAL) return;
        spawner = new MobSpawn();
        spawner.runTaskTimer(Manhunt.getINSTANCE(), 20L, 300L);
    }

    @Override
    public void run() {

        if (!ManhuntGame.getCurrentGame().isRunning()) return;

        World world = Bukkit.getWorld(Settings.NETHER_WORLD_NAME);

        if (world == null) return;

        int blazeCount = count(world, EntityType.BLAZE);
        int endermanCount = count(world, EntityType.ENDERMAN);

        for (Player player : world.getPlayers()) {

            if (ThreadLocalRandom.current().nextDouble() < 0.45) {

                Location spawnLoc = findSpawnLocation(player);

                if (spawnLoc == null) continue;

                boolean spawnBlaze = ThreadLocalRandom.current().nextBoolean();

                if (spawnBlaze) {

                    if (blazeCount >= MAX_BLAZES) continue;

                    world.spawnEntity(spawnLoc, EntityType.BLAZE);
                    blazeCount++;

                } else {

                    if (endermanCount >= MAX_ENDERMEN) continue;

                    world.spawnEntity(spawnLoc, EntityType.ENDERMAN);
                    endermanCount++;
                }
            }
        }
    }

    private Location findSpawnLocation(Player player) {

        World world = player.getWorld();

        for (int i = 0; i < 15; i++) {

            int x = player.getLocation().getBlockX()
                    + ThreadLocalRandom.current().nextInt(-SPAWN_RADIUS, SPAWN_RADIUS + 1);

            int z = player.getLocation().getBlockZ()
                    + ThreadLocalRandom.current().nextInt(-SPAWN_RADIUS, SPAWN_RADIUS + 1);

            int y = player.getLocation().getBlockY()
                    + ThreadLocalRandom.current().nextInt(-8, 9);

            Location loc = new Location(world, x + 0.5, y, z + 0.5);

            if (loc.distanceSquared(player.getLocation()) < MIN_DISTANCE * MIN_DISTANCE) {
                continue;
            }

            if (!loc.getBlock().isEmpty()) continue;

            if (loc.clone().subtract(0, 1, 0).getBlock().isPassable()) continue;

            if (!loc.clone().add(0, 1, 0).getBlock().isEmpty()) continue;

            return loc;
        }

        return null;
    }

    private int count(World world, EntityType type) {

        int count = 0;

        Collection<Entity> entities = world.getEntities();

        for (Entity entity : entities) {
            if (entity.getType() == type) {
                count++;
            }
        }

        return count;
    }
}