package org.velas.manhunt.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.game.ManhuntGame;


public class ManhuntMatchStopEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final @NotNull ManhuntGame game;

    public ManhuntMatchStopEvent(@NotNull ManhuntGame game){
        this.game=game;
    }

    public @NotNull ManhuntGame getGame(){
        return this.game;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
