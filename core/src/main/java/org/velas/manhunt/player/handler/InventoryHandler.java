package org.velas.manhunt.player.handler;

import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.velas.manhunt.customItems.CustomItem;
import org.velas.manhunt.customItems.CustomItemType;

import java.util.Optional;

public abstract class InventoryHandler {

    protected abstract boolean inventoryClick(InventoryClickEvent event);
    public void onInventoryClick(InventoryClickEvent event){
        if(!this.inventoryClick(event)) return;
        Optional<CustomItem> customItem = CustomItemType.fromItem(event.getCursor());
        if(customItem.isEmpty()) return;
        customItem.get().placeItemToHeldSlot(event);
    }

    protected abstract boolean inventoryDrag(InventoryDragEvent event);
    public void onInventoryDrag(InventoryDragEvent event){
        if(!this.inventoryDrag(event)) return;
    }

    protected abstract boolean itemDrop(PlayerDropItemEvent event);
    public void onItemDrop(PlayerDropItemEvent event){
        if(!this.itemDrop(event)) return;
        Optional<CustomItem> customItem = CustomItemType.fromItem(event.getItemDrop().getItemStack());
        if(customItem.isEmpty()) return;
        customItem.get().onItemDrop(event);
    }

    protected abstract boolean itemPickup(EntityPickupItemEvent event);
    public void onItemPickup(EntityPickupItemEvent event){
        if(!this.itemPickup(event)) return;
        ItemStack pickedItem = event.getItem().getItemStack();
        Optional<CustomItem> customItem = CustomItemType.fromItem(pickedItem);
        if(customItem.isEmpty()) return;
        customItem.get().onPickup(event);
    }

    protected abstract boolean handSwap(PlayerSwapHandItemsEvent event);
    public void onHandSwap(PlayerSwapHandItemsEvent event){
        if(!this.handSwap(event)) return;
        ItemStack item = event.getMainHandItem();
        Optional<CustomItem> customItem = CustomItemType.fromItem(item);
        if(customItem.isEmpty()) return;
        customItem.get().onItemSwap(event);
    }
}
