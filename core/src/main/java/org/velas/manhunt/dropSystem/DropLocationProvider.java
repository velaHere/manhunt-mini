package org.velas.manhunt.dropSystem;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.velas.manhunt.config.Settings;
import org.velas.manhunt.game.ManhuntGame;
import org.velas.manhunt.player.ManhuntPlayer;

public final class DropLocationProvider {

    public static Location getRandomDropLoc(){

        double huntersX = 0;
        double huntersZ = 0;
        int noOfHunters = 0;

        double runnersX = 0;
        double runnersZ = 0;
        int noOfRunners = 0;

        boolean atLeastOneRunnerOnline = false;
        boolean atLeastOneHunterOnline = false;

        for(ManhuntPlayer manhuntPlayer: ManhuntGame.getCurrentGame().getGameManager().getPlayerManager().getPlayers()){

            if(!manhuntPlayer.isOnline() || manhuntPlayer.isSpectator()) continue;

            Location loc = manhuntPlayer.getPlayer().getLocation();

            if(loc.getWorld().getEnvironment().equals(World.Environment.NETHER) || loc.getWorld().getEnvironment().equals(World.Environment.THE_END)) continue;

            if(manhuntPlayer.isHunter()){
                huntersX+= loc.getX();
                huntersZ+= loc.getZ();
                noOfHunters++;
                atLeastOneHunterOnline = true;
            }else if(manhuntPlayer.isRunner()){
                runnersX+= loc.getX();
                runnersZ+= loc.getZ();
                noOfRunners++;
                atLeastOneRunnerOnline = true;
            }
        }

        if(atLeastOneHunterOnline && atLeastOneRunnerOnline){

            double huntersAvgX = huntersX/noOfHunters;
            double huntersAvgZ = huntersZ/noOfHunters;

            double runnersAvgX = runnersX/noOfRunners;
            double runnersAvgZ = runnersZ/noOfRunners;

            double midX = (huntersAvgX + runnersAvgX)/2.0f;
            double midZ = (huntersAvgZ + runnersAvgZ)/2.0f;

            return new Location(Bukkit.getWorld(Settings.OVERWORLD_NAME), midX, 0, midZ);
        }
        else return null;
    }
}
