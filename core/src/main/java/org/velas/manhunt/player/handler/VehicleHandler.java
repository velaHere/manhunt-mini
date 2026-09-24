package org.velas.manhunt.player.handler;

import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;

public interface VehicleHandler {
    void onVehicleEnter(VehicleEnterEvent event);
    void onVehicleDamage(VehicleDamageEvent event);
}