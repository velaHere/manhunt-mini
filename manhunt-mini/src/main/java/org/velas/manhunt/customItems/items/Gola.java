package org.velas.manhunt.customItems.items;

import org.bukkit.*;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.recipe.CraftingBookCategory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.velas.manhunt.Manhunt;
import org.velas.manhunt.config.Settings;
import org.velas.manhunt.customItems.CustomItem;
import org.velas.manhunt.customItems.CustomItemType;
import org.velas.manhunt.customItems.CustomRecipe;
import org.velas.manhunt.game.GameState;
import org.velas.manhunt.game.ManhuntGame;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class Gola implements CustomItem {

    public static final Gola INSTANCE;
    public static final Map<UUID, Long> cooldowns = new HashMap<>();

    private final NamespacedKey namespacedKey;

    static{
        INSTANCE=new Gola();
    }

    private Gola(){
        this.namespacedKey=new NamespacedKey(Manhunt.getINSTANCE(), "gola");
    }

    @Override
    public NamespacedKey getKey() {
        return this.namespacedKey;
    }

    @Override
    public ItemStack createItem() {
        ItemStack item = new ItemStack(Material.FIRE_CHARGE, 3);

        ItemMeta meta = item.getItemMeta();
        if(meta!=null){
            meta.setDisplayName(ChatColor.RED + "Gola");
            meta.getPersistentDataContainer().set(this.namespacedKey, PersistentDataType.BYTE, (byte) 1);
            item.setItemMeta(meta);
        }
        return item;
    }

    @Override
    public void registerRecipe(JavaPlugin plugin) {

        Bukkit.removeRecipe(NamespacedKey.minecraft("fire_charge"));

        ShapelessRecipe recipe = new ShapelessRecipe(this.namespacedKey, this.createItem()) {
        };
        recipe.setCategory(CraftingBookCategory.MISC);
        recipe.addIngredient(Material.BLAZE_POWDER);
        recipe.addIngredient(Material.COAL);
        recipe.addIngredient(Material.GUNPOWDER);

        CustomRecipe.register(CustomItemType.GOLA, new CustomRecipe("Gola", this)
                .shape(new String[]{"BCG", "   ", "   "})
                .setIngredient('B', new ItemStack(Material.BLAZE_POWDER))
                .setIngredient('C', new ItemStack(Material.COAL))
                .setIngredient('G', new ItemStack(Material.GUNPOWDER)));

        Bukkit.addRecipe(recipe);
    }

    @Override
    public void onUse(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        Long cooldown = cooldowns.get(player.getUniqueId());

        if(cooldown != null && System.currentTimeMillis() < cooldown) return;

        cooldowns.put(player.getUniqueId(), System.currentTimeMillis() + Settings.config().item().gola().getCooldown() * 1000L);

        ManhuntGame currentGame = ManhuntGame.getCurrentGame();
        if(!currentGame.isRunning()) return;
        if(currentGame.getGameState().equals(GameState.TRANSITION)) return;
        if(!event.getAction().equals(Action.RIGHT_CLICK_AIR) && !event.getAction().equals(Action.RIGHT_CLICK_BLOCK)
        ) return;

        ItemStack item = event.getItem();
        if (item == null) return;

        if (item.getType() != Material.FIRE_CHARGE) return;

        launchFireball(player);
        if (player.getGameMode() != GameMode.CREATIVE) {
            item.setAmount(item.getAmount() - 1);
        }
    }

    @Override
    public void onItemDrop(PlayerDropItemEvent event) {

    }

    private void launchFireball(Player player) {
        Location eye = player.getEyeLocation();

        Fireball fireball = player.getWorld().spawn(eye, Fireball.class);

        fireball.setVelocity(eye.getDirection().multiply(1.5)); // speed
        fireball.setShooter(player);
        fireball.setMetadata("gola", new FixedMetadataValue(Manhunt.getINSTANCE(), true));
        fireball.setIsIncendiary(true);
        fireball.setYield(0);
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
