package org.velas.manhunt.dropSystem.drops;

import org.bukkit.Location;
import org.velas.manhunt.dropSystem.lootProvider.EndDLP;

public class EndDrop extends AirDrop {
    public EndDrop(Location initialDropLoc) {
        super("End", initialDropLoc, EndDLP.INSTANCE);
    }
}
