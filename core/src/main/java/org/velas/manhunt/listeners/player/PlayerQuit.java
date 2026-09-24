package org.velas.manhunt.listeners.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.velas.manhunt.game.ManhuntGame;
import org.velas.manhunt.game.ManhuntManager;
import org.velas.manhunt.player.ManhuntPlayer;


public class PlayerQuit implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event){

        Player player = event.getPlayer();

        event.setQuitMessage("[§c-§f] " + player.getName());

        if (!ManhuntGame.getCurrentGame().isRunning()) return;

        ManhuntManager gameManager = ManhuntGame.getCurrentGame().getGameManager();

        ManhuntPlayer manhuntPlayer = gameManager.getPlayerManager().getManhuntPlayer(player);

        if (manhuntPlayer == null) return;
        manhuntPlayer.getHandlerBundle().lifecycleHandler().onPlayerQuitServer();
    }
}
