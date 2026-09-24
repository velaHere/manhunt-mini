package org.velas.manhunt.game.timeline;

import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.Manhunt;
import org.velas.manhunt.game.ManhuntManager;

public class GameTimeLine{

    private int time;
    private int strongholdTime;
    private TimelineEventsRegistry events;
    private final ManhuntManager gameManager;
    private BukkitRunnable runnable;

    public GameTimeLine(@NotNull ManhuntManager gameManager){
        this.time=-1;
        this.strongholdTime=-1;
        this.gameManager=gameManager;
    }

    public void init(){
        this.events=new TimelineEventsRegistry(gameManager).registerEvents(Manhunt.getPROVIDER().getEventHandlerBundle(gameManager.getGame()));
    }

    private BukkitRunnable createNewRunnable(){
        return new BukkitRunnable() {
            @Override
            public void run() {
                time++;
                events.processTick(time);
            }
        };
    }

    public void start(){
        this.runnable=createNewRunnable();
        this.runnable.runTaskTimer(Manhunt.getINSTANCE(), 0L, 20L);
    }

    public void stop(){
        this.runnable.cancel();
    }

    public int getTime() {
        return time;
    }

    public int getStrongholdTime() {
        return strongholdTime;
    }

    public void setStrongholdTime(int time){
        this.strongholdTime=time;
    }

    public int getTimeElapsedAfterStrongholdRequest(){
        return time-strongholdTime;
    }
}
