package io.github.spikey84.spikeyrtp.events;

import io.github.spikey84.spikeyrtp.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        //imports data from the database into the temporary map
        Main.users.put(event.getPlayer(), Main.db.getTokens(event.getPlayer().getName()));
    }
}
