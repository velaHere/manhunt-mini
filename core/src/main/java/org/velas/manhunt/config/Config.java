package org.velas.manhunt.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.Manhunt;

public final class Config {

    private static boolean initialized;

    private final @NotNull GameSettings gameSettings;
    private final @NotNull DropSettings dropSettings;
    private final @NotNull AbilitySettings abilitySettings;
    private final @NotNull TeamSettings teamSettings;
    private final @NotNull ItemSettings itemSettings;

    private Config(
            @NotNull GameSettings gameSettings,
            @NotNull DropSettings dropSettings,
            @NotNull AbilitySettings abilitySettings,
            @NotNull TeamSettings teamSettings,
            @NotNull ItemSettings itemSettings
    ) {
        this.gameSettings = gameSettings;
        this.dropSettings = dropSettings;
        this.abilitySettings = abilitySettings;
        this.teamSettings = teamSettings;
        this.itemSettings = itemSettings;
    }

    public static void init() {
        if(initialized) return;
        initialized = true;

        Manhunt.getINSTANCE().saveDefaultConfig();
        Manhunt.getINSTANCE().getConfig().options().copyDefaults(true);
        Manhunt.getINSTANCE().saveConfig();
        reload();
    }

    public static void reload() {

        FileConfiguration config = Manhunt.getINSTANCE().getConfig();

        WorldSettings world = new WorldSettings(
                config.getString("game.world.default", "world"),
                config.getString("game.world.lobby.name", "lobby"),
                config.getBoolean("game.world.lobby.allow-interactions.op", true),
                config.getBoolean("game.world.lobby.allow-interactions.default", false)
        );

        GameSettings game = new GameSettings(
                config.getInt("game.head-start", 30),
                config.getInt("game.compass-radius", 15),
                config.getInt("keep-inventory-percentage", 30),
                world
        );

        DropSettings.RESPAWN respawn = new DropSettings.RESPAWN(
                config.getBoolean("drop.RESPAWN.enabled", true),
                config.getInt("drop.RESPAWN.cooldown", 300),
                config.getInt("drop.RESPAWN.radius", 10),
                config.getInt("drop.RESPAWN.lifetime", 45)
        );

        DropSettings drop = new DropSettings(
                config.getInt("drop.interval", 120),
                config.getInt("drop.lifetime", 180),
                respawn
        );

        AbilitySettings ability = new AbilitySettings(
                config.getBoolean("ability.enabled", true)
        );

        TeamSettings.HUNTER hunter = new TeamSettings.HUNTER(
                config.getString("team.HUNTER.prefix", "&4[Hunter]")
        );

        TeamSettings.RUNNER runner = new TeamSettings.RUNNER(
                config.getString("team.RUNNER.prefix", "&2[Runner]")
        );

        TeamSettings.SPECTATOR spectator = new TeamSettings.SPECTATOR(
                config.getString("team.SPECTATOR.prefix", "&8[Spectator]")
        );

        TeamSettings team = new TeamSettings(hunter, runner, spectator);

        ItemSettings.GOLA gola = new ItemSettings.GOLA(
                config.getInt("item.GOLA.cooldown", 1)
        );

        ItemSettings item = new ItemSettings(gola);

        Config configuration = new Config(game, drop, ability, team, item);
        Settings.setConfig(configuration);
    }

    public @NotNull GameSettings game() {
        return gameSettings;
    }

    public @NotNull DropSettings drop() {
        return dropSettings;
    }

    public @NotNull AbilitySettings ability() {
        return abilitySettings;
    }

    public @NotNull TeamSettings team() {
        return teamSettings;
    }

    public @NotNull ItemSettings item() {
        return itemSettings;
    }
}
