package org.velas.manhunt.player.handler.playerInteraction;

import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.game.GameState;
import org.velas.manhunt.player.Runner;

public class RunnerInteractionHandler extends BaseRunnerInteractionHandler {

    public RunnerInteractionHandler(@NotNull Runner runner) {
        super(runner);
    }

    @Override
    protected boolean playerInteractionEvent(PlayerInteractEvent event) {
        if(this.runner.getManhuntGame().getGameState().equals(GameState.TRANSITION)){
            event.setCancelled(true);
            return false;
        }
        return true;
    }
}
