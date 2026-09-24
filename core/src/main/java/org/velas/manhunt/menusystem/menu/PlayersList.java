package org.velas.manhunt.menusystem.menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.Color;
import org.velas.manhunt.config.Settings;
import org.velas.manhunt.game.scoreboard.ScoreManager;
import org.velas.manhunt.menusystem.PlayerMenuUtility;
import org.velas.scoreboard.api.Scoreboard;
import org.velas.scoreboard.api.team.Team;

import java.util.ArrayList;


public class PlayersList extends PaginatedMenu {

    private final ArrayList<Player> playersAdd = new ArrayList<>();
    private final ArrayList<String> playersRemove = new ArrayList<>();

    public PlayersList(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "Players";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {

        if(event.isShiftClick()) {
            event.setCancelled(true);
            return;
        }

        ItemStack item = event.getCurrentItem();

        if(item == null || item.getType().equals(Material.AIR)) return;
        if(item.getType().equals(Material.GRAY_STAINED_GLASS_PANE)) return;

        if (item.getType().equals(Material.BARRIER)){
            new TeamOptions(playerMenuUtility).open();
            return;
        }

        if(item.getType().equals(Material.DARK_OAK_BUTTON)){
            String option = item.getItemMeta().getDisplayName().substring(2);
            if(option.equals("Left")){
                if(page==0) return;
                page=page-1;
                super.open();
            }else if(option.equals("Right")){
                switch (playerMenuUtility.getSelectionTeamOption()){
                    case "add" -> {
                        if(!((index+1) >= playersAdd.size())){
                            page=page+1;
                            super.open();
                        }
                    }
                    case "remove" -> {
                        if(!((index+1) >= playersRemove.size())){
                            page=page+1;
                            super.open();
                        }
                    }
                }

            }
            event.setCancelled(true);
            return;
        }

        String targetName = item.getItemMeta().getDisplayName();

        String teamName = playerMenuUtility.getSelectedTeam();

        Scoreboard scoreboard = ScoreManager.getScoreboard();
        Team team = scoreboard.getTeam(teamName);

        if (team == null) return;

        inventory.remove(item);

        switch (playerMenuUtility.getSelectionTeamOption()){

            case "add" -> {

                Player targetPlayer = Bukkit.getPlayer(targetName);

                if (targetPlayer == null){
                    playerMenuUtility.getOwner().sendMessage(Color.RED + "That player either doesn't exist or isn't online.");
                    return;
                }

                if (team.hasPlayer(targetPlayer.getUniqueId())){
                    playerMenuUtility.getOwner().sendMessage(Color.RED + "This players is already in " + teamName + " team");
                    return;
                }

                team.addPlayer(targetPlayer.getUniqueId());
                playerMenuUtility.getOwner().sendMessage(Color.GREEN + "Successfully added " + targetName + " to " + teamName);
            }
            case "remove" -> {

                Player targetPlayer = Bukkit.getPlayer(targetName);

                if (targetPlayer == null){
                    playerMenuUtility.getOwner().sendMessage(Color.RED + "That player either doesn't exist or isn't online.");
                    return;
                }

                if (!team.hasPlayer(targetPlayer.getUniqueId())){
                    playerMenuUtility.getOwner().sendMessage(Color.RED + targetName + " was not found in " + teamName);
                    return;
                }

                team.removePlayer(targetPlayer.getUniqueId());

                playerMenuUtility.getOwner().sendMessage(Color.GREEN + "Successfully removed " + targetName + " from " + teamName);
            }
        }
    }

    @Override
    public void setMenuItems() {

        super.addMenuBorder();
        Scoreboard scoreboard = ScoreManager.getScoreboard();
        switch (playerMenuUtility.getSelectionTeamOption()){

            case "add" -> {

                playersAdd.clear();
                playersAdd.addAll(Bukkit.getOnlinePlayers());

                if(playersAdd.isEmpty()) return;

                for(int i =0; i<super.maxItemsPerPage; i++){
                    index = super.maxItemsPerPage*page+i;
                    if(index >= playersAdd.size()) break;
                    Player player = playersAdd.get(index);

                    Team team = scoreboard.getTeamOf(player.getUniqueId());

                    if (team == null){

                        ItemStack playerHead = new ItemStack(Material.WHITE_WOOL);

                        ItemMeta meta = playerHead.getItemMeta();
                        meta.setDisplayName(player.getName());
                        playerHead.setItemMeta(meta);

                        inventory.addItem(playerHead);

                    }else if (!team.getName().equals(playerMenuUtility.getSelectedTeam())){
                        ItemStack playerHead = getItemStack(team, player);
                        inventory.addItem(playerHead);
                    }
                }
            }

            case "remove" -> {

                ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
                String teamName = playerMenuUtility.getSelectedTeam();
                Team team = scoreboard.getTeam(teamName);
                if (team == null) return;

                playersRemove.clear();
                playersRemove.addAll(
                        team.getPlayers().stream()
                                .map(Bukkit::getOfflinePlayer)
                                .filter(OfflinePlayer::hasPlayedBefore)
                                .map(OfflinePlayer::getName)
                                .toList()
                );

                if(playersRemove.isEmpty()) return;

                for (int i = 0; i < super.maxItemsPerPage; i++){
                    index = super.maxItemsPerPage*page + i;
                    if(index>= playersRemove.size()) break;

                    String playerName = playersRemove.get(index);

                    if(playerName==null) continue;

                    ItemMeta meta = playerHead.getItemMeta();
                    meta.setDisplayName(playerName);
                    playerHead.setItemMeta(meta);
                    inventory.addItem(playerHead);
                }
            }
        }
    }

    private static @NotNull ItemStack getItemStack(Team team, Player player) {
        ItemStack playerHead = switch (team.getName()){
            case Settings.HUNTER_TEAM_NAME -> new ItemStack(Material.RED_WOOL);
            case Settings.RUNNER_TEAM_NAME -> new ItemStack(Material.LIME_WOOL);
            default -> new ItemStack(Material.LIGHT_GRAY_WOOL);
        };

        ItemMeta meta = playerHead.getItemMeta();
        meta.setDisplayName(player.getName());
        playerHead.setItemMeta(meta);
        return playerHead;
    }
}
