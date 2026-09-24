package org.velas.manhunt.dropSystem;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.scheduler.BukkitRunnable;
import org.velas.manhunt.config.Settings;
import org.velas.manhunt.Manhunt;
import org.velas.manhunt.Utils;
import org.velas.manhunt.dropSystem.drops.AirDrop;
import org.velas.manhunt.game.GameState;
import org.velas.manhunt.game.ManhuntGame;
import org.velas.manhunt.game.ManhuntManager;
import org.velas.manhunt.player.ManhuntPlayer;

import java.util.*;

public class DropManager {

    private final HashMap<UUID, AirDrop> activeDrops;
    private int currentDropX;
    private int currentDropZ;
    private int dropStartTime;
    private int nextDropIndex;
    private boolean respawnDropUnlocked;
    private final ManhuntManager manhuntManager;
    private final List<AirDropType> featuredDrops = new ArrayList<>(
            List.of(
                    AirDropType.MINING,
                    AirDropType.GATEWAY,
                    AirDropType.EARLY_COMBAT,
                    AirDropType.GATEWAY,
                    AirDropType.EARLY_COMBAT,
                    AirDropType.ANCIENT,
                    AirDropType.COMBAT,
                    AirDropType.END
    ));

    public DropManager(ManhuntManager manhuntManager){
        this.activeDrops=new HashMap<>();
        this.dropStartTime=-1;
        this.nextDropIndex=0;
        this.respawnDropUnlocked=false;
        this.currentDropX=0;
        this.currentDropZ=0;
        this.manhuntManager=manhuntManager;
    }

    public int getCurrentDropZ() {
        return currentDropZ;
    }

    public int getCurrentDropX() {
        return currentDropX;
    }

    public int getNextDropSeconds(){

        int currentTime = manhuntManager.getGameTimeLine().getTime();
        int interval = Settings.getConfigSettings().getDROP_INTERVAL_SEC();

        int elapsed = currentTime - dropStartTime;
        int remaining = interval - (elapsed%interval);

        return remaining==interval?0:remaining;
    }

    public void addDrop(UUID uuid, AirDrop drop){
        if(!activeDrops.containsKey(uuid)) this.activeDrops.put(uuid, drop);
        else Utils.severe("This drop already added to the active drop list");
    }

    public void removeDrop(UUID uuid){
        if(activeDrops.containsKey(uuid)) this.activeDrops.remove(uuid);
        else Utils.severe("Drop was not found in active drops list");
    }

    public void setDropStartTime(int time){
        this.dropStartTime=time;
    }

    public boolean hasRespawnDropUnlocked(){
        return respawnDropUnlocked;
    }

    public void summonDrop(){

        if(!respawnDropUnlocked)
            this.respawnDropUnlocked=true;

        Location dropLoc = DropLocationProvider.getRandomDropLoc();

        if(dropLoc == null){
            return;
        }

        featuredDrops.get(nextDropIndex).create(dropLoc);
        nextDropIndex = (nextDropIndex + 1) % featuredDrops.size();
        this.currentDropX=dropLoc.getBlockX();
        this.currentDropZ=dropLoc.getBlockZ();
    }

    public void startExpiringDrops(){

        // Drop Remover
        new BukkitRunnable(){

            @Override
            public void run() {

                if(!ManhuntGame.getCurrentGame().isRunning()) this.cancel();

                if(activeDrops.isEmpty()) return;

                var iterator = activeDrops.entrySet().iterator();

                while(iterator.hasNext()){

                    var entry = iterator.next();
                    AirDrop drop = entry.getValue();

                    if(drop.isExpired()){
                        if(drop.getChestCart() instanceof StorageMinecart chestCart)
                            chestCart.getInventory().clear();
                        drop.remove();
                        iterator.remove();
                    }
                }
            }
        }.runTaskTimer(Manhunt.getINSTANCE(), 0L, 20L);
    }

    public boolean canSummonDrop(){

        boolean hunter = false;
        boolean runner = false;

        for(ManhuntPlayer manhuntPlayer: ManhuntGame.getCurrentGame().getGameManager().getPlayerManager().getPlayers()) {

            if (!manhuntPlayer.isOnline() || manhuntPlayer.isSpectator()) continue;

            Location loc = Objects.requireNonNull(manhuntPlayer.getPlayer()).getLocation();

            if (Objects.requireNonNull(loc.getWorld()).getEnvironment().equals(World.Environment.NETHER) || loc.getWorld().getEnvironment().equals(World.Environment.THE_END))
                continue;

            if(manhuntPlayer.isHunter()) hunter = true;
            else if(manhuntPlayer.isRunner()) runner = true;
        }

        return hunter && runner && !this.manhuntManager.getGame().getGameState().equals(GameState.TRANSITION);
    }
}
