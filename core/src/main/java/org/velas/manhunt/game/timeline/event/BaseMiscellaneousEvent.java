package org.velas.manhunt.game.timeline.event;

import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.game.ManhuntGame;

public abstract class BaseMiscellaneousEvent extends RangeEvent {
    protected BaseMiscellaneousEvent(@NotNull ManhuntGame game) {
        super(game, 0, Integer.MAX_VALUE);
    }
}
