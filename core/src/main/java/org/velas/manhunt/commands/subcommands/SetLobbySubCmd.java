package org.velas.manhunt.commands.subcommands;

import org.bukkit.entity.Player;
import org.velas.manhunt.Color;
import org.velas.manhunt.commands.CustomSubCommand;
import org.velas.manhunt.config.Settings;

public class SetLobbySubCmd extends CustomSubCommand {

    public SetLobbySubCmd(String name, String alias, String permissionName) {
        super(name, alias, permissionName);
    }

    @Override
    protected boolean runPlayerCommand(Player player, String[] args) {

        if(args.length != 1){
            player.sendMessage(" ");
            player.sendMessage(Color.RED + "Your command syntax is incorrect");
            sendUsage(player);
            return true;
        }

        if (!player.getWorld().getName().equals(Settings.getConfigSettings().getLOBBY_WORLD_NAME())){
            player.sendMessage(Color.RED + "Lobby can only be set in the world with name -lobby-");
            return true;
        }

        Settings.getConfigSettings().saveLobbySpawn(player.getLocation());
        player.sendMessage(Color.GREEN + "Lobby have been set to your current location");
        return true;
    }

    @Override
    public void sendUsage(Player player) {
        player.sendMessage("Usage: /manhunt setlobby");
    }
}
