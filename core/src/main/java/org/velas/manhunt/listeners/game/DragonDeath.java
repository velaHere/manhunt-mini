package org.velas.manhunt.listeners.game;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.velas.manhunt.Color;
import org.velas.manhunt.Utils;
import org.velas.manhunt.game.ManhuntGame;

public class DragonDeath implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDragonDies(EntityDeathEvent event){

        if (!ManhuntGame.getCurrentGame().isRunning()) return;

        if (!(event.getEntity().getType() == EntityType.ENDER_DRAGON)) return;

        Utils.warning("EnderDragon got Slain away, Match Stopped");

        ManhuntGame.getCurrentGame().stop();
        Utils.sendMsgToAllOnlinePlayers(Color.GREEN + "Congratulations to the Runners, the EnderDragon has met its end");
        Utils.announceMatchStop("§eVictory over the End!");
    }
}