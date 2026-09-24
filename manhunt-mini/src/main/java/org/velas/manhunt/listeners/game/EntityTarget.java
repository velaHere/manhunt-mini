package org.velas.manhunt.listeners.game;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.velas.manhunt.config.Settings;
import org.velas.manhunt.game.ManhuntGame;

public class EntityTarget implements Listener {

    @EventHandler
    public void onEntityTarget(EntityTargetEvent event){
        ManhuntGame currentGame = ManhuntGame.getCurrentGame();
        if (!currentGame.isRunning()) return;
        Entity target = event.getTarget();
        if (target==null) return;
        if (!target.getType().equals(EntityType.VILLAGER)) return;
        if (target.getWorld().getName().equals(Settings.OVERWORLD_NAME)) {
            event.setCancelled(true);
            event.setTarget(null);
        }
    }
}
