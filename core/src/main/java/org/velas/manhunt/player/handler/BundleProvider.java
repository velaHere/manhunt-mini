package org.velas.manhunt.player.handler;

import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.player.Hunter;
import org.velas.manhunt.player.Runner;
import org.velas.manhunt.player.Spectator;

public interface BundleProvider {
    @NotNull HandlerBundle getHunterBundle(@NotNull Hunter hunter);
    @NotNull HandlerBundle getRunnerBundle(@NotNull Runner runner);
    @NotNull HandlerBundle getSpectatorBundle(@NotNull Spectator spectator);
}
