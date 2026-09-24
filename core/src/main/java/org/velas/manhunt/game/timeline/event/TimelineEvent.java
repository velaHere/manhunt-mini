package org.velas.manhunt.game.timeline.event;

import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.game.ManhuntGame;

public abstract class TimelineEvent {

    protected final @NotNull ManhuntGame game;
    protected final int triggerTime;

    protected TimelineEvent(@NotNull ManhuntGame game, int triggerTime) {
        this.game=game;
        this.triggerTime = triggerTime;
    }

    public int getTriggerTime() {
        return triggerTime;
    }

    protected int getCurrentTime(){
        return this.game.getGameManager().getGameTimeLine().getTime();
    }

    public abstract void execute();

    public boolean shouldTrigger(int currentTime) {
        return currentTime == triggerTime;
    }
}
