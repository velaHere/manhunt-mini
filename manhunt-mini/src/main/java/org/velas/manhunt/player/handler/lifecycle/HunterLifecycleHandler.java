package org.velas.manhunt.player.handler.lifecycle;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.Utils;
import org.velas.manhunt.config.Settings;
import org.velas.manhunt.Manhunt;
import org.velas.manhunt.customItems.CustomItemType;
import org.velas.manhunt.dropSystem.drops.RespawnDrop;
import org.velas.manhunt.game.GameFlowManager;
import org.velas.manhunt.game.GameState;
import org.velas.manhunt.game.WorldManager;
import org.velas.manhunt.player.Hunter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class HunterLifecycleHandler extends BaseHunterLifecycleHandler {

    private static final int DROP_CAP = Settings.config().drop().respawn().getCooldown(); // -> seconds
    private static final int DROP_RADIUS = Settings.config().drop().respawn().getRadius(); // -> blocks
    private static final int RESPAWN_TIME_INTERVAL_SECONDS = 10;

    private final List<ItemStack> keepContents = new ArrayList<>();

    public HunterLifecycleHandler(@NotNull Hunter hunter) {
        super(hunter);
    }

    @Override
    protected void matchStartEvent() {
    }

    @Override
    protected void matchStopEvent() {
    }

    @Override
    protected void playerJoinServerEvent() {

        Player player = hunter.getPlayer();
        if(hunter.getManhuntGame().getGameState().equals(GameState.STRONGHOLD) && player!=null)
            GameFlowManager.stopIntenseVoidEffect(player);

        if(!this.hunter.isAlive()) {
            if(hunter.getManhuntGame().getGameState().equals(GameState.TRANSITION)) {
                respawnOnTransition();
                return;
            }
            createRespawnTimer();
            giveSpectatorEffects();
        }
    }

    @Override
    protected void playerQuitServerEvent() {

    }

    @Override
    protected void playerRespawnEvent() {

        if(hunter.getManhuntGame().getGameState().equals(GameState.TRANSITION)) {
            Bukkit.getScheduler().runTaskLater(
                    Manhunt.getINSTANCE(), this::respawnOnTransition, 1L
            );
            return;
        }

        Bukkit.getScheduler().runTaskLater(
                Manhunt.getINSTANCE(),
                () -> {
                    if (!hunter.isOnline() || hunter.isAlive()) {
                        return;
                    }
                    giveSpectatorEffects();
                    createRespawnTimer();
                },
                1L
        );
    }

    @Override
    protected void playerDeathEvent(PlayerDeathEvent event) {
        this.hunter.setAlive(false);
        keepContents.clear();
        Player player = event.getEntity().getPlayer();
        if(player==null) return;

        if (this.hunter.getManhuntGame().getGameState().equals(GameState.STRONGHOLD)) {
            List<ItemStack> drops = event.getDrops();

            double percentage = Math.max(0, Math.min(Settings.config().game().getKeepInventoryPercentage(), 100));

            if (!drops.isEmpty() && percentage > 0) {
                Collections.shuffle(drops);

                int keepAmount = (int) Math.round(drops.size() * percentage / 100.0);
                keepAmount = Math.min(keepAmount, drops.size());

                List<ItemStack> kept = drops.subList(0, keepAmount);
                for (ItemStack item : kept) {
                    if (item != null && !item.getType().isAir()) {
                        keepContents.add(item);
                    }
                }

                kept.clear();
            }
        }

        Bukkit.getScheduler().runTaskLater(Manhunt.getINSTANCE(), () -> {
            if (!player.isOnline()) return;
            player.spigot().respawn();
        }, 1L);
    }

    private void giveSpectatorEffects() {
        Player player = this.hunter.getPlayer();
        if(player == null) return;

        Location finalLocation = player.getLastDeathLocation();
        if(finalLocation == null) finalLocation = player.getBedSpawnLocation();

        if(isInvalidFinalLocation(finalLocation)) {
            finalLocation = this.hunter.getManhuntGame().getGameManager().getWorldManager().getOverworld().getSpawnLocation();
        }

        player.teleport(finalLocation);

        // something
        player.setInvulnerable(false);
        player.setInvisible(true);

        player.setGameMode(GameMode.ADVENTURE);

        player.setAllowFlight(true);
        player.setFlying(true);
        player.setInvulnerable(true);
    }

    private boolean isInvalidFinalLocation(Location location) {
        if (location == null || location.getWorld() == null) {
            return true;
        }

        World world = location.getWorld();

        if (world.getName().equals(
                Settings.config().game().world().getLobbyWorld()
        )) {
            return true;
        }

        WorldManager worldManager = hunter.getManhuntGame()
                .getGameManager()
                .getWorldManager();

        if (world.getEnvironment() == World.Environment.NETHER) {
            return !worldManager.getNetherWorld()
                    .getWorldBorder()
                    .isInside(location);
        }

        if (world.getEnvironment() == World.Environment.NORMAL) {
            return !worldManager.getOverworld()
                    .getWorldBorder()
                    .isInside(location);
        }

        return false;
    }

    private void respawnOnTransition() {

        Player player = this.hunter.getPlayer();
        if(player==null) return;

        if(this.hunter.isAlive()) return;

        hunter.setAlive(true);

        removeEffectsAndGiveItems(player);
    }

    private void removeSpectatorEffects() {

        if(this.hunter.isAlive()) return;

        Player player = this.hunter.getPlayer();
        if(player == null) return;
        hunter.setAlive(true);
        player.teleport(
                Objects.requireNonNullElseGet(
                        player.getBedSpawnLocation(),
                        () -> this.hunter
                                .getManhuntGame().getGameManager().getWorldManager()
                                .getOverworld().getSpawnLocation()
                )
        );

        removeEffectsAndGiveItems(player);

        if(!Settings.config().drop().respawn().isEnabled()) return;

        Bukkit.getScheduler().runTaskLater(Manhunt.getINSTANCE(), () -> {

            if(!player.getWorld().getName().equals(Settings.OVERWORLD_NAME)) return;

            if(this.hunter.getManhuntGame().getGameManager().getDropManager().hasRespawnDropUnlocked()) {
                long l = System.currentTimeMillis();
                if ((l - lastRespawnDropMillis) / 1000 > DROP_CAP) {

                    double theta = ThreadLocalRandom.current().nextDouble(0, 2*Math.PI);
                    double radius = DROP_RADIUS * ThreadLocalRandom.current().nextDouble(0.3, 1.0);

                    double offsetX = radius * Math.cos(theta);
                    double offsetZ = radius * Math.sin(theta);

                    Location dropLoc = player.getLocation().clone().add(offsetX, 0, offsetZ);

                    Bukkit.getScheduler().runTaskLater(Manhunt.getINSTANCE(), () ->
                                    new RespawnDrop(dropLoc).spawnDrop(),
                            20L);
                    this.lastRespawnDropMillis = l;
                }
            }
        }, 5L);
    }

    private void removeEffectsAndGiveItems(Player player) {
        player.setInvulnerable(false);
        player.setInvisible(false);
        player.setGameMode(GameMode.SURVIVAL);
        player.setFlying(false);
        player.setAllowFlight(false);

        player.getInventory().setItem(8, CustomItemType.create(CustomItemType.TRACKER_COMPASS));

        this.giveItems(player);
        if(!keepContents.isEmpty()) {
            keepContents.forEach(item -> player.getInventory().addItem(item));
            keepContents.clear();
        }
    }

    private void createRespawnTimer() {
        new RespawnTimer().runTaskTimer(Manhunt.getINSTANCE(), 0L, 20L);
    }

    @Override
    protected void transition() {

        Utils.warning("Alright, shit");

        Player player = hunter.getPlayer();

        if(player == null) return;

        if(player.isDead()) {
            Bukkit.getScheduler().runTaskLater(
                    Manhunt.getINSTANCE(),
                    () -> player.spigot().respawn(),
                    1L
            );
            return;
        }

        if(!hunter.isAlive()) {
            Bukkit.getScheduler().runTaskLater(
                    Manhunt.getINSTANCE(),
                    this::respawnOnTransition,
                    1L
            );
        }
    }

    private class RespawnTimer extends BukkitRunnable {

        private int time = RESPAWN_TIME_INTERVAL_SECONDS;

        @Override
        public void run() {

            if(this.time == 0) {
                removeSpectatorEffects();
                this.cancel();
                return;
            }

            if(!hunter.isOnline() || hunter.isAlive()) {
                this.cancel();
                return;
            }

            Player player = hunter.getPlayer();
            if(player == null) {
                this.cancel();
                return;
            }

            String subtitle = "§eYou will respawn in §c" + time-- + "§e seconds!";

            player.sendTitle("§cYOU DIED!", subtitle, 0, 20, 0);
        }
    }
}
