package org.velas.manhunt.listeners.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.velas.manhunt.game.ManhuntGame;
import org.velas.manhunt.player.ManhuntPlayer;

public class PlayerTeleport implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerTeleport(PlayerTeleportEvent event){

        Player player = event.getPlayer();

        if (!ManhuntGame.getCurrentGame().isRunning()) return;

        ManhuntPlayer manhuntPlayer = ManhuntGame.getCurrentGame().getGameManager().getPlayerManager().getManhuntPlayer(player);

        if (manhuntPlayer == null) return;
        if (event.getFrom().getWorld() == event.getTo().getWorld()) return;

        manhuntPlayer.getHandlerBundle().movementHandler().onPlayerTeleport(event);
    }
}
