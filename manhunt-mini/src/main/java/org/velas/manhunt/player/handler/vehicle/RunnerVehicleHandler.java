package org.velas.manhunt.player.handler.vehicle;

import org.bukkit.event.Cancellable;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.game.GameState;
import org.velas.manhunt.player.Runner;

public class RunnerVehicleHandler extends BaseRunnerVehicleHandler {

    public RunnerVehicleHandler(@NotNull Runner runner) {
        super(runner);
    }

    @Override
    protected void vehicleEnterEvent(VehicleEnterEvent event) {
        this.onTransition(event);
    }

    @Override
    protected void vehicleDamageEvent(VehicleDamageEvent event) {
        this.onTransition(event);
    }

    private void onTransition(Cancellable event){
        if(this.runner.getManhuntGame().getGameState().equals(GameState.TRANSITION))
            event.setCancelled(true);
    }
}
