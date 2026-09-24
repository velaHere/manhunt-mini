package org.velas.manhunt.avatar;

import org.velas.manhunt.ability.AbilityType;
import org.velas.manhunt.ability.active.Dash;
import org.velas.manhunt.ability.active.Stealth;
import org.velas.manhunt.ability.passive.LightWeight;
import org.velas.manhunt.player.ManhuntPlayer;

import java.util.List;

public class Assassin extends Avatar{
    public Assassin(ManhuntPlayer manhuntPlayer) {
        super(manhuntPlayer);
        abilities.addAll(List.of(
                new Dash(this, AbilityType.PRIMARY_ACTIVE),
                new Stealth(this, AbilityType.SECONDARY_ACTIVE),
                new LightWeight(this, AbilityType.PASSIVE)
        ));
    }
}
