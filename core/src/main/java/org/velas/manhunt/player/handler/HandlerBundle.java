package org.velas.manhunt.player.handler;

public record HandlerBundle(SidebarHandler sidebarHandler,
                            MovementHandler movementHandler, CombatHandler combatHandler, InventoryHandler inventoryHandler,
                            LifecycleHandler lifecycleHandler, PlayerInteractionHandler playerInteractionHandler,
                            VehicleHandler vehicleHandler, WorldInteractionHandler worldInteractionHandler) {

}
