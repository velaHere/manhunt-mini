package org.velas.manhunt.listeners.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.velas.manhunt.config.Settings;
import org.velas.manhunt.game.ManhuntGame;
import org.velas.manhunt.game.scoreboard.ScoreManager;
import org.velas.scoreboard.api.team.Team;

public class FoodLeveChange implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFoodLeveChange(FoodLevelChangeEvent event){

        if(!ManhuntGame.getCurrentGame().isRunning()) return;

        if(!(event.getEntity() instanceof Player player)) return;

        Team team = ScoreManager.getScoreboard()
                .getTeamOf(player.getUniqueId());

        if (team != null && team.getName().equals(Settings.SPECTATOR_TEAM_NAME)) {
            event.setCancelled(true);
            player.setFoodLevel(20);
            player.setSaturation(20f);
            player.setExhaustion(0f);
        }
    }
}
