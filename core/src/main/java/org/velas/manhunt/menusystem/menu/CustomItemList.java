package org.velas.manhunt.menusystem.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.velas.manhunt.Manhunt;
import org.velas.manhunt.customItems.CustomItem;
import org.velas.manhunt.customItems.CustomItemType;
import org.velas.manhunt.customItems.CustomRecipe;
import org.velas.manhunt.menusystem.PlayerMenuUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomItemList extends PaginatedMenu {

    private final List<CustomRecipe> recipes = new ArrayList<>(CustomRecipe.getRecipeList());

    public CustomItemList(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "Recipes";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {

        event.setCancelled(true);

        ItemStack item = event.getCurrentItem();

        if(item == null || item.getType().equals(Material.AIR)) return;
        if(item.getType().equals(Material.GRAY_STAINED_GLASS_PANE)) return;

        if (item.getType().equals(Material.BARRIER)){
            event.getWhoClicked().closeInventory();
            return;
        }

        if(item.getType().equals(Material.DARK_OAK_BUTTON)){
            ItemMeta meta = item.getItemMeta();
            if(meta == null) return;
            String option = meta.getDisplayName().substring(2);
            if(option.equals("Left")){
                if(page==0) return;
                page=page-1;
                super.open();
            }else if(option.equals("Right")){
                if(!((index+1) >= recipes.size())){
                    page=page+1;
                    super.open();
                }
            }
            return;
        }

        Optional<CustomItem> customItem = CustomItemType.fromItem(item);
        if(customItem.isEmpty()) return;

        CustomRecipe recipe = CustomRecipe.getRecipe(customItem.get());
        if(recipe == null) return;

        if(event.getWhoClicked() instanceof Player player) {
            recipe.new RecipeGUI(Manhunt.getPlayerMenuUtility(player)).open();
        }
    }

    @Override
    public void setMenuItems() {
        super.addMenuBorder();
        for(int i =0; i<super.maxItemsPerPage; i++){
            index = super.maxItemsPerPage*page+i;
            if(index >= recipes.size()) break;
            CustomRecipe recipe = recipes.get(index);
            inventory.addItem(recipe.getResult());
        }
    }
}
