package org.velas.manhunt.player.handler.worldInteraction;

import org.bukkit.event.Cancellable;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.game.GameState;
import org.velas.manhunt.player.Hunter;

public class HunterWorldInteractionHandler extends BaseHunterWorldInteractionHandler {

    public HunterWorldInteractionHandler(@NotNull Hunter hunter) {
        super(hunter);
    }

    @Override
    protected void blockPlaceEvent(BlockPlaceEvent event) {
        this.onTransition(event);
        this.checkAlive(event);
    }

    @Override
    protected void blockBreakEvent(BlockBreakEvent event) {
        this.onTransition(event);
        this.checkAlive(event);
    }

    private void onTransition(Cancellable event){
        if(this.hunter.getManhuntGame().getGameState().equals(GameState.TRANSITION))
            event.setCancelled(true);
    }

    private void checkAlive(Cancellable event) {
        if(!this.hunter.isAlive()) event.setCancelled(true);
    }
}
