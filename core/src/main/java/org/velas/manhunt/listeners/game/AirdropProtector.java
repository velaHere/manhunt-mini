package org.velas.manhunt.listeners.game;

import org.bukkit.entity.Minecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.persistence.PersistentDataType;
import org.velas.manhunt.dropSystem.drops.AirDrop;

public class AirdropProtector implements Listener {

    private boolean isDropCart(Object vehicle) {
        if (!(vehicle instanceof Minecart cart)) return false;
        return cart.getPersistentDataContainer().has(AirDrop.KEY, PersistentDataType.BYTE);
    }

    @EventHandler(ignoreCancelled = true)
    public void onVehicleDamage(VehicleDamageEvent e) {
        if (isDropCart(e.getVehicle())) e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onVehicleDestroy(VehicleDestroyEvent e) {
        if (isDropCart(e.getVehicle())) e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent e) {
        if (isDropCart(e.getEntity())) e.setCancelled(true);
    }
}
