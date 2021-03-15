package io.github.spikey84.spikeyrtp.commands;

import io.github.spikey84.spikeyrtp.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.EndGateway;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class RemoveGatewayBeams implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(((Player) sender).getTargetBlockExact(100).getState() instanceof EndGateway) {
            EndGateway gate = (EndGateway) ((Player) sender).getTargetBlockExact(100).getState();
            gate.setAge(-9223372036854775808L);
            gate.update();
            sender.sendMessage(Main.prefix+ "Beam removed.");
        } else {
            sender.sendMessage(Main.prefix+ "You must be looking at a end gateway block.");
        }
        return true;
    }
}
