package org.velas.manhunt.dropSystem.landingSystem;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Mob;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.velas.manhunt.Manhunt;
import org.velas.manhunt.Utils;
import org.velas.manhunt.dropSystem.drops.AirDrop;
import org.velas.manhunt.game.ManhuntGame;

import java.util.List;


public class LandingRunnable extends BukkitRunnable {

    private final AirDrop drop;

    public LandingRunnable(AirDrop drop){
        this.drop = drop;
    }

    @Override
    public void run() {

        if(drop.hasLanded()){
            this.cancel();
            return;
        }

        Minecart chestCart = drop.getChestCart();
        List<Mob> carriers = drop.getParachute().getCarriers();
        ArmorStand stand = drop.getParachute().getStand();

        if(chestCart.isDead()){
            if(drop.getParachute() != null) drop.getParachute().remove();
            ManhuntGame.getCurrentGame().getGameManager().getDropManager().removeDrop(drop.getUniqueId());
            this.drop.getDropChunk().setForceLoaded(false);

            this.cancel();
            return;
        }


        World world = chestCart.getWorld();

        EntityKillerAndLandingDetector.run(drop, chestCart);

        if (this.drop.hasLanded()){

            drop.setLandedAt(System.currentTimeMillis());

            new BukkitRunnable(){

                int timer = 0;

                @Override
                public void run() {

                    if (timer >= 1200 || chestCart.isDead()) { // stop after 1 min or if cart removed
                        this.cancel();
                        return;
                    }

                    Location smokeLoc = chestCart.getLocation();

                    DropEffects.spawnSmoke(smokeLoc);
                    timer += 5;
                }
            }.runTaskTimer(Manhunt.getINSTANCE(), 0L, 5L);

            drop.removeParachute();

            DropEffects.spawnLandingParticles(world, chestCart);

            Bukkit.getScheduler().runTaskLater(Manhunt.getINSTANCE(), () -> drop.getDropChunk().setForceLoaded(false),7L);

            if(chestCart.isDead() || !chestCart.isValid()){
                Utils.warning("Something wrong after landed");
            }

            this.cancel();
            return;
        }

        chestCart.setVelocity(new Vector(0, -0.005, 0));
        stand.teleport(chestCart.getLocation().clone().add(0,3,0));

        for (Mob mob: carriers){

            double x = mob.getLocation().getX();
            double z = mob.getLocation().getZ();
            Location mobLoc = mob.getLocation().clone();
            mobLoc.setX(x);
            mobLoc.setY(stand.getLocation().getY()+1);
            mobLoc.setZ(z);

            mob.teleport(mobLoc);
        }
    }

}
