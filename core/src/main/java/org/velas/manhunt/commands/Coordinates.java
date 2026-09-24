package org.velas.manhunt.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.velas.manhunt.game.scoreboard.ScoreManager;
import org.velas.scoreboard.api.team.Team;

import java.awt.*;
import java.util.UUID;

public class Coordinates extends CustomCommand {

    public Coordinates(String name, String alias, String permissionName) {
        super(name, alias, permissionName);
    }

    @Override
    protected boolean runPlayerCommand(Player player, String[] args) {

        if (args.length > 1){
            player.sendMessage(" ");
            player.sendMessage(ChatColor.RED + "Your command syntax is incorrect");
            sendUsage(player);
            return true;
        }

        if(args.length==0){

            Team playerTeam = ScoreManager.getScoreboard().getTeamOf(player.getUniqueId());
            if (playerTeam == null){
                player.sendMessage(ChatColor.RED + "You are not in a team");
                return true;
            }

            sendToTeam(player, playerTeam);
            return true;
        }

        Player toPlayer = Bukkit.getPlayer(args[0]);
        if(toPlayer==null){
            player.sendMessage(ChatColor.RED+"This player either doesn't exists or not online");
            return true;
        }

        sendToPlayer(player, toPlayer);
        return true;
    }

    @Override
    public void sendUsage(Player player) {
        player.sendMessage(ChatColor.AQUA + "Usage: /cords or /cords <playerName>");
    }

    private void sendToTeam(Player player, Team playerTeam){

        Location location = player.getLocation();

        for (UUID targetUUID: playerTeam.getPlayers()){

            Player player1 = Bukkit.getPlayer(targetUUID);
            if (player1 != null && player1.isOnline()){

                player1.sendMessage(ChatColor.GOLD + "[Team] " + ChatColor.YELLOW + player.getName() + ":");

                String indent = "      "; // spaces to align under the name

                if(location.getWorld()==null) return;
                String rawName = location.getWorld().getName();
                String worldName = rawName.contains("/")
                        ? rawName.substring(rawName.lastIndexOf("/") + 1)
                        : rawName;

                player1.sendMessage(
                        ChatColor.GRAY + indent + "➤ " +
                                ChatColor.AQUA + "World: " +
                                ChatColor.WHITE + worldName
                );

                player1.sendMessage(
                        ChatColor.GRAY + indent + "➤ " +
                                ChatColor.AQUA + "X: " + ChatColor.WHITE + Math.round(location.getX()) + "  " +
                                ChatColor.AQUA + "Y: " + ChatColor.WHITE + Math.round(location.getY()) + "  " +
                                ChatColor.AQUA + "Z: " + ChatColor.WHITE + Math.round(location.getZ())
                );
            }
        }
    }

    private void sendToPlayer(Player player, Player toPlayer){

        player.sendMessage(ChatColor.GREEN + "Sent your location to " + toPlayer.getName());

        Location location = player.getLocation();

        String message = net.md_5.bungee.api.ChatColor.of(new Color(255, 105, 0))+"[Whisper] "+
                net.md_5.bungee.api.ChatColor.of(new Color(255, 159, 0))+player.getName()+ ChatColor.GRAY +":";
        toPlayer.sendMessage(message);

        String indent = "         ";

        if(location.getWorld()==null) return;
        String rawName = location.getWorld().getName();
        String worldName = rawName.contains("/")
                ? rawName.substring(rawName.lastIndexOf("/") + 1)
                : rawName;

        String message2 = indent+ChatColor.GRAY+"➤ "+ChatColor.AQUA+"World: "+ChatColor.WHITE+worldName;
        toPlayer.sendMessage(message2);

        String message3 = indent+ChatColor.GRAY+"➤ "+
                ChatColor.AQUA+"X: "+ChatColor.WHITE+(int) location.getX() + "  "+
                ChatColor.AQUA+"Y: "+ChatColor.WHITE+(int) location.getY() + "  "+
                ChatColor.AQUA+"Z: "+ChatColor.WHITE+(int) location.getZ();

        toPlayer.sendMessage(message3);
    }
}
