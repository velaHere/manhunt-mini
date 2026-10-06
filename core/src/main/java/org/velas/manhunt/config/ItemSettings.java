package org.velas.manhunt.config;

public final class ItemSettings {

    private final GOLA gola;

    ItemSettings(GOLA gola) {
        this.gola = gola;
    }

    public GOLA gola() {
        return gola;
    }

    public static final class GOLA {

        private final int cooldown;

        GOLA(int cooldown) {
            if (cooldown < 0)
                throw new IllegalArgumentException(
                        "item.gola.cooldown cannot be negative."
                );

            this.cooldown = cooldown;
        }

        public int getCooldown() {
            return cooldown;
        }
    }
}