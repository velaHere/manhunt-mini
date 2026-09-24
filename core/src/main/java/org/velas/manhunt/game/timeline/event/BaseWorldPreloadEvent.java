package org.velas.manhunt.game.timeline.event;

import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.game.ManhuntGame;

public abstract class BaseWorldPreloadEvent extends TimelineEvent {

    protected BaseWorldPreloadEvent(@NotNull ManhuntGame game) {
        super(game, 15);
    }

    @Override
    public void execute() {
        this.exe();
        this.game.getGameManager().getWorldManager().preloadAsync(1);
    }

    protected abstract void exe();
}
