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

public class EvasiveStep extends ActiveAbility implements OnInteractAbility {

    public EvasiveStep(Avatar avatar, int cooldownSec, AbilityType type) {
        super("outnumbered_evasive_step", avatar, type, cooldownSec);
    }

    @Override
    protected boolean onActivate(Event event) {
        if (!(event instanceof PlayerInteractEvent e)) return false;
        if (e.getAction() != Action.RIGHT_CLICK_AIR) return false;

        Player p = e.getPlayer();
        if (!p.isSneaking()) return false; // makes it harder to trigger by accident

        Vector dir = p.getLocation().getDirection().normalize();
        Vector vel = dir.multiply(0.95);
        vel.setY(Math.max(vel.getY(), 0.08)); // tiny lift so it feels responsive

        p.setVelocity(vel);
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 0.7f, 1.6f);
        p.spawnParticle(Particle.SWEEP_ATTACK, p.getLocation().add(0, 1.0, 0), 1, 0, 0, 0, 0);
        p.spawnParticle(Particle.CLOUD, p.getLocation().add(0, 0.2, 0), 10, 0.2, 0.05, 0.2, 0.01);
        return true;
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
        tryActivate(event);
    }
}
