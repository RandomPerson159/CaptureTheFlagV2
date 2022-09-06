package com.random.captureTheFlag.commands;

import com.random.captureTheFlag.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class EnabledCommand implements CommandExecutor {
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

        if (args.length == 0) {
            player.sendMessage(ChatColor.YELLOW + "Capture the Flag wants to reload the server.  Run /enable confirm to confirm.");
        } else if (args[0].equals("confirm")) {
            FileConfiguration cfg = Main.getInstance().getConfig("settings");
            cfg.set("enabled", !Main.getInstance().getEnabled());
            try {
                cfg.save(new File("./plugins/CaptureTheFlag/settings.yml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Bukkit.reload();
        }
        return false;
    }
}
