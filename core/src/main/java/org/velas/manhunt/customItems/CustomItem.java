package org.velas.manhunt.customItems;

import org.bukkit.NamespacedKey;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public interface CustomItem {

    NamespacedKey getKey();
    ItemStack createItem();
    void registerRecipe(JavaPlugin plugin);
    void onUse(PlayerInteractEvent event);
    void onItemDrop(PlayerDropItemEvent event);
    void onPickup(EntityPickupItemEvent event);
    void onItemSwitch(PlayerItemHeldEvent event);
    void onItemSwap(PlayerSwapHandItemsEvent event);
    void placeItemToHeldSlot(InventoryClickEvent event);
    default boolean matches(ItemStack item){
        if(item == null || item.getItemMeta()==null) return false;
        return item.getItemMeta().getPersistentDataContainer().has(this.getKey(), PersistentDataType.BYTE);
    }
}
