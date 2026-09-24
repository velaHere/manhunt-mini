package org.velas.manhunt.listeners.game;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.velas.manhunt.config.Settings;
import org.velas.manhunt.game.ManhuntGame;

public class EntityHit implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityHit(EntityDamageByEntityEvent event){
        ManhuntGame currentGame = ManhuntGame.getCurrentGame();
        if (!currentGame.isRunning()) return;

        Entity entity = event.getEntity();
        if (entity.getType().equals(EntityType.VILLAGER)){
            if (entity.getWorld().getName().equals(Settings.OVERWORLD_NAME))
                event.setCancelled(true);
        }
    }
}
