package org.velas.manhunt.game.timeline.event;

import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.config.Settings;
import org.velas.manhunt.game.ManhuntGame;

public abstract class BaseDropSummonEvent extends IntervalEvent {

    protected BaseDropSummonEvent(@NotNull ManhuntGame game) {
        super(game, 60+game.getHeadStartTimeInSec(), Settings.config().drop().getInterval());
    }

    @Override
    public void execute() {
        if(this.exe()) return;
        this.game.getGameManager().getDropManager().summonDrop();
    }

    protected abstract boolean exe();
}
