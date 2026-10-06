package org.velas.manhunt.player.handler.movement;

import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.config.Settings;
import org.velas.manhunt.game.ManhuntGame;
import org.velas.manhunt.player.Runner;
import org.velas.manhunt.player.handler.MovementHandler;

public final class BaseRunnerMovementHandler implements MovementHandler {

    private final @NotNull Runner runner;

    public BaseRunnerMovementHandler(@NotNull Runner runner){
        this.runner=runner;
    }

    @Override
    public void onPlayerMove(PlayerMoveEvent event) {
        if(!this.runner.isOnline()) return;
        if(event.getPlayer().getWorld().getName().equals(Settings.config().game().world().getLobbyWorld())) return;
        ManhuntGame game = this.runner.getManhuntGame();
        if(game.isStarting()) event.setCancelled(true);
    }

    @Override
    public void onPlayerTeleport(PlayerTeleportEvent event) {
    }
}
