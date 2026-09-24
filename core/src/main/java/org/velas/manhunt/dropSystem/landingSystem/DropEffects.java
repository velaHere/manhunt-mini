package org.velas.manhunt.dropSystem.landingSystem;

import org.bukkit.*;
import org.bukkit.entity.Minecart;


public final class DropEffects {

    public static void spawnSmoke(Location smokeLoc){

        for(int i = 1; i<=40; i++){
            Location ploc = smokeLoc.clone().add(0,i,0);
            smokeLoc.getWorld().spawnParticle(
                    Particle.REDSTONE,
                    ploc,
                    1, // count
                    0.2, 1, 0.2, // spread (X,Y,Z)
                    new Particle.DustOptions(Color.RED, 2) // bright red smoke
            );
        }
    }

    public static void spawnLandingParticles(World world, Minecart chestCart){
        world.playSound(chestCart.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1f, 0.8f);
        world.spawnParticle(Particle.CLOUD, chestCart.getLocation(), 200, 1, 0.2, 1, 0.1);
        world.spawnParticle(Particle.BLOCK_CRACK, chestCart.getLocation(), 100, 1, 0.2, 1, 0.1, Material.SAND.createBlockData());

    }
}
