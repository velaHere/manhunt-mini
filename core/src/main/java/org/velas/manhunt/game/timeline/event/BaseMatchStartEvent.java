package org.velas.manhunt.game.timeline.event;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.Color;
import org.velas.manhunt.game.ManhuntGame;
import org.velas.manhunt.game.ManhuntManager;
import org.velas.manhunt.tracker.Tracker;

public final class BaseMatchStartEvent extends RangeEvent {

    public BaseMatchStartEvent(@NotNull ManhuntGame game) {
        super(game, 50, 60);
    }

    @Override
    public void execute() {
        int time = this.getCurrentTime();
        ManhuntManager gameManager = this.game.getGameManager();

        if(time == 50){
            Bukkit.getOnlinePlayers().forEach(player -> {
                player.teleport(gameManager.getWorldManager().getOverworld().getSpawnLocation());
                player.getInventory().clear();
            });

            gameManager.getGameFlowManager().setHuntersCanMove(false);

            if (gameManager.getTrackerManager().getTracker() == null) {
                gameManager.getTrackerManager().setTracker(new Tracker(gameManager.getTrackerManager()));
            }
            gameManager.getTrackerManager().getTracker().start();
        }

        if(time == 51){
            Bukkit.getOnlinePlayers().forEach(player -> {
                player.setAllowFlight(true);
                player.setFlying(true);
            });
        }

        if(time>=55 && time<60){
            int t = 60-time;
            Bukkit.getOnlinePlayers().forEach(player ->{
                player.sendMessage(Color.PINK + "Starting in " + t);
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
            });
        }
    }
}
