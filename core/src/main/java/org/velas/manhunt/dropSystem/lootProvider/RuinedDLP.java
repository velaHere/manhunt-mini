package org.velas.manhunt.dropSystem.lootProvider;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;
import org.velas.manhunt.Utils;
import org.velas.manhunt.dropSystem.loot.LootItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum RuinedDLP implements LootProvider {
    INSTANCE;

    @Override
    public List<LootItem> generateLoot(Location location) {

        List<LootItem> items = new ArrayList<>();

        LootTable table = Bukkit.getLootTable(
                NamespacedKey.minecraft("chests/ruined_portal")
        );

        LootContext context = new LootContext.Builder(location).build();

        if(table == null){
            Utils.warning("Table was null in the AncientDrop");
            return items;
        }

        for(ItemStack itemStack: table.populateLoot(new Random(), context)){
            items.add(new LootItem(itemStack));
        }

        return items;
    }
}
