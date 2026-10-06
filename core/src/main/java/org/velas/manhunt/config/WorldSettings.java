package org.velas.manhunt.config;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.velas.manhunt.Manhunt;

import java.util.Objects;

public final class WorldSettings {

    private static final String PATH = "game.world.lobby-spawn.";

    private final @NotNull String defaultWorld;
    private final @NotNull String lobbyWorld;
    private final boolean allowOpInteractionsInLobby;
    private final boolean allowDefaultInteractionsInLobby;

    private @Nullable Location lobbySpawn;

    WorldSettings(
            @NotNull String defaultWorld,
            @NotNull String lobbyWorld,
            boolean opInteraction,
            boolean defaultInteraction
    ) {
        if (defaultWorld.isBlank())
            throw new IllegalArgumentException(
                    "game.world.default cannot be empty."
            );

        if (lobbyWorld.isBlank())
            throw new IllegalArgumentException(
                    "game.world.lobby cannot be empty."
            );

        if (Bukkit.getWorld(defaultWorld) == null)
            throw new IllegalArgumentException(
                    "Default world does not exist: " + defaultWorld
            );

        this.defaultWorld = defaultWorld;
        this.lobbyWorld = lobbyWorld;
        this.lobbySpawn = loadLobbySpawn();
        this.allowOpInteractionsInLobby = opInteraction;
        this.allowDefaultInteractionsInLobby = defaultInteraction;
    }

    public @NotNull String getDefaultWorld() {
        return defaultWorld;
    }

    public @NotNull String getLobbyWorld() {
        return lobbyWorld;
    }

    public @Nullable Location getLobbySpawn() {
        return lobbySpawn == null ? null : lobbySpawn.clone();
    }

    public void setLobbySpawn(@NotNull Location location) {
        if (!Objects.requireNonNull(location.getWorld()).getName().equals(lobbyWorld))
            throw new IllegalArgumentException(
                    "Lobby spawn must be in the lobby world: " + lobbyWorld
            );

        this.lobbySpawn = location.clone();

        FileConfiguration config = Manhunt.getINSTANCE().getConfig();

        config.set(PATH + "x", location.getX());
        config.set(PATH + "y", location.getY());
        config.set(PATH + "z", location.getZ());
        config.set(PATH + "yaw", location.getYaw());
        config.set(PATH + "pitch", location.getPitch());

        Manhunt.getINSTANCE().saveConfig();
    }

    private @Nullable Location loadLobbySpawn() {
        World world = Bukkit.getWorld(lobbyWorld);

        if (world == null)
            return null;

        FileConfiguration config = Manhunt.getINSTANCE().getConfig();

        if (!config.isSet(PATH + "x")
                || !config.isSet(PATH + "y")
                || !config.isSet(PATH + "z")
                || !config.isSet(PATH + "yaw")
                || !config.isSet(PATH + "pitch")) {
            return null;
        }

        return new Location(
                world,
                config.getDouble(PATH + "x"),
                config.getDouble(PATH + "y"),
                config.getDouble(PATH + "z"),
                (float) config.getDouble(PATH + "yaw"),
                (float) config.getDouble(PATH + "pitch")
        );
    }

    public boolean canInteractInLobby(@NotNull Player player) {
        if (!player.getWorld().getName().equals(lobbyWorld))
            return true;

        if (player.isOp())
            return allowOpInteractionsInLobby;

        return allowDefaultInteractionsInLobby;
    }
}