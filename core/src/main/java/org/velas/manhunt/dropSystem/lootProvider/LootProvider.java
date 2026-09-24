package org.velas.manhunt.dropSystem.lootProvider;

import org.bukkit.Location;
import org.velas.manhunt.dropSystem.loot.LootItem;

import java.util.List;

public interface LootProvider {
    List<LootItem> generateLoot(Location location);
}
