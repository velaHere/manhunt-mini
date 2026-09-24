package org.velas.manhunt.game.timeline.event;

import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.game.BroadcastTimer;
import org.velas.manhunt.game.ManhuntGame;

public final class BaseMatchStartBroadcastEvent extends RangeEvent {

    public BaseMatchStartBroadcastEvent(@NotNull ManhuntGame game) {
        super(game, 40, 50);
    }

    @Override
    public void execute() {
        int currentTime = this.getCurrentTime();
        BroadcastTimer.broadcast(currentTime - 40);
    }
}
