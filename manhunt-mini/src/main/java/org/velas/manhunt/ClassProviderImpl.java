package org.velas.manhunt;

import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.customItems.CustomItemType;
import org.velas.manhunt.customItems.items.Gola;
import org.velas.manhunt.customItems.items.IronReaver;
import org.velas.manhunt.customItems.items.KeyToConvergence;
import org.velas.manhunt.game.ManhuntGame;
import org.velas.manhunt.game.timeline.event.*;
import org.velas.manhunt.player.BundleProviderImpl;
import org.velas.manhunt.player.handler.BundleProvider;

public class ClassProviderImpl implements ClassProvider {
    @Override
    public @NotNull BundleProvider getBundleProvider() {
        return new BundleProviderImpl();
    }

    @Override
    public @NotNull EventHandlerBundle getEventHandlerBundle(@NotNull ManhuntGame currentGame) {
        return new EventHandlerBundle(
                new DropSummonEvent(currentGame),
                new BaseManhuntGameStartEvent(currentGame),
                new BaseWorldGenerationEvent(currentGame),
                new WorldPreloadEvent(currentGame),
                new BaseMatchStartBroadcastEvent(currentGame),
                new BaseMatchStartEvent(currentGame),
                new BaseMatchHeadStartEvent(currentGame),
                new MatchPlayingPhaseEvent(currentGame),
                new MiscellaneousEvent(currentGame)
        );
    }

    @Override
    public void registerCustomItems() {
        CustomItemType.register(CustomItemType.GOLA, Gola.INSTANCE);
        CustomItemType.register(CustomItemType.IRON_REAVER, IronReaver.INSTANCE);
        CustomItemType.register(CustomItemType.KEY_TO_CONVERGENCE, KeyToConvergence.INSTANCE);
    }
}
