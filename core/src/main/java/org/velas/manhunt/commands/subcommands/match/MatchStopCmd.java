package org.velas.manhunt.commands.subcommands.match;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.velas.manhunt.Color;
import org.velas.manhunt.commands.CustomSubCommand;
import org.velas.manhunt.game.ManhuntGame;

public class MatchStopCmd extends CustomSubCommand {

    public MatchStopCmd(String name, String alias, String permissionName) {
        super(name, alias, permissionName);
    }

    @Override
    protected boolean runPlayerCommand(Player player, String[] args) {

        if (args.length != 2){
            player.sendMessage(" ");
            player.sendMessage(Color.RED + "Your command syntax is incorrect");
            sendUsage(player);
            return true;
        }

        if (!ManhuntGame.getCurrentGame().isRunning()){
            player.sendMessage(Color.RED + "There is no match to stop");
            return true;
        }

        ManhuntGame.getCurrentGame().stop();

        Bukkit.getOnlinePlayers().forEach(p ->
                player.sendTitle("", Color.RED + "Match Terminated", 20, 2*20, 20)
        );

        return true;
    }

    @Override
    public void sendUsage(Player player) {
        player.sendMessage(Color.AQUA + "Usage: /manhunt match stop");
    }

}
