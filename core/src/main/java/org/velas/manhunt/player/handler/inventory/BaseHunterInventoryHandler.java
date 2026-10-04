package org.velas.manhunt.player.handler.inventory;

import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.player.Hunter;
import org.velas.manhunt.player.handler.InventoryHandler;

public final class BaseHunterInventoryHandler extends InventoryHandler {

    private final @NotNull Hunter hunter;

    public BaseHunterInventoryHandler(@NotNull Hunter hunter){
        this.hunter=hunter;
    }

    @Override
    public boolean inventoryClick(InventoryClickEvent event) {
        if(!hunter.isOnline()) return false;
        if(!hunter.isAlive()) {
            event.setCancelled(true);
            return false;
        }
        Inventory inv = event.getClickedInventory();
        if (inv == null) return false;
        if (inv.getType() != InventoryType.PLAYER) return false;
        if (event.getSlot() == 8){
            event.setCancelled(true);
            return false;
        }
        return true;
    }

    @Override
    public boolean inventoryDrag(InventoryDragEvent event) {
        if(!hunter.isOnline()) return false;
        if(!hunter.isAlive()) {
            event.setCancelled(true);
            return false;
        }
        if (event.getInventory().getType() != InventoryType.PLAYER) return false;
        if (event.getInventorySlots().contains(8)){
            event.setCancelled(true);
            return false;
        }
        return true;
    }

    @Override
    public boolean itemDrop(PlayerDropItemEvent event) {
        if(!hunter.isOnline()) return false;
        if(!hunter.isAlive()) {
            event.setCancelled(true);
            return false;
        }
        if (this.hunter.getManhuntGame().getGameManager().getGameFlowManager().ifHuntersCannotMove()) {
            event.setCancelled(true);
            return false;
        }
        return true;
    }

    @Override
    public boolean itemPickup(EntityPickupItemEvent event) {
        if(!hunter.isOnline()) return false;
        if(!hunter.isAlive()) {
            event.setCancelled(true);
            return false;
        }
        if (this.hunter.getManhuntGame().getGameManager().getGameFlowManager().ifHuntersCannotMove()){
            event.setCancelled(true);
            return false;
        }
        return true;
    }

    @Override
    public boolean handSwap(PlayerSwapHandItemsEvent event) {
        if(!hunter.isAlive()) {
            event.setCancelled(true);
            return false;
        }
        return hunter.isOnline();
    }
}
