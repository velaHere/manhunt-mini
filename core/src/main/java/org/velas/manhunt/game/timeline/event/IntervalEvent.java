package org.velas.manhunt.game.timeline.event;

import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.game.ManhuntGame;

public abstract class IntervalEvent extends TimelineEvent {

    protected final int startTime;
    protected final int interval;

    protected IntervalEvent(@NotNull ManhuntGame game, int startTime, int interval) {
        super(game, startTime);
        this.startTime=startTime;
        this.interval=interval;
    }

    @Override
    public boolean shouldTrigger(int currentTime) {
        if (currentTime <= startTime) return false;
        return (currentTime - startTime) % interval == 0;
    }

    public int getInterval() {
        return interval;
    }
}
