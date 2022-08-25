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
        // lp user {username} group add {rank}
        // red-team
        // blue-team
        Player p = ev.getPlayer();

        if ((Main.getInstance().getState() == GameState.GAME || Main.getInstance().getState() == GameState.STARTING)
                && Main.getInstance().getPlayers().get(p.getUniqueId()) != null) {
            CapturePlayer cp = Main.getInstance().getPlayers().get(p.getUniqueId());
            ev.setJoinMessage(null);
            if (cp.getTeam() != Team.SPEC) {
                ev.setJoinMessage(cp.getTeam().getColor() + p.getName() + ChatColor.GRAY + " has rejoined.");
                if (Main.getInstance().getState() == GameState.GAME) {
                    p.setHealth(0);
                    if (cp.getKit() == null) {
                        cp.setKit(Main.getInstance().getRandomKit(cp.getTeam()));
                    }
                } else {
                    p.teleport(Main.getInstance().getWait());
                }
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p.getName() + " group add " + cp.getTeam().name().toLowerCase() + "-team");
            }
            return;
        } else if (Main.getInstance().getState() == GameState.WAIT) {
            Main.getInstance().getPlayers().put(p.getUniqueId(), new CapturePlayer(p.getUniqueId()));
        } else {
            if (p.isOp()) return;
            if (Main.getInstance().getSettings().isAllowSpectators()) {
                Main.getInstance().getPlayers().put(p.getUniqueId(), new CapturePlayer(p.getUniqueId()));
                CapturePlayer cp = Main.getInstance().getPlayers().get(p.getUniqueId());
                cp.setTeam(Team.SPEC);
                return;
            }
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

        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + ev.getPlayer().getName() + " group remove red-team");
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + ev.getPlayer().getName() + " group remove blue-team");
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + ev.getPlayer().getName() + " group remove spec-team");

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
        }

        Main.getInstance().tryEnd();

        ev.setQuitMessage(cp.getTeam().getColor() + ev.getPlayer().getName() + ChatColor.GRAY + " has quit.");

        for (Flag flag : Main.getInstance().getFlags()) {
            if (flag.getHolder() == cp) {
                flag.drop(ev.getPlayer().getLocation(), cp);
            }
        }
    }
}
