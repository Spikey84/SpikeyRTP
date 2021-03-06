package io.github.spikey84.spikeyrtp.commands;

import io.github.spikey84.spikeyrtp.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RTPReset implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //command output messages speak for themselves
        if(args.length == 0) {
            sender.sendMessage(Main.prefix + "" + ChatColor.WHITE + "Please list at least one player whose data you wish to reset.");
            return true;
        }
        sender.sendMessage(Main.prefix + "" + ChatColor.WHITE + "Data reset started:");
        for(String name : args) {
            if(Main.users.get((Player) sender)>0) {
                Main.users.replace((Player) sender,0);
                sender.sendMessage(ChatColor.GRAY + "" + name + "'s rtp data has been reset.");
            } else {
                sender.sendMessage(ChatColor.GRAY + "" + name + " has no rtp data to delete.");
            }
        }
        sender.sendMessage("Data reset complete for all applicable targets.");
        return true;
    }
}
