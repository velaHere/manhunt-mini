package org.velas.manhunt.listeners.game;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.block.Block;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;
import org.velas.manhunt.Manhunt;
import org.velas.manhunt.config.Settings;
import org.velas.manhunt.event.ManhuntMatchStartEvent;
import org.velas.manhunt.event.ManhuntMatchStopEvent;
import org.velas.manhunt.game.ManhuntGame;

import java.util.concurrent.ThreadLocalRandom;

public class MobSpawn implements Listener {

    private static final int SPAWN_RADIUS = 32;
    private static final int MIN_DISTANCE = 12;
    private static final int VERTICAL_RANGE = 8;
    private static final int ATTEMPTS = 15;
    private static final double BORDER_MARGIN = 2.0;

    private static final int MAX_BLAZES = 20;
    private static final int MAX_ENDERMEN = 15;

    private static final double SPAWN_CHANCE = 0.45;

    private MobSpawn SPAWNER;

    private BukkitTask task;

    @EventHandler
    public void onMatchStart(ManhuntMatchStartEvent event) {
        SPAWNER = new MobSpawn();
        SPAWNER.start();
    }

    @EventHandler
    public void onMatchStop(ManhuntMatchStopEvent event) {
        if(SPAWNER!= null) SPAWNER.stop();
    }

    public void start() {
        if (task != null) {
            return;
        }
        task = Bukkit.getScheduler().runTaskTimer(
                Manhunt.getINSTANCE(), this::tick, 20L, 100L);
    }

    public void stop() {
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    private void tick() {

        ManhuntGame game = ManhuntGame.getCurrentGame();
        if (!game.isRunning()) {
            return;
        }

        World world = Bukkit.getWorld(Settings.NETHER_WORLD_NAME);
        if (world == null) {
            return;
        }

        // Real counts straight from the world, limited to entities inside the border.
        int blazes = countInsideBorder(world, Blaze.class);
        int endermen = countInsideBorder(world, Enderman.class);

        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (Player player : world.getPlayers()) {

            if (player.getGameMode() == GameMode.SPECTATOR || player.isDead()) {
                continue;
            }

            if (random.nextDouble() > SPAWN_CHANCE) {
                continue;
            }

            boolean wantBlaze = random.nextBoolean();

            if (wantBlaze && blazes >= MAX_BLAZES) {
                continue;
            }
            if (!wantBlaze && endermen >= MAX_ENDERMEN) {
                continue;
            }

            Location location = findSpawnLocation(player);
            if (location == null) {
                continue;
            }

            LivingEntity mob = (LivingEntity) world.spawnEntity(
                    location, wantBlaze ? EntityType.BLAZE : EntityType.ENDERMAN);

            // Stops the server despawning them when the player is far away.
            mob.setRemoveWhenFarAway(false);

            if (wantBlaze) {
                blazes++;
            } else {
                endermen++;
            }
        }
    }

    private Location findSpawnLocation(Player player) {

        World world = player.getWorld();
        Location origin = player.getLocation();

        int px = origin.getBlockX();
        int py = origin.getBlockY();
        int pz = origin.getBlockZ();

        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (int i = 0; i < ATTEMPTS; i++) {

            int x = px + random.nextInt(-SPAWN_RADIUS, SPAWN_RADIUS + 1);
            int z = pz + random.nextInt(-SPAWN_RADIUS, SPAWN_RADIUS + 1);

            int dx = x - px;
            int dz = z - pz;
            if (dx * dx + dz * dz < MIN_DISTANCE * MIN_DISTANCE) {
                continue;
            }

            // Only spawn inside the (possibly shrinking) world border.
            if (!isInsideBorder(world, x + 0.5, z + 0.5, BORDER_MARGIN)) {
                continue;
            }

            // Don't force chunk loads from the spawn task.
            if (!world.isChunkLoaded(x >> 4, z >> 4)) {
                continue;
            }

            Location found = scanColumn(world, x, z, py);
            if (found != null) {
                return found;
            }
        }

        return null;
    }

    /**
     * True if the x/z position is inside the world border, keeping a small
     * margin so mobs don't spawn right on the edge.
     */
    private boolean isInsideBorder(World world, double x, double z, double margin) {

        WorldBorder border = world.getWorldBorder();
        Location center = border.getCenter();
        double half = border.getSize() / 2.0 - margin;

        return Math.abs(x - center.getX()) <= half
                && Math.abs(z - center.getZ()) <= half;
    }

    /**
     * Counts entities of the given type that are inside the world border.
     * Anything outside the border (e.g. left over from before it shrank)
     * doesn't count toward the caps.
     */
    private int countInsideBorder(World world, Class<? extends Entity> type) {

        int count = 0;

        for (Entity entity : world.getEntitiesByClass(type)) {
            Location loc = entity.getLocation();
            if (isInsideBorder(world, loc.getX(), loc.getZ(), 0)) {
                count++;
            }
        }

        return count;
    }

    /**
     * Scans a column top to bottom around the player's height and returns the
     * first solid floor that has 3 blocks of air above it (enderman height).
     */
    private Location scanColumn(World world, int x, int z, int centerY) {

        int minY = Math.max(world.getMinHeight() + 1, centerY - VERTICAL_RANGE);
        int maxY = Math.min(world.getMaxHeight() - 4, centerY + VERTICAL_RANGE);

        for (int y = maxY; y >= minY; y--) {

            Block floor = world.getBlockAt(x, y - 1, z);

            if (!floor.getType().isSolid() || floor.getType() == Material.BEDROCK) {
                continue;
            }

            if (world.getBlockAt(x, y, z).isEmpty()
                    && world.getBlockAt(x, y + 1, z).isEmpty()
                    && world.getBlockAt(x, y + 2, z).isEmpty()) {
                return new Location(world, x + 0.5, y, z + 0.5);
            }
        }

        return null;
    }
}