package org.velas.manhunt.player.handler.vehicle;

import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.player.Spectator;
import org.velas.manhunt.player.handler.VehicleHandler;

public final class BaseSpectatorVehicleHandler implements VehicleHandler {

    private final @NotNull Spectator spectator;

    public BaseSpectatorVehicleHandler(@NotNull Spectator spectator){
        this.spectator=spectator;
    }

    @Override
    public void onVehicleEnter(VehicleEnterEvent event) {
        if(!spectator.isOnline()) return;
        event.setCancelled(true);
    }

    @Override
    public void onVehicleDamage(VehicleDamageEvent event) {
        if(!spectator.isOnline()) return;
        event.setCancelled(true);
    }
}
