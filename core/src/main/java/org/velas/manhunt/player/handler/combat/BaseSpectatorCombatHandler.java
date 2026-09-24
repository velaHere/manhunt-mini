package org.velas.manhunt.player.handler.combat;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.player.Spectator;
import org.velas.manhunt.player.handler.CombatHandler;

public final class BaseSpectatorCombatHandler implements CombatHandler {

    private final @NotNull Spectator spectator;

    public BaseSpectatorCombatHandler(@NotNull Spectator spectator){
        this.spectator=spectator;
    }

    @Override
    public void onPlayerDoDamage(EntityDamageByEntityEvent event) {
        if(!spectator.isOnline()) return;
        event.setCancelled(true);
    }

    @Override
    public void onPlayerGetDamage(EntityDamageEvent event) {
        if(!spectator.isOnline()) return;
        event.setCancelled(true);
    }
}
