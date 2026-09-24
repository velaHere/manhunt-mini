package org.velas.manhunt.menusystem.menu;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.Color;
import org.velas.manhunt.menusystem.Menu;
import org.velas.manhunt.menusystem.PlayerMenuUtility;

import java.util.List;

public class YesOrNo extends Menu {

    private final @NotNull String name;
    private final @NotNull Runnable yes;
    private final @NotNull Runnable no;

    public YesOrNo(PlayerMenuUtility playerMenuUtility, @NotNull String name, @NotNull Runnable yes, @NotNull Runnable no) {
        super(playerMenuUtility);
        this.name=name;
        this.yes=yes;
        this.no=no;
    }

    @Override
    public String getMenuName() {
        return name;
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        if(event.isShiftClick()) {
            event.setCancelled(true);
            return;
        }

        ItemStack item = event.getCurrentItem();

        if (item == null || item.getType().equals(Material.AIR)) return;

        if(item.getType().equals(Material.LIME_WOOL)) yes.run();
        if(item.getType().equals(Material.RED_WOOL)) no.run();

        event.setCancelled(true);
    }

    @Override
    public void setMenuItems() {
        inventory.clear();

        ItemStack yes = new ItemStack(Material.LIME_WOOL);
        ItemMeta yesMeta = yes.getItemMeta();
        if(yesMeta != null) {
            yesMeta.setDisplayName(Color.GREEN + "✔ YES");
            yesMeta.setLore(List.of(Color.GRAY + "Confirm"));
            yes.setItemMeta(yesMeta);
        }

        ItemStack no = new ItemStack(Material.RED_WOOL);
        ItemMeta noMeta = no.getItemMeta();
        if(noMeta != null) {
            noMeta.setDisplayName(Color.RED + "✘ NO");
            noMeta.setLore(List.of(Color.GRAY + "Cancel"));
            no.setItemMeta(noMeta);
        }

        inventory.setItem(11, yes);
        inventory.setItem(15, no);
    }
}
