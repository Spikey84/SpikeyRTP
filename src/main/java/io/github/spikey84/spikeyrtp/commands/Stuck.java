package io.github.spikey84.spikeyrtp.commands;

import io.github.spikey84.spikeyrtp.Main;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Stuck implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        //checks if the command is enabled. Can we toggled in config.yml
        if(!Main.stuck) {
            sender.sendMessage(Main.prefix + "This command is disabled.");
            return true;
        }

        //these statements make default entries in case a player does not have one
        if(!Main.stuckStatus.containsKey(player)) {
            Main.stuckStatus.put(player,false);
        }
        if(!Main.stuckCode.containsKey(player)) {
            Main.stuckCode.put(player,0);
        }

        //sets a code for a player and displays it to them
        if(!Main.stuckStatus.get(player)) {
            int rand = (int) Math.round((Math.random() * 1000) % 200);
            Main.stuckCode.put(player,rand);
            sender.sendMessage(Main.prefix + "This command will kill you! If you would really like run this command type " + ChatColor.DARK_AQUA + "/stuck " + rand);
            Main.stuckStatus.put(player,true);
            return true;
        }

        //if the player has been given a code this will run
        if(Main.stuckStatus.get(player)) {
            //if there are more/less than one argument this will return
            if(args.length != 1) {
                sender.sendMessage(Main.prefix + "Please use /stuck to generate a confirmation code.");
                Main.stuckStatus.put(player,false);
                return true;
            }
            //checks if the code is wrong then if it is correct
            if(Integer.parseInt(args[0]) != Main.stuckCode.get(player)) {
                sender.sendMessage(Main.prefix + "Incorrect confirmation code.");
                Main.stuckStatus.put(player,false);
            } else if(Integer.parseInt(args[0]) == Main.stuckCode.get(player)) {
                Location tmpLoc = player.getLocation();
                player.setHealth(0); //kills player
                sender.sendMessage(Main.prefix + "You have been killed at " + ChatColor.DARK_AQUA + tmpLoc.getBlockX() + ChatColor.WHITE + ", " + ChatColor.DARK_AQUA + tmpLoc.getBlockY() + ChatColor.WHITE + ", " + ChatColor.DARK_AQUA + tmpLoc.getBlockZ() + ChatColor.WHITE + ".");
                Main.stuckStatus.put(player,false);
            }
        }
        return true;
    }
}