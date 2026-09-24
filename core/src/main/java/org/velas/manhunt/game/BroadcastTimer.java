package org.velas.manhunt.game;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.velas.manhunt.Color;

public class BroadcastTimer{
    private BroadcastTimer() {
        /* This utility class should not be instantiated */
    }

    //"⓯", "⓮", "⓭", "⓬", "⓫",

    private static final String [] tenSeconds = {
            "❿", "❾", "❽", "❼", "❻", "❺", "❹", "❸", "❷", "❶"
    };

    public static void broadcast(int index){
        for (Player player: Bukkit.getOnlinePlayers()){
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f);
            player.sendTitle(Color.AQUA + tenSeconds[index], "", 0, 20, 0);
        }
    }
}
