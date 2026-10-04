package org.velas.manhunt.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class ConfigManager {

    private static final String KEEP_INVENTORY_PERCENTAGE = "keep-inventory-percentage";
    private static final String DROP_INTERVAL_SEC = "drop-interval";
    private static final String COMPASS_RADIUS = "compass-radius";
    private static final String HEAD_START_SEC = "headstart";
    private static final String DROP_LIFE_TIME = "drop-lifetime"; // 3 minutes
    private static final String HUNTER_TEAM_PREFIX = "team.hunter.prefix";
    private static final String RUNNER_TEAM_PREFIX = "team.runner.prefix";
    private static final String SPECTATOR_TEAM_PREFIX = "team.spectator.prefix";
    private static final String LEVEL_WORLD_NAME = "world.default";
    private static final String LOBBY_WORLD_NAME = "world.lobby";
    private static final String ALLOW_ABILITIES_NAME = "allow-abilities";

    private ConfigManager() {}

    public static void init(JavaPlugin plugin) {
        plugin.saveDefaultConfig();
        plugin.getConfig().options().copyDefaults(true);
        plugin.saveConfig();
        reload(plugin);
    }

    public static void reload(JavaPlugin plugin) {
        plugin.reloadConfig();

        FileConfiguration config = plugin.getConfig();

        Settings.setConfigSettings(
                ConfigSettings.getConfigBuilder()
                        .keepInventoryPercentage(config.getInt(KEEP_INVENTORY_PERCENTAGE))
                        .dropIntervalSeconds(config.getInt(DROP_INTERVAL_SEC))
                        .compassRadius(config.getInt(COMPASS_RADIUS))
                        .headStartTime(config.getInt(HEAD_START_SEC))
                        .dropLifeTime(config.getInt(DROP_LIFE_TIME))
                        .hunterTeamPrefix(config.getString(HUNTER_TEAM_PREFIX))
                        .runnerTeamPrefix(config.getString(RUNNER_TEAM_PREFIX))
                        .spectatorTeamPrefix(config.getString(SPECTATOR_TEAM_PREFIX))
                        .defaultWorldName(config.getString(LEVEL_WORLD_NAME))
                        .lobbyWorldName(config.getString(LOBBY_WORLD_NAME))
                        .allowAbilities(config.getBoolean(ALLOW_ABILITIES_NAME))
                        .build()
        );
    }
}
