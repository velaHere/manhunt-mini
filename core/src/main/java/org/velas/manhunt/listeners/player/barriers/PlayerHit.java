package org.velas.manhunt.listeners.player.barriers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.velas.manhunt.Utils;
import org.velas.manhunt.config.Settings;
import org.velas.manhunt.game.ManhuntGame;
import org.velas.manhunt.player.ManhuntPlayer;

public class PlayerHit implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerHit(EntityDamageByEntityEvent event){

        String lobbyName = Settings.config().game().world().getLobbyWorld();
        if (event.getDamager().getLocation().getWorld().getName().equals(lobbyName)){
            event.setCancelled(true);
        }

        ManhuntGame currentGame = ManhuntGame.getCurrentGame();
        if (!currentGame.isRunning()) return;

        if (!(event.getDamager() instanceof Player player)) return;

        ManhuntPlayer manhuntPlayer = ManhuntGame.getCurrentGame().getGameManager().getPlayerManager().getManhuntPlayer(player);

        if (manhuntPlayer == null) {
            Utils.warning("ManhuntPlayer was null for player: " + player.getName());
            return;
        }

        manhuntPlayer.getHandlerBundle().combatHandler().onPlayerDoDamage(event);
    }
}
