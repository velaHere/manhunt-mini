package org.velas.manhunt.dropSystem.drops;

import org.bukkit.Location;
import org.velas.manhunt.config.Settings;
import org.velas.manhunt.dropSystem.lootProvider.RespawnDLP;

public class RespawnDrop extends AirDrop{
    public RespawnDrop(Location initialDropLoc) {
        super("Respawn", initialDropLoc, Settings.config().drop().respawn().getLifetime(), RespawnDLP.INSTANCE);
    }
}
