package org.velas.manhunt.menusystem.menu;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.velas.manhunt.avatar.AvatarType;
import org.velas.manhunt.game.GameState;
import org.velas.manhunt.game.ManhuntGame;
import org.velas.manhunt.menusystem.Menu;
import org.velas.manhunt.menusystem.PlayerMenuUtility;
import org.velas.manhunt.player.ManhuntPlayer;

import java.util.ArrayList;
import java.util.List;

public class ClassSelection extends Menu {

    public ClassSelection(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "Select Class:";
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
        if(!ManhuntGame.getCurrentGame().getGameState().equals(GameState.STARTING)) return;

        ItemStack item = event.getCurrentItem();

        if (item == null || item.getType().equals(Material.AIR)) return;
        if(item.getItemMeta()==null) return;

        if(!(event.getWhoClicked() instanceof Player player)) return;

        ManhuntPlayer mp = ManhuntGame.getCurrentGame().getGameManager().getPlayerManager().getManhuntPlayer(player);
        if(mp==null) return;

        String name = item.getItemMeta().getDisplayName();

        switch (name) {
            case "Assassin" -> {
                mp.setAvatar(AvatarType.ASSASSIN);
                player.sendMessage(createMessage(name));
            }
            case "Swordsman" -> {
                mp.setAvatar(AvatarType.SWORDSMAN);
                player.sendMessage(createMessage(name));
            }
            default -> mp.setAvatar(AvatarType.DEFAULT);
        }

        event.setCancelled(true);
        player.closeInventory();
    }

    private String createMessage(String name) {
        return ChatColor.GREEN + "✔ " + ChatColor.WHITE + name + ChatColor.GRAY + " selected";
    }

    @Override
    public void setMenuItems() {
        inventory.setItem(12, assassin());
        inventory.setItem(14, swordsman());
    }


    public static ItemStack assassin() {
        // Black/Purple "stealth dagger"
        List<Pattern> patterns = List.of(
                p(DyeColor.PURPLE, PatternType.STRIPE_DOWNLEFT),
                p(DyeColor.PURPLE, PatternType.STRIPE_DOWNRIGHT),
                p(DyeColor.BLACK, PatternType.CIRCLE_MIDDLE),
                p(DyeColor.BLACK, PatternType.BORDER)
        );
        return banner(
                DyeColor.BLACK,
                "Assassin",
                List.of("Burst + reposition", "Stealth / Dash"),
                patterns
        );
    }

    public static ItemStack swordsman() {
        // White/Red "blade"
        List<Pattern> patterns = List.of(
                p(DyeColor.RED, PatternType.STRIPE_MIDDLE),
                p(DyeColor.LIGHT_GRAY, PatternType.GRADIENT_UP),
                p(DyeColor.BLACK, PatternType.STRIPE_BOTTOM),
                p(DyeColor.BLACK, PatternType.BORDER)
        );
        return banner(
                DyeColor.WHITE,
                "Swordsman",
                List.of("Melee expert", "Riposte / Lunge"),
                patterns
        );
    }

    private static Pattern p(DyeColor color, PatternType type) {
        return new Pattern(color, type);
    }

    private static ItemStack banner(DyeColor base, String displayName, List<String> lore, List<Pattern> patterns) {
        ItemStack item = new ItemStack(Material.WHITE_BANNER);
        BannerMeta meta = (BannerMeta) item.getItemMeta();
        if (meta == null) return item;

        meta.setBaseColor(base);

        meta.setDisplayName(ChatColor.RESET + displayName);

        if (lore != null && !lore.isEmpty()) {
            List<String> colored = new ArrayList<>();
            for (String line : lore) colored.add(ChatColor.GRAY + line);
            meta.setLore(colored);
        }

        if (patterns != null) {
            for (Pattern p : patterns) meta.addPattern(p);
        }

        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        return item;
    }

}
