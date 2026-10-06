package org.velas.manhunt.listeners.player;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.velas.manhunt.Manhunt;
import org.velas.manhunt.config.Settings;
import org.velas.manhunt.game.ManhuntGame;
import org.velas.manhunt.player.ManhuntPlayer;

public class PlayerRespawn implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent event){

        Player player = event.getPlayer();

        if (!ManhuntGame.getCurrentGame().isRunning()){
            Location loc = Settings.config().game().world().getLobbySpawn();
            if (loc != null){
                Bukkit.getScheduler().runTaskLater(Manhunt.getINSTANCE(), () -> player.teleport(loc), 5L);
            }
            return;
        }

        ManhuntPlayer manhuntPlayer = ManhuntGame.getCurrentGame().getGameManager().getPlayerManager().getManhuntPlayer(player);
        if (manhuntPlayer == null) return;
        manhuntPlayer.getHandlerBundle().lifecycleHandler().onPlayerRespawn();
    }
}
