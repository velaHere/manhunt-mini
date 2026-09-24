package org.velas.manhunt.player.handler.combat;

import org.bukkit.event.Cancellable;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.player.Hunter;
import org.velas.manhunt.player.handler.CombatHandler;

public abstract class BaseHunterCombatHandler implements CombatHandler {

    protected final @NotNull Hunter hunter;

    public BaseHunterCombatHandler(@NotNull Hunter hunter){
        this.hunter=hunter;
    }

    @Override
    public void onPlayerDoDamage(EntityDamageByEntityEvent event) {
        cancelOnFreeze(event);
        this.playerDoDamageEvent(event);
    }

    @Override
    public void onPlayerGetDamage(EntityDamageEvent event) {
        cancelOnFreeze(event);
        this.playerTakeDamageEvent(event);
    }

    private void cancelOnFreeze(Cancellable event){
        if(!this.hunter.isOnline()) return;
        if (this.hunter.getManhuntGame().getGameManager().getGameFlowManager().ifHuntersCannotMove())
            event.setCancelled(true);
    }

    protected abstract void playerDoDamageEvent(EntityDamageByEntityEvent event);
    protected abstract void playerTakeDamageEvent(EntityDamageEvent event);
}
