package org.velas.manhunt.dropSystem.drops;

import org.bukkit.Location;
import org.velas.manhunt.dropSystem.lootProvider.AncientDLP;

public class AncientDrop extends AirDrop{

    public AncientDrop(Location initialDropLoc) {
        super("Ancient", initialDropLoc, AncientDLP.INSTANCE);
    }
}
