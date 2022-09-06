package com.random.captureTheFlag.commands;

import com.random.captureTheFlag.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetupCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + "Command must be run by a player!");
            return true;
        }

        Player player = (Player) commandSender;
        if (!player.isOp()) {
            player.sendMessage(ChatColor.RED + "[⚠] Whoops!  You do not have permission to run this command!");
            return true;
        }

        if (args.length != 3) {
            player.sendMessage(ChatColor.RED + "Incorrect usage!  Run /ctfhelp for help!");
            return true;
        }

        if (args[0].equals("set")) {
            if (args[1].equals("lobby")) {
                Main.getInstance().setLocation("lobby", player.getLocation());
                return true;
            }
            if (!Main.getInstance().getSettings().getMaps().contains(args[1])) {
                player.sendMessage(ChatColor.RED + "Map \"" + args[1] + "\" does not exist!  Please check your spelling and try again.");
                return true;
            }
            if (args[2].equals("pos1") || args[1].equals("pos2")) {
                Main.getInstance().setLocation(Main.getInstance().getSettings().getMapName() + "." + args[2], player.getLocation());
                return true;
            }
            Main.getInstance().getSettings().setMap(args[1]);
            Main.getInstance().setLocation(args[1] + "." + args[2], player.getLocation());
            player.sendMessage(ChatColor.GREEN + "Location " + args[2] + " successfully set for map " + args[1] + "!");
            return true;
        }

        if (args[0].equals("tp")) {
            if (!Main.getInstance().getSettings().getMaps().contains(args[1])) {
                player.sendMessage(ChatColor.RED + "Map \"" + args[1] + "\" does not exist!  Please check your spelling and try again.");
                return true;
            }
            player.teleport(Main.getInstance().getLocation((args[1] + "." + args[2])));
            player.sendMessage(ChatColor.GREEN + "Teleported to " + args[1] + "! (Will be 0, 0, 0 if location is not set or does not exist)");
            return true;
        }

        return false;
    }
}
