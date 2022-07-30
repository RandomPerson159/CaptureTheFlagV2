package com.random.captureTheFlag.game;

import com.random.captureTheFlag.player.CapturePlayer;
import com.random.captureTheFlag.player.Team;
import com.random.captureTheFlag.util.ItemBuilder;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Flag {
    private final Team team;
    private final Location home;
    private boolean isDropped;
    private CapturePlayer holder;
    private final ItemStack stack;
    private Item item;

    public Flag(Team team, Location home) {
        this.team = team;
        this.home = home;
        this.isDropped = false;
        this.holder = null;
        stack = new ItemBuilder()
                .setMaterial(Material.valueOf(team.name() + "_BANNER"))
                .setDisplayName(team.getColor() + team.getName() + ChatColor.GRAY + "'s Flag")
                .setLore(ChatColor.GRAY + "If this is your flag, click an empty beacon to return it!", ChatColor.GRAY + "If this is not your flag, click on your flag to capture it!")
                .getItem();
    }

    public Team getTeam() {
        return team;
    }

    public void put(FlagEvent cause, CapturePlayer cp) {
        home.getBlock().setType(Material.valueOf(team.name() + "_BANNER"));
        home.clone().subtract(0, 3, 0).getBlock().setType(Material.IRON_BLOCK);
        home.clone().subtract(0, 1, 0).getBlock().setType(Material.valueOf(team.name() + "_STAINED_GLASS"));

        if (cause == FlagEvent.RETURN) {
            String msg = ChatColor.GRAY + "[====================================================]\n"
                    + ChatColor.GOLD + "                     Flag Returned\n \n"
                    + "     " + team.getColor() + cp.getPlayer().getName() + ChatColor.GRAY + " has returned " + team.getColor() + team.getName() + " Team" + ChatColor.GRAY + "'s flag!\n \n"
                    + "[====================================================]";
            for (Player all : Bukkit.getOnlinePlayers()) {
                all.sendMessage(msg);
                all.playSound(all.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 7, 1);
            }
            this.holder = null;
        } else if (cause == FlagEvent.RETURN1) {
            String msg = ChatColor.GRAY + "[====================================================]\n"
                    + ChatColor.GOLD + "                     Flag Returned\n \n"
                    + "     " + team.getColor() + cp.getPlayer().getName() + ChatColor.GRAY + " has returned " + team.getColor() + team.getName() + " Team" + ChatColor.GRAY + "'s flag!\n \n"
                    + "[====================================================]";
            for (Player all : Bukkit.getOnlinePlayers()) {
                all.sendMessage(msg);
                all.playSound(all.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 7, 1);
            }
        }
    }

    public void take(CapturePlayer cp) {
        home.getBlock().setType(Material.AIR);
        home.clone().subtract(0, 3, 0).getBlock().setType(Material.AIR);
        home.clone().subtract(0, 1, 0).getBlock().setType(Material.valueOf("GRAY_STAINED_GLASS"));

        if (cp == null) return;


        this.holder = cp;

        String msg = ChatColor.GRAY + "[====================================================]\n"
                + ChatColor.GOLD + "                      Flag Taken\n \n"
                + "     " + cp.getTeam().getColor() + cp.getPlayer().getName() + ChatColor.GRAY + " has taken " + team.getColor() + team.getName() + " Team" + ChatColor.GRAY + "'s flag!\n \n"
                + "[====================================================]";

        for (Player all : Bukkit.getOnlinePlayers()) {
            all.sendMessage(msg);
            all.playSound(all.getLocation(), Sound.ENTITY_WITHER_SPAWN, 7, 1);
        }

    }

    public void drop(Location loc, CapturePlayer cp) {
        item = loc.getWorld().dropItem(loc, stack);
        setDropped(true);

        String msg = ChatColor.GRAY + "[====================================================]\n"
                + ChatColor.GOLD + "                     Flag Dropped\n \n"
                + "     " + cp.getTeam().getColor() + cp.getPlayer().getName() + ChatColor.GRAY + " has dropped " + team.getColor() + team.getName() + " Team" + ChatColor.GRAY + "'s flag!\n"
                + "     Look for this flag to capture it or return it!\n \n"
                + "[====================================================]";

        for (Player all : Bukkit.getOnlinePlayers()) {
            all.sendMessage(msg);
            all.playSound(all.getLocation(), Sound.ENTITY_WITHER_SPAWN, 7, 1);
        }

        this.holder = null;
    }

    public void setHolder(CapturePlayer holder) {
        this.holder = holder;
    }

    public CapturePlayer getHolder() {
        return holder;
    }

    public Location getHome() {
        return home;
    }

    public Item getItem() {
        return item;
    }

    public ItemStack getStack() {
        return stack;
    }

    public void setDropped(boolean dropped) {
        isDropped = dropped;
    }

    public boolean isDropped() {
        return isDropped;
    }
}
