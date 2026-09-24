package org.velas.manhunt.listeners.player.barriers;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.velas.manhunt.Utils;
import org.velas.manhunt.game.ManhuntGame;
import org.velas.manhunt.menusystem.Menu;
import org.velas.manhunt.player.ManhuntPlayer;

public class InventoryClick implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event){

        Inventory inv = event.getClickedInventory();
        if (inv == null) return;
        InventoryHolder holder = inv.getHolder();

        if (holder instanceof Menu menu) {
            menu.handleMenu(event);
        }

        if (!ManhuntGame.getCurrentGame().isRunning()) return;

        HumanEntity entity = event.getWhoClicked();

        if (!(entity instanceof Player player)) return;

        ManhuntPlayer manhuntPlayer = ManhuntGame.getCurrentGame().getGameManager().getPlayerManager().getManhuntPlayer(player);

        if (manhuntPlayer == null) {
            Utils.warning("ManhuntPlayer was null for player: " + player.getName());
            return;
        }
        manhuntPlayer.getHandlerBundle().inventoryHandler().onInventoryClick(event);
    }
}
