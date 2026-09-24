package org.velas.manhunt.player.handler.worldInteraction;

import org.bukkit.event.Cancellable;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.game.ManhuntGame;
import org.velas.manhunt.player.Runner;
import org.velas.manhunt.player.handler.WorldInteractionHandler;

public abstract class BaseRunnerWorldInteractionHandler implements WorldInteractionHandler {

    protected final @NotNull Runner runner;

    public BaseRunnerWorldInteractionHandler(@NotNull Runner runner){
        this.runner=runner;
    }

    @Override
    public void onBlockPlace(BlockPlaceEvent event) {
        if(!runner.isOnline()) return;
        cancelOnFreeze(event);
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        if(!runner.isOnline()) return;
        cancelOnFreeze(event);
    }

    private void cancelOnFreeze(Cancellable event){
        ManhuntGame game = runner.getManhuntGame();
        if(game.isStarting())
            event.setCancelled(true);
    }

    protected abstract void blockPlaceEvent(BlockPlaceEvent event);
    protected abstract void blockBreakEvent(BlockBreakEvent event);
}
