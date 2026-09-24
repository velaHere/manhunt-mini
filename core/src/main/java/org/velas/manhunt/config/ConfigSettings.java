package org.velas.manhunt.config;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.velas.manhunt.Manhunt;

public final class ConfigSettings {
    private double KEEP_INVENTORY_PERCENTAGE = 30;
    private int DROP_INTERVAL_SEC = 120;
    private double COMPASS_RADIUS = 15.0;
    private long HEAD_START_SEC = 30;
    private int DROP_LIFE_TIME = 3*60; // 3 minutes
    private String HUNTER_TEAM_PREFIX = ChatColor.DARK_RED + "[Hunter] ";
    private String RUNNER_TEAM_PREFIX = ChatColor.DARK_GREEN + "[Runner] ";
    private String SPECTATOR_TEAM_PREFIX = ChatColor.DARK_GRAY + "[Spectator] ";
    private String LEVEL_WORLD_NAME = "world";
    private String LOBBY_WORLD_NAME = "lobby";

    ConfigSettings() {}

    static ConfigBuilder getConfigBuilder() {
        return new ConfigBuilder();
    }

    @Nullable
    public Location getLobbySpawn() {

        World world = Bukkit.getWorld(getLOBBY_WORLD_NAME());
        if(world == null) return null;

        String path = "lobby-spawn.";
        FileConfiguration config = Manhunt.getINSTANCE().getConfig();
        if(
                !config.isSet(path+"x")
                || !config.isSet(path+"y")
                || !config.isSet(path+"z")
                || !config.isSet(path+"yaw")
                || !config.isSet(path+"pitch")
        ) return null;

        double x = config.getDouble(path + "x");
        double y = config.getDouble(path + "y");
        double z = config.getDouble(path + "z");
        float yaw = (float) config.getDouble(path + "yaw");
        float pitch = (float) config.getDouble(path + "pitch");

        return new Location(world, x, y, z, yaw, pitch);
    }

    public void saveLobbySpawn(@NotNull Location location) {
        String path = "lobby-spawn.";
        FileConfiguration config = Manhunt.getINSTANCE().getConfig();

        config.set(path+"x", location.getX());
        config.set(path+"y", location.getY());
        config.set(path+"z", location.getZ());
        config.set(path+"yaw", location.getYaw());
        config.set(path+"pitch", location.getPitch());
        Manhunt.getINSTANCE().saveConfig();
    }

    public double getKEEP_INVENTORY_PERCENTAGE() {
        return KEEP_INVENTORY_PERCENTAGE;
    }

    public int getDROP_INTERVAL_SEC() {
        return DROP_INTERVAL_SEC;
    }

    public double getCOMPASS_RADIUS() {
        return COMPASS_RADIUS;
    }

    public long getHEAD_START_SEC() {
        return HEAD_START_SEC;
    }

    public int getDROP_LIFE_TIME() {
        return DROP_LIFE_TIME;
    }

    public String getHUNTER_TEAM_PREFIX() {
        return HUNTER_TEAM_PREFIX;
    }

    public String getRUNNER_TEAM_PREFIX() {
        return RUNNER_TEAM_PREFIX;
    }

    public String getSPECTATOR_TEAM_PREFIX() {
        return SPECTATOR_TEAM_PREFIX;
    }

    public String getLEVEL_WORLD_NAME() {
        return LEVEL_WORLD_NAME;
    }

    public String getLOBBY_WORLD_NAME() {
        return LOBBY_WORLD_NAME;
    }

    static class ConfigBuilder {

        private final ConfigSettings settings;

        ConfigBuilder() {
            this.settings = new ConfigSettings();
        }

        public ConfigSettings build() {
            return this.settings;
        }

        public ConfigBuilder keepInventoryPercentage(double amt) {
            if(amt<0 || amt>100)
                throw new IllegalArgumentException("Keep inventory percentage cannot be less than 0 or greater than 100, fix it in config.yml");
            settings.KEEP_INVENTORY_PERCENTAGE = Math.ceil(amt/100);
            return this;
        }

        public ConfigBuilder dropIntervalSeconds(int seconds) {
            if(seconds < 0 || seconds == Integer.MAX_VALUE)
                throw new IllegalArgumentException("Drop interval cannot be negative, fix it in config.yml");
            settings.DROP_INTERVAL_SEC=seconds;
            return this;
        }

        public ConfigBuilder compassRadius(int amt) {
            if(amt < 0 || amt == Integer.MAX_VALUE)
                throw new IllegalArgumentException("Compass Radius cannot be negative or too long, fix it in config.yml");
            settings.COMPASS_RADIUS=amt;
            return this;
        }

        public ConfigBuilder headStartTime(int time) {
            if(time < 0)
                throw new IllegalArgumentException("Drop interval cannot be negative, fix it in config.yml");
            settings.HEAD_START_SEC=time;
            return this;
        }

        public ConfigBuilder dropLifeTime(int seconds) {
            if(seconds < 0)
                throw new IllegalArgumentException("Drop life time cannot be negative, fix it in config.yml");
            settings.DROP_LIFE_TIME=seconds;
            return this;
        }

        public ConfigBuilder hunterTeamPrefix(@NotNull String prefix) {
            if(prefix.isEmpty())
                throw new IllegalArgumentException("Hunter team prefix is empty, edit it in config.yml");
            settings.HUNTER_TEAM_PREFIX = prefix.replace('&', '§')+' ';
            return this;
        }

        public ConfigBuilder runnerTeamPrefix(@NotNull String prefix) {
            if(prefix.isEmpty())
                throw new IllegalArgumentException("Runner team prefix is empty, edit it in config.yml");
            settings.RUNNER_TEAM_PREFIX = prefix.replace('&', '§')+' ';
            return this;
        }

        public ConfigBuilder spectatorTeamPrefix(@NotNull String prefix) {
            if(prefix.isEmpty())
                throw new IllegalArgumentException("Spectator team prefix is empty, edit it in config.yml");
            settings.SPECTATOR_TEAM_PREFIX = prefix.replace('&', '§')+' ';
            return this;
        }

        public ConfigBuilder defaultWorldName(@NotNull String name) {
            if(name.isEmpty())
                throw new IllegalArgumentException("Empty default world name, edit it in config.yml");
            if(Bukkit.getWorld(name) == null)
                throw new IllegalArgumentException("There is no world exists with the name " + name + ", edit default world name in config.yml");
            settings.LEVEL_WORLD_NAME = name;
            return this;
        }

        public ConfigBuilder lobbyWorldName(@NotNull String name) {
            if(name.isEmpty())
                throw new IllegalArgumentException("Empty lobby world name, edit it in config.yml");
            settings.LOBBY_WORLD_NAME=name;
            return this;
        }
    }
}
