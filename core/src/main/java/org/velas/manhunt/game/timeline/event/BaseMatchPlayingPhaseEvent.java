package org.velas.manhunt.game.timeline.event;

import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.game.ManhuntGame;
import org.velas.manhunt.game.ManhuntManager;

public abstract class BaseMatchPlayingPhaseEvent extends TimelineEvent {

    protected BaseMatchPlayingPhaseEvent(@NotNull ManhuntGame game) {
        super(game, game.getHeadStartTimeInSec()+60);
    }

    @Override
    public void execute() {
        this.exe();

        int time = this.getCurrentTime();
        ManhuntManager gameManager = this.game.getGameManager();
        gameManager.getGameFlowManager().setHuntersCanMove(true);
        gameManager.getGameFlowManager().releaseHunters();
        gameManager.getDropManager().setDropStartTime(time);
        gameManager.getDropManager().startExpiringDrops();
    }

    protected abstract void exe();
}
