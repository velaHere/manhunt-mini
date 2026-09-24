package org.velas.manhunt.game.timeline.event;

import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.game.ManhuntGame;

public final class BaseWorldGenerationEvent extends TimelineEvent {

    public BaseWorldGenerationEvent(@NotNull ManhuntGame game) {
        super(game, 5);
    }

    @Override
    public void execute() {
        this.game.getGameManager().getWorldManager().generateManhuntWorlds();
    }
}
