package org.velas.manhunt.dropSystem.lootProvider;

import org.bukkit.Location;
import org.bukkit.Material;
import org.velas.manhunt.dropSystem.loot.LootItem;
import org.velas.manhunt.dropSystem.loot.LootItemGroup;
import org.velas.manhunt.dropSystem.loot.LootSelector;

import java.util.ArrayList;
import java.util.List;

public enum RespawnDLP implements LootProvider {
    INSTANCE;

    @Override
    public List<LootItem> generateLoot(Location location) {

        List<LootItem> result = new ArrayList<>();

        LootItemGroup wooden = new LootItemGroup(
                List.of(
                        new LootItem(Material.WOODEN_SWORD, 30, 1, 1),
                        new LootItem(Material.WOODEN_PICKAXE, 30, 1, 1),
                        new LootItem(Material.WOODEN_AXE, 20, 1, 1),
                        new LootItem(Material.WOODEN_SHOVEL, 10, 1, 1),
                        new LootItem(Material.WOODEN_HOE, 10, 1, 1)
                )
                ,2);

        LootItemGroup stone = new LootItemGroup(
                List.of(
                        new LootItem(Material.STONE_SWORD, 30, 1, 1),
                        new LootItem(Material.STONE_PICKAXE, 30, 1, 1),
                        new LootItem(Material.STONE_AXE, 20, 1, 1),
                        new LootItem(Material.STONE_SHOVEL, 10, 1, 1),
                        new LootItem(Material.STONE_HOE, 10, 1, 1)
                ),2);

        LootItemGroup food = new LootItemGroup(
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
                ,1);

        LootItemGroup oakWood = new LootItemGroup(
                List.of(
                        new LootItem(Material.OAK_LOG, 20, 2, 6),
                        new LootItem(Material.OAK_PLANKS, 40, 4, 12),
                        new LootItem(Material.STICK, 60, 6, 18)
                )
                ,1);

        result.addAll(LootSelector.selectFromGroups(List.of(wooden, stone), 2));
        result.addAll(LootSelector.selectFromGroup(food));
        result.addAll(LootSelector.selectFromGroup(oakWood));

        return result;
    }
}
