package org.velas.manhunt.game.timeline.event;

import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.game.ManhuntGame;

public abstract class RangeEvent extends TimelineEvent {

    protected final int startTime;
    protected final int endTime;

    protected RangeEvent(@NotNull ManhuntGame game, int startTime, int endTime) {
        super(game, startTime);
        this.startTime=startTime;
        this.endTime=endTime;
    }

    @Override
    public boolean shouldTrigger(int currentTime) {
        return currentTime >= startTime && currentTime < endTime;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }
}
