package org.velas.manhunt.dropSystem.drops;

import org.bukkit.Location;
import org.velas.manhunt.dropSystem.lootProvider.RespawnDLP;


public class RespawnDrop extends AirDrop{
    
    public RespawnDrop(Location initialDropLoc) {
        super("Respawn", initialDropLoc, 45, RespawnDLP.INSTANCE);
    }
}
