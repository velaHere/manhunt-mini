package org.velas.manhunt.player.handler.movement;

import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.player.Hunter;
import org.velas.manhunt.player.handler.MovementHandler;

public final class BaseHunterMovementHandler implements MovementHandler {

    private final @NotNull Hunter hunter;

    public BaseHunterMovementHandler(@NotNull Hunter hunter){
        this.hunter=hunter;
    }

    @Override
    public void onPlayerMove(PlayerMoveEvent event) {
        if(!this.hunter.isOnline()) return;
        if (this.hunter.getManhuntGame().getGameManager().getGameFlowManager().ifHuntersCannotMove())
            event.setCancelled(true);
    }

    @Override
    public void onPlayerTeleport(PlayerTeleportEvent event) {

    }
}
