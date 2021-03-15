package io.github.spikey84.spikeyrtp.events;

import io.github.spikey84.spikeyrtp.Main;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {

    @EventHandler
    public void playerMove(PlayerMoveEvent event) {
        for(Location loc : Main.portalBlocks) {
            if(event.getPlayer().getLocation().getBlockX() == loc.getBlockX() && event.getPlayer().getLocation().getBlockY() == loc.getBlockY() && event.getPlayer().getLocation().getBlockZ() == loc.getBlockZ()) {
                boolean tele = Main.rtp(event.getPlayer());
                Location tempLoc = Main.returnLoc;
                tempLoc.setPitch(event.getPlayer().getLocation().getPitch());
                tempLoc.setYaw(event.getPlayer().getLocation().getYaw());
                tempLoc.setDirection(event.getPlayer().getLocation().getDirection());
                tempLoc.setX(tempLoc.getBlockX()+0.5);
                tempLoc.setZ(tempLoc.getBlockZ()+0.5);
                if(!tele) event.getPlayer().teleport(Main.returnLoc);
                return;
            }
        }

    }
}
