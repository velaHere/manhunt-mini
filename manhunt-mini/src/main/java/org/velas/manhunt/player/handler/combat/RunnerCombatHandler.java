package org.velas.manhunt.player.handler.combat;

import org.bukkit.event.Cancellable;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.game.GameState;
import org.velas.manhunt.game.ManhuntGame;
import org.velas.manhunt.player.Runner;

public class RunnerCombatHandler extends BaseRunnerCombatHandler {

    public RunnerCombatHandler(@NotNull Runner runner) {
        super(runner);
    }

    @Override
    protected void playerDoDamageEvent(EntityDamageByEntityEvent event) {
        this.onTransition(event);
    }

    @Override
    protected void playerTakeDamageEvent(EntityDamageEvent event) {
        this.onTransition(event);
    }

    private void onTransition(Cancellable event){
        ManhuntGame game = this.runner.getManhuntGame();
        if(game.getGameState().equals(GameState.TRANSITION))
            event.setCancelled(true);
    }
}
