package org.velas.manhunt.player.handler.vehicle;

import org.bukkit.event.Cancellable;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.game.GameState;
import org.velas.manhunt.player.Hunter;

public class HunterVehicleHandler extends BaseHunterVehicleHandler {

    public HunterVehicleHandler(@NotNull Hunter hunter) {
        super(hunter);
    }

    @Override
    protected void vehicleEnterEvent(VehicleEnterEvent event) {
        this.cancelOnTransition(event);
        this.checkAlive(event);
    }

    @Override
    protected void vehicleDamageEvent(VehicleDamageEvent event) {
        this.cancelOnTransition(event);
        this.checkAlive(event);
    }

    private void cancelOnTransition(Cancellable event){
        if(this.hunter.getManhuntGame().getGameState().equals(GameState.TRANSITION))
            event.setCancelled(true);
    }

    private void checkAlive(Cancellable event) {
        if(!this.hunter.isAlive()) event.setCancelled(true);
    }
}
