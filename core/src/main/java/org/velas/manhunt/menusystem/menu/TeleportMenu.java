package org.velas.manhunt.menusystem.menu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.velas.manhunt.Color;
import org.velas.manhunt.Manhunt;
import org.velas.manhunt.game.scoreboard.ScoreManager;
import org.velas.manhunt.menusystem.Menu;
import org.velas.manhunt.menusystem.PlayerMenuUtility;
import org.velas.scoreboard.api.team.Team;

import java.util.UUID;

public class TeleportMenu extends Menu {

    private final NamespacedKey targetKey = new NamespacedKey(Manhunt.getINSTANCE(), "teleport_target");

    public TeleportMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return Color.GREEN + "Teleport to:";
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

        if (!(event.getWhoClicked() instanceof Player player)) return;

        ItemStack currentItem = event.getCurrentItem();
        if (currentItem == null || currentItem.getType() == Material.AIR) return;

        ItemMeta meta = currentItem.getItemMeta();
        if (meta == null) return;

        String uuidStr = meta.getPersistentDataContainer().get(targetKey, PersistentDataType.STRING);
        if (uuidStr == null) {
            player.sendMessage(Color.RED + "Invalid target.");
            return;
        }

        UUID uuid;
        try {
            uuid = UUID.fromString(uuidStr);
        } catch (IllegalArgumentException ex) {
            player.sendMessage(Color.RED + "Invalid target UUID.");
            return;
        }

        Player target = Bukkit.getPlayer(uuid);
        if (target == null || !target.isOnline()) {
            player.sendMessage(Color.YELLOW + "This player is currently offline, unable to teleport.");
            return;
        }

        if (target.isDead()) {
            player.sendMessage(Color.YELLOW + "This player is currently dead, unable to teleport.");
            return;
        }

        // optional: prevent teleporting to spectators / different worlds / etc.

        player.closeInventory();
        player.teleport(target.getLocation());

        event.setCancelled(true);
    }

    @Override
    public void setMenuItems() {

        inventory.clear();

        Player owner = playerMenuUtility.getOwner();

        for (Player target : Bukkit.getOnlinePlayers()) {

            if (owner != null && owner.getUniqueId().equals(target.getUniqueId())) continue;

            // Create a NEW item per player (do not reuse one ItemStack)
            ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
            ItemMeta meta = head.getItemMeta();
            if (meta == null) continue;

            Team targetTeam = ScoreManager.getScoreboard().getTeamOf(target.getUniqueId());

            String displayName;
            if (targetTeam == null) {
                displayName = ChatColor.GREEN + target.getName();
            } else {
                displayName = ChatColor.BOLD + "" + targetTeam.getChatColor() + targetTeam.getName().charAt(0)
                        + ChatColor.RESET + targetTeam.getChatColor() + ": " + ChatColor.WHITE + target.getName();
            }

            meta.setDisplayName(displayName);

            // Store UUID safely (no substring hacks)
            meta.getPersistentDataContainer().set(targetKey, PersistentDataType.STRING, target.getUniqueId().toString());

            head.setItemMeta(meta);

            inventory.addItem(head);
        }

        // Optional: fill empty slots with glass panes, add a close button, etc.
    }
}