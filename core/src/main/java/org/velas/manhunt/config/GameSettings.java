package org.velas.manhunt.config;

import org.jetbrains.annotations.NotNull;

public final class GameSettings {

    private final int headStart;
    private final double compassRadius;
    private final double keepInventoryPercentage;
    private final @NotNull WorldSettings world;

    GameSettings(
            int headStart,
            double compassRadius,
            double keepInventoryPercentage,
            @NotNull WorldSettings world
    ) {
        if (headStart < 0)
            throw new IllegalArgumentException(
                    "game.head-start cannot be negative."
            );

        if (compassRadius < 0)
            throw new IllegalArgumentException(
                    "game.compass-radius cannot be negative."
            );

        if (keepInventoryPercentage < 0 || keepInventoryPercentage > 100)
            throw new IllegalArgumentException(
                    "game.keep-inventory-percentage must be between 0 and 100."
            );

        this.headStart = headStart;
        this.compassRadius = compassRadius;
        this.keepInventoryPercentage = keepInventoryPercentage;
        this.world = world;
    }

    public int getHeadStart() {
        return headStart;
    }

    public double getCompassRadius() {
        return compassRadius;
    }

    public double getKeepInventoryPercentage() {
        return keepInventoryPercentage;
    }

    public @NotNull WorldSettings world() {
        return world;
    }
}