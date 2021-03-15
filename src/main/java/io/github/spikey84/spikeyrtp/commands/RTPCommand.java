package io.github.spikey84.spikeyrtp.commands;

import io.github.spikey84.spikeyrtp.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RTPCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender.hasPermission("spikeyrtp.rtp")) {
            Main.rtp((Player) sender);
        } else {
            sender.sendMessage(Main.prefix + "Sorry, you do not have access to this command.");
        }
        return true;
    }
}
