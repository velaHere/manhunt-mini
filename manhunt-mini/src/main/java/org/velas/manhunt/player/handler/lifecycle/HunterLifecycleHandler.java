package org.velas.manhunt.player.handler.lifecycle;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.config.Settings;
import org.velas.manhunt.Manhunt;
import org.velas.manhunt.customItems.CustomItemType;
import org.velas.manhunt.player.Hunter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class HunterLifecycleHandler extends BaseHunterLifecycleHandler {

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
        if(!this.hunter.isAlive()) {
            createRespawnTimer();
            giveSpectatorEffects();
        }
    }

    @Override
    protected void playerQuitServerEvent() {

    }

    @Override
    protected void playerRespawnEvent() {
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
        List<ItemStack> allContents = event.getDrops();
        Collections.shuffle(allContents);

        int keepAmount = (int) Math.ceil(allContents.size()* (Settings.getConfigSettings().getKEEP_INVENTORY_PERCENTAGE()/100));

        for(int i = 0; i < keepAmount; i++) keepContents.add(allContents.get(i));
        allContents.removeAll(keepContents);

        Bukkit.getScheduler().runTaskLater(Manhunt.getINSTANCE(), () ->{
            if (!player.isOnline()) return;
            player.spigot().respawn();
        }, 4L);
    }

    private void giveSpectatorEffects() {
        Player player = this.hunter.getPlayer();
        if(player == null) return;

        player.teleport(Objects.requireNonNullElseGet(player.getLastDeathLocation(), () -> {
            Location bedLoc = player.getBedSpawnLocation();
            return (
                    bedLoc != null &&
                    Objects.requireNonNull(bedLoc.getWorld()).getName().equals(Settings.getConfigSettings().getLOBBY_WORLD_NAME()))
                    ? bedLoc : this.hunter.getManhuntGame().getGameManager().getWorldManager().getOverworld().getSpawnLocation();
        }));

        // something
        player.setInvulnerable(false);
        player.setInvisible(true);

        player.setGameMode(GameMode.ADVENTURE);

        player.setAllowFlight(true);
        player.setFlying(true);
        player.setInvulnerable(true);
    }

    private void removeSpectatorEffects() {
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

        player.setInvulnerable(false);
        player.setInvisible(false);
        player.setGameMode(GameMode.SURVIVAL);
        player.setFlying(false);
        player.setAllowFlight(false);

        player.getInventory().setItem(8, CustomItemType.create(CustomItemType.TRACKER_COMPASS));

        this.giveItems(player);
        keepContents.forEach(item -> player.getInventory().addItem(item));
        keepContents.clear();
    }

    private void createRespawnTimer() {
        new RespawnTimer().runTaskTimer(Manhunt.getINSTANCE(), 0L, 20L);
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

            String title = "§6You will be Respawned in";
            String subtitle = String.valueOf(time--);

            player.sendTitle(title, subtitle, 0, 20, 0);
        }
    }
}
