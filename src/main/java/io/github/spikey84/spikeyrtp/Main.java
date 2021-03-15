package io.github.spikey84.spikeyrtp;

import io.github.spikey84.spikeyrtp.commands.RTPCommand;
import io.github.spikey84.spikeyrtp.commands.RTPReset;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin {
    static int maxLoc;
    static FileConfiguration config;
    public static List<String> users;
    public static String prefix = ChatColor.WHITE + "["+ ChatColor.DARK_AQUA + "SpikeyRTP" + ChatColor.WHITE + "] ";

    @Override
    public void onEnable() {
        getCommand("rtp").setExecutor(new RTPCommand());
        getCommand("rtpreset").setExecutor(new RTPReset());

        this.saveDefaultConfig();
        config = this.getConfig();
        Bukkit.getLogger().info(config.getStringList("users")+ "");
        users = config.getStringList("users");
        maxLoc = config.getInt("maxdistance");


    }

    @Override
    public void onDisable() {
        config.set("users", users);
        config.set("maxdistance",maxLoc);
        saveConfig();
    }


    public static void rtp(Player player) {
        if(users.contains(player.getName()) && !player.hasPermission("spikeyrtp.bypasslimit")) {
            player.sendMessage(prefix + ChatColor.WHITE + "You have already used your one allowed RTP.");
            return;
        }
        int x;
        int z;
        Location loc = player.getLocation();
        boolean validLoc = false;
        int attempts = 0;
        //get init loc
        while(validLoc == false) {
            x = genLoc();
            z = genLoc();
            //go down and get sea lev
            loc = new Location(Bukkit.getWorld(player.getWorld().getName()), x, 255, z);
            while (loc.getBlock().getType() == Material.AIR) {
                loc.setY(loc.getBlockY() - 1);
            }
            //if its not valid find new
            if (loc.getBlock().getType() == Material.GRASS_BLOCK && loc.getWorld().getBlockAt(loc.getBlockX(),loc.getBlockY()+1,loc.getBlockZ()).getType() == Material.AIR && loc.getWorld().getBlockAt(loc.getBlockX(),loc.getBlockY()+2,loc.getBlockZ()).getType() == Material.AIR) validLoc = true;
            attempts++;
            if(attempts > 40) {
                player.sendMessage(prefix + "" + ChatColor.WHITE + "Unable to find a suitable location. Please re-run command or re-enter portal. Sorry.");
                return;
            }
        }
        //teleport player
        loc.setY(loc.getBlockY()+1);
        loc.setX(loc.getX() + 0.5);
        loc.setZ(loc.getZ() + 0.5);
        player.teleport(loc);
        if(!player.hasPermission("spikeyrtp.bypasslimit")) {
            users.add(player.getName());
        }
    }

    private static int genLoc() {
        return (int) Math.round(((Math.random()*10000)%maxLoc)-(maxLoc/2));
    }
}
