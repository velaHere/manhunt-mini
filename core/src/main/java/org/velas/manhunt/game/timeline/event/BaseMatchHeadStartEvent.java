package org.velas.manhunt.game.timeline.event;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.Utils;
import org.velas.manhunt.event.ManhuntMatchStartEvent;
import org.velas.manhunt.game.GameState;
import org.velas.manhunt.game.ManhuntGame;
import org.velas.manhunt.player.ManhuntPlayer;

public final class BaseMatchHeadStartEvent extends TimelineEvent {

    public BaseMatchHeadStartEvent(@NotNull ManhuntGame game) {
        super(game, 60);
    }

    @Override
    public void execute() {
        this.game.setGameState(GameState.HEAD_START);
        Bukkit.getPluginManager().callEvent(new ManhuntMatchStartEvent(this.game));
        Utils.announceMatchStart();
        for (ManhuntPlayer player : this.game.getGameManager().getPlayerManager().getPlayers()){
            player.getHandlerBundle().lifecycleHandler().onMatchStart();
        }
    }
}
