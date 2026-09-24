package org.velas.manhunt.avatar;

import org.velas.manhunt.ability.AbilityType;
import org.velas.manhunt.ability.active.Lunge;
import org.velas.manhunt.ability.active.Riposte;
import org.velas.manhunt.ability.passive.BladeDiscipline;
import org.velas.manhunt.player.ManhuntPlayer;

import java.util.List;

public final class Swordsman extends Avatar {
    public Swordsman(ManhuntPlayer mp) {
        super(mp);
        abilities.addAll(List.of(
                new Riposte(this, AbilityType.PRIMARY_ACTIVE),
                new Lunge(this, AbilityType.SECONDARY_ACTIVE),
                new BladeDiscipline(this, AbilityType.PASSIVE)
        ));
    }
}
