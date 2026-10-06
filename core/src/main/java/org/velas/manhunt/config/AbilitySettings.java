package org.velas.manhunt.config;

public final class AbilitySettings {

    private final boolean enabled;

    AbilitySettings(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }
}