package org.velas.manhunt.game.timeline.event;

import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.game.GameState;
import org.velas.manhunt.game.ManhuntGame;

public class DropSummonEvent extends BaseDropSummonEvent {

    public DropSummonEvent(@NotNull ManhuntGame game) {
        super(game);
    }

    @Override
    protected boolean exe() {
        return this.game.getGameState().equals(GameState.TRANSITION);
    }
}
