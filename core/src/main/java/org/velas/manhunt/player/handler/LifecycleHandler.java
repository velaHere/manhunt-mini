package org.velas.manhunt.player.handler;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.config.Settings;
import org.velas.manhunt.Manhunt;
import org.velas.manhunt.ability.ActiveAbility;
import org.velas.manhunt.avatar.Avatar;
import org.velas.manhunt.customItems.CustomItemType;
import org.velas.manhunt.game.GameFlowManager;
import org.velas.manhunt.game.GameState;
import org.velas.manhunt.game.ManhuntGame;
import org.velas.manhunt.game.scoreboard.ScoreManager;
import org.velas.manhunt.player.ManhuntPlayer;
import org.velas.scoreboard.api.team.Team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class LifecycleHandler {

    private final @NotNull ManhuntPlayer manhuntPlayer;
    private boolean given = false;

    protected LifecycleHandler(@NotNull ManhuntPlayer manhuntPlayer){
        this.manhuntPlayer=manhuntPlayer;
    }

    protected abstract void matchStart();
    public void onMatchStart(){
        if(!manhuntPlayer.isOnline()) return;

        Player player = manhuntPlayer.getPlayer();
        if(player==null) return;
        player.getInventory().clear();

        this.matchStart();

        this.giveItems(player);
    }

    protected abstract void matchStop();
    public void onMatchStop(){
        if(!manhuntPlayer.isOnline()) return;
        this.manhuntPlayer.getHandlerBundle().sidebarHandler().removeSidebar();
        Player player = manhuntPlayer.getPlayer();
        assert player != null;
        player.getInventory().setItem(8, CustomItemType.create(CustomItemType.BACK_TO_LOBBY));
        this.matchStop();
    }

    protected abstract void playerJoinServer();
    public void onPlayerJoinServer(){
        this.manhuntPlayer.setOnline(true);
        if(!manhuntPlayer.isOnline()) return;
        Player player=this.manhuntPlayer.getPlayer();
        if(player==null)return;
        this.enforce(player, this.manhuntPlayer.getManhuntGame());

        // Hiding all the spectators from the player just joined
        Team team = ScoreManager.getScoreboard().getTeam(Settings.SPECTATOR_TEAM_NAME);
        if (team == null) return;
        team.getPlayers().forEach(spectatorUUID -> {
            Player spectatorPlayer = Bukkit.getPlayer(spectatorUUID);
            if(spectatorPlayer==null) return;
            player.hidePlayer(Manhunt.getINSTANCE(), spectatorPlayer);
        });
        this.playerJoinServer();

        int currentTime = this.manhuntPlayer.getManhuntGame().getGameManager().getGameTimeLine().getTime();
        if(currentTime > 0 && currentTime < 50 && !manhuntPlayer.isSpectator()) {
            if (Arrays.stream(player.getInventory().getContents())
                    .noneMatch(item -> CustomItemType.matches(item, CustomItemType.AVATAR_SIGIL))) {

                player.getInventory().addItem(
                        CustomItemType.create(CustomItemType.AVATAR_SIGIL)
                );
            }
        }

        if(this.given) return;

        if(currentTime > 55) {
            this.giveItems(player);
        }
    }

    protected abstract void playerQuitServer();
    public void onPlayerQuitServer(){
        manhuntPlayer.setOnline(false);
        if(manhuntPlayer.isOnline()) return;
        this.playerQuitServer();
    }

    protected abstract void playerDeath(PlayerDeathEvent event);
    public void onPlayerDeath(PlayerDeathEvent event){

        List<ItemStack> itemsToRemove = new ArrayList<>();
        for (ItemStack item: event.getDrops()){
            if(CustomItemType.matches(item, CustomItemType.PRIMARY_ABILITY_ACTIVATOR))
                itemsToRemove.add(item);
            if(CustomItemType.matches(item, CustomItemType.SECONDARY_ABILITY_ACTIVATOR))
                itemsToRemove.add(item);
            if(CustomItemType.matches(item, CustomItemType.TRACKER_COMPASS))
                itemsToRemove.add(item);
        }
        event.getDrops().removeAll(itemsToRemove);
        this.playerDeath(event);
    }

    protected abstract void playerRespawn();
    public void onPlayerRespawn(){
        if(!this.manhuntPlayer.isOnline()) return;
        this.playerRespawn();
    }

    protected void giveItems(@NotNull Player player) {

        Avatar avatar = manhuntPlayer.getAvatar();
        if(avatar==null) return;

        avatar.getAbilities().forEach(ability -> {
            if(!(ability instanceof ActiveAbility)) return;
            if(ability.isPrimaryActive())
                player.getInventory()
                        .addItem(CustomItemType.create(CustomItemType.PRIMARY_ABILITY_ACTIVATOR));
            if(ability.isSecondaryActive())
                player.getInventory()
                        .addItem(CustomItemType.create(CustomItemType.SECONDARY_ABILITY_ACTIVATOR));
        });

        this.given=true;
    }

    private void enforce(Player player, ManhuntGame manhuntGame){
        GameState gameState=manhuntGame.getGameState();
        switch (gameState){
            case STARTING -> GameFlowManager.teleportPlayerToLobby(player);
            case HEAD_START -> {
                ManhuntPlayer manhuntPlayer = manhuntGame.getGameManager().getPlayerManager().getManhuntPlayer(player);
                if(manhuntPlayer==null) return;
                if(manhuntPlayer.isRunner()) {
                    if(player.getWorld().getName().equals(Settings.getConfigSettings().getLOBBY_WORLD_NAME())) {
                        player.teleport(manhuntGame.getGameManager().getWorldManager()
                                .getOverworld().getSpawnLocation());
                    }
                    return;
                }
                player.teleport(manhuntGame.getGameManager().getWorldManager()
                        .getOverworld().getSpawnLocation());
                if(!(manhuntPlayer.isHunter())) return;
                player.setAllowFlight(true);
                player.setFlying(true);
            }
            case PLAYING -> {
                if (!player.getWorld().getName().equals(Settings.OVERWORLD_NAME) &&
                        !player.getWorld().getName().equals(Settings.NETHER_WORLD_NAME) &&
                        !player.getWorld().getName().equals(Settings.END_WORLD_NAME)){
                    World world = Bukkit.getWorld(Settings.OVERWORLD_NAME);
                    if (world == null) return;
                    player.teleport(world.getSpawnLocation());
                }
            }
            case VILLAGE, STRONGHOLD -> {
                World overWorld = manhuntGame.getGameManager().getWorldManager().getOverworld();
                switch (player.getWorld().getEnvironment()){
                    case NORMAL -> {
                        WorldBorder borderOverworld = overWorld.getWorldBorder();
                        if(borderOverworld.isInside(player.getLocation())) return;
                        player.teleport(overWorld.getSpawnLocation());
                    }
                    case NETHER -> {
                        World netherWorld = manhuntGame.getGameManager().getWorldManager().getNetherWorld();
                        WorldBorder borderNetherWorld = netherWorld.getWorldBorder();
                        if(borderNetherWorld.isInside(player.getLocation())) return;
                        player.teleport(overWorld.getSpawnLocation());
                    }
                }
            }
            case TRANSITION -> {
                int timeElapsedAfterStrongholdRequest = manhuntGame.getGameManager()
                        .getGameTimeLine().getTimeElapsedAfterStrongholdRequest();
                if (timeElapsedAfterStrongholdRequest >=10 && timeElapsedAfterStrongholdRequest <=60) {
                    GameFlowManager.tpPlayerToLobbyForTransition(player);
                    GameFlowManager.playIntenseVoidEffect(player);
                }else{
                    World overWorld = manhuntGame.getGameManager().getWorldManager().getOverworld();
                    player.teleport(overWorld.getSpawnLocation());
                }
            }
        }
    }
}