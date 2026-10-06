package org.velas.manhunt.listeners.player.barriers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.velas.manhunt.Color;
import org.velas.manhunt.Utils;
import org.velas.manhunt.config.Settings;
import org.velas.manhunt.game.ManhuntGame;
import org.velas.manhunt.player.ManhuntPlayer;

public class BlockBreak implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerBreaks(BlockBreakEvent event){

        Player player = event.getPlayer();

        if (!Settings.config().game().world().canInteractInLobby(player)){
            event.setCancelled(true);
            player.sendMessage(Color.YELLOW + ">> " + Color.RED + "Cannot perform this action here");
        }

        if (!ManhuntGame.getCurrentGame().isRunning()) return;

        ManhuntPlayer manhuntPlayer = ManhuntGame.getCurrentGame().getGameManager().getPlayerManager().getManhuntPlayer(player);

        if (manhuntPlayer == null) {
            Utils.warning("ManhuntPlayer was null for player: " + player.getName());
            return;
        }

        manhuntPlayer.getHandlerBundle().worldInteractionHandler().onBlockBreak(event);
    }
}
