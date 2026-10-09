package org.velas.manhunt.listeners.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.velas.manhunt.config.Settings;
import org.velas.manhunt.Utils;
import org.velas.manhunt.avatar.AvatarType;
import org.velas.manhunt.customItems.CustomItem;
import org.velas.manhunt.customItems.CustomItemType;
import org.velas.manhunt.customItems.CustomRecipe;
import org.velas.manhunt.game.GameFlowManager;
import org.velas.manhunt.game.ManhuntGame;
import org.velas.manhunt.game.PlayerManager;
import org.velas.manhunt.game.scoreboard.ScoreManager;
import org.velas.manhunt.player.ManhuntPlayer;
import org.velas.scoreboard.api.Scoreboard;
import org.velas.scoreboard.api.team.Team;

import java.util.Optional;

public class PlayerJoin implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event){

        Player player = event.getPlayer();

        player.setResourcePack(
                "https://github.com/velaHere/ManhuntPack/releases/download/v1/Manhunt.zip",
                hexToBytes("5ae45221da9fd625020cd52c80c6379d54f0684c")
        );

        for (String id : CustomRecipe.getRecipeIdList()) {
            Optional<CustomItem> customItem = CustomItemType.getCustomItem(id);
            if(customItem.isEmpty()) continue;
            player.undiscoverRecipe(customItem.get().getKey());
            player.discoverRecipe(customItem.get().getKey());
        }

        ScoreManager.getScoreboard().addPlayer(player);

        String playerName = player.getName();

        event.setJoinMessage("[§a+§f] " + playerName);

        String header = "§e-----------------------------------\n\n§5§kV§6§lManhunt§5§kV\n";  // --> 35
        String footer = "\n§bSome §aRUNNERS§b. The rest? §cHUNTERS§b.\n\n§e-----------------------------------";

        player.setPlayerListHeaderFooter(header, footer);
        Utils.playerChatInfo.putIfAbsent(playerName, true);

        if (!ManhuntGame.getCurrentGame().isRunning()){
            GameFlowManager.teleportPlayerToLobby(player);
            return;
        }

        PlayerManager playerManager = ManhuntGame.getCurrentGame().getGameManager().getPlayerManager();
        ManhuntPlayer manhuntPlayer = playerManager.getManhuntPlayer(player);

        if (manhuntPlayer == null) {
            Scoreboard scoreboard = ScoreManager.getScoreboard();
            scoreboard.addPlayer(player);
            Team team = scoreboard.getTeam(Settings.SPECTATOR_TEAM_NAME);
            if(team!=null) team.addPlayer(player);
            manhuntPlayer = ManhuntPlayer.builder()
                    .player(player.getUniqueId()).game(ManhuntGame.getCurrentGame()).avatarType(AvatarType.DEFAULT).buildSpectator();
            playerManager.addPlayer(manhuntPlayer);
        }

        manhuntPlayer.getHandlerBundle().lifecycleHandler().onPlayerJoinServer();
    }

    private static byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) (
                    (Character.digit(hex.charAt(i), 16) << 4)
                            + Character.digit(hex.charAt(i + 1), 16)
            );
        }
        return data;
    }
}