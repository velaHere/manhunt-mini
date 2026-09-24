package org.velas.manhunt.dropSystem.drops;

import org.bukkit.Location;
import org.velas.manhunt.dropSystem.lootProvider.EarlyCombatDLP;

public class EarlyCombatDrop extends AirDrop {
    public EarlyCombatDrop(Location initialDropLoc){
        super("EarlyCombat", initialDropLoc, EarlyCombatDLP.INSTANCE);
    }
}
