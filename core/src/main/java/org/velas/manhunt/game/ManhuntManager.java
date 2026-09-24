package org.velas.manhunt.game;


import org.bukkit.generator.structure.Structure;
import org.velas.manhunt.config.Settings;
import org.velas.manhunt.ability.AbilityDispatcher;
import org.velas.manhunt.dropSystem.DropManager;
import org.velas.manhunt.game.scoreboard.ScoreManager;
import org.velas.manhunt.game.timeline.GameTimeLine;
import org.velas.manhunt.tracker.TrackerManager;
import org.velas.scoreboard.api.team.Team;

import java.util.UUID;

public class ManhuntManager{

    private final ManhuntGame game;
    private final WorldManager worldManager;
    private final PlayerManager playerManager;
    private final TrackerManager trackerManager;
    private final ScoreManager scoreboardManager;
    private final GameFlowManager gameFlowManager;
    private final DropManager dropManager;
    private final AbilityDispatcher abilityDispatcher;
    private final GameTimeLine gameTimeLine;

    public ManhuntManager(ManhuntGame game, Structure village){
        this.game=game;
        this.gameFlowManager=new GameFlowManager(this);
        this.playerManager=new PlayerManager(this);
        this.trackerManager=this.playerManager.getTrackerManager();
        this.worldManager=new WorldManager(village);
        this.scoreboardManager=new ScoreManager(this);
        this.dropManager=new DropManager(this);
        this.abilityDispatcher = new AbilityDispatcher(this);
        this.gameTimeLine=new GameTimeLine(this);
    }

    public void init(){
        gameTimeLine.init();
        abilityDispatcher.init();
    }

    public AbilityDispatcher getAbilityDispatcher() {
        return abilityDispatcher;
    }

    public void onGameStart(){
        this.gameTimeLine.start();
    }

    public void onGameStop(){

        this.game.setGameState(org.velas.manhunt.game.GameState.ENDED);
        this.gameTimeLine.stop();
        this.trackerManager.getTracker().stop();

        playerManager.getPlayers().forEach(manhuntPlayer ->
                manhuntPlayer.getHandlerBundle().lifecycleHandler().onMatchStop());

        Team hunterTeam = ScoreManager.getScoreboard().getTeam(Settings.HUNTER_TEAM_NAME);
        if(hunterTeam!=null) for(UUID playerUUID: hunterTeam.getPlayers())
            hunterTeam.removePlayer(playerUUID);
        Team runnerTeam = ScoreManager.getScoreboard().getTeam(Settings.RUNNER_TEAM_NAME);
        if(runnerTeam!=null) for(UUID playerUUID: runnerTeam.getPlayers())
            runnerTeam.removePlayer(playerUUID);
        Team spectatorTeam = ScoreManager.getScoreboard().getTeam(Settings.SPECTATOR_TEAM_NAME);
        if(spectatorTeam!=null) for(UUID playerUUID: spectatorTeam.getPlayers())
            spectatorTeam.removePlayer(playerUUID);
    }

    public ManhuntGame getGame(){
        return this.game;
    }

    public TrackerManager getTrackerManager() {
        return trackerManager;
    }

    public GameFlowManager getGameFlowManager() {
        return gameFlowManager;
    }

    public ScoreManager getScoreboardManager(){
        return this.scoreboardManager;
    }

    public GameTimeLine getGameTimeLine(){
        return this.gameTimeLine;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public WorldManager getWorldManager(){
        return this.worldManager;
    }

    public DropManager getDropManager() {
        return dropManager;
    }
}