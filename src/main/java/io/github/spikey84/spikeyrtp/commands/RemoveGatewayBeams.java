package io.github.spikey84.spikeyrtp.commands;

import io.github.spikey84.spikeyrtp.Main;
import org.bukkit.block.EndGateway;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemoveGatewayBeams implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //end gateways have a annoying beam effect that makes using them in building impossible this will prevent them from producing that beam.
        if(((Player) sender).getTargetBlockExact(100).getState() instanceof EndGateway) {
            EndGateway gate = (EndGateway) ((Player) sender).getTargetBlockExact(100).getState();//gets the block state data
            gate.setAge(-9223372036854775808L);//sets the tick age to the minimum value; will not last forever but will last longer than only server has been online
            gate.update();//updates the block with the new age data
            sender.sendMessage(Main.prefix+ "Beam removed.");
        } else {
            sender.sendMessage(Main.prefix+ "You must be looking at a end gateway block.");
        }
        return true;
    }
}
