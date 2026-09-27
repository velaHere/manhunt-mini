package org.velas.manhunt.player.handler.playerInteraction;

import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.customItems.CustomItemType;
import org.velas.manhunt.player.Runner;
import org.velas.manhunt.player.handler.PlayerInteractionHandler;


public abstract class BaseRunnerInteractionHandler extends PlayerInteractionHandler {

    protected final @NotNull Runner runner;

    public BaseRunnerInteractionHandler(@NotNull Runner runner){
        super(runner);
        this.runner = runner;
    }

    @Override
    protected boolean playerInteraction(PlayerInteractEvent event) {
        if(!runner.isOnline()) return false;
        if(this.runner.getManhuntGame().isStarting()) {
            event.setCancelled(true);
            return CustomItemType.isCustomItem(event.getPlayer().getInventory().getItemInMainHand());
        }
        return this.playerInteractionEvent(event);
    }

    protected abstract boolean playerInteractionEvent(PlayerInteractEvent event);
}