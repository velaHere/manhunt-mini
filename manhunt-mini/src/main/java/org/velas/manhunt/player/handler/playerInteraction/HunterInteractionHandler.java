package org.velas.manhunt.player.handler.playerInteraction;

import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.game.GameState;
import org.velas.manhunt.player.Hunter;

public class HunterInteractionHandler extends BaseHunterInteractionHandler {

    public HunterInteractionHandler(@NotNull Hunter hunter) {
        super(hunter);
    }

    @Override
    protected boolean playerInteractionEvent(PlayerInteractEvent event) {
        if(hunter.getManhuntGame().getGameState().equals(GameState.TRANSITION)){
            event.setCancelled(true);
            return false;
        }

        if(!this.hunter.isAlive()) {
            event.setCancelled(true);
            return false;
        }

        return true;
    }
}
