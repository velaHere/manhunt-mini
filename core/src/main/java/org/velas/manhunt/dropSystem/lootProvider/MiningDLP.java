package org.velas.manhunt.dropSystem.lootProvider;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.velas.manhunt.dropSystem.loot.LootItem;
import org.velas.manhunt.dropSystem.loot.LootItemGroup;
import org.velas.manhunt.dropSystem.loot.LootSelector;
import org.velas.manhunt.dropSystem.lootProvider.LootProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public enum MiningDLP implements LootProvider {
    INSTANCE;

    @Override
    public List<LootItem> generateLoot(Location location) {

        List<LootItem> result = new ArrayList<>();

        // --- Pickaxes Group (1 selection) ---
        LootItemGroup pickaxes = new LootItemGroup(
                List.of(
                        new LootItem(Material.STONE_PICKAXE, 40, 1, 1),
                        new LootItem(Material.IRON_PICKAXE, 30, 1, 1),
                        new LootItem(Material.GOLDEN_PICKAXE, 20, 1, 1),
                        new LootItem(Material.DIAMOND_PICKAXE, 10, 1, 1),

                        new LootItem(Material.IRON_PICKAXE, 15, 1, 1, Map.of(Enchantment.DIG_SPEED, 2)),
                        new LootItem(Material.IRON_PICKAXE, 12, 1, 1, Map.of(Enchantment.LOOT_BONUS_BLOCKS, 1)),
                        new LootItem(Material.IRON_PICKAXE, 4, 1, 1, Map.of(Enchantment.LOOT_BONUS_BLOCKS, 1, Enchantment.DIG_SPEED, 2)),
                        new LootItem(Material.DIAMOND_PICKAXE, 6, 1,1 ,Map.of(Enchantment.DIG_SPEED, 2)),
                        new LootItem(Material.DIAMOND_PICKAXE, 3, 1,1 ,Map.of(Enchantment.LOOT_BONUS_BLOCKS, 2)),
                        new LootItem(Material.DIAMOND_PICKAXE, 1, 1,1 ,Map.of(Enchantment.LOOT_BONUS_BLOCKS, 2, Enchantment.DIG_SPEED, 2))

                ), 1);

        // --- Ores Group (2 selections) ---
        LootItemGroup ores = new LootItemGroup(
                List.of(
                        new LootItem(Material.COAL, 40, 6, 16),
                        new LootItem(Material.IRON_INGOT, 30, 5, 11),
                        new LootItem(Material.COPPER_INGOT, 25, 3, 9),
                        new LootItem(Material.GOLD_INGOT, 15, 2, 5),
                        new LootItem(Material.REDSTONE, 20, 4, 12),
                        new LootItem(Material.LAPIS_LAZULI, 20, 4, 12),
                        new LootItem(Material.EMERALD, 5, 3, 6),
                        new LootItem(Material.DIAMOND, 5, 3, 6)
                ),
                3
        );

        // --- Utility Items (1 selection) ---
        LootItemGroup utility = new LootItemGroup(
                List.of(
                        new LootItem(Material.TORCH, 50, 8, 24),
                        new LootItem(Material.COAL_BLOCK, 20, 1, 2),
                        new LootItem(Material.BUCKET, 20, 1, 1),
                        new LootItem(Material.WATER_BUCKET, 15, 1, 1),
                        new LootItem(Material.LADDER, 30, 6, 18)
                ),
                2
        );

        // --- Cave Survival Items (1 selection) ---
        LootItemGroup survival = new LootItemGroup(
                List.of(
                        new LootItem(Material.BEEF, 60, 4, 12),
                        new LootItem(Material.COOKED_BEEF, 35, 1, 3),
                        new LootItem(Material.PORKCHOP, 60, 4, 12),
                        new LootItem(Material.COOKED_PORKCHOP, 30, 1, 3),
                        new LootItem(Material.MUTTON, 70, 5, 14),
                        new LootItem(Material.COOKED_MUTTON, 35, 2, 6),
                        new LootItem(Material.CHICKEN, 70, 5, 14),
                        new LootItem(Material.COOKED_CHICKEN, 30, 2, 6)
                )
                ,2);

        // SELECTING LOOT
        result.addAll(LootSelector.selectFromGroup(pickaxes));     // 1 pickaxe
        result.addAll(LootSelector.selectFromGroup(utility));      // 2 utility item
        result.addAll(LootSelector.selectFromGroup(survival));     // 2 survival item
        result.addAll(LootSelector.selectFromGroup(ores));         // 3 ore items

        return result;
    }
}
