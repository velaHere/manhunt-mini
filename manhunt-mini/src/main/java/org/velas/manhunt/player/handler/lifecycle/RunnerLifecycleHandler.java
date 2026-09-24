package org.velas.manhunt.player.handler.lifecycle;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.player.Runner;

public class RunnerLifecycleHandler extends BaseRunnerLifecycleHandler {

    private boolean kitGiven = false;

    public RunnerLifecycleHandler(@NotNull Runner runner) {
        super(runner);
    }

    @Override
    protected void matchStartEvent() {
        Player player = runner.getPlayer();
        if(player==null) return;
        this.giveKit(player);
        this.kitGiven=true;
    }

    @Override
    protected void matchStopEvent() {
    }

    @Override
    protected void playerJoinServerEvent() {

        if(this.kitGiven) return;

        int currentTime = this.runner.getManhuntGame().getGameManager().getGameTimeLine().getTime();
        if(currentTime > 60) {
            Player player = this.runner.getPlayer();
            if(player==null) return;
            this.giveKit(player);
            this.kitGiven=true;
        }
    }

    @Override
    protected void playerQuitServerEvent() {

    }

    @Override
    protected void playerRespawnEvent() {

    }

    private void giveKit(@NotNull Player player) {
        PlayerInventory inv = player.getInventory();

        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        ItemStack leggings = new ItemStack(Material.IRON_LEGGINGS);
        ItemStack boots = new ItemStack(Material.IRON_BOOTS);

        helmet.setItemMeta(colorArmor(helmet));
        chestplate.setItemMeta(colorArmor(chestplate));

        inv.setHelmet(helmet);
        inv.setChestplate(chestplate);
        inv.setLeggings(leggings);
        inv.setBoots(boots);

        inv.addItem(
                new ItemStack(Material.STONE_SWORD),
                new ItemStack(Material.WOODEN_AXE),
                new ItemStack(Material.IRON_PICKAXE),
                new ItemStack(Material.BREAD, 16),
                new ItemStack(Material.WATER_BUCKET),
                new ItemStack(Material.COBBLESTONE, 32)
        );
    }

    private ItemMeta colorArmor(ItemStack item) {
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        assert meta!=null;
        meta.setColor(org.bukkit.Color.GREEN); // nice green shade
        return meta;
    }
}
