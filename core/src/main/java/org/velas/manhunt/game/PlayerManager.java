package org.velas.manhunt.game;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.velas.manhunt.player.Hunter;
import org.velas.manhunt.player.ManhuntPlayer;
import org.velas.manhunt.player.Runner;
import org.velas.manhunt.player.Spectator;
import org.velas.manhunt.tracker.LastTrackedLocation;
import org.velas.manhunt.tracker.TrackerManager;

import java.util.ArrayList;
import java.util.List;

public class PlayerManager {

    private final ManhuntManager manager;
    private final TrackerManager trackerManager;
    protected final ArrayList<ManhuntPlayer> players;
    protected final ArrayList<Runner> runners;
    protected int noOfRunnersLeft;

    public PlayerManager(ManhuntManager manager){
        this.manager=manager;
        this.trackerManager=new TrackerManager(this);
        this.players = new ArrayList<>();
        this.runners = new ArrayList<>();
        this.noOfRunnersLeft = 0;
    }

    public ManhuntManager getGameManager(){
        return this.manager;
    }

    public TrackerManager getTrackerManager(){
        return this.trackerManager;
    }

    public List<ManhuntPlayer> getPlayers(){
        return this.players;
    }

    public void addPlayerInMatch(ManhuntPlayer player){
        this.players.add(player);
    }

    public void removeRunner(Runner runner){
        runners.remove(runner);
        players.remove(runner);
    }

    public void addPlayer(ManhuntPlayer player){

        if(player == null) return;

        if(player instanceof Hunter hunter){
            players.add(hunter);
            this.trackerManager.putLastTrackedLocations(hunter, new LastTrackedLocation());
        }else if(player instanceof Runner runner){

            noOfRunnersLeft += 1;
            runners.add(runner);
            players.add(runner);
            this.trackerManager.putLastTrackedLocations(runner, new LastTrackedLocation());
        } else if (player instanceof Spectator spectator) {
            players.add(spectator);
        }
    }

    public int getNoOfRunnersLeft() { return noOfRunnersLeft; }

    public void reduceRunners() {
        noOfRunnersLeft -= 1;
    }

    public @Nullable ManhuntPlayer getManhuntPlayer(Player player){
        for (ManhuntPlayer manhuntPlayer: players){
            if (player.getUniqueId().equals(manhuntPlayer.getUniqueId())){
                return manhuntPlayer;
            }
        }
        return null;
    }

    public boolean isPlayerInMatch(Player player){
        for (ManhuntPlayer player1: this.players){
            if (player.getUniqueId().equals(player1.getUniqueId())){
                return true;
            }
        }
        return false;
    }

    public ArrayList<Runner> getRunners() {return runners;}
}
