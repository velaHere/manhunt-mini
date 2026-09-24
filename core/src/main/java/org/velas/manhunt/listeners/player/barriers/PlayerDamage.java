package org.velas.manhunt.listeners.player.barriers;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.velas.manhunt.config.Settings;
import org.velas.manhunt.Utils;
import org.velas.manhunt.dropSystem.Parachute;
import org.velas.manhunt.game.ManhuntGame;
import org.velas.manhunt.player.ManhuntPlayer;

public class PlayerDamage implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDamage(EntityDamageEvent event){

        if(event.getEntity() instanceof Chicken chicken)
            if(Parachute.isEntityPartOFParachute(chicken)) {
                event.setCancelled(true);
                return;
            }
        else if (event.getEntity() instanceof ArmorStand stand)
            if(Parachute.isEntityPartOFParachute(stand)) {
                event.setCancelled(true);
                return;
            }

        if (!(event.getEntity() instanceof Player player)) return;

        if (player.getLocation().getWorld().getName().equals(Settings.getConfigSettings().getLOBBY_WORLD_NAME())){
            event.setCancelled(true);
        }

        if (!ManhuntGame.getCurrentGame().isRunning()) return;

        ManhuntPlayer manhuntPlayer = ManhuntGame.getCurrentGame().getGameManager().getPlayerManager().getManhuntPlayer(player);

        if (manhuntPlayer == null) {
            Utils.warning("ManhuntPlayer was null for player: " + player.getName());
            return;
        }

        manhuntPlayer.getHandlerBundle().combatHandler().onPlayerGetDamage(event);
    }
}
