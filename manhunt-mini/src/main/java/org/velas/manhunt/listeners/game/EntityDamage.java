package org.velas.manhunt.listeners.game;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.velas.manhunt.config.Settings;
import org.velas.manhunt.game.ManhuntGame;

import java.util.Objects;

public class EntityDamage implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamage(EntityDamageEvent event){

        ManhuntGame currentGame = ManhuntGame.getCurrentGame();
        if (currentGame.isRunning()) {
            Entity entity = event.getEntity();
            if (entity.getType().equals(EntityType.VILLAGER)){
                if (entity.getWorld().getName().equals(Settings.OVERWORLD_NAME))
                    event.setCancelled(true);
            }
        }

        if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION ||
                event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {

            Location entityLoc = event.getEntity().getLocation();

            for (Location loc : ProjectileHit.customExplosions) {
                if (Objects.equals(loc.getWorld(), entityLoc.getWorld()) &&
                        loc.distance(entityLoc) <= 4) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }
}
