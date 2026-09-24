package org.velas.manhunt.commands;

import org.bukkit.entity.Player;
import org.velas.manhunt.Color;
import org.velas.manhunt.Utils;
import org.velas.manhunt.game.scoreboard.ScoreManager;
import org.velas.scoreboard.api.team.Team;

public class CrewMsgCmd extends CustomCommand {


    public CrewMsgCmd(String name, String alias, String permissionName) {
        super(name, alias, permissionName);
    }

    @Override
    protected boolean runPlayerCommand(Player player, String[] args) {

        if (args.length != 0){
            if(args.length != 1){
                player.sendMessage(" ");
                player.sendMessage(Color.RED + "Your command syntax is incorrect");
                sendUsage(player);
                return true;
            }
        }

        Team currentTeam = ScoreManager.getScoreboard().getTeamOf(player.getUniqueId());

        if (currentTeam == null){
            player.sendMessage(Color.RED + "You are not in a team");
            return true;
        }

        String playerName = player.getName();

        Boolean chat = Utils.playerChatInfo.get(playerName);

        if (chat == null){
            Utils.warning("Unable to find " + playerName + "'s data in ChatInfo");
            Utils.playerChatInfo.put(playerName, true);
            return true;
        }

        Utils.playerChatInfo.replace(playerName, Utils.playerChatInfo.get(playerName), !Utils.playerChatInfo.get(playerName));

        return true;
    }

    @Override
    public void sendUsage(Player player) {
        player.sendMessage(Color.AQUA + "Usage: /crewmsg or /cm");
    }
}
