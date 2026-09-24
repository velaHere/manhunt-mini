package org.velas.manhunt.ability.active;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;
import org.velas.manhunt.ability.AbilityType;
import org.velas.manhunt.ability.ActiveAbility;
import org.velas.manhunt.ability.event.OnInteractAbility;
import org.velas.manhunt.avatar.Avatar;

public final class Lunge extends ActiveAbility implements OnInteractAbility {

    public Lunge(Avatar avatar, AbilityType type) {
        super("lunge", avatar, type, 14);
    }

    @Override public void onInteract(PlayerInteractEvent event) {
        tryActivate(event);
    }

    @Override
    protected boolean onActivate(Event event) {
        if (!(event instanceof PlayerInteractEvent e)) return false;
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return false;

        Player p = e.getPlayer();
        if (!p.isSprinting()) return false; // must be sprinting for "duelist feel"

        Vector dir = p.getLocation().getDirection().normalize();
        Vector vel = dir.multiply(0.75);
        vel.setY(Math.max(vel.getY(), 0.05));
        p.setVelocity(vel);

        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 0.8f, 1.3f);
        p.spawnParticle(Particle.SWEEP_ATTACK, p.getLocation().add(0, 1.0, 0), 1);
        return true;
    }
}
