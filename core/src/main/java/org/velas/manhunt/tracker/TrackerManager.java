package org.velas.manhunt.tracker;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.velas.manhunt.Color;
import org.velas.manhunt.config.Settings;
import org.velas.manhunt.customItems.CustomItemType;
import org.velas.manhunt.game.ManhuntManager;
import org.velas.manhunt.game.PlayerManager;
import org.velas.manhunt.player.Hunter;
import org.velas.manhunt.player.ManhuntPlayer;
import org.velas.manhunt.player.Runner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TrackerManager {

    final ManhuntManager manager;
    private final PlayerManager playerManager;
    private final Map<String, LastTrackedLocation> lastTrackedLocations; //Tracked by hunter
    private final Map<String, LastTrackedLocation> runnerLastTrackedLocations; //Runners current location
    private Tracker tracker;
    private final Random random;

    public TrackerManager(PlayerManager playerManager){
        this.playerManager=playerManager;
        this.manager = playerManager.getGameManager();
        this.lastTrackedLocations = new HashMap<>();
        this.runnerLastTrackedLocations = new HashMap<>();
        this.tracker = new Tracker(this);
        this.random = new Random();
    }

    public void putLastTrackedLocations(ManhuntPlayer player, LastTrackedLocation locations){
        Player player1 = player.getPlayer();
        if(player1==null) return;
        String playerName = player1.getName();
        if(player.isHunter()) lastTrackedLocations.put(playerName, locations);
        else if(player.isRunner()) runnerLastTrackedLocations.put(playerName, locations);
    }

    public Tracker getTracker(){
        return this.tracker;
    }

    public void setTracker(Tracker tracker){
        this.tracker = tracker;
    }

    public void resetTargetNoForAll(){

        for (ManhuntPlayer player: playerManager.getPlayers()){

            List<Runner> runners = playerManager.getRunners();

            if (!(player instanceof Hunter hunter)) continue;
            hunter.setTarget(tracker.getTrackerManager().getValidTarget(runners.size() - 1));

            Player runnerPlayer = runners.get(hunter.getTarget()).getPlayer();
            Player hunterPlayer = hunter.getPlayer();
            if(hunterPlayer==null) continue;

            if (runnerPlayer != null){
                hunterPlayer.sendMessage(Color.GREEN + "Now Tracking: " + runnerPlayer.getName());
            }else {
                hunterPlayer.sendMessage(Color.GREEN + "Tracking target changed!");
            }
        }
    }

    public Location getLastTrackedLocation(String playerName, String worldName){
        if (!lastTrackedLocations.containsKey(playerName)) return null;
        return lastTrackedLocations.get(playerName).getLocation(worldName);
    }

    public Location getRunnerLastTrackedLocation(String playerName, String worldName){

        if (!runnerLastTrackedLocations.containsKey(playerName)) return null;
        return runnerLastTrackedLocations.get(playerName).getLocation(worldName);
    }

    public void updateTrackLocation(String playerName, Location location){
        if (!lastTrackedLocations.containsKey(playerName)) return;
        lastTrackedLocations.get(playerName).setLocation(location);
    }

    public void updateRunnerTrackLocation(String playerName, Location location){
        if (!runnerLastTrackedLocations.containsKey(playerName)) return;
        runnerLastTrackedLocations.get(playerName).setLocation(location);
    }

    public void updateRunnersLastLocationToCurrent() {
        for (ManhuntPlayer manhuntPlayer : this.playerManager.getPlayers()) {
            if (manhuntPlayer == null) continue;
            if(!manhuntPlayer.isOnline()) continue;
            if (!(manhuntPlayer instanceof Runner runner)) continue;
            Player player = runner.getPlayer();
            if(player==null) continue;
            String playerName = player.getName();
            Location currentLocation = player.getLocation();
            this.updateRunnerTrackLocation(playerName, currentLocation);
        }
    }

    public void updateHuntersCompassLocation(){

        for (ManhuntPlayer manhuntPlayer: this.manager.getPlayerManager().getPlayers()){

            if (manhuntPlayer == null) continue;
            if (!(manhuntPlayer instanceof Hunter hunter)) continue;
            if(!manhuntPlayer.isOnline()) continue;
            if(!hunter.isAlive()) continue;

            Player player = manhuntPlayer.getPlayer();
            if(player==null) continue;
            String playerName = player.getName(),
                    worldName = player.getWorld().getName();
            int targetNo = hunter.getTarget();

            Player target = null;
            try {
                target = playerManager.getRunners().get(targetNo).getPlayer();
            } catch (Exception ignored) {
            }

            if (target == null || !target.isOnline() || target.isDead()){
                if (target != null && target.isDead()) continue;
                player.sendMessage(Color.YELLOW + "Resetting the player tracked");
                player.sendMessage(Color.AQUA + "If this message is spamming then please switch your compass target");
                hunter.setTarget(getValidTarget(playerManager.getRunners().size() - 1));
                continue;
            }

            if(target.isInvisible()) continue;

            Location newTrackLocation = this.getRunnerLastTrackedLocation(target.getName(), worldName);

            if (newTrackLocation == null){
                Location randomLoc = player.getLocation().clone().add(random.nextInt(200) - 100, 0, random.nextInt(200) - 100);
                this.setCompassLocation(player, randomLoc);
                continue;
            }

            Location previouslyTracked = this.getLastTrackedLocation(playerName, worldName);

            if (previouslyTracked == null) {
                this.setCompassLocation(player, newTrackLocation);
                this.updateTrackLocation(playerName, newTrackLocation);
                continue;
            }

            this.setCompassLocation(player, previouslyTracked);

            if (this.calculateDistance(newTrackLocation, previouslyTracked) <= Settings.getConfigSettings().getCOMPASS_RADIUS()){
                continue;
            }

            this.setCompassLocation(player, newTrackLocation);
            this.updateTrackLocation(playerName, newTrackLocation);
        }
    }

    private double calculateDistance(Location loc1, Location loc2){

        double x1 = loc1.getX(); double z1 = loc1.getZ();
        double x2 = loc2.getX(); double z2 = loc2.getZ();
        double dx = x1 - x2;
        double dz = z1 - z2;

        return Math.sqrt(dx*dx + dz*dz);
    }

    private void setCompassLocation(Player player, Location newTrackLocation){
        ItemStack compass = player.getInventory().getItem(8);
        if (compass == null || !compass.getType().equals(Material.COMPASS)) {
            player.getInventory().setItem(8, CustomItemType.create(CustomItemType.TRACKER_COMPASS));
            return;
        }

        ItemMeta meta = compass.getItemMeta();
        if (meta == null) return;
        if (!(meta instanceof CompassMeta compassMeta)) return;

        compassMeta.setLodestone(newTrackLocation);
        compassMeta.setLodestoneTracked(false);
        compass.setItemMeta(meta);
    }

    public int getValidTarget(int index) {

        List<Runner> runners = playerManager.getRunners();
        if(runners.isEmpty()) return -1;
        int runnersSize = runners.size();
        int i = (index == runnersSize - 1) ? 0 : index + 1;

        for (; i < runnersSize; i++) {
            Player player = runners.get(i).getPlayer();
            if (player != null) return i;
        }
        for (int x = 0; x < index; x++) {
            Player player = runners.get(x).getPlayer();
            if (player != null) return x;
        }
        if (runners.get(index).getPlayer() == null){ return -1; }
        return index; // fallback if no valid player found
    }
}
