package org.velas.manhunt.dropSystem.lootProvider;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.velas.manhunt.dropSystem.loot.LootItem;
import org.velas.manhunt.dropSystem.loot.LootItemGroup;
import org.velas.manhunt.dropSystem.loot.LootSelector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public enum CombatDLP implements LootProvider {
    INSTANCE;

    @Override
    public List<LootItem> generateLoot(Location location) {

        List<LootItem> result = new ArrayList<>();

        LootItemGroup weapons = new LootItemGroup(
                List.of(
                        new LootItem(Material.IRON_SWORD, 25, 1, 1,
                                Map.of(Enchantment.DAMAGE_ALL, 2)),
                        new LootItem(Material.DIAMOND_SWORD, 12, 1, 1,
                                Map.of(Enchantment.DAMAGE_ALL, 1)),
                        new LootItem(Material.DIAMOND_SWORD, 8, 1, 1,
                                Map.of(Enchantment.DAMAGE_ALL, 2)),
                        new LootItem(Material.DIAMOND_SWORD, 4, 1, 1,
                                Map.of(Enchantment.DAMAGE_ALL, 3)),
                        new LootItem(Material.NETHERITE_SWORD, 1, 1, 1,
                                Map.of(Enchantment.DAMAGE_ALL, 2))
                ),
                1
        );

        LootItemGroup armor = new LootItemGroup(
                List.of(
                        // Iron Prot II pieces
                        new LootItem(Material.IRON_HELMET, 10, 1, 1,
                                Map.of(Enchantment.PROTECTION_ENVIRONMENTAL, 2)),
                        new LootItem(Material.IRON_CHESTPLATE, 10, 1, 1,
                                Map.of(Enchantment.PROTECTION_ENVIRONMENTAL, 2)),
                        new LootItem(Material.IRON_LEGGINGS, 10, 1, 1,
                                Map.of(Enchantment.PROTECTION_ENVIRONMENTAL, 2)),
                        new LootItem(Material.IRON_BOOTS, 10, 1, 1,
                                Map.of(Enchantment.PROTECTION_ENVIRONMENTAL, 2)),

                        // Diamond Prot I
                        new LootItem(Material.DIAMOND_HELMET, 6, 1, 1,
                                Map.of(Enchantment.PROTECTION_ENVIRONMENTAL, 1)),
                        new LootItem(Material.DIAMOND_CHESTPLATE, 6, 1, 1,
                                Map.of(Enchantment.PROTECTION_ENVIRONMENTAL, 1)),
                        new LootItem(Material.DIAMOND_LEGGINGS, 6, 1, 1,
                                Map.of(Enchantment.PROTECTION_ENVIRONMENTAL, 1)),
                        new LootItem(Material.DIAMOND_BOOTS, 6, 1, 1,
                                Map.of(Enchantment.PROTECTION_ENVIRONMENTAL, 1)),

                        // Diamond Prot II (rare)
                        new LootItem(Material.DIAMOND_CHESTPLATE, 2, 1, 1,
                                Map.of(Enchantment.PROTECTION_ENVIRONMENTAL, 2)),
                        new LootItem(Material.DIAMOND_LEGGINGS, 2, 1, 1,
                                Map.of(Enchantment.PROTECTION_ENVIRONMENTAL, 2))
                ),
                2
        );

        LootItemGroup shields = new LootItemGroup(
                List.of(
                        new LootItem(Material.SHIELD, 80, 1, 1),
                        new LootItem(Material.SHIELD, 20, 1, 1,
                                Map.of(Enchantment.DURABILITY, 2))
                ),
                1
        );

        LootItemGroup projectiles = new LootItemGroup(
                List.of(
                        new LootItem(Material.ARROW, 40, 32, 64),
                        new LootItem(Material.SPECTRAL_ARROW, 15, 16, 32),
                        new LootItem(Material.TIPPED_ARROW, 10, 8, 16),
                        new LootItem(Material.ARROW, 10, 64, 96),
                        new LootItem(Material.EXPERIENCE_BOTTLE, 25, 12, 24)
                ),
                1
        );

        LootItemGroup utilities = new LootItemGroup(
                List.of(
                        new LootItem(Material.WATER_BUCKET, 40, 1, 1),
                        new LootItem(Material.LAVA_BUCKET, 15, 1, 1),
                        new LootItem(Material.COBBLESTONE, 40, 32, 48),
                        new LootItem(Material.OAK_PLANKS, 40, 32, 48),
                        new LootItem(Material.GOLDEN_APPLE, 15, 1, 2),
                        new LootItem(Material.ENDER_PEARL, 12, 1, 2),
                        new LootItem(Material.BOW, 15, 1, 1,
                                Map.of(Enchantment.ARROW_DAMAGE, 2)),
                        new LootItem(Material.BOW, 6, 1, 1,
                                Map.of(Enchantment.ARROW_DAMAGE, 3))
                ),
                2
        );

        LootItemGroup special = new LootItemGroup(
                List.of(
                        new LootItem(Material.ENDER_PEARL, 20, 1, 2),
                        new LootItem(Material.GOLDEN_APPLE, 10, 1, 1),
                        new LootItem(Material.ENCHANTED_GOLDEN_APPLE, 1, 1, 1),
                        new LootItem(Material.EXPERIENCE_BOTTLE, 20, 16, 32)
                ),
                1
        );

        // FINAL SELECTION
        result.addAll(LootSelector.selectFromGroup(weapons));
        result.addAll(LootSelector.selectFromGroup(armor));
        result.addAll(LootSelector.selectFromGroup(shields));
        result.addAll(LootSelector.selectFromGroup(projectiles));
        result.addAll(LootSelector.selectFromGroup(utilities));
        result.addAll(LootSelector.selectFromGroup(special));

        return result;
    }
}
