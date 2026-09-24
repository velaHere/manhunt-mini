package org.velas.manhunt.menusystem.menu;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.velas.manhunt.config.Settings;
import org.velas.manhunt.menusystem.Menu;
import org.velas.manhunt.menusystem.PlayerMenuUtility;


public class TeamMenu extends Menu {

    public TeamMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "Teams";
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {

        if(event.isShiftClick()) {
            event.setCancelled(true);
            return;
        }

        ItemStack item = event.getCurrentItem();

        if (item == null || item.getType().equals(Material.AIR)) return;

        String name = item.getItemMeta().getDisplayName().substring(2);

        playerMenuUtility.setSelectedTeam(name);

        new TeamOptions(playerMenuUtility).open();

        event.setCancelled(true);
    }

    @Override
    public void setMenuItems() {

        ItemStack hunters = new ItemStack(Material.RED_WOOL);
        ItemMeta huntersMeta = hunters.getItemMeta();
        huntersMeta.setDisplayName(ChatColor.RED+ Settings.HUNTER_TEAM_NAME);
        hunters.setItemMeta(huntersMeta);

        ItemStack runners = new ItemStack(Material.LIME_WOOL);
        ItemMeta runnersMeta = runners.getItemMeta();
        runnersMeta.setDisplayName(ChatColor.GREEN+ Settings.RUNNER_TEAM_NAME);
        runners.setItemMeta(runnersMeta);

        ItemStack spectators = new ItemStack(Material.LIGHT_GRAY_WOOL);
        ItemMeta spectatorsMeta = spectators.getItemMeta();
        spectatorsMeta.setDisplayName(ChatColor.GRAY+ Settings.SPECTATOR_TEAM_NAME);
        spectators.setItemMeta(spectatorsMeta);

        inventory.setItem(11, hunters);
        inventory.setItem(13, runners);
        inventory.setItem(15, spectators);

    }
}
