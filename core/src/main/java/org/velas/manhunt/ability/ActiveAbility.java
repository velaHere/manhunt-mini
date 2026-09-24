package org.velas.manhunt.ability;


import org.bukkit.event.Event;
import org.velas.manhunt.avatar.Avatar;
import org.velas.manhunt.player.ManhuntPlayer;

public abstract class ActiveAbility extends Ability {

    private final int cooldownSec;
    private long lastUseMillis = 0L;

    protected ActiveAbility(String name, Avatar avatar, AbilityType type, int cooldownSec) {
        super(name, avatar, type);
        this.cooldownSec = cooldownSec;
    }

    public int getCooldownSec(){
        return this.cooldownSec;
    }

    public int getTimeElapsedAfterLastUse(){
        int remaining;
        remaining= Math.round((System.currentTimeMillis()- lastUseMillis)/1000f);
        return remaining;
    }

    public int getRemainingTime(){
        return cooldownSec-getTimeElapsedAfterLastUse();
    }

    public void tryActivate(Event event){

        ManhuntPlayer player = this.avatar.getManhuntPlayer();
        if(!AbilityGate.canUseAbilities(player)) return;

        long currentTimeMilli = System.currentTimeMillis();
        if(currentTimeMilli - lastUseMillis < cooldownSec*1000L) return;

        if(this.onActivate(event)){
            this.lastUseMillis = currentTimeMilli;
        }
    }

    protected abstract boolean onActivate(Event event);
}