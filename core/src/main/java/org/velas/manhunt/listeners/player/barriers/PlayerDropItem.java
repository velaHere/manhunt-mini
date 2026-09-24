package org.velas.manhunt.listeners.player.barriers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.velas.manhunt.config.Settings;
import org.velas.manhunt.game.ManhuntGame;
import org.velas.manhunt.player.ManhuntPlayer;

public class PlayerDropItem implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDropsItem(PlayerDropItemEvent event) {

        Player player = event.getPlayer();

        if (player.getLocation().getWorld().getName().equals(Settings.getConfigSettings().getLOBBY_WORLD_NAME()) && !player.hasPermission("manhunt.setup")){
            event.setCancelled(true);
        }

        ManhuntPlayer manhuntPlayer = ManhuntGame.getCurrentGame().getGameManager().getPlayerManager().getManhuntPlayer(player);
        if (manhuntPlayer == null) return;
        manhuntPlayer.getHandlerBundle().inventoryHandler().onItemDrop(event);
    }
}
