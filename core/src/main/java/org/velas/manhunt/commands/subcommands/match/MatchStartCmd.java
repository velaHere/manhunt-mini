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

        if (args.length != 5){
            player.sendMessage(" ");
            player.sendMessage(Color.RED + "Your command syntax is incorrect");
            sendUsage(player);
            return true;
        }

        if (ManhuntGame.getCurrentGame().isRunning()){
            player.sendMessage(Color.RED + "You cannot start a match again");
            return true;
        }

        Location lobby = Settings.config().game().world().getLobbySpawn();

        if (lobby == null){
            player.sendMessage(Color.RED + "Please set manhunt lobby first");
            return true;
        }

        if (!ManhuntGame.canStartGame()){
            player.sendMessage(Color.RED + "There should be at least 1 player in the runners and hunters team");
            return true;
        }

        Integer headStart = null;
        Long seed = null;

        if(!args[3].equalsIgnoreCase("default")) {
            try {
                headStart = Integer.parseInt(args[3]);
                if (headStart < 0){
                    player.sendMessage(Color.RED + "Time cannot be negative");
                    return true;
                }
            }catch (Exception e){
                player.sendMessage(Color.RED + "Invalid Head Start Time Given");
                sendUsage(player);
                return true;
            }
        }

        if(!args[4].equalsIgnoreCase("random")) {
            try {
                seed = Long.parseLong(args[4]);
                if (seed < 0){
                    player.sendMessage(Color.RED + "Seed cannot be negative");
                    return true;
                }
            }catch (Exception e){
                player.sendMessage(Color.RED + "Invalid Seed Given");
                sendUsage(player);
                return true;
            }
        }

        switch (args[2].toLowerCase()) {
            case "random" -> ManhuntGame.createNewGame(headStart, null, seed);
            case "plains" -> ManhuntGame.createNewGame(headStart, Structure.VILLAGE_PLAINS, seed);
            case "desert" -> ManhuntGame.createNewGame(headStart, Structure.VILLAGE_DESERT, seed);
            case "savanna" -> ManhuntGame.createNewGame(headStart, Structure.VILLAGE_SAVANNA, seed);
            case "snowy" -> ManhuntGame.createNewGame(headStart, Structure.VILLAGE_SNOWY, seed);
            case "taiga" -> ManhuntGame.createNewGame(headStart, Structure.VILLAGE_TAIGA, seed);
            default -> {
                player.sendMessage(ChatColor.RED + "Invalid Village Type");
                return true;
            }
        }

        Bukkit.getOnlinePlayers().forEach(p ->
                p.sendTitle(
                        "",
                        Color.GOLD +
                                "Starting" +
                                ChatColor.RED +
                                ChatColor.BOLD +
                                " MANHUNT " +
                                ChatColor.GOLD +
                                "in a while",
                        20,
                        3*20,
                        20
                )
        );
        ManhuntGame.getCurrentGame().start();
        player.sendMessage(Color.GREEN + "Setting up Manhunt Match");
        return true;
    }

    @Override
    public void sendUsage(Player player) {
        player.sendMessage(Color.AQUA + "Usage: /manhunt match start <village> <time(in SEC)> <seed>");
    }
}
