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

public class ShoutCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + "Command must be run by a player!");
            return true;
        }

        Player player = (Player) commandSender;
        if (!Main.getInstance().getSettings().isShouts()) {
            player.sendMessage(ChatColor.RED + "[⚠] Whoops!  Shout command is disabled!");
            return true;
        }
        if (!Main.getInstance().getEnabled()) {
            player.sendMessage(ChatColor.RED + "[⚠] Whoops!  Capture the Flag is not enabled!  Please run /enable to enable it!");
            return true;
        }
        if (Main.getInstance().getState() != GameState.GAME) {
            player.sendMessage(ChatColor.RED + "[⚠] Whoops!  Command can only be used during the game!");
            return true;
        }

        if (Main.getInstance().getPlayers().get(player.getUniqueId()).getTeam() == Team.SPEC) {
            player.sendMessage(ChatColor.RED + "[⚠] Whoops!  Command can only be used by players!");
            return true;
        }

        StringBuilder msg = new StringBuilder();

        for (String arg : args) {
            msg.append(arg).append(" ");
        }

        CapturePlayer cp = Main.getInstance().getPlayers().get(player.getUniqueId());
        String finalMsg = ChatColor.GOLD + "[Shout " + cp.getTeam().getColor() + cp.getTeam().getName() + ChatColor.GOLD + "] "
                + cp.getTeam().getColor() + player.getName() + ChatColor.WHITE + ": " + msg.toString();

        for (Player all : Bukkit.getOnlinePlayers()) {
            all.sendMessage(finalMsg);
        }

        return false;
    }
}
