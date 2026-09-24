package org.velas.manhunt.dropSystem.drops;

import org.bukkit.Location;
import org.velas.manhunt.dropSystem.lootProvider.RuinedDLP;

public class RuinedDrop extends AirDrop{

    public RuinedDrop(Location initialDropLoc) {
        super("Ruined", initialDropLoc, RuinedDLP.INSTANCE);
    }
}
