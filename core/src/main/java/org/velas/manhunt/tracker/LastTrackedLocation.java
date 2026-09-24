package org.velas.manhunt.tracker;

import org.bukkit.Location;
import org.velas.manhunt.config.Settings;

public class LastTrackedLocation {

    private Location overworld;
    private Location nether;
    private Location end;

    public LastTrackedLocation(){
        this.overworld=null;
        this.nether=null;
        this.end=null;
    }

    public Location getLocation(String worldName){

        switch (worldName){
            case Settings.OVERWORLD_NAME -> {return overworld;}
            case Settings.NETHER_WORLD_NAME -> {return nether;}
            case Settings.END_WORLD_NAME -> {return end;}
        }
        return null;
    }

    public void setLocation(Location location){

        switch (location.getWorld().getName()){
            case Settings.OVERWORLD_NAME -> this.overworld=location;
            case Settings.NETHER_WORLD_NAME -> this.nether=location;
            case Settings.END_WORLD_NAME -> this.end=location;
        }
    }
}
