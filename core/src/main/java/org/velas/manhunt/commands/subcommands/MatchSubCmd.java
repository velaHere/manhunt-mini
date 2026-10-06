package org.velas.manhunt.commands.subcommands;

import org.bukkit.entity.Player;
import org.velas.manhunt.Color;
import org.velas.manhunt.commands.subcommands.match.MatchStartPreviousCmd;
import org.velas.manhunt.config.Settings;
import org.velas.manhunt.commands.CustomSubCommand;
import org.velas.manhunt.commands.subcommands.match.MatchStartCmd;
import org.velas.manhunt.commands.subcommands.match.MatchStopCmd;

public class MatchSubCmd extends CustomSubCommand {

    public MatchSubCmd(String name, String alias, String permissionName) {
        super(name, alias, permissionName);
        this.subCommands.add(new MatchStartCmd("start", null, Settings.SETUP_PERMISSION));
        this.subCommands.add(new MatchStopCmd("stop", null, Settings.SETUP_PERMISSION));
        this.subCommands.add(new MatchStartPreviousCmd("start_previous", null, Settings.SETUP_PERMISSION));
    }

    @Override
    protected boolean runPlayerCommand(Player player, String[] args) {

        if(args.length != 1){
            player.sendMessage(" ");
            player.sendMessage(Color.RED + "This command doesn't exists");
        }
        sendUsage(player);
        return true;
    }

    @Override
    public void sendUsage(Player player) {
        player.sendMessage(Color.AQUA + "Usage: /manhunt match start <time(in SEC)> <seed>");
        player.sendMessage(Color.AQUA + "Usage: /manhunt match <stop/start_previous>");
    }
}
