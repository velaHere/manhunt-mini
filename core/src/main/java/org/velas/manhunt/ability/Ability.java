package org.velas.manhunt.ability;

import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.avatar.Avatar;

public abstract class Ability {

    protected final @NotNull String name;
    protected final @NotNull Avatar avatar;
    protected final @NotNull AbilityType type;

    protected Ability(@NotNull String name,@NotNull Avatar avatar, @NotNull AbilityType type){
        this.name = name;
        this.avatar = avatar;
        this.type=type;
    }

    public @NotNull String getName(){
        return this.name;
    }

    public @NotNull Avatar getAvatar(){
        return this.avatar;
    }

    public boolean idPassive() {
        if(!(this instanceof PassiveAbility) && !(this instanceof Grantable)) return false;
        return this.type.equals(AbilityType.PASSIVE);
    }

    public boolean isPrimaryActive() {
        if(!(this instanceof ActiveAbility)) return false;
        return this.type.equals(AbilityType.PRIMARY_ACTIVE);
    }

    public boolean isSecondaryActive() {
        if(!(this instanceof ActiveAbility)) return false;
        return this.type.equals(AbilityType.SECONDARY_ACTIVE);
    }
}