package org.velas.manhunt.ability;

import org.velas.manhunt.game.GameState;
import org.velas.manhunt.game.ManhuntGame;
import org.velas.manhunt.player.ManhuntPlayer;

public final class AbilityGate {

    private AbilityGate() {}

    public static boolean canUseAbilities(ManhuntPlayer player) {
        if (player == null) return false;
        ManhuntGame game = player.getManhuntGame();
        GameState state = game.getGameState();

        if (state == GameState.TRANSITION ||
                state == GameState.ENDED ||
                state == GameState.LOBBY ||
                state == GameState.STARTING) {
            return false;
        }

        return !player.isHunter() || state != GameState.HEAD_START;
    }
}
