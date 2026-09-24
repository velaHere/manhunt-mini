package org.velas.manhunt.dropSystem.drops;

import org.bukkit.Location;
import org.velas.manhunt.dropSystem.lootProvider.MiningDLP;

public class MiningDrop extends AirDrop {
    public MiningDrop(Location initialDropLoc) {
        super("Mining", initialDropLoc, MiningDLP.INSTANCE);
    }
}
