package org.velas.manhunt.listeners.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.velas.manhunt.customItems.CustomItem;
import org.velas.manhunt.customItems.CustomItemType;
import org.velas.manhunt.game.ManhuntGame;

import java.util.Optional;

public class ItemSwitch implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemSwitch(PlayerItemHeldEvent event){

        if (!ManhuntGame.getCurrentGame().isRunning()) return;

        Player player = event.getPlayer();
        int newSlot = event.getNewSlot();
        ItemStack newItem = player.getInventory().getItem(newSlot);
        Optional<CustomItem> customItem = CustomItemType.fromItem(newItem);
        if(customItem.isEmpty()) return;
        customItem.get().onItemSwitch(event);
    }
}
