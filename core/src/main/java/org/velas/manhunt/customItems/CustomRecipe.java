package org.velas.manhunt.customItems;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.velas.manhunt.Manhunt;
import org.velas.manhunt.menusystem.Menu;
import org.velas.manhunt.menusystem.PlayerMenuUtility;
import org.velas.manhunt.menusystem.menu.CustomItemList;

import java.util.*;

public final class CustomRecipe {

    private static final Map<String, CustomRecipe> registeredRecipes = new HashMap<>();

    private final @NotNull String name;
    private final @NotNull CustomItem item;
    private String [] rows = new String[3];
    private final Map<Character, ItemStack> characterMapping = new HashMap<>();

    public CustomRecipe(@NotNull String name, @NotNull CustomItem item) {
        this.name = name;
        this.item = item;
    }

    public static void register(String id, CustomRecipe recipe) {
        if(registeredRecipes.containsKey(id)) return;
        registeredRecipes.put(id, recipe);
    }

    @Nullable
    public static CustomRecipe getRecipe(String id) {
        return registeredRecipes.get(id);
    }

    @Nullable
    public static CustomRecipe getRecipe(CustomItem item) {
        for(CustomRecipe recipe: registeredRecipes.values()) {
            if(item.matches(recipe.item.createItem())) return recipe;
        }
        return null;
    }

    public static Set<String> getRecipeIdList() {
        return registeredRecipes.keySet();
    }

    public static Collection<CustomRecipe> getRecipeList() {
        return registeredRecipes.values();
    }

    public ItemStack getResult() {
        return this.item.createItem();
    }

    public CustomRecipe shape(String [] rows) {
        if(rows.length != 3)
            throw new RuntimeException("Rows were not equal to 3 in creating the CustomRecipe");

        if(rows[0].length() != 3 || rows[1].length() != 3 || rows[2].length() != 3)
            throw new RuntimeException("Columns were not equal to 3 in creating the CustomRecipe");

        this.rows = rows;
        return this;
    }

    public CustomRecipe setIngredient(char character, ItemStack ingredient) {

        if (ingredient == null || ingredient.getType().isAir()) {
            throw new IllegalArgumentException(
                    "Ingredient cannot be null or air"
            );
        }

        boolean found = false;

        for (String row : rows) {
            if (row.indexOf(character) != -1) {
                found = true;
                break;
            }
        }

        if (!found) {
            throw new IllegalArgumentException(
                    "Character '" + character +
                            "' does not exist in the recipe shape"
            );
        }

        characterMapping.put(character, ingredient.clone());

        return this;
    }

    public class RecipeGUI extends Menu {

        public RecipeGUI(PlayerMenuUtility playerMenuUtility) {
            super(playerMenuUtility);
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
            event.setCancelled(true);

            ItemStack item = event.getCurrentItem();
            if(item == null || item.getType().equals(Material.AIR)) return;
            ItemMeta meta = item.getItemMeta();
            if(meta == null) return;

            if(event.getWhoClicked() instanceof Player player) {
                if(item.getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Back")) new CustomItemList(Manhunt.getPlayerMenuUtility(player)).open();
            }
        }

        @Override
        public void setMenuItems() {

            int[] slots = {
                    1, 2, 3,
                    10, 11, 12,
                    19, 20, 21
            };

            for (int row = 0; row < 3; row++) {
                for (int column = 0; column < 3; column++) {

                    char character = rows[row].charAt(column);

                    // Empty space in the recipe
                    if (character == ' ') {
                        continue;
                    }

                    ItemStack ingredient = characterMapping.get(character);

                    if (ingredient == null) {
                        continue;
                    }

                    inventory.setItem(
                            slots[row * 3 + column],
                            ingredient.clone()
                    );
                }
            }

            inventory.setItem(15, item.createItem());

            ItemStack back = new ItemStack(Material.ARROW);
            ItemMeta meta = back.getItemMeta();
            if(meta == null) return;
            meta.setDisplayName(ChatColor.YELLOW + "Back");
            back.setItemMeta(meta);

            inventory.setItem(26, back);

            List<Integer> filled = List.of(1, 2, 3, 10, 11, 12, 19, 20, 21, 15, 26);

            for(int i = 0; i < 27; i++) {
                if(filled.contains(i)) continue;
                inventory.setItem(i, FILLER_GLASS);
            }
        }
    }
}
