package org.velas.manhunt.player.handler.inventory;

import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.menusystem.Menu;
import org.velas.manhunt.player.Spectator;
import org.velas.manhunt.player.handler.InventoryHandler;

public final class BaseSpectatorInventoryHandler extends InventoryHandler {

    private final @NotNull Spectator spectator;

    public BaseSpectatorInventoryHandler(@NotNull Spectator spectator){
        this.spectator=spectator;
    }

    @Override
    protected boolean inventoryClick(InventoryClickEvent event) {
        if(!(spectator.isOnline())) return false;
        event.setCancelled(true);
        Inventory inventory = event.getClickedInventory();
        if (inventory == null) return false;
        if (inventory.getHolder() instanceof Menu menu){
            menu.handleMenu(event);
        }
        return false;
    }

    @Override
    protected boolean inventoryDrag(InventoryDragEvent event) {
        if(!(spectator.isOnline())) return false;
        event.setCancelled(true);
        return false;
    }

    @Override
    protected boolean itemDrop(PlayerDropItemEvent event) {
        if(!(spectator.isOnline())) return false;
        event.setCancelled(true);
        return false;
    }

    @Override
    protected boolean itemPickup(EntityPickupItemEvent event) {
        if(!(spectator.isOnline())) return false;
        event.setCancelled(true);
        return false;
    }

    @Override
    protected boolean handSwap(PlayerSwapHandItemsEvent event) {
        if(!(spectator.isOnline())) return false;
        event.setCancelled(true);
        return false;
    }
}
