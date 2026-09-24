package org.velas.manhunt.commands;

import org.bukkit.entity.Player;
import org.velas.manhunt.Color;
import org.velas.manhunt.config.Settings;
import org.velas.manhunt.commands.subcommands.LobbySubCmd;
import org.velas.manhunt.commands.subcommands.MatchSubCmd;
import org.velas.manhunt.commands.subcommands.SetLobbySubCmd;
import org.velas.manhunt.commands.subcommands.TeamSubCmd;

public class ManhuntCmd extends CustomCommand{

    public ManhuntCmd(String name, String alias, String permissionName) {
        super(name, alias, permissionName);
        this.subCommands.add(new LobbySubCmd("lobby", null, null));
        this.subCommands.add(new SetLobbySubCmd("setlobby", null, Settings.SETUP_PERMISSION));
        this.subCommands.add(new TeamSubCmd("teams", null, Settings.SETUP_PERMISSION));
        this.subCommands.add(new MatchSubCmd("match", null, Settings.SETUP_PERMISSION));
    }

    @Override
    protected boolean runPlayerCommand(Player player, String[] args) {

        if(args.length != 0){
            player.sendMessage(" ");
            player.sendMessage(Color.RED + "This command doesn't exists");
        }
        sendUsage(player);
        return true;
    }

    @Override
    public void sendUsage(Player player) {
        if(!player.hasPermission("manhunt.setup")) {
            player.sendMessage(" ");
            player.sendMessage(Color.AQUA + "-------[Manhunt]-------");
            player.sendMessage(Color.AQUA + "/manhunt lobby");
        }else{
            player.sendMessage(" ");
            player.sendMessage(Color.AQUA + "-------[Manhunt]-------");
            player.sendMessage(Color.AQUA + "/manhunt lobby");
            player.sendMessage(Color.AQUA + "/manhunt setlobby");
            player.sendMessage(Color.AQUA + "/manhunt teams <teamName> <add/remove> <playerName>");
            player.sendMessage(Color.AQUA + "/manhunt match <start/stop> time(in SEC)");
        }
    }
}
