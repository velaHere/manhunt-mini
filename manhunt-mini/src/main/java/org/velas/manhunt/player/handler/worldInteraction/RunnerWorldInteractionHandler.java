package org.velas.manhunt.player.handler.worldInteraction;

import org.bukkit.event.Cancellable;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.game.GameState;
import org.velas.manhunt.game.ManhuntGame;
import org.velas.manhunt.player.Runner;

public class RunnerWorldInteractionHandler extends BaseRunnerWorldInteractionHandler {

    public RunnerWorldInteractionHandler(@NotNull Runner runner) {
        super(runner);
    }

    @Override
    protected void blockPlaceEvent(BlockPlaceEvent event) {
        this.onTransition(event);
    }

    @Override
    protected void blockBreakEvent(BlockBreakEvent event) {
        this.onTransition(event);
    }

    private void onTransition(Cancellable event){
        ManhuntGame game = runner.getManhuntGame();
        if(game.getGameState().equals(GameState.TRANSITION)) event.setCancelled(true);
    }
}
