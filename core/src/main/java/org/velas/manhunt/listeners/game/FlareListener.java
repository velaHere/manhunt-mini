package org.velas.manhunt.listeners.game;

import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FireworkExplodeEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.persistence.PersistentDataType;
import org.velas.manhunt.customItems.CustomItemType;
import org.velas.manhunt.customItems.items.FlareGun;

public class FlareListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFlareLaunch(FireworkExplodeEvent event){

        Firework firework = event.getEntity();

        FireworkMeta meta = firework.getFireworkMeta();

        //if(!firework.getItem().hasItemMeta()) return;

        if(!meta.getPersistentDataContainer()
                .has(CustomItemType.getKey(CustomItemType.FLARE_GUN), PersistentDataType.BYTE)) return;

        FlareGun flareGun = (FlareGun) CustomItemType.getOrThrow(CustomItemType.FLARE_GUN);

        flareGun.onExplode(event);
    }
}
