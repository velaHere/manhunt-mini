package org.velas.manhunt.player.handler.combat;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.game.ManhuntGame;
import org.velas.manhunt.player.Runner;
import org.velas.manhunt.player.handler.CombatHandler;

public abstract class BaseRunnerCombatHandler implements CombatHandler {

    protected final @NotNull Runner runner;

    public BaseRunnerCombatHandler(@NotNull Runner runner){
        this.runner=runner;
    }

    @Override
    public void onPlayerDoDamage(EntityDamageByEntityEvent event) {
        if(!this.runner.isOnline()) return;
        ManhuntGame game = this.runner.getManhuntGame();
        if(game.isStarting())
            event.setCancelled(true);
        this.playerDoDamageEvent(event);
    }

    @Override
    public void onPlayerGetDamage(EntityDamageEvent event) {
        if(!this.runner.isOnline()) return;
        ManhuntGame game = this.runner.getManhuntGame();
        if(game.isStarting()) event.setCancelled(true);
        this.playerTakeDamageEvent(event);
    }

    protected abstract void playerDoDamageEvent(EntityDamageByEntityEvent event);
    protected abstract void playerTakeDamageEvent(EntityDamageEvent event);
}
