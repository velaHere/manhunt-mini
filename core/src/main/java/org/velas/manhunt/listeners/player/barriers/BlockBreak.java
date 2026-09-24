package org.velas.manhunt.listeners.player.barriers;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.velas.manhunt.Color;
import org.velas.manhunt.Manhunt;
import org.velas.manhunt.Utils;
import org.velas.manhunt.config.Settings;
import org.velas.manhunt.game.ManhuntGame;
import org.velas.manhunt.player.ManhuntPlayer;

public class BlockBreak implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerBreaks(BlockBreakEvent event){

        Location loc = Settings.getConfigSettings().getLobbySpawn();
        if (loc != null){
            if (event.getBlock().getLocation().getWorld().getName().equals(loc.getWorld().getName()) && !event.getPlayer().hasPermission("manhunt.setup")){
                event.setCancelled(true);
                event.getPlayer().sendMessage(Color.YELLOW + ">> " + Color.RED + "You cannot break blocks here");
            }
        }

        if (!ManhuntGame.getCurrentGame().isRunning()) return;

        Player player = event.getPlayer();

        ManhuntPlayer manhuntPlayer = ManhuntGame.getCurrentGame().getGameManager().getPlayerManager().getManhuntPlayer(player);

        if (manhuntPlayer == null) {
            Utils.warning("ManhuntPlayer was null for player: " + player.getName());
            return;
        }

        manhuntPlayer.getHandlerBundle().worldInteractionHandler().onBlockBreak(event);
    }
}
