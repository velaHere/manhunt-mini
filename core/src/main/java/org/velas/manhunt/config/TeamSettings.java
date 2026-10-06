package org.velas.manhunt.config;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

public final class TeamSettings {

    private final @NotNull HUNTER hunter;
    private final @NotNull RUNNER runner;
    private final @NotNull SPECTATOR spectator;

    TeamSettings(
            @NotNull HUNTER hunter,
            @NotNull RUNNER runner,
            @NotNull SPECTATOR spectator
    ) {
        this.hunter = hunter;
        this.runner = runner;
        this.spectator = spectator;
    }

    public @NotNull HUNTER hunter() {
        return hunter;
    }

    public @NotNull RUNNER runner() {
        return runner;
    }

    public @NotNull SPECTATOR spectator() {
        return spectator;
    }

    public static final class HUNTER {

        private final String prefix;

        HUNTER(@NotNull String prefix) {
            this.prefix = formatPrefix(prefix, "team.hunter.prefix");
        }

        public String getPrefix() {
            return prefix;
        }
    }

    public static final class RUNNER {

        private final String prefix;

        RUNNER(@NotNull String prefix) {
            this.prefix = formatPrefix(prefix, "team.runner.prefix");
        }

        public String getPrefix() {
            return prefix;
        }
    }

    public static final class SPECTATOR {

        private final String prefix;

        SPECTATOR(@NotNull String prefix) {
            this.prefix = formatPrefix(prefix, "team.spectator.prefix");
        }

        public String getPrefix() {
            return prefix;
        }
    }

    private static String formatPrefix(
            String prefix,
            String path
    ) {
        if (prefix.isBlank())
            throw new IllegalArgumentException(
                    path + " cannot be empty."
            );

        return ChatColor.translateAlternateColorCodes('&', prefix) + " ";
    }
}