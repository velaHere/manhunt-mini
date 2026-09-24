package org.velas.manhunt.dropSystem.drops;

import org.bukkit.Location;
import org.velas.manhunt.dropSystem.lootProvider.CombatDLP;

public class CombatDrop extends AirDrop {

    public CombatDrop(Location initialDropLoc) {
        super("Combat", initialDropLoc, CombatDLP.INSTANCE);
    }
}
