package org.velas.manhunt.ability;


import org.velas.manhunt.avatar.Avatar;

public abstract class PassiveAbility extends Ability{

    protected PassiveAbility(String name, Avatar avatar, AbilityType type) {
        super(name, avatar, type);
    }

    public abstract void onGrant();
    public abstract void onRevoke();
}
