package org.velas.manhunt.listeners.game;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.velas.manhunt.customItems.CustomItemType;

public class OnItemCraft implements Listener {

    @EventHandler
    public void onItemCraft(PrepareItemCraftEvent event) {
        for (ItemStack item : event.getInventory().getMatrix()) {
            if (item == null) continue;
            if (CustomItemType.fromItem(item).isPresent()) {
                event.getInventory().setResult(null);
                return;
            }
        }
    }
}
