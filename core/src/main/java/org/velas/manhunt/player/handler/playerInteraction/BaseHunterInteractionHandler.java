package org.velas.manhunt.player.handler.playerInteraction;

import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.player.Hunter;
import org.velas.manhunt.player.handler.PlayerInteractionHandler;

public abstract class BaseHunterInteractionHandler extends PlayerInteractionHandler {

    protected final @NotNull Hunter hunter;

    public BaseHunterInteractionHandler(@NotNull Hunter hunter){
        super(hunter);
        this.hunter=hunter;
    }

    @Override
    protected boolean playerInteraction(PlayerInteractEvent event) {
        if (hunter.getManhuntGame().getGameManager().getGameFlowManager().ifHuntersCannotMove()){
            event.setCancelled(true);
            return false;
        }
        return this.playerInteractionEvent(event);
    }

    protected abstract boolean playerInteractionEvent(PlayerInteractEvent event);
}
