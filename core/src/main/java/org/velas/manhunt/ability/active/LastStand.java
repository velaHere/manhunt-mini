package org.velas.manhunt.ability.active;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.velas.manhunt.Manhunt;
import org.velas.manhunt.ability.AbilityType;
import org.velas.manhunt.ability.ActiveAbility;
import org.velas.manhunt.ability.event.OnInteractAbility;
import org.velas.manhunt.avatar.Avatar;

import java.util.UUID;

public final class LastStand extends ActiveAbility implements OnInteractAbility {

    private static final UUID KB_UUID = UUID.fromString("2b3d2c4a-6c61-4dd3-aac8-8fd9e75a5a9d");

    public LastStand(Avatar avatar, AbilityType type) {
        super("outnumbered_last_stand", avatar, type,55);
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

        int durationTicks = 6 * 20;

        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, durationTicks, 0, false, true));
        p.playSound(p.getLocation(), Sound.ITEM_TOTEM_USE, 0.9f, 1.0f);
        p.spawnParticle(Particle.TOTEM, p.getLocation().add(0, 1.0, 0), 18, 0.35, 0.35, 0.35, 0.02);

        // Knockback resistance via attribute modifier
        var attr = p.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
        if (attr != null) {
            // remove existing
            attr.getModifiers().stream()
                    .filter(m -> m.getUniqueId().equals(KB_UUID))
                    .forEach(attr::removeModifier);

            attr.addModifier(new AttributeModifier(KB_UUID, "outnumbered_last_stand_kb", 0.35, AttributeModifier.Operation.ADD_NUMBER));
        }

        // remove modifier after duration
        org.bukkit.Bukkit.getScheduler().runTaskLater(Manhunt.getINSTANCE(), () -> {
            var a = p.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
            if (a != null) {
                a.getModifiers().stream()
                        .filter(m -> m.getUniqueId().equals(KB_UUID))
                        .forEach(a::removeModifier);
            }
        }, durationTicks);

        return true;
    }
}
