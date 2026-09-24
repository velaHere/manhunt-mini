package org.velas.manhunt.menusystem.menu;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.velas.manhunt.menusystem.Menu;
import org.velas.manhunt.menusystem.PlayerMenuUtility;

public class Shop extends Menu {

    public Shop(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "Shop";
    }

    @Override
    public int getSlots() {
        return 0;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {

        if(event.isShiftClick()) {
            event.setCancelled(true);
            return;
        }

        event.setCancelled(true);
    }

    @Override
    public void setMenuItems() {

    }
}
