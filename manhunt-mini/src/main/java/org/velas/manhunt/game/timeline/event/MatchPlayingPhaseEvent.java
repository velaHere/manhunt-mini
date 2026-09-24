package org.velas.manhunt.game.timeline.event;

import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.game.GameState;
import org.velas.manhunt.game.ManhuntGame;

public final class MatchPlayingPhaseEvent extends BaseMatchPlayingPhaseEvent {

    public MatchPlayingPhaseEvent(@NotNull ManhuntGame game) {
        super(game);
    }

    @Override
    protected void exe() {
        this.game.setGameState(GameState.VILLAGE);
    }
}
