package org.velas.manhunt.player;

import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.player.handler.BundleProvider;
import org.velas.manhunt.player.handler.HandlerBundle;
import org.velas.manhunt.player.handler.SidebarHandler;
import org.velas.manhunt.player.handler.combat.BaseSpectatorCombatHandler;
import org.velas.manhunt.player.handler.combat.HunterCombatHandler;
import org.velas.manhunt.player.handler.combat.RunnerCombatHandler;
import org.velas.manhunt.player.handler.inventory.BaseHunterInventoryHandler;
import org.velas.manhunt.player.handler.inventory.BaseRunnerInventoryHandler;
import org.velas.manhunt.player.handler.inventory.BaseSpectatorInventoryHandler;
import org.velas.manhunt.player.handler.lifecycle.BaseSpectatorLifecycleHandler;
import org.velas.manhunt.player.handler.lifecycle.HunterLifecycleHandler;
import org.velas.manhunt.player.handler.lifecycle.RunnerLifecycleHandler;
import org.velas.manhunt.player.handler.movement.BaseHunterMovementHandler;
import org.velas.manhunt.player.handler.movement.BaseRunnerMovementHandler;
import org.velas.manhunt.player.handler.movement.BaseSpectatorMovementHandler;
import org.velas.manhunt.player.handler.playerInteraction.BaseSpectatorInteractionHandler;
import org.velas.manhunt.player.handler.playerInteraction.HunterInteractionHandler;
import org.velas.manhunt.player.handler.playerInteraction.RunnerInteractionHandler;
import org.velas.manhunt.player.handler.vehicle.BaseSpectatorVehicleHandler;
import org.velas.manhunt.player.handler.vehicle.HunterVehicleHandler;
import org.velas.manhunt.player.handler.vehicle.RunnerVehicleHandler;
import org.velas.manhunt.player.handler.worldInteraction.BaseSpectatorWorldInteractionHandler;
import org.velas.manhunt.player.handler.worldInteraction.HunterWorldInteractionHandler;
import org.velas.manhunt.player.handler.worldInteraction.RunnerWorldInteractionHandler;

public class BundleProviderImpl implements BundleProvider {

    @Override
    public @NotNull HandlerBundle getHunterBundle(@NotNull Hunter hunter) {
        return new HandlerBundle(
                new SidebarHandler(hunter),
                new BaseHunterMovementHandler(hunter),
                new HunterCombatHandler(hunter),
                new BaseHunterInventoryHandler(hunter),
                new HunterLifecycleHandler(hunter),
                new HunterInteractionHandler(hunter),
                new HunterVehicleHandler(hunter),
                new HunterWorldInteractionHandler(hunter)
                );
    }

    @Override
    public @NotNull HandlerBundle getRunnerBundle(@NotNull Runner runner) {
        return new HandlerBundle(
                new SidebarHandler(runner),
                new BaseRunnerMovementHandler(runner),
                new RunnerCombatHandler(runner),
                new BaseRunnerInventoryHandler(runner),
                new RunnerLifecycleHandler(runner),
                new RunnerInteractionHandler(runner),
                new RunnerVehicleHandler(runner),
                new RunnerWorldInteractionHandler(runner)
        );
    }

    @Override
    public @NotNull HandlerBundle getSpectatorBundle(@NotNull Spectator spectator) {
        return new HandlerBundle(
                new SidebarHandler(spectator),
                new BaseSpectatorMovementHandler(spectator),
                new BaseSpectatorCombatHandler(spectator),
                new BaseSpectatorInventoryHandler(spectator),
                new BaseSpectatorLifecycleHandler(spectator),
                new BaseSpectatorInteractionHandler(spectator),
                new BaseSpectatorVehicleHandler(spectator),
                new BaseSpectatorWorldInteractionHandler(spectator)
        );
    }
}
