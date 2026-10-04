package org.velas.manhunt.game.scoreboard;

import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.config.Settings;
import org.velas.manhunt.game.ManhuntManager;
import org.velas.manhunt.player.ManhuntPlayer;
import org.velas.scoreboard.api.Scoreboard;
import org.velas.scoreboard.api.ScoreboardAPI;
import org.velas.scoreboard.api.team.Team;
import org.velas.scoreboard.api.team.TeamColor;

public class ScoreManager {

    private static Scoreboard scoreboard;
    @NotNull
    private final ManhuntManager manager;

    public ScoreManager(@NotNull ManhuntManager manager){
        this.manager = manager;
    }

    public static void init() {
        scoreboard = ScoreboardAPI.getHandler().createScoreboard();
    }

    public static @NotNull Scoreboard getScoreboard(){
        return scoreboard;
    }

    public void resetSideBar(){
        for(ManhuntPlayer manhuntPlayer: this.manager.getPlayerManager().getPlayers()){
            if(!manhuntPlayer.isOnline()) return;
            manhuntPlayer.getHandlerBundle().sidebarHandler().resetSidebar();
        }
    }

    public void updateSideBar(int time){
        for(ManhuntPlayer manhuntPlayer: this.manager.getPlayerManager().getPlayers()){
            if(!manhuntPlayer.isOnline()) continue;
            manhuntPlayer.getHandlerBundle().sidebarHandler().updateSidebar(time);
        }
    }

    public static void setUpTeams(){
        String hunterTeamName = Settings.HUNTER_TEAM_NAME;
        String runnerTeamName = Settings.RUNNER_TEAM_NAME;
        String spectatorTeamName = Settings.SPECTATOR_TEAM_NAME;

        //Hunters
        Team teamHunter = scoreboard.team(hunterTeamName);
        teamHunter.setPrefix(Settings.getConfigSettings().getHUNTER_TEAM_PREFIX());
        teamHunter.setColor(TeamColor.RED);
        teamHunter.setFriendlyFire(true);
        teamHunter.setCanSeeFriendlyInvisibilities(true);

        //Runners
        Team teamRunner = scoreboard.team(runnerTeamName);
        teamRunner.setPrefix(Settings.getConfigSettings().getRUNNER_TEAM_PREFIX());
        teamRunner.setColor(TeamColor.GREEN);
        teamRunner.setFriendlyFire(true);
        teamRunner.setCanSeeFriendlyInvisibilities(true);

        //Spectators
        Team teamSpectator = scoreboard.team(spectatorTeamName);
        teamSpectator.setPrefix(Settings.getConfigSettings().getSPECTATOR_TEAM_PREFIX());
        teamSpectator.setColor(TeamColor.GRAY);
        teamSpectator.setFriendlyFire(false);
        teamSpectator.setCanSeeFriendlyInvisibilities(true);
    }
}