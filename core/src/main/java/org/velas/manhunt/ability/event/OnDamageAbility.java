package org.velas.manhunt.ability.event;

import org.bukkit.event.entity.EntityDamageByEntityEvent;

public interface OnDamageAbility {
    void onDamage(EntityDamageByEntityEvent event);
}