package org.velas.manhunt.listeners.player.barriers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.velas.manhunt.Color;
import org.velas.manhunt.config.Settings;
import org.velas.manhunt.game.ManhuntGame;
import org.velas.manhunt.player.ManhuntPlayer;

public class PlayerDropItem implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDropsItem(PlayerDropItemEvent event) {

        Player player = event.getPlayer();

        if (!Settings.config().game().world().canInteractInLobby(player)){
            event.setCancelled(true);
            event.getPlayer().sendMessage(Color.YELLOW + ">> " + Color.RED + "Cannot perform this action here");
        }

        ManhuntPlayer manhuntPlayer = ManhuntGame.getCurrentGame().getGameManager().getPlayerManager().getManhuntPlayer(player);
        if (manhuntPlayer == null) return;
        manhuntPlayer.getHandlerBundle().inventoryHandler().onItemDrop(event);
    }
}
