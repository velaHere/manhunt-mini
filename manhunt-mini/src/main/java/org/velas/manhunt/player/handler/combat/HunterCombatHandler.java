package org.velas.manhunt.player.handler.combat;

import org.bukkit.event.Cancellable;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.game.GameState;
import org.velas.manhunt.player.Hunter;

public class HunterCombatHandler extends BaseHunterCombatHandler {

    public HunterCombatHandler(@NotNull Hunter hunter) {
        super(hunter);
    }

    @Override
    protected void playerDoDamageEvent(EntityDamageByEntityEvent event) {
        this.onTransition(event);
        this.checkAlive(event);
    }

    @Override
    protected void playerTakeDamageEvent(EntityDamageEvent event) {
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
