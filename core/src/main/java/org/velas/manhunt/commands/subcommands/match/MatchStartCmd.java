package org.velas.manhunt.commands.subcommands.match;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.generator.structure.Structure;
import org.velas.manhunt.Color;
import org.velas.manhunt.commands.CustomSubCommand;
import org.velas.manhunt.config.Settings;
import org.velas.manhunt.game.ManhuntGame;

public class MatchStartCmd extends CustomSubCommand {

    public MatchStartCmd(String name, String alias, String permissionName) {
        super(name, alias, permissionName);
    }

    @Override
    protected boolean runPlayerCommand(Player player, String[] args) {

        if (args.length != 3 && args.length != 4){
            player.sendMessage(" ");
            player.sendMessage(Color.RED + "Your command syntax is incorrect");
            sendUsage(player);
            return true;
        }

        if (ManhuntGame.getCurrentGame().isRunning()){
            player.sendMessage(Color.RED + "You cannot start a match again");
            return true;
        }

        Location lobby = Settings.getConfigSettings().getLobbySpawn();

        if (lobby == null){
            player.sendMessage(Color.RED + "Please set manhunt lobby first");
            return true;
        }

        if (!ManhuntGame.canStartGame()){
            player.sendMessage(Color.RED + "There should be at least 1 player in the runners and hunters team");
            return true;
        }

        if(args.length==3){
            switch (args[2]) {
                case "RANDOM" -> ManhuntGame.createNewGame();
                case "PLAINS" -> ManhuntGame.createNewGame(Structure.VILLAGE_PLAINS);
                case "DESERT" -> ManhuntGame.createNewGame(Structure.VILLAGE_DESERT);
                case "SAVANNA" -> ManhuntGame.createNewGame(Structure.VILLAGE_SAVANNA);
                case "SNOWY" -> ManhuntGame.createNewGame(Structure.VILLAGE_SNOWY);
                case "TAIGA" -> ManhuntGame.createNewGame(Structure.VILLAGE_TAIGA);
                default -> {
                    player.sendMessage(ChatColor.RED + "Invalid Village Type");
                    return true;
                }
            }
        } else {
            int l;
            try {
                l = Integer.parseInt(args[3]);
                if (l < 0){
                    player.sendMessage(Color.RED + "Time cannot be negative");
                    return true;
                }
            }catch (Exception e){
                player.sendMessage(Color.RED + "Invalid Head Start Time Given");
                sendUsage(player);
                return true;
            }
            switch (args[2]) {
                case "RANDOM" -> ManhuntGame.createNewGame(l*20);
                case "PLAINS" -> ManhuntGame.createNewGame(l*20, Structure.VILLAGE_PLAINS);
                case "DESERT" -> ManhuntGame.createNewGame(l*20, Structure.VILLAGE_DESERT);
                case "SAVANNA" -> ManhuntGame.createNewGame(l*20, Structure.VILLAGE_SAVANNA);
                case "SNOWY" -> ManhuntGame.createNewGame(l*20, Structure.VILLAGE_SNOWY);
                case "TAIGA" -> ManhuntGame.createNewGame(l*20, Structure.VILLAGE_TAIGA);
                default -> {
                    player.sendMessage(ChatColor.RED + "Invalid Village Type");
                    return true;
                }
            }
        }

        Bukkit.getOnlinePlayers().forEach(p ->
                player.sendTitle("", Color.AQUA + "You will be teleported to the MANHUNT in a while", 20, 2*20, 20)
        );
        ManhuntGame.getCurrentGame().start();
        player.sendMessage(Color.GREEN + "Setting up Manhunt Match");
        return true;
    }

    @Override
    public void sendUsage(Player player) {
        player.sendMessage(Color.AQUA + "Usage: /manhunt match start <MINI/NORMAL>");
        player.sendMessage(Color.AQUA + "Usage: /manhunt match start <MINI/NORMAL> time(in SEC)");
    }
}
