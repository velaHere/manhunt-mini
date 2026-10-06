package org.velas.manhunt.listeners.player.barriers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.velas.manhunt.Color;
import org.velas.manhunt.config.Settings;
import org.velas.manhunt.game.ManhuntGame;
import org.velas.manhunt.player.ManhuntPlayer;

public class PlayerInteract implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteraction(PlayerInteractEvent event){

        Player player = event.getPlayer();

        if (!Settings.config().game().world().canInteractInLobby(player)){
            if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                event.setCancelled(true);
                if(event.isBlockInHand()) {
                    player.sendMessage(Color.YELLOW + ">> " + Color.RED + "Cannot perform this action here");
                }
            }
        }

        ManhuntPlayer manhuntPlayer = ManhuntGame.getCurrentGame().getGameManager().getPlayerManager().getManhuntPlayer(player);
        if (manhuntPlayer == null) return;

        manhuntPlayer.getHandlerBundle().playerInteractionHandler().onPlayerInteraction(event);
    }
}
