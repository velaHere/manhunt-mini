package org.velas.manhunt.listeners.player.barriers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.velas.manhunt.Utils;
import org.velas.manhunt.game.ManhuntGame;
import org.velas.manhunt.player.ManhuntPlayer;


public class VehicleDamage implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onVehicleDamage(VehicleDamageEvent event){

        if (!(event.getAttacker() instanceof Player player)) return;

        if (!ManhuntGame.getCurrentGame().isRunning()) return;

        ManhuntPlayer manhuntPlayer = ManhuntGame.getCurrentGame().getGameManager().getPlayerManager().getManhuntPlayer(player);

        if (manhuntPlayer == null) {
            Utils.warning("ManhuntPlayer was null for player: " + player.getName());
            return;
        }

        manhuntPlayer.getHandlerBundle().vehicleHandler().onVehicleDamage(event);
    }
}
