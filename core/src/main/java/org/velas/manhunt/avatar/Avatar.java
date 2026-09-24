package org.velas.manhunt.avatar;


import org.velas.manhunt.ability.Ability;
import org.velas.manhunt.player.ManhuntPlayer;

import java.util.ArrayList;
import java.util.List;

public abstract class Avatar {

    protected final ManhuntPlayer manhuntPlayer;
    protected final List<Ability> abilities = new ArrayList<>();

    protected Avatar(ManhuntPlayer manhuntPlayer){
        this.manhuntPlayer = manhuntPlayer;
    }

    public List<Ability> getAbilities(){
        return this.abilities;
    }

    public ManhuntPlayer getManhuntPlayer(){
        return this.manhuntPlayer;
    }
}
