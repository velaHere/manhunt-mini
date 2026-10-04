package org.velas.manhunt.menusystem;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public abstract class Menu implements InventoryHolder {

    protected Inventory inventory;

    protected PlayerMenuUtility playerMenuUtility;

    protected final ItemStack FILLER_GLASS;

    public Menu(PlayerMenuUtility playerMenuUtility){
        this.playerMenuUtility = playerMenuUtility;

        ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta itemMeta = filler.getItemMeta();
        if(itemMeta != null) itemMeta.setDisplayName(" ");

        filler.setItemMeta(itemMeta);
        this.FILLER_GLASS = filler;
    }

    public abstract String getMenuName();

    public abstract int getSlots();

    public abstract void handleMenu(InventoryClickEvent event);

    public abstract void setMenuItems();

    public void open(){
        inventory = Bukkit.createInventory(this, getSlots(), getMenuName());
        this.setMenuItems();
        playerMenuUtility.getOwner().openInventory(inventory);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
