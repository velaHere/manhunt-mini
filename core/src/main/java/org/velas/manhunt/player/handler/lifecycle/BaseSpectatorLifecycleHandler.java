package org.velas.manhunt.player.handler.lifecycle;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.Color;
import org.velas.manhunt.Manhunt;
import org.velas.manhunt.config.Settings;
import org.velas.manhunt.game.GameFlowManager;
import org.velas.manhunt.player.Spectator;
import org.velas.manhunt.player.handler.LifecycleHandler;

public final class BaseSpectatorLifecycleHandler extends LifecycleHandler {

    private final @NotNull Spectator spectator;

    public BaseSpectatorLifecycleHandler(@NotNull Spectator spectator) {
        super(spectator);
        this.spectator=spectator;
    }

    @Override
    protected void matchStart() {
        Player player = spectator.getPlayer();
        if(player==null)return;
        GameFlowManager.applySpectatorsEffects(player);
    }

    @Override
    protected void matchStop() {
        Player player = spectator.getPlayer();
        if(player==null)return;
        GameFlowManager.removeSpectatorsEffects(player);
    }

    @Override
    protected void playerJoinServer() {
        Player player = spectator.getPlayer();
        if(player==null) return;
        player.getInventory().clear();
        player.sendMessage(Color.YELLOW + "A manhunt match is going on you can only spectate right now\n" + Color.GREEN + "Use compass to teleports to other players");

        Location loc = Settings.getConfigSettings().getLobbySpawn();
        if (loc == null) return;
        player.teleport(loc);
        GameFlowManager.applySpectatorsEffects(player);
    }

    @Override
    protected void playerQuitServer() {
    }

    @Override
    protected void playerDeath(PlayerDeathEvent event) {
    }

    @Override
    protected void playerRespawn() {
        Player player = spectator.getPlayer();
        if(player==null) return;
        GameFlowManager.applySpectatorsEffects(player);
        Location loc = Settings.getConfigSettings().getLobbySpawn();

        if (loc != null){
            if (!player.getLocation().getWorld().getName().equals(loc.getWorld().getName())){
                Bukkit.getScheduler().runTaskLater(Manhunt.getINSTANCE(),
                        () -> player.teleport(loc), 5L);
            }
        }
    }
}
