package org.velas.manhunt.dropSystem.drops;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.velas.manhunt.config.Settings;
import org.velas.manhunt.Manhunt;
import org.velas.manhunt.dropSystem.Parachute;
import org.velas.manhunt.dropSystem.landingSystem.LandingRunnable;
import org.velas.manhunt.dropSystem.loot.LootItem;
import org.velas.manhunt.dropSystem.lootProvider.LootProvider;
import org.velas.manhunt.game.ManhuntGame;

import java.util.UUID;


public abstract class AirDrop {

    public static final NamespacedKey KEY = new NamespacedKey(Manhunt.getINSTANCE(), "airdrop_cart");

    private UUID uuid;
    private final String name;
    private final Location initialDropLoc;
    private final World world;
    private Minecart chestCart;
    private Parachute parachute;
    private final Chunk dropChunk;
    private long lifeTime;
    private long landedAt;
    private boolean hasLanded;
    private final LootProvider lootProvider;
    private String playerName;

    protected AirDrop(String name, Location initialDropLoc, LootProvider lootProvider){

        this.name = name;
        this.world = initialDropLoc.getWorld();
        this.lifeTime = Settings.config().drop().getLifetime() * 1000L; //in milli_seconds
        this.lootProvider = lootProvider;
        this.playerName = "";
        this.landedAt=0;
        this.hasLanded = false;
        Location tempLoc = world.getHighestBlockAt(initialDropLoc).getLocation();

        if(initialDropLoc.getY()> tempLoc.getY()) this.initialDropLoc = new Location(initialDropLoc.getWorld(), tempLoc.getX()+0.5, initialDropLoc.getY() + 20, tempLoc.getZ()+0.5);
        else this.initialDropLoc = new Location(initialDropLoc.getWorld(), tempLoc.getX()+0.5, tempLoc.getY() + 50, tempLoc.getZ()+0.5);

        this.dropChunk = this.initialDropLoc.getChunk();
    }

    protected AirDrop(String name, Location initialDropLoc, int lifeTime, LootProvider lootProvider){
        this(name, initialDropLoc, lootProvider);
        this.lifeTime=lifeTime*1000L;
    }

    protected AirDrop(String name, Location initialDropLoc, LootProvider lootProvider, String playerName){
        this(name, initialDropLoc, lootProvider);
        this.playerName=playerName;
    }

    public void spawnDrop(){

        dropChunk.setForceLoaded(true);

        chestCart = (Minecart) world.spawnEntity(initialDropLoc, EntityType.MINECART_CHEST);
        chestCart.setCustomName("Supplies");
        chestCart.setCustomNameVisible(true);
        chestCart.setInvulnerable(true);
        chestCart.setPersistent(true);
        chestCart.getPersistentDataContainer().set(KEY, PersistentDataType.BYTE, (byte) 1);
        this.fillDrop();

        this.uuid = chestCart.getUniqueId();

        ManhuntGame.getCurrentGame().getGameManager().getDropManager().addDrop(uuid, this);

        //Parachute
        parachute = new Parachute(initialDropLoc, chestCart);

        new LandingRunnable(this).runTaskTimer(Manhunt.getINSTANCE(), 0L, 1L);
    }

    public void remove(){
        this.chestCart.remove();
        if(this.parachute != null) this.parachute.remove();
    }

    public World getWorld() {
        return world;
    }

    public String getName() {
        return name;
    }

    public String getPlayerName(){
        return this.playerName;
    }

    public Location getInitialDropLoc(){
        return this.initialDropLoc;
    }

    public Minecart getChestCart() {
        return chestCart;
    }

    public Parachute getParachute() {
        return parachute;
    }

    public void removeParachute() {
        if(this.parachute!=null){
            this.parachute.remove();
            this.parachute = null;
        }
    }

    public boolean isExpired(){
        if(!hasLanded()) return false;
        return (System.currentTimeMillis() - this.landedAt) >= lifeTime;
    }

    public void setLandedAt(long timeInMillis){
        this.landedAt = timeInMillis;
    }

//    public abstract List<LootItem> getLootItems();

    private void fillDrop(){

        StorageMinecart storageMinecart = (StorageMinecart) chestCart;

        for (LootItem item: lootProvider.generateLoot(this.initialDropLoc)){

            if(item.getItem() != null){
                storageMinecart.getInventory().addItem(item.getItem());
                continue;
            }

            if(item.getEnchantments() == null || item.getEnchantments().isEmpty()){
                storageMinecart.getInventory().addItem(new ItemStack(item.getMaterial(), item.getAmount()));
                continue;
            }
            ItemStack itemInBox = new ItemStack(item.getMaterial(), item.getAmount());
            itemInBox.addEnchantments(item.getEnchantments());
            storageMinecart.getInventory().addItem(itemInBox);
        }
    }

    public Chunk getDropChunk() {
        return dropChunk;
    }

    public UUID getUniqueId(){
        return this.uuid;
    }

    public boolean hasLanded(){
        return hasLanded;
    }

    public void setLanded(){
        this.hasLanded=true;
    }
}
