package org.velas.manhunt.ability.passive;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.velas.manhunt.Manhunt;
import org.velas.manhunt.ability.Ability;
import org.velas.manhunt.ability.AbilityType;
import org.velas.manhunt.ability.Grantable;
import org.velas.manhunt.avatar.Avatar;

public final class BladeDiscipline extends Ability implements Grantable, Listener {

    private boolean enabled = false;

    public BladeDiscipline(Avatar avatar, AbilityType type) {
        super("blade_discipline", avatar, type);
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
    public void onSwap(PlayerItemHeldEvent event) {
        if (!enabled) return;
        Player owner = avatar.getManhuntPlayer().getPlayer();
        if (!event.getPlayer().equals(owner)) return;
        refresh();
    }

    private void refresh() {
        Player p = avatar.getManhuntPlayer().getPlayer();
        if (p == null) return;

        Material type = p.getInventory().getItemInMainHand().getType();
        boolean sword = type.name().endsWith("_SWORD");

        if (sword) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 80, 0, false, false));
        } else {
            p.removePotionEffect(PotionEffectType.SPEED);
        }
    }
}
