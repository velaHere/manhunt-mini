package org.velas.manhunt.customItems.items;

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
import org.velas.manhunt.game.GameState;
import org.velas.manhunt.game.ManhuntGame;
import org.velas.manhunt.menusystem.menu.ClassSelection;

import java.util.Arrays;

public final class AvatarSigil implements CustomItem {

    public static final AvatarSigil INSTANCE;

    private final NamespacedKey namespacedKey;

    static{
        INSTANCE=new AvatarSigil();
    }

    private AvatarSigil(){
        this.namespacedKey=new NamespacedKey(Manhunt.getINSTANCE(), "avatar_sigil");
    }

    @Override
    public NamespacedKey getKey() {
        return this.namespacedKey;
    }

    @Override
    public ItemStack createItem() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        if(meta==null) return item;

        meta.setDisplayName("§d§lAvatar Sigil");

        meta.setLore(Arrays.asList(
                "§7A mystical parchment imbued",
                "§7with the power of choice.",
                "",
                "§eRight-click to choose your path",
                "§eand bind your Avatar."
        ));
        meta.getPersistentDataContainer().set(this.namespacedKey, PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public void registerRecipe(JavaPlugin plugin) {

    }

    @Override
    public void onUse(PlayerInteractEvent event) {
        event.setCancelled(true);
        ManhuntGame currentGame = ManhuntGame.getCurrentGame();
        if(!currentGame.getGameState().equals(GameState.STARTING)) return;

        Player player = event.getPlayer();
        if (
                event.getAction() == Action.LEFT_CLICK_AIR ||
                event.getAction() == Action.LEFT_CLICK_BLOCK ||
                event.getAction() == Action.RIGHT_CLICK_AIR ||
                event.getAction() == Action.RIGHT_CLICK_BLOCK
        ) new ClassSelection(Manhunt.getPlayerMenuUtility(player)).open();
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
