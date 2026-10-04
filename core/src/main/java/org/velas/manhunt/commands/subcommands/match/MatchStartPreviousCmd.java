package org.velas.manhunt.commands.subcommands.match;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.velas.manhunt.Color;
import org.velas.manhunt.commands.CustomSubCommand;
import org.velas.manhunt.config.Settings;
import org.velas.manhunt.game.ManhuntGame;
import org.velas.manhunt.game.PreviousGameState;
import org.velas.manhunt.game.scoreboard.ScoreManager;
import org.velas.scoreboard.api.team.Team;

public class MatchStartPreviousCmd extends CustomSubCommand {

    public MatchStartPreviousCmd(String name, String alias, String permissionName) {
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

        if (ManhuntGame.getCurrentGame().isRunning()){
            player.sendMessage(Color.RED + "You cannot start a match again");
            return true;
        }

        Location lobby = Settings.getConfigSettings().getLobbySpawn();

        if (lobby == null){
            player.sendMessage(Color.RED + "Please set manhunt lobby first");
            return true;
        }

        PreviousGameState.getPlayerTeams().forEach(
                (uuid, team) -> {
                    Player player1 = Bukkit.getPlayer(uuid);
                    if(player1==null) return;
                    Team team1 = ScoreManager.getScoreboard().getTeam(team.toString());
                    if(team1 == null) return;
                    team1.addPlayer(uuid);
                }
        );

        if (!ManhuntGame.canStartGame()){
            player.sendMessage(Color.RED + "There should be at least 1 player in the runners and hunters team");
            return true;
        }

        ManhuntGame.createNewGame(
                PreviousGameState.headStart(),
                PreviousGameState.village(),
                PreviousGameState.seed()
        );

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
        player.sendMessage(Color.AQUA + "Usage: /manhunt match start_previous");
    }
}
