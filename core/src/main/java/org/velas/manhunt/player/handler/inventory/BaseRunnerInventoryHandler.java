package org.velas.manhunt.player.handler.inventory;

import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.player.Runner;
import org.velas.manhunt.player.handler.InventoryHandler;

public final class BaseRunnerInventoryHandler extends InventoryHandler {

    private final @NotNull Runner runner;

    public BaseRunnerInventoryHandler(@NotNull Runner runner){
        this.runner=runner;
    }

    @Override
    protected boolean inventoryClick(InventoryClickEvent event) {
        if(!(runner.isOnline())) return false;
        if(this.runner.getManhuntGame().isStarting()) {
            event.setCancelled(true);
            return false;
        }
        return true;
    }

    @Override
    protected boolean inventoryDrag(InventoryDragEvent event) {
        if(!(runner.isOnline())) return false;
        if(this.runner.getManhuntGame().isStarting()) {
            event.setCancelled(true);
            return false;
        }
        return true;
    }

    @Override
    protected boolean itemDrop(PlayerDropItemEvent event) {
        if(!(runner.isOnline())) return false;
        if(this.runner.getManhuntGame().isStarting()) {
            event.setCancelled(true);
            return false;
        }
        return true;
    }

    @Override
    protected boolean itemPickup(EntityPickupItemEvent event) {
        if(!(runner.isOnline())) return false;
        if(this.runner.getManhuntGame().isStarting()) {
            event.setCancelled(true);
            return false;
        }
        return true;
    }

    @Override
    protected boolean handSwap(PlayerSwapHandItemsEvent event) {
        if(!(runner.isOnline())) return false;
        if(this.runner.getManhuntGame().isStarting()) {
            event.setCancelled(true);
            return false;
        }
        return true;
    }
}
