package org.velas.manhunt.dropSystem.drops;

import org.bukkit.Location;
import org.velas.manhunt.dropSystem.lootProvider.DesertDLP;

public class DesertDrop extends AirDrop{

    public DesertDrop(Location initialDropLoc) {
        super("Desert", initialDropLoc, DesertDLP.INSTANCE);
    }
}
