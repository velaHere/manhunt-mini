package org.velas.manhunt.player;

import org.bukkit.entity.Player;
import org.velas.manhunt.Color;
import org.velas.manhunt.game.ManhuntManager;

import java.util.ArrayList;
import java.util.List;

public final class Hunter extends ManhuntPlayer {

    private int target;
    private boolean alive = true;

    Hunter(PlayerBuilder builder){
        super(builder);
        target = 0;
    }

    public int getTarget() {
        ManhuntManager gameManager = this.getManhuntGame().getGameManager();
        ArrayList<Runner> runners = gameManager.getPlayerManager().getRunners();
        if (this.target < 0 || this.target >= runners.size())
            this.resetTarget(gameManager, runners);
        return target;
    }

    public void setTarget(int i){
        target = i;
    }

    private void resetTarget(ManhuntManager gameManager, List<Runner> runners) {
        int validTarget = gameManager.getTrackerManager().getValidTarget(runners.size() - 1);
        if(validTarget == -1)
            throw new RuntimeException("Unable to find any valid target for being followed by compass");
        Player player = this.getPlayer();
        if (player!=null)
            player.sendMessage(Color.GREEN + "Tracking target changed. Right-click with the compass to see who it’s pointing to.");
        this.target=validTarget;
    }

    public void setAlive(boolean status) {
        this.alive = status;
    }

    public boolean isAlive() {
        return this.alive;
    }
}
