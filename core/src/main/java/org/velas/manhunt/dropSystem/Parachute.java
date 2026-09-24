package org.velas.manhunt.dropSystem;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.velas.manhunt.Manhunt;

import java.util.ArrayList;
import java.util.List;

public final class Parachute {

    private static final NamespacedKey KEY = new NamespacedKey(Manhunt.getINSTANCE(), "parachute");

    private final ArmorStand stand;
    private final List<Mob> carriers;

    public Parachute(Location initialDropLocation, Minecart chestCart){

        World world  = initialDropLocation.getWorld();

        this.stand = world.spawn(initialDropLocation.clone().add(0,3,0), ArmorStand.class);
        this.stand.getPersistentDataContainer().set(KEY, PersistentDataType.BYTE, (byte) 1);
        this.stand.setInvulnerable(true);
        this.stand.setInvisible(true);
        this.stand.setMarker(true);
        this.stand.setGravity(false);

        ItemStack parachute = new ItemStack(Material.CARROT_ON_A_STICK);
        ItemMeta meta = parachute.getItemMeta();
        meta.setCustomModelData(12345);
        parachute.setItemMeta(meta);
        stand.getEquipment().setHelmet(parachute);

        carriers = new ArrayList<>();

        for (int i = -1; i<=1; i++){
            for (int j = -1; j<=1; j++){
                if (i==0 || j==0) continue;

                Location mobLoc;
                mobLoc = initialDropLocation.clone().add(i, 0, j);
                Chicken chicken = world.spawn(mobLoc, Chicken.class);
                chicken.getPersistentDataContainer().set(KEY, PersistentDataType.BYTE, (byte) 1);
                chicken.setLeashHolder(chestCart);
                chicken.setInvulnerable(true);
                chicken.setAI(false);
                chicken.setGravity(false);
                chicken.setInvisible(true);

                double x = chicken.getLocation().getX();
                double z = chicken.getLocation().getZ();
                Location mobLoc1 = chicken.getLocation().clone();
                mobLoc1.setX(x);
                mobLoc1.setY(stand.getLocation().getY()+1);
                mobLoc1.setZ(z);

                if(j<0) mobLoc1.setYaw(180f); //Towards -Z

                chicken.teleport(mobLoc1);

                carriers.add(chicken);
            }
        }
    }

    public ArmorStand getStand() {
        return stand;
    }

    public List<Mob> getCarriers() {
        return carriers;
    }

    public void remove(){

        this.stand.getEquipment().clear();
        this.stand.remove();
        for (Mob mob: carriers){
            mob.setLeashHolder(null);
            mob.setInvulnerable(false);
            mob.remove();
        }
    }

    public static boolean isEntityPartOFParachute(Entity entity) {
        return entity.getPersistentDataContainer().has(KEY, PersistentDataType.BYTE);
    }
}
