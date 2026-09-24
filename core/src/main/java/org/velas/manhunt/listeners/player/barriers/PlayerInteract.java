package org.velas.manhunt.listeners.player.barriers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.velas.manhunt.config.Settings;
import org.velas.manhunt.game.ManhuntGame;
import org.velas.manhunt.player.ManhuntPlayer;

public class PlayerInteract implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteraction(PlayerInteractEvent event){

        if (event.getPlayer().getLocation().getWorld().getName().equals(Settings.getConfigSettings().getLOBBY_WORLD_NAME()) && !event.getPlayer().hasPermission("manhunt.setup")){
            event.setCancelled(true);
        }

        Player player = event.getPlayer();

        ManhuntPlayer manhuntPlayer = ManhuntGame.getCurrentGame().getGameManager().getPlayerManager().getManhuntPlayer(player);
        if (manhuntPlayer == null) return;

        manhuntPlayer.getHandlerBundle().playerInteractionHandler().onPlayerInteraction(event);
    }
}
