package org.velas.manhunt.player.handler;

import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.game.scoreboard.ContentProvider;
import org.velas.manhunt.player.ManhuntPlayer;
import org.velas.scoreboard.api.sidebar.PlayerSidebar;

import java.util.List;


public final class SidebarHandler {

    private final @NotNull ManhuntPlayer manhuntPlayer;
    private final @NotNull ContentProvider contentProvider;

    public SidebarHandler(@NotNull ManhuntPlayer manhuntPlayer){
        this.manhuntPlayer=manhuntPlayer;
        this.contentProvider=new ContentProvider(manhuntPlayer);
    }

    public void updateSidebar(int time){
        if(!manhuntPlayer.isOnline()) return;
        PlayerSidebar sidebar = manhuntPlayer.getSidebar();
        List<String> lines = contentProvider.getLines(time);
        for(int i = 0; i<lines.size(); i++){
            int index = lines.size() - 1 - i;
            sidebar.setLine(index, lines.get(i));
        }
    }

    public void resetSidebar(){
        if(!manhuntPlayer.isOnline()) return;
        this.manhuntPlayer.getSidebar().clearLines();
    }

    public void removeSidebar() {
        if (!manhuntPlayer.isOnline()) return;
        this.manhuntPlayer.getSidebar().remove();
    }
}
