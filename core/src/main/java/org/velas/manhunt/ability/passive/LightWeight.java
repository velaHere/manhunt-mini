package org.velas.manhunt.ability.passive;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.Manhunt;
import org.velas.manhunt.ability.Ability;
import org.velas.manhunt.ability.AbilityType;
import org.velas.manhunt.ability.Grantable;
import org.velas.manhunt.avatar.Avatar;

public final class LightWeight extends Ability implements Grantable, Listener {

    private boolean enabled = false;

    public LightWeight(@NotNull Avatar avatar, @NotNull AbilityType type) {
        super("lightweight", avatar, type);
    }

    @Override
    public void onGrant() {
        enabled = true;
        Bukkit.getPluginManager().registerEvents(this, Manhunt.getINSTANCE());
    }

    @Override
    public void onRevoke() {
        enabled = false;
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onFall(EntityDamageEvent event){
        if (!enabled) return;
        if (!(event.getEntity() instanceof Player p)) return;

        Player owner = avatar.getManhuntPlayer().getPlayer();
        if (owner == null || !owner.equals(p)) return;

        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            event.setDamage(event.getDamage() * 0.70); // 30% less fall damage
        }
    }
}
