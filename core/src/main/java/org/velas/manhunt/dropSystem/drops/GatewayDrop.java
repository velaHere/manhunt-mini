package org.velas.manhunt.dropSystem.drops;

import org.bukkit.Location;
import org.velas.manhunt.dropSystem.lootProvider.GatewayDLP;

public class GatewayDrop extends AirDrop {
    public GatewayDrop(Location initialDropLoc){
        super("Gateway", initialDropLoc, GatewayDLP.INSTANCE);
    }
}
