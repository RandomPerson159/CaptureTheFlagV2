package com.random.captureTheFlag.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + "Command must be run by a player!");
            return true;
        }
        Player player = (Player) commandSender;
        if (!player.isOp()) {
            player.sendMessage(ChatColor.GREEN + "Commands: \n" + ChatColor.GRAY + " - " + ChatColor.YELLOW + "/shout {message} " + ChatColor.GRAY + ": Allows you to talk to other teams\nMore commands coming soon!");
            return true;
        } else {
            player.sendMessage(ChatColor.GREEN + "Commands: \n"
                    + ChatColor.DARK_GRAY + " - " + ChatColor.YELLOW + "/shout {message} " + ChatColor.GRAY + ": Allows you to talk to other teams\n"
                    + ChatColor.DARK_GRAY + " - " + ChatColor.YELLOW + "/{start|cancel} " + ChatColor.GRAY + ": Starts and cancels games\n"
                    + ChatColor.DARK_GRAY + " - " + ChatColor.YELLOW + "/settings " + ChatColor.GRAY + ": Opens a game-settings menu\n"
                    + ChatColor.DARK_GRAY + " - " + ChatColor.YELLOW + "/teams " + ChatColor.GRAY + ": Lists players and their current team\n"
                    + ChatColor.DARK_GRAY + " - " + ChatColor.YELLOW + "/teams assign {players...} {team} " + ChatColor.GRAY + ": Assigns players to desired team\n"
                    + ChatColor.DARK_GRAY + " - " + ChatColor.YELLOW + "/teams unlist {players...}" + ChatColor.GRAY + ": Un-assigns players from any team\n"
                    + ChatColor.DARK_GRAY + " - " + ChatColor.YELLOW + "/setup list {locations/setlocations/unsetlocations} {1,2,3,etc...} " + ChatColor.GRAY + ": Lists locations, locations that are set, and locations that need to be set\n"
                    + ChatColor.DARK_GRAY + " - " + ChatColor.YELLOW + "/setup set {location name} " + ChatColor.GRAY + ": Sets a location\n"
                    + ChatColor.DARK_GRAY + " - " + ChatColor.YELLOW + "/setup tp {location name} " + ChatColor.GRAY + ": Teleports you to location (will be 0, 0, 0 if location is not set)"
                    + ChatColor.DARK_GRAY + " - " + ChatColor.YELLOW + "/enable " + ChatColor.GRAY + ": Enables / Un-enables Capture the Flag\n");

        }

        return false;
    }
}
