package org.velas.manhunt.customItems.items;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.velas.manhunt.Color;
import org.velas.manhunt.config.Settings;
import org.velas.manhunt.Manhunt;
import org.velas.manhunt.Utils;
import org.velas.manhunt.customItems.CustomItem;
import org.velas.manhunt.customItems.CustomItemType;
import org.velas.manhunt.game.GameState;
import org.velas.manhunt.game.ManhuntGame;
import org.velas.manhunt.game.ManhuntManager;
import org.velas.manhunt.game.PlayerManager;
import org.velas.manhunt.player.Hunter;
import org.velas.manhunt.player.ManhuntPlayer;
import org.velas.manhunt.tracker.TrackerManager;


public final class TrackerCompass implements CustomItem {

    public static final TrackerCompass INSTANCE;

    private final NamespacedKey namespacedKey;

    static{
        INSTANCE=new TrackerCompass();
    }

    private TrackerCompass(){
        this.namespacedKey=new NamespacedKey(Manhunt.getINSTANCE(), CustomItemType.TRACKER_COMPASS);
    }

    @Override
    public NamespacedKey getKey() {
        return this.namespacedKey;
    }

    @Override
    public ItemStack createItem() {

        ItemStack compass = new ItemStack(Material.COMPASS, 1);
        CompassMeta meta = (CompassMeta) compass.getItemMeta();
        World overworld = Bukkit.getWorld(Settings.OVERWORLD_NAME);
        if(overworld == null){
            return null;
        }
        if(meta==null){
            Utils.warning("Meta was null in TrackerCompass");
            return compass;
        }
        meta.setLodestone(overworld.getSpawnLocation()); // Check
        meta.setLodestoneTracked(false);
        meta.setDisplayName(ChatColor.GOLD + "Tracker");

        meta.getPersistentDataContainer().set(this.namespacedKey, PersistentDataType.BYTE, (byte) 1);

        compass.setItemMeta(meta);
        return compass;
    }

    @Override
    public void registerRecipe(JavaPlugin plugin) {

    }

    @Override
    public void onUse(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        ManhuntGame currentGame = ManhuntGame.getCurrentGame();
        if(!currentGame.isRunning()) return;
        if(currentGame.getGameState().equals(GameState.TRANSITION)) return;
        ManhuntPlayer manhuntPlayer = currentGame.getGameManager().getPlayerManager().getManhuntPlayer(player);
        if(manhuntPlayer==null) return;
        if(!manhuntPlayer.isHunter()) return;

        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) switchTarget(event);
        else if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) checkTarget(event);
    }

    @Override
    public void onItemDrop(PlayerDropItemEvent event) {
        ManhuntGame currentGame = ManhuntGame.getCurrentGame();
        if(!currentGame.isRunning()) return;
        event.setCancelled(true);
    }

    @Override
    public void onPickup(EntityPickupItemEvent event) {

    }

    @Override
    public void onItemSwitch(PlayerItemHeldEvent event) {

    }

    @Override
    public void onItemSwap(PlayerSwapHandItemsEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void placeItemToHeldSlot(InventoryClickEvent event) {

    }

    private void switchTarget(PlayerInteractEvent event){

        ManhuntManager manager = ManhuntGame.getCurrentGame().getGameManager();
        PlayerManager playerManager = manager.getPlayerManager();

        Player player = event.getPlayer();
        Hunter hunter = (Hunter) playerManager.getManhuntPlayer(player);
        if(hunter==null) return;

        int targetCheck = manager.getTrackerManager().getValidTarget(hunter.getTarget());

        if (targetCheck == -1){
            player.sendMessage(Color.RED + "Cannot find any runner to track");
            return;
        }
        hunter.setTarget(targetCheck);

        Player check = playerManager.getRunners().get(hunter.getTarget()).getPlayer();
        if(check == null) return;
        player.sendMessage(Color.GREEN + "Switched target to: " + check.getName());
    }

    private void checkTarget(PlayerInteractEvent event){

        ManhuntManager manager = ManhuntGame.getCurrentGame().getGameManager();
        PlayerManager playerManager = manager.getPlayerManager();
        TrackerManager trackerManager = manager.getTrackerManager();

        Player player = event.getPlayer();
        Hunter hunter = (Hunter) playerManager.getManhuntPlayer(player);
        if(hunter==null) return;

        Player targetPlayer = playerManager.getRunners().get(hunter.getTarget()).getPlayer();

        if (targetPlayer == null){
            player.sendMessage(Color.RED + "Cannot find this player to track please switch to another one");
            return;
        }

        Location targetLocation = trackerManager.getRunnerLastTrackedLocation(targetPlayer.getName(), player.getWorld().getName());

        Location location = trackerManager.getLastTrackedLocation(player.getName(), player.getWorld().getName());

        if (targetLocation == null){
            player.sendMessage(Color.RED + "The player you are tracking haven't been in this world yet, so tracker will not work");
            return;
        }

        if (location == null) return;

        World targetLocationWorld = targetLocation.getWorld();
        if(targetLocationWorld == null){
            player.sendMessage(ChatColor.RED+"TargetPlayer's world is not found");
            return;
        }

        if (!targetPlayer.getWorld().getName().equals(targetLocationWorld.getName())){
            player.sendMessage(Color.YELLOW + "The player you are tracking is not in this world\n" + Color.GREEN + "So tracker will be pointing at his/her last location in this world");
            return;
        }
        player.sendMessage(Color.GREEN + "Tracking: " + targetPlayer.getName());
    }
}
