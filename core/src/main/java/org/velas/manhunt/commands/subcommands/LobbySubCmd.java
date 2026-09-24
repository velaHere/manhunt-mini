package org.velas.manhunt.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.velas.manhunt.Color;
import org.velas.manhunt.commands.CustomSubCommand;
import org.velas.manhunt.config.Settings;
import org.velas.manhunt.game.GameFlowManager;
import org.velas.manhunt.game.ManhuntGame;

public class LobbySubCmd extends CustomSubCommand {

    public LobbySubCmd(String name, String alias, String permissionName) {
        super(name, alias, permissionName);
    }

    @Override
    protected boolean runPlayerCommand(Player player, String[] args) {

        if(args.length > 2){
            player.sendMessage(" ");
            player.sendMessage(Color.RED + "Your command syntax is incorrect");
            sendUsage(player);
            return true;
        }

        if(ManhuntGame.getCurrentGame().isRunning()){
         player.sendMessage(Color.YELLOW+"You cannot use use this command while match is going on!");
         return true;
        }

        if(args.length == 1) {
            GameFlowManager.teleportPlayerToLobby(player);
        } else {

            if(!player.hasPermission(Settings.SETUP_PERMISSION)) {
                sendUsage(player);
                return true;
            }

            if(args[1].equals("@a"))
                Bukkit.getOnlinePlayers().forEach(GameFlowManager::teleportPlayerToLobby);
            else {
                String playerName = args[1];
                Player targetPlayer = Bukkit.getPlayer(playerName);
                if(targetPlayer == null) {
                    player.sendMessage( ChatColor.RED + "Player is currently unavailable!");
                    return true;
                }
                GameFlowManager.teleportPlayerToLobby(targetPlayer);
            }
        }
        return true;
    }

    @Override
    public void sendUsage(Player player) {
        player.sendMessage(Color.AQUA + "Usage: /manhunt lobby");
    }
}
