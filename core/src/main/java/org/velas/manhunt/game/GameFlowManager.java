package org.velas.manhunt.game;

import org.bukkit.*;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.velas.manhunt.Color;
import org.velas.manhunt.config.Settings;
import org.velas.manhunt.Manhunt;
import org.velas.manhunt.avatar.AvatarType;
import org.velas.manhunt.customItems.CustomItemType;
import org.velas.manhunt.game.scoreboard.ScoreManager;
import org.velas.manhunt.player.ManhuntPlayer;
import org.velas.scoreboard.api.Scoreboard;
import org.velas.scoreboard.api.team.Team;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class GameFlowManager {

    private static final Set<UUID> voidEffectPlayers = new HashSet<>();

    private final ManhuntManager manager;
    private boolean huntersCanMove;

    GameFlowManager(ManhuntManager manager){
        this.manager=manager;
        this.huntersCanMove = true;
    }

    public boolean ifHuntersCannotMove(){
        return !this.huntersCanMove;
    }

    public void setHuntersCanMove(boolean value){
        this.huntersCanMove = value;
    }

    public void setupPlayers(){

        Scoreboard mainScoreboard = ScoreManager.getScoreboard();

        Bukkit.getOnlinePlayers().forEach(player -> {
            Team teamOf = mainScoreboard.getTeamOf(player);
            if(teamOf!=null) return;
            Team specs = mainScoreboard.getTeam(Settings.SPECTATOR_TEAM_NAME);
            if(specs==null) return;
            specs.addPlayer(player);
        });

        Team hunters = mainScoreboard.getTeam(Settings.HUNTER_TEAM_NAME);
        if(hunters!=null) {
            hunters.getPlayers().forEach(playerUUID -> {
                Player player = Bukkit.getPlayer(playerUUID);
                if(player!=null) {
                    teleportPlayerToLobby(player);
                }
                this.manager.getPlayerManager().addPlayer(
                        ManhuntPlayer.builder().player(playerUUID).game(this.manager.getGame())
                                .avatarType(AvatarType.DEFAULT).buildHunter()
                );
            });
        }

        Team runners = mainScoreboard.getTeam(Settings.RUNNER_TEAM_NAME);
        if(runners!=null) {
            runners.getPlayers().forEach(playerUUID -> {
                Player player = Bukkit.getPlayer(playerUUID);
                if(player!=null) {
                    teleportPlayerToLobby(player);
                }
                this.manager.getPlayerManager().addPlayer(
                        ManhuntPlayer.builder().player(playerUUID).game(this.manager.getGame())
                                .avatarType(AvatarType.DEFAULT).buildRunner()
                );
            });
        }

        Team spectators = mainScoreboard.getTeam(Settings.SPECTATOR_TEAM_NAME);
        if(spectators!=null) {
            spectators.getPlayers().forEach(playerUUID -> {
                Player player = Bukkit.getPlayer(playerUUID);
                if(player!=null) {
                    teleportPlayerToLobby(player);
                    applySpectatorsEffects(player);
                }
                this.manager.getPlayerManager().addPlayer(
                        ManhuntPlayer.builder().player(playerUUID).game(this.manager.getGame())
                                .avatarType(AvatarType.DEFAULT).buildSpectator()
                );
            });
        }
    }

    public void releaseHunters(){

        for (ManhuntPlayer manhuntPlayer: this.manager.getPlayerManager().getPlayers()){

            if (!manhuntPlayer.isOnline()) return;

            Player player = manhuntPlayer.getPlayer();
            if(player==null) continue;

            if (manhuntPlayer.isHunter()){
                player.setFlying(false);
                player.setAllowFlight(false);
                player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
                player.sendTitle("", Color.GREEN + "Go Go Go", 20, 2*20,20);
            }else{
                player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
                player.sendTitle("", Color.RED + "Hunters are free now", 20, 2*20, 20);
            }
        }
    }

    public static void resetPlayer(Player player){

        removeSpectatorsEffects(player);

        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        player.setFlying(false);
        player.setAllowFlight(false);

        player.getInventory().clear();
        player.getInventory().setArmorContents(null);

        AttributeInstance maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealth != null){
            player.setHealth(maxHealth.getValue());
        }

        player.setFoodLevel(20);
        player.setSaturation(5.0f);

        player.setExp(0);
        player.setLevel(0);

        player.setFireTicks(0);
        player.setFallDistance(0);

        player.setGameMode(GameMode.SURVIVAL);

        Bukkit.advancementIterator().forEachRemaining(
                advancement -> {
                    AdvancementProgress progress = player.getAdvancementProgress(advancement);
                    progress.getAwardedCriteria().forEach(progress::revokeCriteria);
                }
        );
    }

    public static void removeSpectatorsEffects(Player targetPlayer){

        Bukkit.getScheduler().runTaskLater(Manhunt.getINSTANCE(), () -> {

            Bukkit.getOnlinePlayers().forEach(player ->
                    player.showPlayer(Manhunt.getINSTANCE(), targetPlayer));

            targetPlayer.setInvulnerable(false);
            targetPlayer.setInvisible(false);
            targetPlayer.setGameMode(GameMode.SURVIVAL);
            targetPlayer.setFlying(false);
            targetPlayer.setAllowFlight(false);
        }, 5L);
    }

    public static void applySpectatorsEffects(Player targetPlayer){

        if(!targetPlayer.isDead()) {
            ItemStack spectatorCompass = CustomItemType.create(CustomItemType.SPECTATOR_COMPASS);

            if(!targetPlayer.getInventory().contains(spectatorCompass)){
                targetPlayer.getInventory().addItem(spectatorCompass);
            }
        }

        Scoreboard mainScoreboard = ScoreManager.getScoreboard();

        Bukkit.getScheduler().runTaskLater(Manhunt.getINSTANCE(), () -> {

            for (Player player: Bukkit.getOnlinePlayers()) {

                if (targetPlayer.getName().equals(player.getName())) continue;

                Team team = mainScoreboard.getTeamOf(player.getUniqueId());

                if (team == null){
                    Team team1 = mainScoreboard.getTeam(Settings.SPECTATOR_TEAM_NAME);
                    if(team1==null) continue;
                    team1.addPlayer(player.getUniqueId());
                    continue;
                }

                if (team.getName().equals(Settings.SPECTATOR_TEAM_NAME)){
                    player.showPlayer(Manhunt.getINSTANCE(), targetPlayer);
                    targetPlayer.showPlayer(Manhunt.getINSTANCE(), player);
                }else{
                    player.hidePlayer(Manhunt.getINSTANCE(), targetPlayer); // this will be removed when a players leave and rejoin or server restart not at death and respawn
                }
            }

            if(!targetPlayer.isDead()) {
                targetPlayer.setInvulnerable(false);
                targetPlayer.setInvisible(true);

                targetPlayer.setGameMode(GameMode.ADVENTURE);

                Bukkit.getScheduler().runTaskLater(Manhunt.getINSTANCE(), () -> {
                    targetPlayer.setAllowFlight(true);
                    targetPlayer.setFlying(true);
                    targetPlayer.setInvulnerable(true);
                }, 1L);
            }
        }, 5L);
    }

    public static void teleportPlayerToLobby(Player player){

        if (player.isDead()){
            player.spigot().respawn();
        }
        resetPlayer(player);

        Location lobby = Settings.config().game().world().getLobbySpawn();

        if (lobby == null){
            World lobbyWorld = Bukkit.getWorld(Settings.config().game().world().getLobbyWorld());
            if (lobbyWorld == null){
                player.sendMessage(Color.RED + "There is no lobby to teleport to");
                return;
            }
            player.teleport(lobbyWorld.getSpawnLocation());
            if(player.isOp()){
                player.sendMessage(" ");
                player.sendMessage(" ");
                player.sendMessage(Color.RED + "⚠ " + Color.WHITE + "Lobby spawn is not configured.");
                player.sendMessage(Color.GRAY + "    Please set the lobby location, or players");
                player.sendMessage(Color.GRAY + "    will be teleported to the lobby world's spawn point.");
                player.sendMessage(" ");
            }
            return;
        }

        player.teleport(lobby);
    }

    public static void tpPlayerToLobbyForTransition(Player player){

        if (player.isDead()){
            player.spigot().respawn();
        }

        Location lobby = Settings.config().game().world().getLobbySpawn();

        World lobbyWorld = Bukkit.getWorld(Settings.config().game().world().getLobbyWorld());
        if (lobbyWorld == null){
            player.sendMessage(Color.RED + "There is no lobby to teleport to");
            return;
        }

        if (lobby == null){
            Location spawn = lobbyWorld.getSpawnLocation().clone();
            spawn.setY(lobbyWorld.getHighestBlockYAt(spawn));
            player.teleport(spawn);
            return;
        }

        player.teleport(lobby);
    }

    public static void showTransitionTitles(int time){

        Bukkit.getOnlinePlayers().forEach(player -> {
            switch(time){
                case 12 -> {
                    sendTitle(player,
                            "What’s happening…?",
                            "§7Why does the world suddenly feel so quiet?");
                    player.playSound(
                            player.getLocation(),
                            Sound.ENTITY_WARDEN_HEARTBEAT,
                            4f,
                            0.6f
                    );
                }

                case 20 -> {
                    sendTitle(player,
                            "§4SOMETHING JUST CHANGED",
                            "§7I can feel it… deep beneath the caves");
                    player.playSound(
                            player.getLocation(),
                            Sound.ENTITY_WARDEN_HEARTBEAT,
                            4f,
                            0.6f
                    );
                }

                case 37 -> {
                    sendTitle(player,
                            "§6THIS IS MY CHANCE…",
                            "§7To bring this chase to an end");
                    player.playSound(
                            player.getLocation(),
                            Sound.ENTITY_WARDEN_HEARTBEAT,
                            4f,
                            0.6f
                    );
                }
                case 55 -> {
                    sendTitle(player,
                            "§c§lTHE STRONGHOLD",
                            "§7It’s finally here…");
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1f, 0.6f);
                }
            }
        });
    }

    public static void stopIntenseVoidEffect(Player player) {
        if (!voidEffectPlayers.remove(player.getUniqueId())) {
            return;
        }

        player.removePotionEffect(PotionEffectType.INVISIBILITY);
        player.removePotionEffect(PotionEffectType.BLINDNESS);
        player.removePotionEffect(PotionEffectType.DARKNESS);
        player.removePotionEffect(PotionEffectType.SLOW_FALLING);
    }

    public static void playIntenseVoidEffect(Player player) {

        voidEffectPlayers.add(player.getUniqueId());

        Location loc = player.getLocation();

        player.addPotionEffect(new PotionEffect(
                PotionEffectType.INVISIBILITY,
                20*65, 0, false, false
        ));
        // HARD CUT
        player.addPotionEffect(new PotionEffect(
                PotionEffectType.BLINDNESS,
                20*65, 0, false, false
        ));
        // MAIN VOID DARKNESS
        player.addPotionEffect(new PotionEffect(
                PotionEffectType.DARKNESS,
                20*65, // 6 seconds
                0, false, false
        ));

        // FLOATING THROUGH PORTAL FEEL
        /*
        / player.addPotionEffect(new PotionEffect(
        /        PotionEffectType.LEVITATION,
        /        20*15, 0, false, false
        /));
        */

        player.addPotionEffect(new PotionEffect(
                PotionEffectType.SLOW_FALLING,
                20*65, 0, false, false
        ));
        // SOUNDS (PORTAL + WARDEN)
        player.playSound(loc, Sound.ENTITY_WARDEN_AMBIENT, 0.8f, 0.6f);

        // PARTICLE LOOP (PULLING INTO PORTAL)
        new BukkitRunnable() {
            int second = 0;

            @Override
            public void run() {

                if (
                        ManhuntGame.getCurrentGame().getGameState().equals(GameState.STRONGHOLD) ||
                        ManhuntGame.getCurrentGame().getGameState().equals(GameState.ENDED)
                ) {
                    cancel();
                    return;
                }

                Location pLoc = player.getLocation().clone().add(0, 1, 0);
                // Portal pull
                player.getWorld().spawnParticle(
                        Particle.REVERSE_PORTAL,
                        pLoc,
                        40,
                        0.5, 0.7, 0.5,
                        0
                );
                // Void atmosphere
                player.getWorld().spawnParticle(
                        Particle.SCULK_SOUL,
                        pLoc,
                        8,
                        0.3, 0.5, 0.3,
                        0
                );
                second++;
            }
        }.runTaskTimer(Manhunt.getINSTANCE(), 0, 10);
    }

    private static void sendTitle(Player player, String title, String subtitle) {
        player.sendTitle(title, subtitle, 20, 4*20, 20);
    }
}