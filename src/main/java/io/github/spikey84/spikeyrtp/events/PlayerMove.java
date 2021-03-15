package io.github.spikey84.spikeyrtp.events;

import io.github.spikey84.spikeyrtp.Main;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {

    @EventHandler
    public void playerMove(PlayerMoveEvent event) {
        //loops portal blocks
        for(Location loc : Main.portalBlocks) {
            //checks if the player is inside the portal block
            if(event.getPlayer().getLocation().getBlockX() == loc.getBlockX() && event.getPlayer().getLocation().getBlockY() == loc.getBlockY() && event.getPlayer().getLocation().getBlockZ() == loc.getBlockZ()) {
                //runs a teleport and returns whether the teleport completed
                boolean tele = Main.rtp(event.getPlayer());

                //if the teleport fails find the return location and moves the player there
                if(!tele) {
                    Location tempLoc = Main.returnLoc;
                    tempLoc.setPitch(event.getPlayer().getLocation().getPitch());
                    tempLoc.setYaw(event.getPlayer().getLocation().getYaw());
                    tempLoc.setDirection(event.getPlayer().getLocation().getDirection());
                    tempLoc.setX(tempLoc.getBlockX() + 0.5);
                    tempLoc.setZ(tempLoc.getBlockZ() + 0.5);
                    event.getPlayer().teleport(Main.returnLoc);
                }
                return;
            }
        }

    }
}
