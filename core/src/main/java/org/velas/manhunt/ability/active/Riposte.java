package org.velas.manhunt.ability.active;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.velas.manhunt.Manhunt;
import org.velas.manhunt.ability.AbilityType;
import org.velas.manhunt.ability.ActiveAbility;
import org.velas.manhunt.ability.event.OnDamageAbility;
import org.velas.manhunt.ability.event.OnInteractAbility;
import org.velas.manhunt.avatar.Avatar;

public final class Riposte extends ActiveAbility implements OnInteractAbility, OnDamageAbility {

    private boolean windowOpen = false;

    public Riposte(Avatar avatar, AbilityType type) {
        super("riposte", avatar, type,26);
    }

    @Override public void onInteract(PlayerInteractEvent event) {
        tryActivate(event);
    }

    @Override
    protected boolean onActivate(Event event) {
        if (!(event instanceof PlayerInteractEvent e)) return false;
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return false;

        Player p = e.getPlayer();
        if (!p.isSneaking()) return false;

        windowOpen = true;
        p.playSound(p.getLocation(), Sound.ITEM_SHIELD_BLOCK, 0.8f, 1.2f);
        p.spawnParticle(Particle.CRIT, p.getLocation().add(0, 1.0, 0), 10, 0.3, 0.3, 0.3, 0.05);

        // window lasts 1.25s
        org.bukkit.Bukkit.getScheduler().runTaskLater(Manhunt.getINSTANCE(), () -> windowOpen = false, 25L);
        return true;
    }

    @Override
    public void onDamage(EntityDamageByEntityEvent event) {

        if (!windowOpen) return;

        Player owner = avatar.getManhuntPlayer().getPlayer();
        if (owner == null) return;

        // If owner is the one getting hit:
        if (!event.getEntity().equals(owner)) return;
        if (!(event.getDamager() instanceof Player attacker)) return;

        // reduce damage and counter-hit once
        event.setDamage(event.getDamage() * 0.35);
        attacker.damage(3.0, owner);

        owner.playSound(owner.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 0.9f, 1.2f);
        owner.spawnParticle(Particle.SWEEP_ATTACK, owner.getLocation().add(0, 1.0, 0), 1);
        windowOpen = false;
    }
}
