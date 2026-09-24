package org.velas.manhunt.listeners.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.velas.manhunt.Color;
import org.velas.manhunt.game.ManhuntGame;
import org.velas.manhunt.game.ManhuntManager;
import org.velas.manhunt.game.PlayerManager;
import org.velas.manhunt.player.ManhuntPlayer;

public class PlayerDeath implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDies(PlayerDeathEvent event){

        ManhuntGame currentGame = ManhuntGame.getCurrentGame();
        ManhuntManager manager = currentGame.getGameManager();

        if (!currentGame.isRunning()) return;

        Player player = event.getEntity();

        if (player.getKiller() != null){
            event.setDeathMessage(Color.GOLD + "[" + Color.WHITE + Color.BOLD + "\uD83D\uDC80" + Color.GOLD +"] " + Color.YELLOW + player.getName() + Color.RED + " has been taken down by " + Color.YELLOW + player.getKiller().getName());
        }else{
            event.setDeathMessage(Color.GOLD + "[" + Color.WHITE + Color.BOLD + "\uD83D\uDC80" + Color.GOLD +"] " + Color.YELLOW + player.getName() + Color.RED + " has been eliminated");
        }

        PlayerManager playerManager = manager.getPlayerManager();

        ManhuntPlayer manhuntPlayer = playerManager.getManhuntPlayer(player);

        if (manhuntPlayer == null) return;

        manhuntPlayer.getHandlerBundle().lifecycleHandler().onPlayerDeath(event);
    }
}
