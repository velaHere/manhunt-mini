package org.velas.manhunt.customItems.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
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
import org.velas.manhunt.game.GameFlowManager;
import org.velas.manhunt.game.GameState;
import org.velas.manhunt.game.ManhuntGame;
import org.velas.manhunt.menusystem.menu.YesOrNo;

public final class BackToLobbyItem implements CustomItem {

    public static final BackToLobbyItem INSTANCE;

    private final NamespacedKey namespacedKey;

    static {
        INSTANCE = new BackToLobbyItem();
    }

    private BackToLobbyItem() {
        this.namespacedKey=new NamespacedKey(Manhunt.getINSTANCE(), CustomItemType.BACK_TO_LOBBY);
    }

    @Override
    public NamespacedKey getKey() {
        return this.namespacedKey;
    }

    @Override
    public ItemStack createItem() {
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        if(meta==null) return item;

        meta.setDisplayName(ChatColor.RED + "Back to Lobby");

        meta.getPersistentDataContainer().set(this.namespacedKey, PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public void registerRecipe(JavaPlugin plugin) {

    }

    @Override
    public void onUse(PlayerInteractEvent event) {
        ManhuntGame game = ManhuntGame.getCurrentGame();
        if(!game.getGameState().equals(GameState.ENDED)) return;
        Player player = event.getPlayer();
        new YesOrNo(
                Manhunt.getPlayerMenuUtility(player), "Back to Lobby",
                () -> GameFlowManager.teleportPlayerToLobby(player),
                player::closeInventory
        ).open();
    }

    @Override
    public void onItemDrop(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onPickup(EntityPickupItemEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onItemSwitch(PlayerItemHeldEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onItemSwap(PlayerSwapHandItemsEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void placeItemToHeldSlot(InventoryClickEvent event) {
        event.setCancelled(true);
    }
}
