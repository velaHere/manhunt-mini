package org.velas.manhunt.tracker;

import org.bukkit.scheduler.BukkitRunnable;
import org.velas.manhunt.Manhunt;
import org.velas.manhunt.game.GameState;


public class Tracker {

    private BukkitRunnable task;
    private final TrackerManager trackerManager;

    public Tracker(TrackerManager manager){
        this.trackerManager = manager;
        task = null;
    }

    public void start(){
        if (this.isRunning()) return;
        this.task = getNewTask();
        this.task.runTaskTimer(Manhunt.getINSTANCE(), 0, 20);
    }

    public void stop(){
        if (!this.isRunning()) return;
        task.cancel();
        task = null;
    }

    private BukkitRunnable getNewTask(){
        return new BukkitRunnable() {
            @Override
            public void run() {
                Tracker.this.runTask();
            }
        };
    }

    private void runTask() {
        if(trackerManager.manager.getGame().getGameState().equals(GameState.TRANSITION)) return;
        this.trackerManager.updateRunnersLastLocationToCurrent();
        this.trackerManager.updateHuntersCompassLocation();
    }

    public boolean isRunning(){
        return this.task != null;
    }

    public TrackerManager getTrackerManager(){
        return this.trackerManager;
    }
}
