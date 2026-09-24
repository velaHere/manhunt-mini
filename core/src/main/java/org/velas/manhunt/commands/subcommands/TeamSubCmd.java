package org.velas.manhunt.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.velas.manhunt.Color;
import org.velas.manhunt.Manhunt;
import org.velas.manhunt.commands.CustomSubCommand;
import org.velas.manhunt.game.scoreboard.ScoreManager;
import org.velas.manhunt.menusystem.menu.TeamMenu;
import org.velas.scoreboard.api.team.Team;

import java.util.UUID;

public class TeamSubCmd extends CustomSubCommand {

    public TeamSubCmd(String name, String alias, String permissionName) {
        super(name, alias, permissionName);
    }

    @Override
    protected boolean runPlayerCommand(Player player, String[] args) {

        if (args.length == 1){
            new TeamMenu(Manhunt.getPlayerMenuUtility(player)).open();
            return true;
        }

        if (args.length != 4) {
            player.sendMessage(" ");
            player.sendMessage(Color.RED + "Your command syntax is incorrect");
            sendUsage(player);
            return true;
        }

        String teamName = args[1];
        String action = args[2];
        String targetName = args[3];

        Team team = ScoreManager.getScoreboard().getTeam(teamName);

        if (team == null){
            player.sendMessage(Color.RED + "This team does not exist.");
            return true;
        }

        Player targetPlayer = Bukkit.getPlayer(targetName);

        if (targetPlayer == null){
            player.sendMessage(Color.RED + "That player either doesn't exist or isn't online.");
            return true;
        }

        UUID targetPlayerUniqueId = targetPlayer.getUniqueId();

        switch (action){
            case "add"->{
                if (team.hasPlayer(targetPlayerUniqueId)){
                    player.sendMessage(Color.RED + "This players is already in " + teamName + " team");
                    return true;
                }
                team.addPlayer(targetPlayerUniqueId);
                player.sendMessage(Color.GREEN + "Successfully added " + targetName + " to " + teamName);
            }
            case "remove"->{
                if (!team.hasPlayer(targetPlayerUniqueId)){
                    player.sendMessage(Color.RED + targetName + " was not found in " + teamName);
                    return true;
                }
                team.removePlayer(targetPlayerUniqueId);
                player.sendMessage(Color.GREEN + "Successfully removed " + targetName + " from " + teamName);
            }
            default -> {
                player.sendMessage(" ");
                player.sendMessage(Color.RED + "Your command syntax is incorrect");
                sendUsage(player);
            }
        }
        return true;
    }

    @Override
    public void sendUsage(Player player) {
        player.sendMessage(Color.AQUA + "Usage: /manhunt teams <teamName> <add/remove> <playerName>");
    }
}
