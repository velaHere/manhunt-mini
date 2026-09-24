package org.velas.manhunt.runnables;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.velas.manhunt.Manhunt;

public class BroadcastCredits extends BukkitRunnable {

    public static final String LOGO = """
                
                
                §c███╗   ███╗ █████╗ ███╗   ██╗██╗  ██╗██╗   ██╗███╗   ██╗████████╗
                §c████╗ ████║██╔══██╗████╗  ██║██║  ██║██║   ██║████╗  ██║╚══██╔══
                §c██╔████╔██║███████║██╔██╗ ██║███████║██║   ██║██╔██╗ ██║   ██║
                §c██║╚██╔╝██║██╔══██║██║╚██╗██║██╔══██║██║   ██║██║╚██╗██║   ██║
                §c██║ ╚═╝ ██║██║  ██║██║ ╚████║██║  ██║╚██████╔╝██║ ╚████║   ██║
                §c╚═╝     ╚═╝╚═╝  ╚═╝╚═╝  ╚═══╝╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═══╝   ╚═╝
                §7Version: %s
                §7Made with ❤  by §b§lDhiren06
                
                """.formatted(Manhunt.getINSTANCE().getDescription().getVersion());

    @Override
    public void run() {
        String string = "§7Made with ❤, by §b§lDhiren06";
        for (Player player: Bukkit.getOnlinePlayers()){
            player.sendMessage(string);
        }
    }

    public static void printConsoleBranding(){
        Bukkit.getConsoleSender().sendMessage(LOGO);
    }
}
