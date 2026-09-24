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

public enum EarlyCombatDLP implements LootProvider {
    INSTANCE;

    @Override
    public List<LootItem> generateLoot(Location location) {

        List<LootItem> result = new ArrayList<>();

        LootItemGroup combat = new LootItemGroup(List.of(
                new LootItem(Material.IRON_SWORD, 55, 1, 1, Map.of(Enchantment.DAMAGE_ALL, 1)),
                new LootItem(Material.IRON_SWORD, 20, 1, 1, Map.of(Enchantment.DAMAGE_ALL, 2)),
                new LootItem(Material.IRON_HELMET, 35, 1, 1, Map.of(Enchantment.PROTECTION_ENVIRONMENTAL, 1)),
                new LootItem(Material.IRON_BOOTS, 35, 1, 1, Map.of(Enchantment.PROTECTION_ENVIRONMENTAL, 1)),
                new LootItem(Material.SHIELD, 45, 1, 1),
                new LootItem(Material.BOW, 22, 1, 1, Map.of(Enchantment.ARROW_DAMAGE, 1))
        ), 2);

        LootItemGroup consumable = new LootItemGroup(List.of(
                new LootItem(Material.GOLDEN_APPLE, 22, 1, 1),
                new LootItem(Material.COOKED_BEEF, 70, 6, 12),
                new LootItem(Material.ARROW, 65, 16, 32),
                new LootItem(Material.EXPERIENCE_BOTTLE, 25, 6, 12)
        ), 2);

        result.addAll(LootSelector.selectFromGroup(combat));
        result.addAll(LootSelector.selectFromGroup(consumable));
        return result;
    }
}
