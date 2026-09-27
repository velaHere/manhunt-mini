package org.velas.manhunt.dropSystem.landingSystem;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.util.BoundingBox;
import org.velas.manhunt.dropSystem.Parachute;
import org.velas.manhunt.dropSystem.drops.AirDrop;
import org.velas.manhunt.game.ManhuntGame;
import org.velas.manhunt.player.Hunter;
import org.velas.manhunt.player.ManhuntPlayer;

public final class EntityKillerAndLandingDetector {

    // How far below the cart we consider "touching" (in blocks/meters).
    // Keep small: this is the thickness of the landing probe.
    private static final double PROBE_DEPTH = 0.20;

    // Slightly shrink X/Z to ensure "just below the cart" and not neighbors.
    private static final double PROBE_SHRINK_XZ = 0.05; // previous -> 0.05

    // Damage to apply to living entities under the cart (if you still want this behavior).
    private static final double KILL_DAMAGE = 20.0;

    public static void run(AirDrop drop, Minecart chestCart) {
        ManhuntGame game = ManhuntGame.getCurrentGame();
        if (!game.isRunning()) return;
        if (drop == null || chestCart == null || !chestCart.isValid()) return;

        World world = chestCart.getWorld();

        // 1) Build a thin bounding box directly BELOW the minecart
        BoundingBox cartBox = chestCart.getBoundingBox();

        double minX = cartBox.getMinX() + PROBE_SHRINK_XZ;
        double maxX = cartBox.getMaxX() - PROBE_SHRINK_XZ;
        double minZ = cartBox.getMinZ() + PROBE_SHRINK_XZ;
        double maxZ = cartBox.getMaxZ() - PROBE_SHRINK_XZ;

        // Bottom of cart, probe a thin slice below it
        double probeTopY = cartBox.getMinY();
        double probeBottomY = probeTopY - PROBE_DEPTH;

        BoundingBox belowProbe = new BoundingBox(minX, probeBottomY, minZ, maxX, probeTopY, maxZ);
        boolean entityLand = false;

        for (Entity e : world.getNearbyEntities(belowProbe)) {
            if (e.equals(chestCart)) continue;

            if (e instanceof EnderDragon || e instanceof EnderDragonPart) continue;

            if (!e.getBoundingBox().overlaps(belowProbe)) continue;

            if (e instanceof LivingEntity living) {
                if (living instanceof Player player) {
                    ManhuntPlayer mp = game.getGameManager().getPlayerManager().getManhuntPlayer(player);
                    if (mp != null && mp.isSpectator()) {
                        player.teleport(game.getGameManager().getWorldManager().getOverworld().getSpawnLocation());
                        continue;
                    } else if (mp != null && mp.isHunter()) {
                        Hunter hunter = (Hunter) mp;
                        if(!hunter.isAlive()) {
                            player.teleport(game.getGameManager().getWorldManager().getOverworld().getSpawnLocation());
                            continue;
                        }
                    }
                }
                else if(living instanceof Chicken chicken){
                    if(Parachute.isEntityPartOFParachute(chicken)) continue;
                } else if(living instanceof ArmorStand stand) {
                    if(Parachute.isEntityPartOFParachute(stand)) continue;
                }

                living.damage(KILL_DAMAGE);
            }
            entityLand=true;
        }

        // 3) Check BLOCK strictly below (sample a few points under the cart footprint)
        boolean blockBelow = isSolidBlockDirectlyBelow(chestCart, cartBox);

        // You need to store these counters in AirDrop (recommended).
        // If you can't edit AirDrop right now, set CONFIRM_GROUNDED_TICKS = 1 and remove counters.
        if (blockBelow || entityLand) {
            drop.setLanded();
        }
    }

    private static boolean isSolidBlockDirectlyBelow(Minecart cart, BoundingBox cartBox) {
        World world = cart.getWorld();

        // Probe Y just below the cart's bottom
        double y = cartBox.getMinY() - 0.01;

        // Sample 5 points: center + 4 corners (slightly inset)
        double inset = 0.05;

        double minX = cartBox.getMinX() + inset;
        double maxX = cartBox.getMaxX() - inset;
        double minZ = cartBox.getMinZ() + inset;
        double maxZ = cartBox.getMaxZ() - inset;

        double centerX = (minX + maxX) * 0.5;
        double centerZ = (minZ + maxZ) * 0.5;

        return isSolidAt(world, centerX, y, centerZ)
                || isSolidAt(world, minX, y, minZ)
                || isSolidAt(world, minX, y, maxZ)
                || isSolidAt(world, maxX, y, minZ)
                || isSolidAt(world, maxX, y, maxZ);
    }

    private static boolean isSolidAt(World world, double x, double y, double z) {
        Block b = world.getBlockAt((int) Math.floor(x), (int) Math.floor(y), (int) Math.floor(z));
        Material type = b.getType();

        // Treat anything non-air and solid as ground. You can customize special cases here.
        if (type == Material.AIR) return false;
        return type.isSolid();
    }
}