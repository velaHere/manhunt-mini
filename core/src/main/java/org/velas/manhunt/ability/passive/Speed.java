package org.velas.manhunt.ability.passive;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.velas.manhunt.ability.AbilityType;
import org.velas.manhunt.ability.PassiveAbility;
import org.velas.manhunt.avatar.Avatar;

public final class Speed extends PassiveAbility {

    public Speed(Avatar avatar, AbilityType type){
        super("speed", avatar, type);
    }

    @Override
    public void onGrant() {
        Player player = this.avatar.getManhuntPlayer().getPlayer();
        if(player == null) return;
        player.addPotionEffect(
                new PotionEffect(
                        PotionEffectType.SPEED,
                        Integer.MAX_VALUE,
                        0, // amplifier is level-1
                        false,
                        false
                )
        );
    }

    @Override
    public void onRevoke() {
        Player player = this.avatar.getManhuntPlayer().getPlayer();
        if(player == null) return;
        player.removePotionEffect(PotionEffectType.SPEED);
        player.removePotionEffect(PotionEffectType.SLOW);
    }
}
