package org.velas.manhunt.config;

public final class Settings {
    public static final boolean DEBUG = true;
    public static final String SETUP_PERMISSION = "manhunt.setup";
    public static final String HUNTER_TEAM_NAME = "Hunters";
    public static final String RUNNER_TEAM_NAME = "Runners";
    public static final String SPECTATOR_TEAM_NAME = "Spectators";
    public static final String OVERWORLD_NAME = "org/velas/manhunt";
    public static final String NETHER_WORLD_NAME = OVERWORLD_NAME +"_nether";
    public static final String END_WORLD_NAME = OVERWORLD_NAME +"_the_end";
    private static ConfigSettings configSettings = new ConfigSettings();

    public static ConfigSettings getConfigSettings() {
        return configSettings;
    }

    static void setConfigSettings(ConfigSettings configSettings) {
        Settings.configSettings = configSettings;
    }
}
