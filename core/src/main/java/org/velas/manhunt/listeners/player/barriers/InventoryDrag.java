package org.velas.manhunt.listeners.player.barriers;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.velas.manhunt.game.ManhuntGame;
import org.velas.manhunt.player.ManhuntPlayer;


public class InventoryDrag implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryDrag(InventoryDragEvent event){

        if (!ManhuntGame.getCurrentGame().isRunning()) return;

        HumanEntity entity = event.getWhoClicked();

        if (!(entity instanceof Player player)) return;

        ManhuntPlayer manhuntPlayer = ManhuntGame.getCurrentGame().getGameManager().getPlayerManager().getManhuntPlayer(player);

        if (manhuntPlayer == null) return;

        manhuntPlayer.getHandlerBundle().inventoryHandler().onInventoryDrag(event);
    }
}
