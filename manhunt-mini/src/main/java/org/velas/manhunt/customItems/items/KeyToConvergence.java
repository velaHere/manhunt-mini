package org.velas.manhunt.customItems.items;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.Color;
import org.velas.manhunt.Manhunt;
import org.velas.manhunt.Utils;
import org.velas.manhunt.customItems.CustomItem;
import org.velas.manhunt.game.GameState;
import org.velas.manhunt.game.ManhuntGame;
import org.velas.manhunt.game.ManhuntManager;

import java.util.ArrayList;
import java.util.List;

public final class KeyToConvergence implements CustomItem {

    public static final KeyToConvergence INSTANCE;

    private final NamespacedKey namespacedKey;

    static{
        INSTANCE=new KeyToConvergence();
    }

    private KeyToConvergence(){
        this.namespacedKey = new NamespacedKey(Manhunt.getINSTANCE(), "key_to_convergence");
    }

    @Override
    public NamespacedKey getKey() {
        return this.namespacedKey;
    }

    @Override
    public ItemStack createItem() {

        ItemStack item = new ItemStack(Material.CARROT_ON_A_STICK);
        ItemMeta meta = item.getItemMeta();
        if(meta==null){
            Utils.warning("Meta was null in KeyOfConvergence");
            return item;
        }
        meta.setDisplayName(ChatColor.DARK_PURPLE + "\uD83D\uDD2E Eye of Convergence\n");
        meta.setCustomModelData(12346);

        List<String> lores = getStrings();
        meta.setLore(lores);

        meta.getPersistentDataContainer().set(this.namespacedKey, PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(meta);
        return item;
    }

    private static @NotNull List<String> getStrings() {
        List<String> lores = new ArrayList<>();
        lores.add(" ");
        lores.add(ChatColor.AQUA + "A singular key, heavy with purpose");
        lores.add(ChatColor.AQUA + "All paths bend towards the end");
        lores.add(" ");
        lores.add(ChatColor.DARK_AQUA + "• The world grows silent when it awakens");
        lores.add(ChatColor.DARK_AQUA + "• Space folds where it gazes");
        lores.add(ChatColor.DARK_AQUA + "• A key, not a guide");
        lores.add(" ");
        lores.add(ChatColor.DARK_PURPLE + "Right-click to converge with the End");
        return lores;
    }

    @Override
    public void registerRecipe(JavaPlugin plugin) {
        ShapedRecipe recipe = new ShapedRecipe(this.namespacedKey, this.createItem());
        recipe.shape(
                "  R",
                "PR ",
                "EP "
        );
        recipe.setIngredient('R', Material.BLAZE_ROD);
        recipe.setIngredient('P', Material.BLAZE_POWDER);
        recipe.setIngredient('E', Material.ENDER_PEARL);
        Bukkit.addRecipe(recipe);
    }

    @Override
    public void onUse(PlayerInteractEvent event) {

        World world = event.getPlayer().getWorld();
        if(!world.getEnvironment().equals(World.Environment.NORMAL)) {
            event.getPlayer().sendMessage(Color.RED+"You can only use this item in OVERWORLD");
            return;
        }

        ItemStack item = event.getItem();
        if(item==null) return;
        if(!event.getAction().equals(Action.RIGHT_CLICK_AIR) && !event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;

        Player player = event.getPlayer();

        ManhuntGame game = ManhuntGame.getCurrentGame();

        if(!game.getGameState().equals(GameState.VILLAGE)){
            if(game.getGameState().equals(GameState.STRONGHOLD) ||
                    game.getGameState().equals(GameState.TRANSITION)){
                player.sendMessage(ChatColor.DARK_PURPLE+"The Destiny of this world has already been changed!");
                player.sendMessage(ChatColor.DARK_PURPLE+"All paths converges to the end...");
                return;
            }
            player.sendMessage(Color.RED + "You cannot use this item right now");
            return;
        }

        game.setGameState(GameState.TRANSITION);

        player.getInventory().remove(item);
        ManhuntManager manager = ManhuntGame.getCurrentGame().getGameManager();
        manager.getGameTimeLine().setStrongholdTime(manager.getGameTimeLine().getTime());

        Bukkit.getOnlinePlayers().forEach(player1 -> {

            if(player1.equals(player)){
                player.sendMessage(Color.AQUA + "The forces beyond have accepted your wish. Praise will follow.");
                return;
            }
            player1.sendMessage(Color.AQUA + "The forces beyond have accepted " + player.getName() + "'s wish. Praise will follow.");
        });
        manager.getWorldManager().getOverworld().getWorldBorder().reset();
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
