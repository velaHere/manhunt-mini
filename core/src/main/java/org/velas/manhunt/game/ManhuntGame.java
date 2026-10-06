package org.velas.manhunt.game;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.generator.structure.Structure;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.velas.manhunt.config.Settings;
import org.velas.manhunt.event.ManhuntMatchStopEvent;
import org.velas.manhunt.game.scoreboard.ScoreManager;
import org.velas.scoreboard.api.Scoreboard;
import org.velas.scoreboard.api.team.Team;


public final class ManhuntGame {

    private static ManhuntGame game;
    private ManhuntManager gameManager;
    private final long headStartTime;
    private GameState gameState;

    private ManhuntGame(){
        this.gameState=GameState.LOBBY;
        this.headStartTime= Settings.config().game().getHeadStart()*20L;
    }

    private ManhuntGame(int headStartTime){
        this.gameState=GameState.LOBBY;
        this.headStartTime=headStartTime*20L;
    }

    private void init(@Nullable Structure village, @Nullable Long seed){
        this.gameManager=new ManhuntManager(this, village, seed);
        gameManager.init();
    }

    public static boolean canStartGame(){
        Scoreboard scoreboard = ScoreManager.getScoreboard();
        Team hunters = scoreboard.getTeam(Settings.HUNTER_TEAM_NAME);
        Team runners = scoreboard.getTeam(Settings.RUNNER_TEAM_NAME);
        if(hunters == null) return false;
        if(runners == null) return false;

        return !hunters.getPlayers().isEmpty() && !runners.getPlayers().isEmpty();
    }

    public static void createNewGame(@Nullable Integer headStartTime, @Nullable Structure village, @Nullable Long seed) {
        if(game!=null)
            HandlerList.unregisterAll(game.gameManager.getAbilityDispatcher());
        if(headStartTime == null) game=new ManhuntGame();
        else game = new ManhuntGame(headStartTime);
        game.init(village, seed);
    }

    @NotNull
    public static ManhuntGame getCurrentGame(){
        return game;
    }

    @NotNull
    public ManhuntManager getGameManager(){
        return this.gameManager;
    }

    public long getHeadStartTime(){
        return this.headStartTime;
    }

    public int getHeadStartTimeInSec(){
        return (int)this.headStartTime/20;
    }

    public boolean isRunning(){
        return this.gameState.equals(GameState.STARTING) ||
                this.gameState.equals(GameState.HEAD_START) ||
                this.gameState.equals(GameState.PLAYING) ||
                this.gameState.equals(GameState.VILLAGE) ||
                this.gameState.equals(GameState.TRANSITION) ||
                this.gameState.equals(GameState.STRONGHOLD);
    }

    public boolean isStarting(){
        return this.gameState.equals(GameState.STARTING);
    }

    public void start(){
        this.gameManager.onGameStart();
    }

    public void stop(){
        Bukkit.getPluginManager().callEvent(new ManhuntMatchStopEvent(this));
        this.gameManager.onGameStop();
    }

    @NotNull
    public GameState getGameState(){
        return this.gameState;
    }

    public void setGameState(@NotNull GameState gameState) {
        this.gameManager.getScoreboardManager().resetSideBar();
        this.gameState = gameState;
    }
}