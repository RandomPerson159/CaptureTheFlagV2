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
        ev.getPlayer().teleport(Main.getInstance().getLocation("lobby"));
        if (Main.getInstance().getState() != GameState.GAME) {
            Main.getInstance().getPlayers().put(ev.getPlayer().getUniqueId(), new CapturePlayer(ev.getPlayer().getUniqueId()));
            p.setGameMode(GameMode.SURVIVAL);
            if (p.isOp()) {
                p.setGameMode(GameMode.CREATIVE);
            }
            p.getInventory().clear();
            p.setHealth(20);
            p.setSaturation(20);
            return;
        } else if (Main.getInstance().getPlayers().get(ev.getPlayer().getUniqueId()) != null) {
            CapturePlayer cp = Main.getInstance().getPlayers().get(ev.getPlayer().getUniqueId());
            ev.setJoinMessage(null);
            if (cp.getTeam() != Team.SPEC) {
                ev.setJoinMessage(cp.getTeam().getColor() + ev.getPlayer().getName() + ChatColor.GRAY + " has rejoined.");
                ev.getPlayer().setHealth(0);
            }
            return;
        } else {
            ev.getPlayer().kickPlayer(ChatColor.RED + "[⚠] Whoops!  Event has already started! Please wait for a new round to start.");
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
        if (Main.getInstance().getState() != GameState.GAME) return;

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
