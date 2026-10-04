package org.velas.manhunt.game;

import org.velas.manhunt.config.Settings;

public enum Team {
    HUNTER(Settings.HUNTER_TEAM_NAME), RUNNER(Settings.RUNNER_TEAM_NAME), SPECTATOR(Settings.SPECTATOR_TEAM_NAME);

    private final String teamName;

    Team(String teamName) {
        this.teamName = teamName;
    }

    @Override
    public String toString() {
        return teamName;
    }
}
