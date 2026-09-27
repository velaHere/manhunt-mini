package org.velas.manhunt.player.handler.lifecycle;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.Color;
import org.velas.manhunt.config.Settings;
import org.velas.manhunt.Manhunt;
import org.velas.manhunt.customItems.CustomItemType;
import org.velas.manhunt.dropSystem.drops.RespawnDrop;
import org.velas.manhunt.game.ManhuntGame;
import org.velas.manhunt.player.Hunter;
import org.velas.manhunt.player.handler.LifecycleHandler;

import java.util.concurrent.ThreadLocalRandom;

public abstract class BaseHunterLifecycleHandler extends LifecycleHandler {

    private static final int DROP_CAP = 5*60; // -> seconds
    private static final int DROP_RADIUS = 10; // -> blocks

    protected final @NotNull Hunter hunter;
    private long lastRespawnDropMillis;

    public BaseHunterLifecycleHandler(@NotNull Hunter hunter){
        super(hunter);
        this.hunter=hunter;
        this.lastRespawnDropMillis=0;
    }

    @Override
    protected void matchStart() {
        Player player = this.hunter.getPlayer();
        if(player==null) return;

        World world = player.getWorld();
        if(!world.getName().equals(Settings.OVERWORLD_NAME))
            player.teleport(
                    hunter.getManhuntGame()
                    .getGameManager()
                    .getWorldManager()
                    .getOverworld().getSpawnLocation()
            );

        player.getInventory().setItem(8, CustomItemType.create(CustomItemType.TRACKER_COMPASS));
        ManhuntGame game = this.hunter.getManhuntGame();
        player.addPotionEffect(new PotionEffect(
                PotionEffectType.BLINDNESS,
                20*game.getHeadStartTimeInSec(),   // duration in ticks (5 seconds)
                1,        // amplifier (level)
                false,    // ambient
                false     // particles
        ));
        player.addPotionEffect(new PotionEffect(
                PotionEffectType.DARKNESS,
                20*game.getHeadStartTimeInSec(),
                1,
                false,
                false
        ));
        player.sendMessage(Color.YELLOW + "You can move after " + game.getHeadStartTime()/20 +  " seconds");

        this.matchStartEvent();
    }

    @Override
    protected void matchStop() {
        this.matchStopEvent();
    }

    @Override
    protected void playerJoinServer() {
        Player player = this.hunter.getPlayer();
        if(player==null) return;
        player.getInventory().setItem(8, CustomItemType.create(CustomItemType.TRACKER_COMPASS));
        this.playerJoinServerEvent();
    }

    @Override
    protected void playerQuitServer() {
        this.playerQuitServerEvent();
    }

    @Override
    protected void playerDeath(PlayerDeathEvent event) {
        this.playerDeathEvent(event);
    }

    @Override
    protected void playerRespawn() {
        Player player = this.hunter.getPlayer();
        if(player==null)return;
        player.sendMessage(Color.YELLOW + "You can still get this game, don't loose the hopes!");

        World world = Bukkit.getWorld(Settings.OVERWORLD_NAME);
        if (world == null) return;

        Bukkit.getScheduler().runTaskLater(Manhunt.getINSTANCE(), () -> {
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
        this.playerRespawnEvent();
    }

    protected abstract void matchStartEvent();
    protected abstract void matchStopEvent();
    protected abstract void playerJoinServerEvent();
    protected abstract void playerQuitServerEvent();
    protected abstract void playerRespawnEvent();
    protected abstract void playerDeathEvent(PlayerDeathEvent event);
}
