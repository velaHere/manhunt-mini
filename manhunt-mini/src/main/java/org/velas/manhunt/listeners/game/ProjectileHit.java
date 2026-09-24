package org.velas.manhunt.listeners.game;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;
import org.velas.manhunt.Manhunt;
import org.velas.manhunt.game.ManhuntGame;

import java.util.HashSet;
import java.util.Set;

public class ProjectileHit implements Listener {

    public static final Set<Location> customExplosions = new HashSet<>();

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event){
        ManhuntGame currentGame = ManhuntGame.getCurrentGame();
        if (!currentGame.isRunning()) return;
        if (event.getEntity() instanceof Fireball fireball) {
            if (!fireball.hasMetadata("gola")) return;

            Location loc = fireball.getLocation();

            customExplosions.add(loc);

            // remove after short delay
            Bukkit.getScheduler().runTaskLater(Manhunt.getINSTANCE(), () ->
                    customExplosions.remove(loc), 5L);

            fireball.setYield(2.0f);

            World world = loc.getWorld();
            if(world==null) return;

            for (Entity entity : world.getNearbyEntities(loc, 4, 4, 4)) {
                if (entity instanceof Player target) {
                    Vector knockback = target.getLocation().toVector()
                            .subtract(loc.toVector())
                            .normalize()
                            .multiply(2.0)
                            .setY(0.8);
                    target.setVelocity(knockback);

                    double distance = target.getLocation().distance(loc);
                    double maxDamage = 4.0; // 2 hearts max
                    double damage = Math.max(0, maxDamage - distance);

                    target.damage(damage);
                }
            }
        } else if (event.getEntity() instanceof Snowball || event.getEntity() instanceof Egg){
            if (!(event.getHitEntity() instanceof Player victim)) return;

            Projectile projectile = event.getEntity();

            if (!(projectile.getShooter() instanceof Player shooter)) return;

            if (victim.equals(shooter)) return;

            double damage = 0.4; // 1 heart
            victim.damage(damage, shooter);

            Vector direction = victim.getLocation().toVector()
                    .subtract(shooter.getLocation().toVector())
                    .normalize();

            victim.setVelocity(direction.multiply(0.6).setY(0.3));
        }
    }
}
