package org.velas.manhunt.player.handler;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public interface CombatHandler {
    void onPlayerDoDamage(EntityDamageByEntityEvent event);
    void onPlayerGetDamage(EntityDamageEvent event);
}
