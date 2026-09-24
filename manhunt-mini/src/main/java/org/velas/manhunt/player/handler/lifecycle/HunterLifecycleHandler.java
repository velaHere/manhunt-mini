package org.velas.manhunt.player.handler.lifecycle;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.config.Settings;
import org.velas.manhunt.Manhunt;
import org.velas.manhunt.player.Hunter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HunterLifecycleHandler extends BaseHunterLifecycleHandler {

    private final List<ItemStack> keepContents = new ArrayList<>();

    public HunterLifecycleHandler(@NotNull Hunter hunter) {
        super(hunter);
    }

    @Override
    protected void matchStartEvent() {
    }

    @Override
    protected void matchStopEvent() {
    }

    @Override
    protected void playerJoinServerEvent() {
    }

    @Override
    protected void playerQuitServerEvent() {

    }

    @Override
    protected void playerRespawnEvent() {
        Player player = hunter.getPlayer();
        if(player==null) return;
        keepContents.forEach(item -> player.getInventory().addItem(item));
        keepContents.clear();
    }

    @Override
    protected void playerDeathEvent(PlayerDeathEvent event) {
        keepContents.clear();
        Player player = event.getEntity().getPlayer();
        if(player==null) return;
        List<ItemStack> allContents = event.getDrops();
        Collections.shuffle(allContents);

        int keepAmount = (int) Math.ceil(allContents.size()* (Settings.getConfigSettings().getKEEP_INVENTORY_PERCENTAGE()/100));

        for(int i = 0; i < keepAmount; i++) keepContents.add(allContents.get(i));
        allContents.removeAll(keepContents);

        Bukkit.getScheduler().runTaskLater(Manhunt.getINSTANCE(), () ->{
                    if (!player.isOnline()) return;
                    player.spigot().respawn();}, 4L);
    }
}
