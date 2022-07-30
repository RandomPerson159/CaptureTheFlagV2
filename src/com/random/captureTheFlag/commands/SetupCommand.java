package com.random.captureTheFlag.commands;

import com.random.captureTheFlag.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

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

        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Incorrect usage!  Usage: \n/setup set <location>\n/setup list <locations|setlocations|unsetlocations>\n/setup tp <location>");
            return true;
        }

        List<String> locs = new ArrayList<>();
        locs.add("2teams.2flags.redSpawn");
        locs.add("2teams.2flags.blueSpawn");
        locs.add("2teams.2flags.wait");
        locs.add("2teams.2flags.redFlag1");
        locs.add("2teams.2flags.redFlag2");
        locs.add("2teams.2flags.blueFlag1");
        locs.add("2teams.2flags.blueFlag2");
        locs.add("2teams.redSpawn");
        locs.add("2teams.blueSpawn");
        locs.add("2teams.wait");
        locs.add("2teams.blueFlag");
        locs.add("2teams.redFlag");
        locs.add("4teams.2flags.redSpawn");
        locs.add("4teams.2flags.blueSpawn");
        locs.add("4teams.2flags.greenSpawn");
        locs.add("4teams.2flags.yellowSpawn");
        locs.add("4teams.2flags.wait");
        locs.add("4teams.2flags.redFlag1");
        locs.add("4teams.2flags.redFlag2");
        locs.add("4teams.2flags.blueFlag1");
        locs.add("4teams.2flags.blueFlag2");
        locs.add("4teams.2flags.greenFlag1");
        locs.add("4teams.2flags.greenFlag2");
        locs.add("4teams.2flags.yellowFlag1");
        locs.add("4teams.2flags.yellowFlag2");
        locs.add("4teams.redSpawn");
        locs.add("4teams.blueSpawn");
        locs.add("4teams.greenSpawn");
        locs.add("4teams.yellowSpawn");
        locs.add("4teams.wait");
        locs.add("4teams.redFlag");
        locs.add("4teams.blueFlag");
        locs.add("4teams.greenFlag");
        locs.add("4teams.yellowFlag");
        locs.add("lobby");

        switch (args[0].toLowerCase()) {
            case "set":
                switch (args[1]) {
                    case "2teams.2flags.redSpawn":
                    case "2teams.2flags.blueSpawn":
                    case "2teams.2flags.wait":
                    case "2teams.2flags.redFlag1":
                    case "2teams.2flags.redFlag2":
                    case "2teams.2flags.blueFlag1":
                    case "2teams.2flags.blueFlag2":
                    case "2teams.redSpawn":
                    case "2teams.blueSpawn":
                    case "2teams.wait":
                    case "2teams.blueFlag":
                    case "2teams.redFlag":
                    case "4teams.2flags.redSpawn":
                    case "4teams.2flags.blueSpawn":
                    case "4teams.2flags.greenSpawn":
                    case "4teams.2flags.yellowSpawn":
                    case "4teams.2flags.wait":
                    case "4teams.2flags.redFlag1":
                    case "4teams.2flags.redFlag2":
                    case "4teams.2flags.blueFlag1":
                    case "4teams.2flags.blueFlag2":
                    case "4teams.2flags.greenFlag1":
                    case "4teams.2flags.greenFlag2":
                    case "4teams.2flags.yellowFlag1":
                    case "4teams.2flags.yellowFlag2":
                    case "4teams.redSpawn":
                    case "4teams.blueSpawn":
                    case "4teams.greenSpawn":
                    case "4teams.yellowSpawn":
                    case "4teams.wait":
                    case "4teams.redFlag":
                    case "4teams.blueFlag":
                    case "4teams.greenFlag":
                    case "4teams.yellowFlag":
                    case "lobby":
                        Main.getInstance().setLocation(args[1], player.getLocation());
                        player.sendMessage(ChatColor.GREEN + "Location " + args[1] + " was successfully set!");
                        break;
                    default:
                        player.sendMessage(ChatColor.RED + "This location does not exist!");
                        break;
                }
                break;
            case "list":
                switch (args[1].toLowerCase()) {
                    case "locations":
                        if (args.length != 3) {
                            player.sendMessage(ChatColor.RED + "Please select a page!\n/setup list locations {1,2,3,etc...}");
                            return true;
                        }
                        player.sendMessage(ChatColor.GREEN + "Locations " + ChatColor.GRAY + "(Page " + Integer.parseInt(args[2]) + ")" + ChatColor.GREEN + ":");
                        for (int i = Integer.parseInt(args[2]) - 1; i < Integer.parseInt(args[2]) + 5; i++) {
                            Location loc = Main.getInstance().getLocation(locs.get(i));
                            player.sendMessage(ChatColor.DARK_GRAY + " - " + ChatColor.GREEN + locs.get(i) + ChatColor.YELLOW
                                    + "\n   World: " + loc.getWorld().getName()
                                    + "   X: " + loc.getX()
                                    + "   Y: " + loc.getY()
                                    + "   Z: " + loc.getZ()
                                    + "   Yaw: " + loc.getYaw()
                                    + "   Pitch: " + loc.getPitch());
                        }
                        break;
                    case "setlocations":
                        if (args.length != 3) {
                            player.sendMessage(ChatColor.RED + "Please select a page!\n/setup list setlocations {1,2,3,etc...}");
                            return true;
                        }
                        player.sendMessage(ChatColor.GREEN + "Set Locations " + ChatColor.GRAY + "(Page " + Integer.parseInt(args[2]) + ")" + ChatColor.GREEN + ":");
                        for (int i = Integer.parseInt(args[2]) - 1; i < Integer.parseInt(args[2]) + 5; i++) {
                            if (!Main.getInstance().getLocation(locs.get(i)).equals(new Location(Bukkit.getWorld("world"), 0, 0, 0, 0, 0))) {
                                Location loc = Main.getInstance().getLocation(locs.get(i));
                                player.sendMessage(ChatColor.DARK_GRAY + " - " + ChatColor.GREEN + locs.get(i) + ChatColor.YELLOW
                                        + "\n   World: " + loc.getWorld().getName()
                                        + "   X: " + loc.getX()
                                        + "   Y: " + loc.getY()
                                        + "   Z: " + loc.getZ()
                                        + "   Yaw: " + loc.getYaw()
                                        + "   Pitch: " + loc.getPitch());
                            }
                        }
                        break;
                    case "unsetlocations":
                        if (args.length != 3) {
                            player.sendMessage(ChatColor.RED + "Please select a page!\n/setup list unsetlocations {1,2,3,etc...}");
                            return true;
                        }
                        player.sendMessage(ChatColor.GREEN + "Unsetlocations Locations " + ChatColor.GRAY + "(Page " + Integer.parseInt(args[2]) + ")" + ChatColor.GREEN + ":");
                        for (int i = Integer.parseInt(args[2]) - 1; i < Integer.parseInt(args[2]) + 5; i++) {
                            if (Main.getInstance().getLocation(locs.get(i)).equals(new Location(Bukkit.getWorld("world"), 0, 0, 0, 0, 0))) {
                                player.sendMessage(ChatColor.DARK_GRAY + " - " + ChatColor.GREEN + locs.get(i) + ChatColor.GRAY);
                            }
                        }
                    default:
                        player.sendMessage(ChatColor.RED + "Incorrect usage! /set list {locations/setlocations/unsetlocations} {1,2,3,etc...}");
                }
                break;
            case "tp":
                player.teleport(Main.getInstance().getLocation(args[1]));
                break;
            default:
                player.sendMessage(ChatColor.RED + "Incorrect usage!  Usage: \n/setup set <location>\n/setup list <locations|setlocations|unsetlocations>\n/setup tp <location>");
                break;
        }

        return false;
    }
}
