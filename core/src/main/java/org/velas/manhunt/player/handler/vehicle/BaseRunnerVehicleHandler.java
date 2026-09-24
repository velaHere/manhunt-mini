package org.velas.manhunt.player.handler.vehicle;

import org.bukkit.event.Cancellable;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.player.Runner;
import org.velas.manhunt.player.handler.VehicleHandler;

public abstract class BaseRunnerVehicleHandler implements VehicleHandler {

    protected final @NotNull Runner runner;

    public BaseRunnerVehicleHandler(@NotNull Runner runner){
        this.runner=runner;
    }

    @Override
    public void onVehicleEnter(VehicleEnterEvent event) {
        if(!this.runner.isOnline()) return;
        cancelOnFreeze(event);
        this.vehicleEnterEvent(event);
    }

    @Override
    public void onVehicleDamage(VehicleDamageEvent event) {
        if(!this.runner.isOnline()) return;
        cancelOnFreeze(event);
        this.vehicleDamageEvent(event);
    }

    private void cancelOnFreeze(Cancellable event){
        if(this.runner.getManhuntGame().isStarting()) event.setCancelled(true);

    }

    protected abstract void vehicleEnterEvent(VehicleEnterEvent event);
    protected abstract void vehicleDamageEvent(VehicleDamageEvent event);
}
