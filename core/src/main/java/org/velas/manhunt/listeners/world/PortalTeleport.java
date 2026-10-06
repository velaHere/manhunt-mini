package org.velas.manhunt.listeners.world;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.velas.manhunt.config.Settings;
import org.velas.manhunt.Utils;
import org.velas.manhunt.game.GameState;
import org.velas.manhunt.game.ManhuntGame;
import org.velas.manhunt.player.ManhuntPlayer;

import java.util.Set;


public class PortalTeleport implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerPortal(PlayerPortalEvent event){

        Location loc = Settings.config().game().world().getLobbySpawn();
        if (loc != null){
            if (event.getFrom().getWorld().getName().equals(loc.getWorld().getName())){
                event.setCancelled(true);
                return;
            }
        }

        Player player = event.getPlayer();
        ManhuntGame manhuntGame = ManhuntGame.getCurrentGame();
        ManhuntPlayer manhuntPlayer = manhuntGame.getGameManager().getPlayerManager().getManhuntPlayer(player);
        if(manhuntPlayer == null) return;

        GameState gameState = manhuntGame.getGameState();
        if (Set.of(
                GameState.TRANSITION,
                GameState.LOBBY,
                GameState.STARTING
        ).contains(gameState)) {
            event.setCancelled(true);
            return;
        }
        if(manhuntPlayer.isHunter() && gameState.equals(GameState.HEAD_START)) {
            event.setCancelled(true);
            return;
        }


        String levelWorldName = Settings.config().game().world().getDefaultWorld();

        World world2 = Bukkit.getWorld(levelWorldName);
        World nether = Bukkit.getWorld(levelWorldName+"_nether");
        World end = Bukkit.getWorld(levelWorldName+"_the_end");

        World from = event.getFrom().getWorld();

        if (from.equals(world2) || from.equals(nether) || from.equals(end)){
            return;
        }

        String worldName = event.getFrom().getWorld().getName();

        if (worldName.endsWith("_nether")){
            worldName = worldName.replace("_nether", "");
        }else if(worldName.endsWith("_the_end")){
            worldName = worldName.replace("_the_end", "");
        }

        World.Environment envTo = event.getTo().getWorld().getEnvironment();

        World world;

        switch (envTo){
            case NORMAL -> world = Bukkit.getWorld(worldName);
            case NETHER -> world = Bukkit.getWorld(worldName + "_nether");
            case THE_END -> world = Bukkit.getWorld(worldName + "_the_end");
            default -> world = null;
        }

        if (world != null){
            Location toLoc = event.getTo();
            event.setTo(new Location(world, toLoc.getBlockX(), toLoc.getBlockY(), toLoc.getBlockZ(), toLoc.getYaw(), toLoc.getPitch()));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityPortal(EntityPortalEvent event){

        String levelWorldName = Settings.config().game().world().getDefaultWorld();

        World world = Bukkit.getWorld(levelWorldName);
        World nether = Bukkit.getWorld(levelWorldName+"_nether");
        World end = Bukkit.getWorld(levelWorldName+"_the_end");

        World from = event.getFrom().getWorld();

        if (from.equals(world) || from.equals(nether) || from.equals(end)){
            return;
        }

        String worldName = from.getName();

        if (worldName.endsWith("_nether")){
            worldName = worldName.replace("_nether", "");
        }else if(worldName.endsWith("_the_end")){
            worldName = worldName.replace("_the_end", "");
        }

        Location to = event.getTo();

        if (to == null){
            Utils.warning("to world was null when entity tried to go through portal");
            return;
        }

        World.Environment envTo = to.getWorld().getEnvironment();

        World world2;

        switch (envTo){
            case NORMAL -> world2 = Bukkit.getWorld(worldName);
            case NETHER -> world2 = Bukkit.getWorld(worldName + "_nether");
            case THE_END -> world2 = Bukkit.getWorld(worldName + "_the_end");
            default -> world2 = null;
        }

        if (world2 != null){
            event.setTo(new Location(world2, to.getBlockX(), to.getBlockY(), to.getBlockZ(), to.getYaw(), to.getPitch()));
        }
    }
}
