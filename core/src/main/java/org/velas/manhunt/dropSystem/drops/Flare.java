package org.velas.manhunt.dropSystem.drops;

import org.bukkit.Location;
import org.velas.manhunt.dropSystem.lootProvider.FlareDLP;

public class Flare extends AirDrop {
    public Flare(Location initialDropLoc, String playerName) {
        super("Flare", initialDropLoc, FlareDLP.INSTANCE, playerName);
    }
}
