package org.velas.manhunt.player.handler.vehicle;

import org.bukkit.event.Cancellable;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.player.Hunter;
import org.velas.manhunt.player.handler.VehicleHandler;

public abstract class BaseHunterVehicleHandler implements VehicleHandler {

    protected final @NotNull Hunter hunter;

    public BaseHunterVehicleHandler(@NotNull Hunter hunter){
        this.hunter=hunter;
    }

    @Override
    public void onVehicleEnter(VehicleEnterEvent event) {
        if(!this.hunter.isOnline()) return;
        cancelOnFreeze(event);
        this.vehicleEnterEvent(event);
    }

    @Override
    public void onVehicleDamage(VehicleDamageEvent event) {
        if(!this.hunter.isOnline()) return;
        cancelOnFreeze(event);
        this.vehicleDamageEvent(event);
    }

    private void cancelOnFreeze(Cancellable event){
        if (this.hunter.getManhuntGame().getGameManager().getGameFlowManager().ifHuntersCannotMove())
            event.setCancelled(true);
    }

    protected abstract void vehicleEnterEvent(VehicleEnterEvent event);
    protected abstract void vehicleDamageEvent(VehicleDamageEvent event);
}
