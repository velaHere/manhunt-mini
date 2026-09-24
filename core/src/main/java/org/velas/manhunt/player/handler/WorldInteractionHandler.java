package org.velas.manhunt.player.handler;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public interface WorldInteractionHandler {
    void onBlockPlace(BlockPlaceEvent event);
    void onBlockBreak(BlockBreakEvent event);
}
