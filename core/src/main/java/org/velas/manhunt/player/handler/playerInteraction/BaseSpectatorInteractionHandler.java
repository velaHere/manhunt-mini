package org.velas.manhunt.player.handler.playerInteraction;

import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.player.Spectator;
import org.velas.manhunt.player.handler.PlayerInteractionHandler;

public final class BaseSpectatorInteractionHandler extends PlayerInteractionHandler {

    private final @NotNull Spectator spectator;

    public BaseSpectatorInteractionHandler(@NotNull Spectator spectator){
        super(spectator);
        this.spectator=spectator;
    }

    @Override
    protected boolean playerInteraction(PlayerInteractEvent event) {
        if(!this.spectator.isOnline()) return false;
        event.setCancelled(true);
        return true;
    }
}
