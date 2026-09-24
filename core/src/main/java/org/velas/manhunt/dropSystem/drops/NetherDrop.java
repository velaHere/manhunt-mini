package org.velas.manhunt.dropSystem.drops;

import org.bukkit.Location;
import org.velas.manhunt.dropSystem.lootProvider.NetherDLP;

public class NetherDrop extends AirDrop{

    public NetherDrop(Location initialDropLoc) {
        super("Nether", initialDropLoc, NetherDLP.INSTANCE);
    }
}
