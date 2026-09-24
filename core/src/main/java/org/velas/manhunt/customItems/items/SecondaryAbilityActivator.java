package org.velas.manhunt.customItems.items;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.velas.manhunt.Manhunt;
import org.velas.manhunt.customItems.CustomItem;

public final class SecondaryAbilityActivator implements CustomItem {

    public static final SecondaryAbilityActivator INSTANCE;

    private final NamespacedKey namespacedKey;

    static {
        INSTANCE = new SecondaryAbilityActivator();
    }

    private SecondaryAbilityActivator(){
        this.namespacedKey=new NamespacedKey(Manhunt.getINSTANCE(), "secondary_ability_activator");
    }

    @Override
    public NamespacedKey getKey() {
        return this.namespacedKey;
    }

    @Override
    public ItemStack createItem() {
        ItemStack secondary = new ItemStack(Material.BLAZE_ROD);
        ItemMeta meta = secondary.getItemMeta();
        if(meta==null) return secondary;

        meta.setDisplayName("§eSECONDARY ABILITY");
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.getPersistentDataContainer().set(this.namespacedKey, PersistentDataType.BYTE, (byte) 1);
        secondary.setItemMeta(meta);
        return secondary;
    }

    @Override
    public void registerRecipe(JavaPlugin plugin) {

    }

    @Override
    public void onUse(PlayerInteractEvent event) {

    }

    @Override
    public void onItemDrop(PlayerDropItemEvent event) {

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
