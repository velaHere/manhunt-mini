package org.velas.manhunt.player.handler;

import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public interface MovementHandler {
    void onPlayerMove(PlayerMoveEvent event);
    void onPlayerTeleport(PlayerTeleportEvent event);
}
