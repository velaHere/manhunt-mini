package org.velas.manhunt.menusystem.menu;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.velas.manhunt.menusystem.Menu;
import org.velas.manhunt.menusystem.PlayerMenuUtility;
import org.velas.manhunt.menusystem.menu.PlayersList;
import org.velas.manhunt.menusystem.menu.TeamMenu;


public class TeamOptions extends Menu {

    public TeamOptions(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "Add/Remove";
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

        String option = item.getItemMeta().getDisplayName().toLowerCase().substring(2);

        if (item.getType() == Material.ARROW) {
            new TeamMenu(playerMenuUtility).open();
        } else {
            playerMenuUtility.setSelectionTeamOption(option);
            new PlayersList(playerMenuUtility).open();
        }

        event.setCancelled(true);
    }

    @Override
    public void setMenuItems() {

        ItemStack add = new ItemStack(Material.LIME_CONCRETE);
        ItemMeta addMeta = add.getItemMeta();
        addMeta.setDisplayName(ChatColor.GREEN + "Add");
        add.setItemMeta(addMeta);

        ItemStack remove = new ItemStack(Material.RED_CONCRETE);
        ItemMeta removeMeta = remove.getItemMeta();
        removeMeta.setDisplayName(ChatColor.RED + "Remove");
        remove.setItemMeta(removeMeta);

        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(ChatColor.WHITE + "Back");
        back.setItemMeta(backMeta);

        inventory.setItem(11, add);
        inventory.setItem(15, remove);
        inventory.setItem(26, back);

    }
}
