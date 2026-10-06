package org.velas.manhunt.commands.subcommands;

import org.bukkit.entity.Player;
import org.velas.manhunt.Color;
import org.velas.manhunt.Manhunt;
import org.velas.manhunt.commands.CustomSubCommand;
import org.velas.manhunt.customItems.CustomRecipe;
import org.velas.manhunt.menusystem.menu.CustomItemList;

public class RecipeCommand extends CustomSubCommand {

    public RecipeCommand(String name, String alias, String permissionName) {
        super(name, alias, permissionName);
    }

    @Override
    protected boolean runPlayerCommand(Player player, String[] args) {

        if(args.length == 1) {
            new CustomItemList(Manhunt.getPlayerMenuUtility(player)).open();
            return true;
        }

        if(args.length != 2) return true;
        String id = args[1];
        CustomRecipe recipe = CustomRecipe.getRecipe(id);
        if(recipe == null) return true;
        recipe.new RecipeGUI(Manhunt.getPlayerMenuUtility(player)).open();
        return true;
    }

    @Override
    public void sendUsage(Player player) {
        player.sendMessage(Color.AQUA + "Usage: /manhunt recipe");
        player.sendMessage(Color.AQUA + "Usage: /manhunt recipe <item_name>");
    }
}
