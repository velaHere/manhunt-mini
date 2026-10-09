package org.velas.manhunt.customItems.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.velas.manhunt.Manhunt;
import org.velas.manhunt.customItems.CustomItem;
import org.velas.manhunt.customItems.CustomItemType;
import org.velas.manhunt.game.GameState;
import org.velas.manhunt.game.ManhuntGame;
import org.velas.manhunt.menusystem.menu.TeleportMenu;
import org.velas.manhunt.player.ManhuntPlayer;

public final class SpectatorCompass implements CustomItem {

    public static final SpectatorCompass INSTANCE;

    private final NamespacedKey namespacedKey;

    static{
        INSTANCE=new SpectatorCompass();
    }

    private SpectatorCompass(){
        this.namespacedKey=new NamespacedKey(Manhunt.getINSTANCE(), CustomItemType.SPECTATOR_COMPASS);
    }

    @Override
    public NamespacedKey getKey() {
        return this.namespacedKey;
    }

    @Override
    public ItemStack createItem() {

        ItemStack compass = new ItemStack(Material.COMPASS, 1);
        ItemMeta meta = compass.getItemMeta();
        if(meta==null) return compass;

        meta.setDisplayName(ChatColor.GOLD + "Teleporter");
        meta.getPersistentDataContainer().set(this.namespacedKey, PersistentDataType.BYTE, (byte) 1);
        compass.setItemMeta(meta);
        return compass;
    }

    @Override
    public void registerRecipe(JavaPlugin plugin) {

    }

    @Override
    public void onUse(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        ManhuntGame currentGame = ManhuntGame.getCurrentGame();
        if(!currentGame.isRunning()) return;
        if(currentGame.getGameState().equals(GameState.TRANSITION)) return;
        ManhuntPlayer manhuntPlayer = currentGame.getGameManager().getPlayerManager().getManhuntPlayer(player);
        if(manhuntPlayer==null) return;
        if(!manhuntPlayer.isSpectator()) return;

        if (
                event.getAction() == Action.LEFT_CLICK_AIR ||
                        event.getAction() == Action.LEFT_CLICK_BLOCK ||
                        event.getAction() == Action.RIGHT_CLICK_AIR ||
                        event.getAction() == Action.RIGHT_CLICK_BLOCK
        ) new TeleportMenu(Manhunt.getPlayerMenuUtility(player)).open();
        event.setCancelled(true);
    }

    @Override
    public void onItemDrop(PlayerDropItemEvent event) {
        ManhuntGame currentGame = ManhuntGame.getCurrentGame();
        if(!currentGame.isRunning()) return;
        event.setCancelled(true);
    }

    @Override
    public void onPickup(EntityPickupItemEvent event) {

    }

    @Override
    public void onItemSwitch(PlayerItemHeldEvent event) {

    }

    @Override
    public void onItemSwap(PlayerSwapHandItemsEvent event) {

    }

    @Override
    public void placeItemToHeldSlot(InventoryClickEvent event) {

    }
}
