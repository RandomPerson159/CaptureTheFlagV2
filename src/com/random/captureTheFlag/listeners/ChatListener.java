package com.random.captureTheFlag.listeners;

import com.random.captureTheFlag.game.GameState;
import com.random.captureTheFlag.Main;
import com.random.captureTheFlag.player.CapturePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler
    void onChat(AsyncPlayerChatEvent ev) {
        Player p = ev.getPlayer();

        if (Main.getInstance().getState() == GameState.WAIT) {
            return;
        }

        if (Main.getInstance().getState() == GameState.GAME) {
            if (Main.getInstance().getPlayers().get(p.getUniqueId()) == null) {
                for (Player all : Bukkit.getOnlinePlayers()) {
                    all.sendMessage(ChatColor.GRAY + "[Spectator] " + ChatColor.WHITE + p.getName() + ": " + ev.getMessage());
                }
                return;
            }
            CapturePlayer sender = Main.getInstance().getPlayers().get(p.getUniqueId());
            String msg = ChatColor.GRAY + "[" + sender.getTeam().getColor() + sender.getTeam() + ChatColor.GRAY + "] "
                    + ChatColor.WHITE + p.getName() + ": " + ChatColor.GRAY + ev.getMessage();
            for (Player all : Bukkit.getOnlinePlayers()) {
                if (Main.getInstance().getPlayers().get(all.getUniqueId()) == null) {
                    all.sendMessage(msg);
                } else if (Main.getInstance().getPlayers().get(all.getUniqueId()).getTeam() == sender.getTeam()) {
                    all.sendMessage(msg);
                }
            }
            ev.setCancelled(true);
        }
    }
}
