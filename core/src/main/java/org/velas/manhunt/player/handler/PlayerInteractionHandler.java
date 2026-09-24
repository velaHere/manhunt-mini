package org.velas.manhunt.player.handler;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.customItems.CustomItem;
import org.velas.manhunt.customItems.CustomItemType;
import org.velas.manhunt.player.ManhuntPlayer;

import java.util.Optional;

public abstract class PlayerInteractionHandler {

    private final @NotNull ManhuntPlayer manhuntPlayer;

    protected PlayerInteractionHandler(@NotNull ManhuntPlayer manhuntPlayer){
        this.manhuntPlayer=manhuntPlayer;
    }

    protected abstract boolean playerInteraction(PlayerInteractEvent event);
    public void onPlayerInteraction(PlayerInteractEvent event){
        if(!manhuntPlayer.isOnline()) return;

        if(!playerInteraction(event)) return;
        ItemStack itemInMainHand = event.getPlayer().getInventory().getItemInMainHand();
        Optional<CustomItem> customItem = CustomItemType.fromItem(itemInMainHand);
        if(customItem.isEmpty()) return;
        customItem.get().onUse(event);
    }
}
