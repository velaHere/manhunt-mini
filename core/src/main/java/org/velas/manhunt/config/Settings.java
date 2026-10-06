package org.velas.manhunt.config;

public final class Settings {
    public static final boolean DEBUG = true;
    public static final String SETUP_PERMISSION = "manhunt.setup";
    public static final String HUNTER_TEAM_NAME = "Hunters";
    public static final String RUNNER_TEAM_NAME = "Runners";
    public static final String SPECTATOR_TEAM_NAME = "Spectators";
    public static final String OVERWORLD_NAME = "Manhunt/world";
    public static final String NETHER_WORLD_NAME = OVERWORLD_NAME +"_nether";
    public static final String END_WORLD_NAME = OVERWORLD_NAME +"_the_end";
    private static Config config;

    public static Config config() {
        if(config == null) {
            Config.init();
        }

        if(config == null)
            throw new IllegalStateException("Config cannot bt null");

        return config;
    }

    static void setConfig(Config config) {
        Settings.config = config;
    }
}
