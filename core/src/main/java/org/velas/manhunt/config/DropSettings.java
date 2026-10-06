package org.velas.manhunt.config;

import org.jetbrains.annotations.NotNull;

public final class DropSettings {

    private final int interval;
    private final int lifetime;
    private final @NotNull RESPAWN respawn;

    DropSettings(
            int interval,
            int lifetime,
            @NotNull RESPAWN respawn
    ) {
        if (interval < 0)
            throw new IllegalArgumentException(
                    "drop.interval cannot be negative."
            );

        if (lifetime < 0)
            throw new IllegalArgumentException(
                    "drop.lifetime cannot be negative."
            );

        this.interval = interval;
        this.lifetime = lifetime;
        this.respawn = respawn;
    }

    public int getInterval() {
        return interval;
    }

    public int getLifetime() {
        return lifetime;
    }

    public @NotNull RESPAWN respawn() {
        return respawn;
    }

    public static final class RESPAWN {

        private final boolean enabled;
        private final int cooldown;
        private final int radius;
        private final int lifetime;

        RESPAWN(
                boolean enabled,
                int cooldown,
                int radius,
                int lifetime
        ) {
            if (cooldown < 0)
                throw new IllegalArgumentException(
                        "drop.respawn.cooldown cannot be negative."
                );

            if (radius < 0)
                throw new IllegalArgumentException(
                        "drop.respawn.radius cannot be negative."
                );

            if (lifetime < 0)
                throw new IllegalArgumentException(
                        "drop.respawn.lifetime cannot be negative."
                );

            this.enabled = enabled;
            this.cooldown = cooldown;
            this.radius = radius;
            this.lifetime = lifetime;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public int getCooldown() {
            return cooldown;
        }

        public int getRadius() {
            return radius;
        }

        public int getLifetime() {
            return lifetime;
        }
    }
}