package org.velas.manhunt.game.timeline.event;

import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.game.ManhuntGame;

public final class WorldPreloadEvent extends BaseWorldPreloadEvent {

    public WorldPreloadEvent(@NotNull ManhuntGame game) {
        super(game);
    }

    @Override
    protected void exe() {
        this.game.getGameManager().getWorldManager().setSpawnToVillage();
    }
}
