package org.velas.manhunt.dropSystem.lootProvider;

import org.bukkit.Location;
import org.velas.manhunt.dropSystem.AirDropType;
import org.velas.manhunt.dropSystem.loot.LootItem;

import java.util.List;
import java.util.Random;

public enum FlareDLP implements LootProvider {
    INSTANCE;

    @Override
    public List<LootItem> generateLoot(Location location) {

        switch (new Random().nextInt(0,3)){
            case 0 -> {
                return AirDropType.ANCIENT.generateRandomLoot(location);
            }

            case 1 -> {
                return AirDropType.COMBAT.generateRandomLoot(location);
            }

            default -> {
                return AirDropType.END.generateRandomLoot(location);
            }
        }
    }
}
