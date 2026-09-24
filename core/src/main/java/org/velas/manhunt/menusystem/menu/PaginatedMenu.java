package org.velas.manhunt.menusystem.menu;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.velas.manhunt.menusystem.Menu;
import org.velas.manhunt.menusystem.PlayerMenuUtility;

public abstract class PaginatedMenu extends Menu {

    protected int page = 0;
    protected int maxItemsPerPage = 28;
    protected int index = 0;

    public PaginatedMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    public void addMenuBorder(){

        ItemStack left = new ItemStack(Material.DARK_OAK_BUTTON, 1);
        ItemMeta leftMeta = left.getItemMeta();
        leftMeta.setDisplayName(ChatColor.GREEN + "Left");
        left.setItemMeta(leftMeta);

        inventory.setItem(48, left);

        ItemStack close = new ItemStack(Material.BARRIER, 1);
        ItemMeta close_meta = close.getItemMeta();
        close_meta.setDisplayName(ChatColor.DARK_RED + "Close");
        close.setItemMeta(close_meta);

        inventory.setItem(49, close);

        ItemStack right = new ItemStack(Material.DARK_OAK_BUTTON, 1);
        ItemMeta rightMeta = right.getItemMeta();
        rightMeta.setDisplayName(ChatColor.GREEN + "Right");
        right.setItemMeta(rightMeta);

        inventory.setItem(50, right);

        for(int i = 0; i<10; i++){
            if(inventory.getItem(i) == null){
                inventory.setItem(i, super.FILLER_GLASS);
            }
        }

        inventory.setItem(17, super.FILLER_GLASS);
        inventory.setItem(18, super.FILLER_GLASS);
        inventory.setItem(26, super.FILLER_GLASS);
        inventory.setItem(27, super.FILLER_GLASS);
        inventory.setItem(35, super.FILLER_GLASS);
        inventory.setItem(36, super.FILLER_GLASS);

        for(int i = 44; i<54; i++){
            if(inventory.getItem(i)==null){
                inventory.setItem(i, super.FILLER_GLASS);
            }
        }
    }
}
