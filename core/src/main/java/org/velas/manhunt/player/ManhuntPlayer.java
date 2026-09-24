package org.velas.manhunt.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.velas.manhunt.Manhunt;
import org.velas.manhunt.avatar.Avatar;
import org.velas.manhunt.avatar.AvatarType;
import org.velas.manhunt.game.ManhuntGame;
import org.velas.manhunt.game.scoreboard.ScoreManager;
import org.velas.manhunt.player.handler.BundleProvider;
import org.velas.manhunt.player.handler.HandlerBundle;
import org.velas.scoreboard.api.sidebar.PlayerSidebar;

import java.util.UUID;


public abstract class ManhuntPlayer {

    private final @NotNull UUID uuid;
    private @Nullable Avatar avatar;
    private final @NotNull ManhuntGame manhuntGame;
    private final @NotNull PlayerSidebar sidebar;
    private HandlerBundle handlerBundle;

    private boolean isOnline;

    ManhuntPlayer(PlayerBuilder builder){
        this.manhuntGame = builder.manhuntGame;
        this.uuid = builder.uuid;
        this.sidebar = ScoreManager.getScoreboard().createPlayerSidebar("§6§lMANHUNT");
        sidebar.setPlayer(uuid);
        this.isOnline = true;
        this.avatar=builder.avatarType.createNewAvatar(this);
    }

    public void setOnline(boolean status){
        this.isOnline = status;
    }

    public void setAvatar(@NotNull AvatarType type){
        this.avatar=type.createNewAvatar(this);
    }

    public @NotNull PlayerSidebar getSidebar(){
        return this.sidebar;
    }

    public @Nullable Player getPlayer(){
        return Bukkit.getPlayer(this.uuid);
    }
    public @NotNull ManhuntGame getManhuntGame(){
        return this.manhuntGame;
    }
    public UUID getUniqueId(){
        return this.uuid;
    }
    public HandlerBundle getHandlerBundle(){
        return this.handlerBundle;
    }
    public @Nullable Avatar getAvatar(){
        return this.avatar;
    }
    public boolean isOnline(){
        return this.isOnline;
    }
    public boolean isHunter(){
        return this instanceof Hunter;
    }
    public boolean isRunner(){
        return this instanceof Runner;
    }
    public boolean isSpectator(){
        return this instanceof Spectator;
    }

    final void wireHandlers(HandlerBundle bundle){
        this.handlerBundle=bundle;
    }

    public static PlayerBuilder builder(){
        return new PlayerBuilder();
    }

    public static final class PlayerBuilder{

        private static final BundleProvider bundleProvider = Manhunt.getPROVIDER().getBundleProvider();

        private UUID uuid;
        private ManhuntGame manhuntGame;
        private AvatarType avatarType;

        public PlayerBuilder player(UUID playerUUID){
            this.uuid=playerUUID;
            return this;
        }

        public PlayerBuilder game(ManhuntGame game){
            this.manhuntGame=game;
            return this;
        }

        public PlayerBuilder avatarType(AvatarType avatarType){
            this.avatarType=avatarType;
            return this;
        }

        private void validate(){
            if(this.uuid==null || this.manhuntGame==null || this.avatarType==null){
                throw new IllegalStateException("UUID, Game, AvatarType are required");
            }
        }

        public Hunter buildHunter(){
            validate();
            Hunter hunter = new Hunter(this);
            hunter.wireHandlers(bundleProvider.getHunterBundle(hunter));
            return hunter;
        }

        public Runner buildRunner(){
            validate();
            Runner runner = new Runner(this);
            runner.wireHandlers(bundleProvider.getRunnerBundle(runner));
            return runner;
        }

        public Spectator buildSpectator(){
            validate();
            Spectator spectator = new Spectator(this);
            spectator.wireHandlers(bundleProvider.getSpectatorBundle(spectator));
            return spectator;
        }
    }
}