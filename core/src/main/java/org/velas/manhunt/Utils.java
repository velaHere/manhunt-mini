package org.velas.manhunt;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.velas.manhunt.config.Settings;

import java.util.HashMap;

public class Utils {

    public static final HashMap<String, Boolean> playerChatInfo = new HashMap<>();

    public static void warning(String message){
        if (Settings.DEBUG){
            Manhunt.getINSTANCE().getLogger().warning(message);
        }
    }

    public static void severe(String message){
        if (Settings.DEBUG){
            Manhunt.getINSTANCE().getLogger().severe(message);
        }
    }

    public static void sendMsgToAllOnlinePlayers(String msg){
        for (Player player: Bukkit.getOnlinePlayers()){
            player.sendMessage(msg);
        }
    }

    public static void announceMatchStart(){

        String title = "§6§lManhunt";
        String subtitle = "§eRun, hide, or hunt. The game is on!";

        for (Player player: Bukkit.getOnlinePlayers()){
            player.sendTitle(title, subtitle, 20, 2*20, 20);
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        }
    }

    public static void announceMatchStop(String reason){

        String title = "§6§lMatch Over";

        for (Player player: Bukkit.getOnlinePlayers()){
            player.sendTitle(title, reason, 20, 2*20, 20);
        }
    }
}
