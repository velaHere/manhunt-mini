package org.velas.manhunt.player.handler.lifecycle;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.Color;
import org.velas.manhunt.config.Settings;
import org.velas.manhunt.Utils;
import org.velas.manhunt.avatar.AvatarType;
import org.velas.manhunt.game.ManhuntGame;
import org.velas.manhunt.game.ManhuntManager;
import org.velas.manhunt.game.PlayerManager;
import org.velas.manhunt.game.scoreboard.ScoreManager;
import org.velas.manhunt.player.ManhuntPlayer;
import org.velas.manhunt.player.Runner;
import org.velas.manhunt.player.handler.LifecycleHandler;
import org.velas.manhunt.tracker.Tracker;
import org.velas.scoreboard.api.Scoreboard;
import org.velas.scoreboard.api.team.Team;

public abstract class BaseRunnerLifecycleHandler extends LifecycleHandler {

    protected final @NotNull Runner runner;

    public BaseRunnerLifecycleHandler(@NotNull Runner runner) {
        super(runner);
        this.runner=runner;
    }

    @Override
    protected void matchStart() {
        Player player = runner.getPlayer();
        if(player==null)return;

        World world = player.getWorld();
        if(!world.getName().equals(Settings.OVERWORLD_NAME))
            player.teleport(
                    runner.getManhuntGame()
                            .getGameManager()
                            .getWorldManager()
                            .getOverworld().getSpawnLocation()
            );

        player.setFlying(false);
        player.setAllowFlight(false);
        player.sendMessage(Color.GREEN + "You’ve got a head start. Run, runner, run!");

        this.matchStartEvent();
    }

    @Override
    protected void matchStop() {
        this.matchStopEvent();
    }

    @Override
    protected void playerJoinServer() {
        Player player = runner.getPlayer();
        if(player==null)return;

        Tracker tracker = runner.getManhuntGame().getGameManager().getTrackerManager().getTracker();
        if(!tracker.isRunning()) tracker.start();

        this.playerJoinServerEvent();
    }

    @Override
    protected void playerQuitServer() {
        ManhuntManager gameManager = runner.getManhuntGame().getGameManager();
        boolean anyOnline = gameManager.getPlayerManager()
                .getRunners()
                .stream()
                .anyMatch(Runner::isOnline);

        if (!anyOnline) {
            gameManager.getTrackerManager().getTracker().stop();
        }

        this.playerQuitServerEvent();
    }

    @Override
    protected void playerDeath(PlayerDeathEvent event) {

        ManhuntGame currentGame = ManhuntGame.getCurrentGame();
        ManhuntManager manager = currentGame.getGameManager();
        PlayerManager playerManager = manager.getPlayerManager();
        Player player = event.getEntity();


        for (Player player1: Bukkit.getOnlinePlayers()){
            player1.playSound(player1.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 1);
        }

        playerManager.reduceRunners();

        if (playerManager.getNoOfRunnersLeft() == 0){
            currentGame.stop();
            Utils.sendMsgToAllOnlinePlayers("§aCongratulations to Hunters, All the runners were taken down");
            Utils.announceMatchStop("§cAll Runners have fallen");
            return;
        }

        Scoreboard mainScoreBoard = ScoreManager.getScoreboard();
        Team team = mainScoreBoard.getTeam(Settings.SPECTATOR_TEAM_NAME);
        if(team!=null) team.addPlayer(player.getUniqueId());

        playerManager.removeRunner(runner);

        manager.getTrackerManager().resetTargetNoForAll(); // only for hunters

        manager.getPlayerManager().addPlayer(
                ManhuntPlayer.builder()
                        .player(player.getUniqueId())
                        .game(manager.getGame())
                        .avatarType(AvatarType.DEFAULT)
                        .buildSpectator()
        );
    }

    @Override
    protected void playerRespawn() {
        this.playerRespawnEvent();
    }

    protected abstract void matchStartEvent();
    protected abstract void matchStopEvent();
    protected abstract void playerJoinServerEvent();
    protected abstract void playerQuitServerEvent();
    protected abstract void playerRespawnEvent();
}
