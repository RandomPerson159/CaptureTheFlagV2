package com.random.captureTheFlag.commands;

import com.random.captureTheFlag.Main;
import com.random.captureTheFlag.game.GameState;
import com.random.captureTheFlag.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class SettingsCommand implements CommandExecutor {
    public static final Inventory SETTINGS = Bukkit.createInventory(null, 45, ChatColor.DARK_PURPLE + "Settings");

    public static void updateInv() {
        for (int i = 0; i < 9; i++) {
            SETTINGS.setItem(i, new ItemBuilder().setMaterial(Material.GRAY_STAINED_GLASS_PANE).setDisplayName("§c").getItem());
        }

        SETTINGS.setItem(9, new ItemBuilder().setMaterial(Material.PLAYER_HEAD)
                .setAmount(Main.getInstance().getSettings().getPlayers())
                .setDisplayName(ChatColor.DARK_PURPLE + "Number of players:")
                .setLore(ChatColor.GREEN + "" + Main.getInstance().getSettings().getPlayers() + " players going to play", ChatColor.GRAY + "" + (Main.getInstance().getSettings().getPlayers() / Main.getInstance().getSettings().getTeams()) + " players per team", ChatColor.YELLOW + "Number of players must be divisible by number of teams!", ChatColor.GRAY + "Right-click to decrease; left-click to increase.")
                .getItem());
        SETTINGS.setItem(10, new ItemBuilder().setMaterial(Material.ITEM_FRAME)
                .setDisplayName(ChatColor.DARK_PURPLE + "Drop flags:")
                .setLore(Main.getInstance().getSettings().isDropFlags() ? ChatColor.GREEN + "Flags will drop." : ChatColor.RED + "Flags will not drop.", ChatColor.GRAY + "When players do not die to a player, flags on them will drop to be picked up, or they can return immediately.")
                .getItem());
        SETTINGS.setItem(11, new ItemBuilder().setMaterial(Material.BEACON)
                .setDisplayName(ChatColor.DARK_PURPLE + "Allow shouts:")
                .setLore(Main.getInstance().getSettings().isShouts() ? ChatColor.GREEN + "Shouts are allowed." : ChatColor.RED + "Shouts are not allowed.", ChatColor.GRAY + "Allows player to use /shout and talk to the other team.")
                .getItem());
        SETTINGS.setItem(12, new ItemBuilder().setMaterial(Material.PURPLE_CONCRETE)
                .setAmount(Main.getInstance().getSettings().getTeams())
                .setDisplayName(ChatColor.DARK_PURPLE + "Number of teams:")
                .setLore(ChatColor.GREEN + "" + Main.getInstance().getSettings().getTeams() + " teams", ChatColor.YELLOW + "Can only be 2 or 4!", ChatColor.GRAY + "Right-click to decrease; left-click to increase.")
                .getItem());
        SETTINGS.setItem(13, new ItemBuilder().setMaterial(Material.PURPLE_BANNER)
                .setAmount(Main.getInstance().getSettings().getFlags())
                .setDisplayName(ChatColor.DARK_PURPLE + "Number of flags:")
                .setLore(ChatColor.GREEN + "" + Main.getInstance().getSettings().getTeams() + " flags per team", ChatColor.YELLOW + "Can only be 1 or 2!", ChatColor.GRAY + "Right-click to decrease; left-click to increase.")
                .getItem());
        SETTINGS.setItem(14, new ItemBuilder().setMaterial(Material.PURPLE_BED)
                .setDisplayName(ChatColor.DARK_PURPLE + "Respawn time:")
                .setLore(ChatColor.GREEN + "" + Main.getInstance().getSettings().getRespawn() + " seconds", ChatColor.YELLOW + "Maximum of 30 seconds!", ChatColor.GRAY + "Right-click to decrease; left-click to increase.")
                .getItem());
        SETTINGS.setItem(15, new ItemBuilder().setMaterial(Material.NETHER_STAR)
                .setDisplayName(ChatColor.DARK_PURPLE + "Auto-start:")
                .setLore(Main.getInstance().getSettings().isAutoStart() ? ChatColor.GREEN + "Game will automatically start" : ChatColor.RED + "Game will not automatically start")
                .getItem());

        SETTINGS.setItem(27, new ItemBuilder().setMaterial(Material.IRON_AXE)
                .setDisplayName(ChatColor.DARK_PURPLE + "Number of mid-field kits:")
                .setLore(ChatColor.GREEN + "" + Main.getInstance().getSettings().getMidFeildKit() + " mid-field kits per team", ChatColor.YELLOW + "Number of every kit must add up to number of players per team!", ChatColor.YELLOW + "(No less, but can be more.  Number of one kit can be zero.)", ChatColor.GRAY + "Right-click to decrease; left-click to increase.")
                .getItem());
        SETTINGS.setItem(28, new ItemBuilder().setMaterial(Material.SHIELD)
                .setDisplayName(ChatColor.DARK_PURPLE + "Number of defense kits:")
                .setLore(ChatColor.GREEN + "" + Main.getInstance().getSettings().getDefenseKit() + " defense kits per team", ChatColor.YELLOW + "Number of every kit must add up to number of players per team!", ChatColor.YELLOW + "(No less, but can be more.  Number of one kit can be zero.)", ChatColor.GRAY + "Right-click to decrease; left-click to increase.")
                .getItem());
        SETTINGS.setItem(29, new ItemBuilder().setMaterial(Material.IRON_SWORD)
                .setDisplayName(ChatColor.DARK_PURPLE + "Number of flag-stealer kits:")
                .setLore(ChatColor.GREEN + "" + Main.getInstance().getSettings().getFlagStealerKit() + " flag-stealer per team", ChatColor.YELLOW + "Number of every kit must add up to number of players per team!", ChatColor.YELLOW + "(No less, but can be more.  Number of one kit can be zero.)", ChatColor.GRAY + "Right-click to decrease; left-click to increase.")
                .getItem());
        SETTINGS.setItem(30, new ItemBuilder().setMaterial(Material.BOW)
                .setDisplayName(ChatColor.DARK_PURPLE + "Number of bow kits:")
                .setLore(ChatColor.GREEN + "" + Main.getInstance().getSettings().getBowKit() + " bow kits per team", ChatColor.YELLOW + "Number of every kit must add up to number of players per team!", ChatColor.YELLOW + "(No less, but can be more.  Number of one kit can be zero.)", ChatColor.GRAY + "Right-click to decrease; left-click to increase.")
                .getItem());

        for (int i = 36; i < 43; i++) {
            SETTINGS.setItem(i, new ItemBuilder().setMaterial(Material.GRAY_STAINED_GLASS_PANE).setDisplayName("§c").getItem());
        }

        SETTINGS.setItem(43, new ItemBuilder().setMaterial(Material.STRUCTURE_VOID)
                .setDisplayName(ChatColor.DARK_PURPLE + "Select default")
                .setLore(ChatColor.GREEN + "Sets all settings to default.")
                .getItem());
        SETTINGS.setItem(44, new ItemBuilder().setMaterial(Material.BARRIER)
                .setDisplayName(ChatColor.RED + "Close")
                .getItem());
    }

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
        if (!Main.getInstance().getEnabled()) {
            player.sendMessage(ChatColor.RED + "[⚠] Whoops!  Capture the Flag is not enabled!  Please run /enable to enable it!");
            return true;
        }
        if (Main.getInstance().getState() == GameState.GAME) {
            player.sendMessage(ChatColor.RED + "[⚠] Whoops!  This command can only be used before a game!  You can cancel this game to change settings. /cancel");
            return true;
        }
        updateInv();
        player.openInventory(SETTINGS);
        return false;
    }

}
