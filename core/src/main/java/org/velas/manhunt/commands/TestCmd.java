package org.velas.manhunt.commands;

import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.velas.manhunt.customItems.CustomItemType;

public class TestCmd extends CustomCommand {

    public TestCmd(String name, String alias, String permissionName) {
        super(name, alias, permissionName);
    }

    @Override
    protected boolean runPlayerCommand(Player player, String[] args) {
        if(!player.hasPermission("manhunt.setup")) return false;
        player.getInventory().addItem(CustomItemType.create(CustomItemType.FLARE_GUN));
        player.getInventory().addItem(CustomItemType.create(CustomItemType.KEY_TO_CONVERGENCE));
        player.getInventory().addItem(CustomItemType.create(CustomItemType.GOLA));
        return true;
    }

    @Override
    public void sendUsage(Player player) {
        player.sendMessage(Color.RED + "Please use the test command correctly");
    }
}
