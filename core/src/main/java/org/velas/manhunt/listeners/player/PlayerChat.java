package org.velas.manhunt.listeners.player;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.velas.manhunt.Color;
import org.velas.manhunt.config.Settings;
import org.velas.manhunt.Utils;
import org.velas.manhunt.game.scoreboard.ScoreManager;
import org.velas.scoreboard.api.Scoreboard;
import org.velas.scoreboard.api.team.Team;

import java.util.UUID;

public class PlayerChat implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event){

        Scoreboard mainScoreboard = ScoreManager.getScoreboard();
        Player player = event.getPlayer();
        String playerName = player.getName();
        UUID playerUUID = player.getUniqueId();

        Team playerTeam = mainScoreboard.getTeamOf(playerUUID);

        if (playerTeam == null) return;

        Utils.playerChatInfo.putIfAbsent(playerName, true);

        if (Utils.playerChatInfo.get(playerName)){

            event.setCancelled(true);

            String message = ChatColor.GOLD+"[Team] "+ChatColor.YELLOW+playerName+": "
                    +ChatColor.WHITE+event.getMessage();

            for (UUID targetUUID: playerTeam.getPlayers()){

                Player target = Bukkit.getPlayer(targetUUID);
                if (target != null && target.isOnline()){
                    target.sendMessage(message);
                }
            }
            return;
        }

        if(playerTeam.getName().equals(Settings.SPECTATOR_TEAM_NAME)){
            player.sendMessage(Color.RED + "You can only send messages to your team - Spectator");
            event.setCancelled(true);
            return;
        }

        event.setFormat(playerTeam.getPrefix() + playerTeam.getChatColor() + playerName + ChatColor.GRAY + ": " + event.getMessage());
    }
}
