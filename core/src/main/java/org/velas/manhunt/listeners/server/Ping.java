package org.velas.manhunt.listeners.server;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class Ping implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPing(ServerListPingEvent event){
        event.setMotd("                       §5§kV§6§lManhunt§5§kV\n          §bSome §aRUNNERS§b. The rest? §cHUNTERS§b");
    }
}
