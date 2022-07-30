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

import java.util.List;

public class ChatListener implements Listener {
    public static final List<String> bannedWords = (List<String>) Main.getInstance().getConfig("banned_words").getList("banned-words");

    @EventHandler
    void onChat(AsyncPlayerChatEvent ev) {
        Player p = ev.getPlayer();

        String[] words = ev.getMessage().toLowerCase().split(" ");
        for (String word : words) {
            if (bannedWords.contains(word)) {
                ev.setCancelled(true);
                ev.getPlayer().sendMessage(ChatColor.RED + "[⚠] Whoops!  Your message contained banned language!");
                return;
            }
        }

        if (Main.getInstance().getState() == GameState.WAIT) {
            ev.setFormat(ChatColor.GRAY + "<" + ev.getPlayer().getName() + "> " + ChatColor.GRAY + ev.getMessage());
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
                    + ChatColor.WHITE + p.getName() + ": " + ev.getMessage();
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
