package com.random.captureTheFlag.commands;

import com.random.captureTheFlag.Main;
import com.random.captureTheFlag.game.GameState;
import com.random.captureTheFlag.player.CapturePlayer;
import com.random.captureTheFlag.player.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AssignCommand implements CommandExecutor {
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

        if (Main.getInstance().getState() == GameState.GAME) {
            player.sendMessage(ChatColor.YELLOW + "[⚠] Careful!  Changing teams in the middle of the game may cause issues unless the new team is a spectating team.");
        }

        if (args.length == 0) {
            player.sendMessage(ChatColor.GREEN + "Current teams: ");
            for (CapturePlayer p : Main.getInstance().getPlayers().values()) {
                if (p.getTeam() != null) {
                    player.sendMessage(ChatColor.GRAY + " - " + ChatColor.YELLOW
                            + p.getPlayer().getName() + ChatColor.GRAY + ": "
                            + p.getTeam().getColor() + p.getTeam().getName());
                } else {
                    player.sendMessage(ChatColor.GRAY + " - " + ChatColor.YELLOW
                        + p.getPlayer().getName() + ChatColor.GRAY + ": " + "Random");
                }
            }
            return true;
        }

        if (args[0].equals("assign") && args.length > 2) {
            switch (args[args.length - 1]) {
                case "green":
                    for (int i = 1; i < args.length - 1; i++) {
                        if (Bukkit.getPlayer(args[i]) == null) {
                            player.sendMessage(ChatColor.RED + "Player \"" + args[i] + "\" does not exist or is not online!");
                        } else {
                            Main.getInstance().getPlayers().get(Bukkit.getPlayer(args[i]).getUniqueId()).setTeam(Team.LIME);
                            player.sendMessage(ChatColor.GRAY + "Player \"" + args[i] + "\" was assigned to " + ChatColor.GREEN + "Green Team" + ChatColor.GRAY + "!");
                        }
                    }
                    break;
                case "spectator":
                    for (int i = 1; i < args.length - 1; i++) {
                        if (Bukkit.getPlayer(args[i]) == null) {
                            player.sendMessage(ChatColor.RED + "Player \"" + args[i] + "\" does not exist or is not online!");
                        } else {
                            Main.getInstance().getPlayers().get(Bukkit.getPlayer(args[i]).getUniqueId()).setTeam(Team.SPEC);
                            player.sendMessage(ChatColor.GRAY + "Player \"" + args[i] + "\" was assigned to " + ChatColor.DARK_GRAY + "Spectating Team" + ChatColor.GRAY + "!");
                        }
                    }
                    break;
                case "red":
                case "blue":
                case "yellow":
                case "lime":
                case "spec":
                    for (int i = 1; i < args.length - 1; i++) {
                        if (Bukkit.getPlayer(args[i]) == null) {
                            player.sendMessage(ChatColor.RED + "Player \"" + args[i] + "\" does not exist or is not online!");
                        } else {
                            Team team = Team.valueOf(args[args.length - 1].toUpperCase());
                            Main.getInstance().getPlayers().get(Bukkit.getPlayer(args[i]).getUniqueId()).setTeam(team);
                            player.sendMessage(ChatColor.GRAY + "Player \"" + args[i] + "\" was assigned to " + team.getColor() + team.getName() + " Team" + ChatColor.GRAY + "!");
                        }
                    }
                    break;
                default:
                    player.sendMessage(ChatColor.RED + "Team \"" + args[args.length - 1] + "\" does not exist!  Possible teams: Red, Blue, Green, Yellow, Spec");

            }
            return true;
        }

        if ((args[0].equals("remove") || args[0].equals("unlist")) && args.length > 1) {
            for (int i = 1; i < args.length; i++) {
                if (Bukkit.getPlayer(args[i]) == null) {
                    player.sendMessage(ChatColor.RED + "Player \"" + args[i] + "\" does not exist or is not online!");
                } else {
                    Main.getInstance().getPlayers().get(Bukkit.getPlayer(args[i]).getUniqueId()).setTeam(null);
                }
            }
            return true;
        }

        player.sendMessage(ChatColor.RED + "Incorrect usage!\n  /teams\n  /teams assign {players...} {team}\n  /teams unlist {players...}  (Removes players from event)");
        return false;
    }
}
