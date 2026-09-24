package org.velas.manhunt.listeners.player.barriers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.velas.manhunt.Utils;
import org.velas.manhunt.game.ManhuntGame;
import org.velas.manhunt.player.ManhuntPlayer;

public class PlayerPickup implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerPickup(EntityPickupItemEvent event){

        if (!ManhuntGame.getCurrentGame().isRunning()) return;
        if (!(event.getEntity() instanceof Player player)) return;

        ManhuntPlayer manhuntPlayer = ManhuntGame.getCurrentGame().getGameManager().getPlayerManager().getManhuntPlayer(player);
        if (manhuntPlayer == null) {
            Utils.warning("ManhuntPlayer was null for player: " + player.getName());
            return;
        }
        manhuntPlayer.getHandlerBundle().inventoryHandler().onItemPickup(event);
    }
}
