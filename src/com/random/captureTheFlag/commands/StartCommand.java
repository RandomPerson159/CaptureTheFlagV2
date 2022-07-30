package com.random.captureTheFlag.commands;

import com.random.captureTheFlag.Main;
import com.random.captureTheFlag.game.Flag;
import com.random.captureTheFlag.game.GameState;
import com.random.captureTheFlag.player.CapturePlayer;
import com.random.captureTheFlag.player.Team;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartCommand implements CommandExecutor {
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

        if (s.equals("cancel")) {
            if (args.length == 0) {
                player.sendMessage(ChatColor.YELLOW + "[⚠] Careful!  You're about to cancel the game!  Run /cancel confirm to confirm.");
                return true;
            } else if (args[0].equals("confirm")) {
                Main.getInstance().getPlayers().clear();
                for (Player all : Bukkit.getOnlinePlayers()) {
                    all.teleport(Main.getInstance().getLocation("lobby"));
                    all.setGameMode(GameMode.SURVIVAL);
                    if (all.isOp()) {
                        all.setGameMode(GameMode.CREATIVE);
                    }
                    all.getInventory().clear();
                    all.setHealth(20);
                    all.setSaturation(20);

                    Main.getInstance().getPlayers().put(all.getUniqueId(), new CapturePlayer(all.getUniqueId()));
                }
                Main.getInstance().initFlags();
                Main.getInstance().initTeams();
                Main.getInstance().setState(GameState.WAIT);
            }
        } else {
            if (args.length == 0) {
                int i = 0;
                for (Player all : Bukkit.getOnlinePlayers()) {
                    if (Main.getInstance().getPlayers().get(all.getUniqueId()) != null) {
                        if (Main.getInstance().getPlayers().get(all.getUniqueId()).getTeam() != Team.SPEC) {
                            i++;
                        }
                    }
                }
                if (i < Main.getInstance().getSettings().getPlayers()) {
                    player.sendMessage(ChatColor.YELLOW + "[⚠] Careful!  Starting with too few players requires manual team assignment if you want equal distribution!  Run /start confirm to confirm.");
                    return true;
                } else if (i > Main.getInstance().getSettings().getPlayers()) {
                    player.sendMessage(ChatColor.YELLOW + "[⚠] Careful!  You are starting with too many players!  Please change the settings and run /start confirm to confirm.");
                    return true;
                } else {
                    player.sendMessage(ChatColor.GREEN + "Starting...");
                    Main.getInstance().start();
                }
            } else if (args[0].equals("confirm")) {
                player.sendMessage(ChatColor.GREEN + "Starting...");
                Main.getInstance().start();
            }
        }
        return false;
    }
}
