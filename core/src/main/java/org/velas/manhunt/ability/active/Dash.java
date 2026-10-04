package org.velas.manhunt.ability.active;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.velas.manhunt.Manhunt;
import org.velas.manhunt.ability.AbilityType;
import org.velas.manhunt.ability.ActiveAbility;
import org.velas.manhunt.ability.event.OnInteractAbility;
import org.velas.manhunt.avatar.Avatar;

import java.util.*;

public final class Dash extends ActiveAbility implements OnInteractAbility {

    public Dash(Avatar avatar, AbilityType type) {
        super("dash", avatar, type,15);
    }

    @Override
    protected boolean onActivate(Event event) {

        if (!(event instanceof PlayerInteractEvent event1)) return false;
        if (event1.getAction() != Action.RIGHT_CLICK_AIR && event1.getAction() != Action.RIGHT_CLICK_BLOCK) return false;
        ItemStack item = event1.getItem();
        if(item==null) return false;

        Player player = event1.getPlayer();

        Vector direction = player.getLocation().getDirection().normalize();
        Vector velocity = direction.multiply(1.25);

        player.setVelocity(velocity);

        player.getWorld().playSound(
                player.getLocation(),
                Sound.ENTITY_ENDER_DRAGON_FLAP,
                1f,
                1.2f
        );

        spawnLaunchBurst(player);
        spawnDashTrail(player);
        return true;
    }

    private void spawnLaunchBurst(Player player) {
        Location feet = player.getLocation().clone().subtract(0, 0.2, 0);
        player.getWorld().spawnParticle(
                Particle.CLOUD,
                feet,
                25,
                0.3, 0.05, 0.3,
                0.02
        );
    }

    private void spawnDashTrail(Player player) {

        double handHeight = 1.4;
        double handSpacing = 0.35;
        double minSpeed = 0.15;

        double hitRadiusXZ = 1.2;
        double hitRadiusY = 0.55;

        Set<UUID> hitEntities = new HashSet<>();

        new BukkitRunnable() {

            Location lastLocation = player.getLocation().clone();

            @Override
            public void run() {

                if (!player.isOnline()) {
                    cancel();
                    return;
                }

                // horizontal speed check ONLY
                Vector horizontalVel = player.getVelocity().clone();
                horizontalVel.setY(0);

                if (horizontalVel.length() < minSpeed) {
                    cancel();
                    return;
                }

                Location current = player.getLocation().clone();

                Vector forward = horizontalVel.clone().normalize();
                Vector side = new Vector(-forward.getZ(), 0, forward.getX()).normalize();

                // Exact dash path midpoint
                Location trailBase = lastLocation.clone()
                        .add(current.toVector().subtract(lastLocation.toVector()).multiply(0.5));

                // ✅ STRICT Y-LEVEL HITBOX (CHEST LEVEL)
                Location hitCenter = trailBase.clone().add(0, 0.9, 0);

                for (Entity entity : Objects.requireNonNull(hitCenter.getWorld()).getNearbyEntities(
                        hitCenter,
                        hitRadiusXZ,
                        hitRadiusY,
                        hitRadiusXZ
                )) {

                    if(!(entity instanceof LivingEntity targetEntity)) continue;

                    if(entity instanceof Player target && target.equals(player)) continue;

                    if (!hitEntities.add(targetEntity.getUniqueId())) continue;

                    applyDashEffect(player, targetEntity);
                }

                Location rightHand = trailBase.clone()
                        .add(side.clone().multiply(handSpacing))
                        .add(0, handHeight, 0);

                Location leftHand = trailBase.clone()
                        .add(side.clone().multiply(-handSpacing))
                        .add(0, handHeight, 0);

                player.getWorld().spawnParticle(Particle.CRIT, rightHand, 2, 0, 0, 0, 0);
                player.getWorld().spawnParticle(Particle.CRIT, leftHand, 2, 0, 0, 0, 0);

                lastLocation = current;
            }
        }.runTaskTimer(Manhunt.getINSTANCE(), 4L, 1L);
    }

    private void applyDashEffect(Player dasher, LivingEntity target) {

        // horizontal-only knockback
        Vector dir = dasher.getLocation().getDirection().clone();
        dir.setY(0);
        dir.normalize();

        Vector currentVel = target.getVelocity();

        Vector kb = new Vector(
                dir.getX() * 1.2,
                currentVel.getY(), // preserve Y motion
                dir.getZ() * 1.2
        );

        target.setVelocity(kb);
        target.damage(2.0, dasher);
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
        tryActivate(event);
    }
}
