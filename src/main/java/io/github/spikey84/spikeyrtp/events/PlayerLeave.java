package io.github.spikey84.spikeyrtp.events;

import io.github.spikey84.spikeyrtp.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeave implements Listener {
    @EventHandler
    public void playerLeave(PlayerQuitEvent event) {
        //exports temp data to the database
        Main.db.setTokens(event.getPlayer(), Main.users.get(event.getPlayer()));
        Main.users.remove(event.getPlayer());
    }
}
