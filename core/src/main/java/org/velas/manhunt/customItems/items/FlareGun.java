package org.velas.manhunt.customItems.items;

import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.FireworkExplodeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.velas.manhunt.Color;
import org.velas.manhunt.Manhunt;
import org.velas.manhunt.Utils;
import org.velas.manhunt.customItems.CustomItem;
import org.velas.manhunt.customItems.CustomItemType;
import org.velas.manhunt.customItems.CustomRecipe;
import org.velas.manhunt.dropSystem.CargoDragon;
import org.velas.manhunt.dropSystem.drops.Flare;
import org.velas.manhunt.game.GameState;
import org.velas.manhunt.game.ManhuntGame;

import java.util.List;

public final class FlareGun implements CustomItem {

    public static final FlareGun INSTANCE;

    private final NamespacedKey namespacedKey;

    static{
        INSTANCE=new FlareGun();
    }

    private FlareGun(){
        this.namespacedKey=new NamespacedKey(Manhunt.getINSTANCE(), CustomItemType.FLARE_GUN);
    }

    @Override
    public NamespacedKey getKey() {
        return this.namespacedKey;
    }

    public void onExplode(FireworkExplodeEvent event) {

        Firework firework = event.getEntity();
        if(!(firework.getShooter() instanceof Player player)) return;

        new CargoDragon(new Flare(firework.getLocation(), player.getName()));
    }

    @Override
    public void onUse(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        World world = player.getWorld();
        ManhuntGame game = ManhuntGame.getCurrentGame();

        if(!game.isRunning()){
            event.setCancelled(true);
        }else if (world.getEnvironment() != World.Environment.NORMAL) {

            event.setCancelled(true);

            player.sendMessage(Color.RED + "You can only use flares in the Overworld!");
            world.playSound(player.getLocation(),
                    Sound.ENTITY_VILLAGER_NO, 1f, 1f);
        }else if(game.getGameState().equals(GameState.TRANSITION)){
            event.setCancelled(true);
        }
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

    @Override
    public ItemStack createItem() {
        ItemStack flareRocket = new ItemStack(Material.FIREWORK_ROCKET);
        FireworkMeta meta = (FireworkMeta) flareRocket.getItemMeta();
        if(meta==null){
            Utils.warning("meta was null in FlareGun!");
            return flareRocket;
        }

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(
                ItemFlag.HIDE_ATTRIBUTES,
                ItemFlag.HIDE_ENCHANTS,
                ItemFlag.HIDE_UNBREAKABLE,
                ItemFlag.HIDE_DESTROYS,
                ItemFlag.HIDE_PLACED_ON,
                ItemFlag.HIDE_DYE,
                ItemFlag.HIDE_POTION_EFFECTS
        );

        // 🔥 Firework explosion style (THIS controls how it looks when used)
        FireworkEffect effect = FireworkEffect.builder()
                .with(FireworkEffect.Type.BALL_LARGE)
                .withColor(
                        org.bukkit.Color.RED,
                        org.bukkit.Color.ORANGE,
                        org.bukkit.Color.YELLOW,
                        org.bukkit.Color.WHITE
                )
                .withFade(org.bukkit.Color.WHITE)
                .flicker(true)
                .trail(true)
                .build();

        meta.clearEffects();
        meta.addEffect(effect);
        meta.setPower(4); // how high it goes before exploding

        meta.setDisplayName(Color.AQUA + "" +  Color.BOLD + "Flare");

        meta.setLore(List.of(
                Color.PINK + "Launch this flare to",
                Color.PINK + "call an airdrop at",
                Color.PINK + "the landing location"
        ));

        meta.getPersistentDataContainer().set(this.namespacedKey, PersistentDataType.BYTE, (byte) 1);

        flareRocket.setItemMeta(meta);

        return flareRocket;
    }

    @Override
    public void registerRecipe(JavaPlugin plugin) {
        ShapedRecipe recipe = new ShapedRecipe(this.namespacedKey, this.createItem());

        ItemStack rocket = new ItemStack(Material.FIREWORK_ROCKET);

        FireworkMeta meta = (FireworkMeta) rocket.getItemMeta();
        if(meta == null) return;
        meta.setPower(1);

        rocket.setItemMeta(meta);

        recipe.shape(
                " G ",
                "GRG",
                " G "
        );
        recipe.setIngredient('G', new RecipeChoice.ExactChoice(new ItemStack(Material.GOLD_INGOT)));
        recipe.setIngredient('R', new RecipeChoice.ExactChoice(rocket));

        CustomRecipe.register(
                CustomItemType.FLARE_GUN,
                new CustomRecipe("Flare Gun", this)
                    .shape(new String[]{" G ", "GRG", " G "})
                    .setIngredient('G', new ItemStack(Material.GOLD_INGOT))
                    .setIngredient('R', rocket)
        );

        Bukkit.addRecipe(recipe);
    }
}
