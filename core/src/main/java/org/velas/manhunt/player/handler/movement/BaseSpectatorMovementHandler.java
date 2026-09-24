package org.velas.manhunt.player.handler.movement;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.game.GameFlowManager;
import org.velas.manhunt.player.Spectator;
import org.velas.manhunt.player.handler.MovementHandler;

public final class BaseSpectatorMovementHandler implements MovementHandler {

    private final @NotNull Spectator spectator;

    public BaseSpectatorMovementHandler(@NotNull Spectator spectator){
        this.spectator=spectator;
    }

    @Override
    public void onPlayerMove(PlayerMoveEvent event) {
    }

    @Override
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if(!this.spectator.isOnline()) return;
        Player player = event.getPlayer();
        GameFlowManager.applySpectatorsEffects(player);
    }
}
