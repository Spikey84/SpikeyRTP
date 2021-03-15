package io.github.spikey84.spikeyrtp;

import io.github.spikey84.spikeyrtp.commands.RTPCommand;
import io.github.spikey84.spikeyrtp.commands.RTPReset;
import io.github.spikey84.spikeyrtp.commands.RemoveGatewayBeams;
import io.github.spikey84.spikeyrtp.commands.Stuck;
import io.github.spikey84.spikeyrtp.events.PlayerJoin;
import io.github.spikey84.spikeyrtp.events.PlayerLeave;
import io.github.spikey84.spikeyrtp.events.PlayerMove;
import io.github.spikey84.spikeyrtp.sql.Database;
import io.github.spikey84.spikeyrtp.sql.SQLite;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main extends JavaPlugin {
    public static Plugin plugin;
    public static Database db;

    static int maxLoc; //max distance in one direction of rtps
    static int limit; //limit of teleports
    public static Location returnLoc; //return location of a portal
    public static boolean stuck; //used to check if the /stuck command is enabled

    public static Map<Player,Integer> users = new HashMap<Player,Integer>();//temp storage of players and their total rtps
    public static Map<Player, Boolean> stuckStatus = new HashMap<Player, Boolean>();//true if /stuck is in code receiving mode; false if /stuck is in code accepting mode
    public static Map<Player, Integer> stuckCode = new HashMap<Player, Integer>();//code used to confirm /stuck
    public static ArrayList<Location> portalBlocks = new ArrayList<Location>();//container for the blocks inside a portal

    static FileConfiguration config;

    public static String prefix = ChatColor.WHITE + "[" + ChatColor.DARK_AQUA + "SpikeyRTP" + ChatColor.WHITE + "] ";

    @Override
    public void onEnable() {
        plugin = this;

        db = new SQLite(this);
        db.load();

        getCommand("rtp").setExecutor(new RTPCommand());
        getCommand("rtpreset").setExecutor(new RTPReset());
        getCommand("removebeams").setExecutor(new RemoveGatewayBeams());
        getCommand("stuck").setExecutor(new Stuck());

        Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerLeave(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerMove(), this);

        this.saveDefaultConfig();
        config = this.getConfig();
        //sets values from the config
        maxLoc = config.getInt("maxDistance");
        limit = config.getInt("maxTeleports");
        stuck = config.getBoolean("stuck");
        returnLoc = new Location(Bukkit.getWorld(config.getString("world")),config.getInt("returnX"),config.getInt("returnY"),config.getInt("returnZ"));

        //retrieves data from the database for all online players (in case the plugin is reset)
        for (Player player : Bukkit.getOnlinePlayers()) {
            Main.users.put(player, Main.db.getTokens(player.getName()));
        }

        //gets all the blocks that make up the portal
        if(config.getBoolean("portal")) {
            portalBlocks = blocksBetweenPoints(Bukkit.getWorld(config.getString("world")), new Location(Bukkit.getWorld(config.getString("world")), config.getInt("blockOneX"), config.getInt("blockOneY"), config.getInt("blockOneZ")), new Location(Bukkit.getWorld(config.getString("world")), config.getInt("blockTwoX"), config.getInt("blockTwoY"), config.getInt("blockTwoZ")));
        }

    }

    @Override
    public void onDisable() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            db.setTokens(player,Main.users.get(player.getPlayer()));
            users.remove(player.getPlayer());
        }
    }


    public static boolean rtp(Player player) {
        int numTeleports = users.get(player);

        //checks if the player is over or at their limit of teleports and that they do not have the bypass permission
        if(numTeleports >= limit && !player.hasPermission("spikeyrtp.bypasslimit")) {
            player.sendMessage(prefix + ChatColor.WHITE + "You have already used your " + limit + " allowed RTP(s).");
            return false;
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
                player.sendMessage(prefix + "" + ChatColor.WHITE + "Unable to find a suitable location. Please re-run the command or re-enter portal. Sorry.");
                return false;
            }
        }
        //teleport player
        loc.setY(loc.getBlockY() + 1);
        loc.setX(loc.getX() + 0.5);
        loc.setZ(loc.getZ() + 0.5);
        player.teleport(loc);
        player.sendMessage(prefix + "" + ChatColor.WHITE + "Teleported to " + ChatColor.DARK_AQUA + loc.getBlockX() + ChatColor.WHITE + ", " + ChatColor.DARK_AQUA + loc.getBlockY() + ChatColor.WHITE + ", " + ChatColor.DARK_AQUA + loc.getBlockZ() + ChatColor.WHITE + ".");
        //adds 1 to the total teleports to a player unless they have the bypass perm
        if(!player.hasPermission("spikeyrtp.bypasslimit")) {
            users.replace(player, numTeleports+1);
        }
        return true;
    }

    //generates a random number between the max distance and the negative max distance
    private static int genLoc() {
        return (int) Math.round(((Math.random() * 10000) % (maxLoc * 2)) - (maxLoc));
    }

    //returns a list of all blocks inside a box made by two locations given in a specified world
    public static ArrayList<Location> blocksBetweenPoints(World world, Location loc1, Location loc2){
        ArrayList<Location> locations = new ArrayList<Location>();

        int lowX;
        int lowY;
        int lowZ;
        int highX;
        int highY;
        int highZ;
        if(loc1.getBlockX() < loc2.getBlockX()) {
            lowX = loc1.getBlockX();
            highX = loc2.getBlockX();
        } else {
            highX = loc1.getBlockX();
            lowX = loc2.getBlockX();
        }

        if(loc1.getBlockY() < loc2.getBlockY()) {
            lowY = loc1.getBlockY();
            highY = loc2.getBlockY();
        } else {
            highY = loc1.getBlockY();
            lowY = loc2.getBlockY();
        }

        if(loc1.getBlockZ() < loc2.getBlockZ()) {
            lowZ = loc1.getBlockZ();
            highZ = loc2.getBlockZ();
        } else {
            highZ = loc1.getBlockZ();
            lowZ = loc2.getBlockZ();
        }
        Bukkit.getLogger().info("low:"+lowY + " high:" + highY);
        for(int x = lowX; x < highX; x++) {
            for(int y = lowY; y<highY; y++) {
                for(int z = lowZ; z<highZ; z++) {
                    locations.add(new Location(world,x,y,z));
                }
            }
        }
        return locations;
    }
}
