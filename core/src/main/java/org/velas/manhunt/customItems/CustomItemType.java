package org.velas.manhunt.customItems;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.Manhunt;
import org.velas.manhunt.customItems.items.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class CustomItemType {

    private static final Map<String, CustomItem> ITEMS = new LinkedHashMap<>();

    public static final String FLARE_GUN = "FLARE_GUN";
    public static final String TRACKER_COMPASS = "TRACKER_COMPASS";
    public static final String SPECTATOR_COMPASS = "SPECTATOR_COMPASS";
    public static final String KEY_TO_CONVERGENCE = "KEY_TO_CONVERGENCE";
    public static final String IRON_REAVER = "IRON_REAVER";
    public static final String GOLA = "GOLA";
    public static final String AVATAR_SIGIL = "AVATAR_SIGIL";
    public static final String PRIMARY_ABILITY_ACTIVATOR = "PRIMARY_ABILITY_ACTIVATOR";
    public static final String SECONDARY_ABILITY_ACTIVATOR = "SECONDARY_ABILITY_ACTIVATOR";
    public static final String BACK_TO_LOBBY = "BACK_TO_LOBBY";

    private CustomItemType(){}

    private static void registerAll(){
        ITEMS.put(FLARE_GUN, FlareGun.INSTANCE);
        ITEMS.put(TRACKER_COMPASS, TrackerCompass.INSTANCE);
        ITEMS.put(SPECTATOR_COMPASS, SpectatorCompass.INSTANCE);
        ITEMS.put(AVATAR_SIGIL, AvatarSigil.INSTANCE);
        ITEMS.put(PRIMARY_ABILITY_ACTIVATOR, PrimaryAbilityActivator.INSTANCE);
        ITEMS.put(SECONDARY_ABILITY_ACTIVATOR, SecondaryAbilityActivator.INSTANCE);
        ITEMS.put(BACK_TO_LOBBY, BackToLobbyItem.INSTANCE);
        Manhunt.getPROVIDER().registerCustomItems();
    }

    public static void register(String id, CustomItem customItem){
        if(ITEMS.containsKey(id))
            throw new IllegalArgumentException(id + ", is already registered");
        ITEMS.put(id, customItem);
    }

    public static void registerRecipes(JavaPlugin plugin){
        registerAll();
        for(CustomItem customItem: ITEMS.values()){
            customItem.registerRecipe(plugin);
        }
    }

    public static NamespacedKey getKey(@NotNull String id){
        CustomItem item = ITEMS.get(id);
        if(item==null)
            throw new IllegalArgumentException("Item not found: " + id);
        return item.getKey();
    }

    public static boolean isCustomItem(@NotNull ItemStack item) {
        return ITEMS.values().stream().anyMatch(customItem -> customItem.matches(item));
    }

    public static Optional<CustomItem> getCustomItem(@NotNull String id){
        CustomItem item = ITEMS.get(id);
        if(item==null) return Optional.empty();
        return Optional.of(item);
    }

    public static boolean matches(@NotNull ItemStack item, @NotNull String id) {
        Optional<CustomItem> customItem = fromItem(item);
        if(customItem.isEmpty()) return false;
        return ITEMS.get(id).matches(item);
    }

    public static ItemStack create(@NotNull String id){
        return getOrThrow(id).createItem();
    }

    public static CustomItem getOrThrow(@NotNull String id){
        CustomItem item = ITEMS.get(id);
        if(item==null)
            throw new NullPointerException(id + "was not registers as a custom item");
        return item;
    }

    public static Optional<CustomItem> fromItem(ItemStack itemStack){
        for(CustomItem customItem: ITEMS.values()){
            if(customItem.matches(itemStack)) return Optional.of(customItem);
        }
        return Optional.empty();
    }
}
