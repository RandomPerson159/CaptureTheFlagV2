package com.random.captureTheFlag.listeners;

import com.random.captureTheFlag.Main;
import com.random.captureTheFlag.game.Flag;
import com.random.captureTheFlag.game.GameState;
import com.random.captureTheFlag.player.CapturePlayer;
import com.random.captureTheFlag.player.Team;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinListener implements Listener {

    @EventHandler
    void onJoin(PlayerJoinEvent ev) {
        Player p = ev.getPlayer();
        p.teleport(Main.getInstance().getLocation("lobby"));
        p.setScoreboard(Main.getInstance().getBoard().getB());
        p.setPlayerListName(ChatColor.GRAY + p.getName());
        if ((Main.getInstance().getState() == GameState.GAME || Main.getInstance().getState() == GameState.STARTING)
                && Main.getInstance().getPlayers().get(p.getUniqueId()) != null) {
            CapturePlayer cp = Main.getInstance().getPlayers().get(p.getUniqueId());
            ev.setJoinMessage(null);
            if (cp.getTeam() != Team.SPEC) {
                ev.setJoinMessage(cp.getTeam().getColor() + p.getName() + ChatColor.GRAY + " has rejoined.");
                if (Main.getInstance().getState() != GameState.STARTING) {
                    p.setHealth(0);
                }
            }
            p.setPlayerListName(ChatColor.DARK_GRAY + "[" + cp.getTeam().getColor() + cp.getTeam().getName() + ChatColor.DARK_GRAY + "] " + ChatColor.WHITE + p.getName());
            return;
        } else if (Main.getInstance().getState() == GameState.WAIT) {
            Main.getInstance().getPlayers().put(p.getUniqueId(), new CapturePlayer(p.getUniqueId()));
            if (p.isOp()) {
                p.setGameMode(GameMode.CREATIVE);
                p.setPlayerListName(ChatColor.DARK_RED + p.getName());
            } else {
                p.setGameMode(GameMode.SURVIVAL);
                p.setPlayerListName(ChatColor.GRAY + p.getName());
            }
            p.getInventory().clear();
            p.setHealth(20);
            p.setSaturation(20);

        } else {
            p.kickPlayer(ChatColor.RED + "[⚠] Whoops!  Event has already started! Please wait for a new round to start.");
            return;
        }

        int i = 0;
        for (Player all : Bukkit.getOnlinePlayers()) {
            if (Main.getInstance().getPlayers().get(all.getUniqueId()) != null) {
                if (Main.getInstance().getPlayers().get(all.getUniqueId()).getTeam() != Team.SPEC) {
                    i++;
                }
            }
        }

        if (i == Main.getInstance().getSettings().getPlayers() && Main.getInstance().getSettings().isAutoStart()) {
            Main.getInstance().start();
        }
    }

    @EventHandler
    void onLeave(PlayerQuitEvent ev) {
        CapturePlayer cp = Main.getInstance().getPlayers().get(ev.getPlayer().getUniqueId());
        ev.setQuitMessage(null);
        if (cp == null) return;
        if (Main.getInstance().getState() == GameState.WAIT) {
            Main.getInstance().getPlayers().remove(cp.getPlayer().getUniqueId(), cp);
            return;
        }

        boolean teamMates = false;
        for (Player all : Bukkit.getOnlinePlayers()) {
            CapturePlayer allCp = Main.getInstance().getPlayers().get(all.getUniqueId());
            if (allCp == null) continue;
            if (allCp.getPlayer().getUniqueId().equals(ev.getPlayer().getUniqueId())) continue;
            if (allCp.getTeam() == null) continue;
            if (allCp.getTeam() == cp.getTeam()) {
                teamMates = true;
                return;
            }
        }

        if (!teamMates) {
            for (Flag cpFlags : Main.getInstance().getFlags()) {
                if (cpFlags.getTeam() == cp.getTeam()) {
                    cpFlags.take(null);
                    cpFlags.setDropped(false);
                    if (cpFlags.getItem() != null) {
                        cpFlags.getItem().remove();
                    }
                    if (cpFlags.getHolder() != null) {
                        cpFlags.getHolder().getPlayer().getInventory().remove(cpFlags.getStack());
                        cpFlags.setHolder(null);
                    }
                }
            }
            Main.getInstance().tryEnd();
            return;
        }

        ev.setQuitMessage(cp.getTeam().getColor() + ev.getPlayer().getName() + ChatColor.GRAY + " has quit.");

        for (Flag flag : Main.getInstance().getFlags()) {
            if (flag.getHolder() == cp) {
                flag.drop(ev.getPlayer().getLocation(), cp);
                ev.getPlayer().setHealth(0);
            }
        }
    }
}
