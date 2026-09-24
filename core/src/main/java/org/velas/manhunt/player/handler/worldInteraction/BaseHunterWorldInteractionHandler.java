package org.velas.manhunt.player.handler.worldInteraction;

import org.bukkit.event.Cancellable;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.player.Hunter;
import org.velas.manhunt.player.handler.WorldInteractionHandler;

public abstract class BaseHunterWorldInteractionHandler implements WorldInteractionHandler {

    protected final @NotNull Hunter hunter;

    public BaseHunterWorldInteractionHandler(@NotNull Hunter hunter){
        this.hunter=hunter;
    }

    @Override
    public void onBlockPlace(BlockPlaceEvent event) {
        if(!hunter.isOnline()) return;
        cancelOnFreeze(event);
        this.blockPlaceEvent(event);
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        if(!hunter.isOnline()) return;
        cancelOnFreeze(event);
        this.blockBreakEvent(event);
    }

    private void cancelOnFreeze(Cancellable event){
        if (this.hunter.getManhuntGame().getGameManager().getGameFlowManager().ifHuntersCannotMove())
            event.setCancelled(true);
    }

    protected abstract void blockPlaceEvent(BlockPlaceEvent event);
    protected abstract void blockBreakEvent(BlockBreakEvent event);
}
