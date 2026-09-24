package org.velas.manhunt;

import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.game.ManhuntGame;
import org.velas.manhunt.game.timeline.event.EventHandlerBundle;
import org.velas.manhunt.player.handler.BundleProvider;

public interface ClassProvider {
    @NotNull BundleProvider getBundleProvider();
    @NotNull EventHandlerBundle getEventHandlerBundle(@NotNull ManhuntGame currentGame);
    void registerCustomItems();
}
