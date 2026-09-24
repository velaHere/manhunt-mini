package org.velas.manhunt.ability.passive;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.velas.manhunt.Manhunt;
import org.velas.manhunt.ability.Ability;
import org.velas.manhunt.ability.AbilityType;
import org.velas.manhunt.ability.Grantable;
import org.velas.manhunt.avatar.Avatar;

public final class Adrenaline extends Ability implements Grantable, Listener {

    private boolean enabled = false;

    public Adrenaline(Avatar avatar, AbilityType type) {
        super("outnumbered_adrenaline", avatar, type);
    }

    @Override public void onGrant() {
        enabled = true;
        Manhunt.getINSTANCE().getServer().getPluginManager().registerEvents(this, Manhunt.getINSTANCE());
        refresh();
    }

    @Override public void onRevoke() {
        enabled = false;
        org.bukkit.event.HandlerList.unregisterAll(this);

        Player p = avatar.getManhuntPlayer().getPlayer();
        if (p != null) p.removePotionEffect(PotionEffectType.SPEED);
    }

    @EventHandler(ignoreCancelled = true)
    public void onDamage(EntityDamageEvent event) {
        if (!enabled) return;
        if (!(event.getEntity() instanceof Player)) return;
        refresh();
    }

    @EventHandler(ignoreCancelled = true)
    public void onHeal(EntityRegainHealthEvent event) {
        if (!enabled) return;
        if (!(event.getEntity() instanceof Player)) return;
        refresh();
    }

    private void refresh() {
        Player p = avatar.getManhuntPlayer().getPlayer();
        if (p == null) return;

        double hp = p.getHealth();
        double max = p.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH) != null
                ? p.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getValue()
                : 20.0;

        if (hp <= max * 0.50) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, 0, false, false)); // Speed I, refreshed
        } else {
            p.removePotionEffect(PotionEffectType.SPEED);
        }
    }
}
