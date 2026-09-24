package org.velas.manhunt.player.handler.worldInteraction;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.player.Spectator;
import org.velas.manhunt.player.handler.WorldInteractionHandler;

public final class BaseSpectatorWorldInteractionHandler implements WorldInteractionHandler {

    private final @NotNull Spectator spectator;

    public BaseSpectatorWorldInteractionHandler(@NotNull Spectator spectator){
        this.spectator=spectator;
    }

    @Override
    public void onBlockPlace(BlockPlaceEvent event) {
        if(!spectator.isOnline()) return;
        event.setCancelled(true);
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        if(!spectator.isOnline()) return;
        event.setCancelled(true);
    }
}
