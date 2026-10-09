package org.velas.manhunt.customItems.items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.velas.manhunt.Manhunt;
import org.velas.manhunt.Utils;
import org.velas.manhunt.customItems.CustomItem;
import org.velas.manhunt.customItems.CustomItemType;


public final class IronReaver implements CustomItem {

    public static final IronReaver INSTANCE;

    private final NamespacedKey namespacedKey;

    static{
        INSTANCE=new IronReaver();
    }

    private IronReaver(){
        this.namespacedKey=new NamespacedKey(Manhunt.getINSTANCE(), CustomItemType.IRON_REAVER);
    }

    @Override
    public NamespacedKey getKey() {
        return this.namespacedKey;
    }

    @Override
    public ItemStack createItem() {
        ItemStack sword = new ItemStack(Material.IRON_SWORD, 1);
        ItemMeta meta = sword.getItemMeta();
        if(meta==null) return sword;

        meta.setCustomModelData(12348);
        meta.setDisplayName("Iron Reaver");
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(
                ItemFlag.HIDE_ATTRIBUTES,
                ItemFlag.HIDE_ENCHANTS,
                ItemFlag.HIDE_UNBREAKABLE,
                ItemFlag.HIDE_DESTROYS,
                ItemFlag.HIDE_PLACED_ON,
                ItemFlag.HIDE_DYE
        );
        meta.getPersistentDataContainer().set(this.namespacedKey, PersistentDataType.BYTE, (byte) 1);

        sword.setItemMeta(meta);
        return sword;
    }

    @Override
    public void registerRecipe(JavaPlugin plugin) {
        ShapedRecipe recipe = new ShapedRecipe(this.namespacedKey, this.createItem());
        recipe.shape(
                " I ",
                " I ",
                "IS "
        );
        recipe.setIngredient('I', Material.IRON_INGOT);
        recipe.setIngredient('S', Material.STICK);
        Bukkit.addRecipe(recipe);
    }

    @Override
    public void onUse(PlayerInteractEvent event) {

    }

    @Override
    public void onItemDrop(PlayerDropItemEvent event) {

    }

    @Override
    public void onPickup(EntityPickupItemEvent event) {

        if(!(event.getEntity() instanceof Player player)) return;

        ItemStack pickedItem = event.getItem().getItemStack();
        int heldItemSlot = player.getInventory().getHeldItemSlot();

        Bukkit.getScheduler().runTask(Manhunt.getINSTANCE(), () -> {

            ItemStack slotItem = player.getInventory().getItem(heldItemSlot);

            if (slotItem == null || slotItem.getType() == Material.AIR) return;

            if (slotItem.isSimilar(pickedItem)) {
                player.getWorld().playSound(
                        player.getLocation(),
                        "item.sword_draw",
                        SoundCategory.AMBIENT,
                        0.7f,
                        1.2f
                );
            }
        });
    }

    @Override
    public void onItemSwitch(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        player.getWorld().playSound(
                player.getLocation(),
                "item.sword_draw",
                SoundCategory.AMBIENT,
                0.7f,
                1.2f
        );
    }

    @Override
    public void placeItemToHeldSlot(InventoryClickEvent event){

        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.isCancelled()) return;

        Inventory clicked = event.getClickedInventory();
        if (clicked == null || clicked.getType() != InventoryType.PLAYER) return;

        int heldSlot = player.getInventory().getHeldItemSlot();
        if (event.getSlot() != heldSlot) return;

        ItemStack cursor = event.getCursor();
        if(cursor==null){
            Utils.warning("CursorItem was null in IronReaver");
            return;
        }
        if (cursor.getType().isAir()) return;

        ItemMeta meta = cursor.getItemMeta();
        if (meta == null) return;

        if (!meta.getPersistentDataContainer().has(namespacedKey, PersistentDataType.BYTE)) return;

        player.getWorld().playSound(
                player.getLocation(),
                "item.sword_draw",
                SoundCategory.AMBIENT,
                0.7f,
                1.2f
        );
    }

    @Override
    public void onItemSwap(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        player.getWorld().playSound(
                player.getLocation(),
                "item.sword_draw",
                SoundCategory.AMBIENT,
                0.7f,
                1.2f
        );
    }
}
