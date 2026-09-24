package org.velas.manhunt.dropSystem.lootProvider;

import org.bukkit.Location;
import org.bukkit.Material;
import org.velas.manhunt.dropSystem.loot.LootItem;
import org.velas.manhunt.dropSystem.loot.LootItemGroup;
import org.velas.manhunt.dropSystem.loot.LootSelector;

import java.util.ArrayList;
import java.util.List;

public enum GatewayDLP implements LootProvider {
    INSTANCE;

    @Override
    public List<LootItem> generateLoot(Location location) {

        List<LootItem> result = new ArrayList<>();

        LootItemGroup portalBits = new LootItemGroup(List.of(
                new LootItem(Material.OBSIDIAN, 70, 5, 7),
                new LootItem(Material.OBSIDIAN, 25, 4, 6),
                new LootItem(Material.FLINT_AND_STEEL, 45, 1, 1),
                new LootItem(Material.FIRE_CHARGE, 25, 1, 3),
                new LootItem(Material.BUCKET, 25, 1, 1)
        ), 3);

        LootItemGroup basic = new LootItemGroup(List.of(
                new LootItem(Material.OBSIDIAN, 60, 2, 4),
                new LootItem(Material.IRON_INGOT, 55, 3, 7),
                new LootItem(Material.COBBLESTONE, 60, 24, 48),
                new LootItem(Material.OAK_PLANKS, 60, 24, 48),
                new LootItem(Material.TORCH, 50, 8, 16),
                new LootItem(Material.COOKED_BEEF, 70, 6, 12)
        ), 3);

        result.addAll(LootSelector.selectFromGroup(portalBits));
        result.addAll(LootSelector.selectFromGroup(basic));
        return result;
    }
}
