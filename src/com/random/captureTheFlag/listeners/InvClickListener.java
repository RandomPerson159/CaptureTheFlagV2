package com.random.captureTheFlag.listeners;

import com.random.captureTheFlag.Main;
import com.random.captureTheFlag.commands.SettingsCommand;
import com.random.captureTheFlag.game.GameState;
import com.random.captureTheFlag.game.Settings;
import com.random.captureTheFlag.player.CapturePlayer;
import com.random.captureTheFlag.player.KitType;
import com.random.captureTheFlag.player.Team;
import com.random.captureTheFlag.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class InvClickListener implements Listener {
    private static Inventory RED_KITS = Bukkit.createInventory(null, 27, ChatColor.RED + "Red Team Kit Selection");
    public static Map<KitType, Integer> redKits = new HashMap<>();
    private static Inventory BLUE_KITS = Bukkit.createInventory(null, 27, ChatColor.BLUE + "Blue Team Kit Selection");
    public static Map<KitType, Integer> blueKits = new HashMap<>();
    private static Inventory LIME_KITS = Bukkit.createInventory(null, 27, ChatColor.GREEN + "Green Team Kit Selection");
    public static Map<KitType, Integer> greenKits = new HashMap<>();
    private static Inventory YELLOW_KITS = Bukkit.createInventory(null, 27, ChatColor.YELLOW + "Yellow Team Kit Selection");
    public static Map<KitType, Integer> yellowKits = new HashMap<>();

    private static Inventory MID_FIELD = Bukkit.createInventory(null, 27, ChatColor.YELLOW + "Mid-range Kit");
    private static Inventory DEFENSE = Bukkit.createInventory(null, 27, ChatColor.YELLOW + "Defense Kit");
    private static Inventory BOW = Bukkit.createInventory(null, 27, ChatColor.YELLOW + "Long-range Kit");
    private static Inventory FLAG_STEALER = Bukkit.createInventory(null, 27, ChatColor.YELLOW + "Flag-stealer Kit");

    static {
        redKits.put(KitType.MID_FIELD, 0);
        redKits.put(KitType.DEFENSE, 0);
        redKits.put(KitType.BOW, 0);
        redKits.put(KitType.FLAG_STEALER, 0);
        blueKits.put(KitType.MID_FIELD, 0);
        blueKits.put(KitType.DEFENSE, 0);
        blueKits.put(KitType.BOW, 0);
        blueKits.put(KitType.FLAG_STEALER, 0);
        greenKits.put(KitType.MID_FIELD, 0);
        greenKits.put(KitType.DEFENSE, 0);
        greenKits.put(KitType.BOW, 0);
        greenKits.put(KitType.FLAG_STEALER, 0);
        yellowKits.put(KitType.MID_FIELD, 0);
        yellowKits.put(KitType.DEFENSE, 0);
        yellowKits.put(KitType.BOW, 0);
        yellowKits.put(KitType.FLAG_STEALER, 0);
        updateInvs();

        MID_FIELD.addItem(KitType.MID_FIELD.getArmor());
        MID_FIELD.addItem(KitType.MID_FIELD.getItems());
        DEFENSE.addItem(KitType.DEFENSE.getArmor());
        DEFENSE.addItem(KitType.DEFENSE.getItems());
        DEFENSE.addItem(new ItemStack(Material.FIREWORK_ROCKET, 3));
        BOW.addItem(KitType.BOW.getArmor());
        BOW.addItem(KitType.BOW.getItems());
        FLAG_STEALER.addItem(KitType.FLAG_STEALER.getArmor());
        FLAG_STEALER.addItem(KitType.FLAG_STEALER.getItems());
    }

    public static void updateInvs() {
        for (int i = 0; i < 9; i++) {
            RED_KITS.setItem(i, new ItemBuilder().setMaterial(Material.GRAY_STAINED_GLASS_PANE).setDisplayName("§c").getItem());
            BLUE_KITS.setItem(i, new ItemBuilder().setMaterial(Material.GRAY_STAINED_GLASS_PANE).setDisplayName("§c").getItem());
            LIME_KITS.setItem(i, new ItemBuilder().setMaterial(Material.GRAY_STAINED_GLASS_PANE).setDisplayName("§c").getItem());
            YELLOW_KITS.setItem(i, new ItemBuilder().setMaterial(Material.GRAY_STAINED_GLASS_PANE).setDisplayName("§c").getItem());
        }

        RED_KITS.setItem(10, new ItemBuilder(KitType.MID_FIELD.getItem()).setLore(ChatColor.YELLOW + ""
                + redKits.get(KitType.MID_FIELD) + " / " + Main.getInstance().getSettings().getMidFeildKit() + " selected", ChatColor.GRAY
                + "Left click to select", ChatColor.GRAY + "Right click to view").getItem());
        RED_KITS.setItem(12, new ItemBuilder(KitType.DEFENSE.getItem()).setLore(ChatColor.YELLOW + ""
                + redKits.get(KitType.DEFENSE) + " / " + Main.getInstance().getSettings().getDefenseKit() + " selected", ChatColor.GRAY
                + "Left click to select", ChatColor.GRAY + "Right click to view").getItem());
        RED_KITS.setItem(14, new ItemBuilder(KitType.BOW.getItem()).setLore(ChatColor.YELLOW + ""
                + redKits.get(KitType.BOW) + " / " + Main.getInstance().getSettings().getBowKit() + " selected", ChatColor.GRAY
                + "Left click to select", ChatColor.GRAY + "Right click to view").getItem());
        RED_KITS.setItem(16, new ItemBuilder(KitType.FLAG_STEALER.getItem()).setLore(ChatColor.YELLOW + ""
                + redKits.get(KitType.FLAG_STEALER) + " / " + Main.getInstance().getSettings().getFlagStealerKit() + " selected", ChatColor.GRAY
                + "Left click to select", ChatColor.GRAY + "Right click to view").getItem());

        BLUE_KITS.setItem(10, new ItemBuilder(KitType.MID_FIELD.getItem()).setLore(ChatColor.YELLOW + ""
                + blueKits.get(KitType.MID_FIELD) + " / " + Main.getInstance().getSettings().getMidFeildKit() + " selected", ChatColor.GRAY
                + "Left click to select", ChatColor.GRAY + "Right click to view").getItem());
        BLUE_KITS.setItem(12, new ItemBuilder(KitType.DEFENSE.getItem()).setLore(ChatColor.YELLOW + ""
                + blueKits.get(KitType.DEFENSE) + " / " + Main.getInstance().getSettings().getDefenseKit() + " selected", ChatColor.GRAY
                + "Left click to select", ChatColor.GRAY + "Right click to view").getItem());
        BLUE_KITS.setItem(14, new ItemBuilder(KitType.BOW.getItem()).setLore(ChatColor.YELLOW + ""
                + blueKits.get(KitType.BOW) + " / " + Main.getInstance().getSettings().getBowKit() + " selected", ChatColor.GRAY
                + "Left click to select", ChatColor.GRAY + "Right click to view").getItem());
        BLUE_KITS.setItem(16, new ItemBuilder(KitType.FLAG_STEALER.getItem()).setLore(ChatColor.YELLOW + ""
                + blueKits.get(KitType.FLAG_STEALER) + " / " + Main.getInstance().getSettings().getFlagStealerKit() + " selected", ChatColor.GRAY
                + "Left click to select", ChatColor.GRAY + "Right click to view").getItem());

        LIME_KITS.setItem(10, new ItemBuilder(KitType.MID_FIELD.getItem()).setLore(ChatColor.YELLOW + ""
                + greenKits.get(KitType.MID_FIELD) + " / " + Main.getInstance().getSettings().getMidFeildKit() + " selected", ChatColor.GRAY
                + "Left click to select", ChatColor.GRAY + "Right click to view").getItem());
        LIME_KITS.setItem(12, new ItemBuilder(KitType.DEFENSE.getItem()).setLore(ChatColor.YELLOW + ""
                + greenKits.get(KitType.DEFENSE) + " / " + Main.getInstance().getSettings().getDefenseKit() + " selected", ChatColor.GRAY
                + "Left click to select", ChatColor.GRAY + "Right click to view").getItem());
        LIME_KITS.setItem(14, new ItemBuilder(KitType.BOW.getItem()).setLore(ChatColor.YELLOW + ""
                + greenKits.get(KitType.BOW) + " / " + Main.getInstance().getSettings().getBowKit() + " selected", ChatColor.GRAY
                + "Left click to select", ChatColor.GRAY + "Right click to view").getItem());
        LIME_KITS.setItem(16, new ItemBuilder(KitType.FLAG_STEALER.getItem()).setLore(ChatColor.YELLOW + ""
                + greenKits.get(KitType.FLAG_STEALER) + " / " + Main.getInstance().getSettings().getFlagStealerKit() + " selected", ChatColor.GRAY
                + "Left click to select", ChatColor.GRAY + "Right click to view").getItem());


        YELLOW_KITS.setItem(10, new ItemBuilder(KitType.MID_FIELD.getItem()).setLore(ChatColor.YELLOW + ""
                + yellowKits.get(KitType.MID_FIELD) + " / " + Main.getInstance().getSettings().getMidFeildKit() + " selected", ChatColor.GRAY
                + "Left click to select", ChatColor.GRAY + "Right click to view").getItem());
        YELLOW_KITS.setItem(12, new ItemBuilder(KitType.DEFENSE.getItem()).setLore(ChatColor.YELLOW + ""
                + yellowKits.get(KitType.DEFENSE) + " / " + Main.getInstance().getSettings().getDefenseKit() + " selected", ChatColor.GRAY
                + "Left click to select", ChatColor.GRAY + "Right click to view").getItem());
        YELLOW_KITS.setItem(14, new ItemBuilder(KitType.BOW.getItem()).setLore(ChatColor.YELLOW + ""
                + yellowKits.get(KitType.BOW) + " / " + Main.getInstance().getSettings().getBowKit() + " selected", ChatColor.GRAY
                + "Left click to select", ChatColor.GRAY + "Right click to view").getItem());
        YELLOW_KITS.setItem(16, new ItemBuilder(KitType.FLAG_STEALER.getItem()).setLore(ChatColor.YELLOW + ""
                + yellowKits.get(KitType.FLAG_STEALER) + " / " + Main.getInstance().getSettings().getFlagStealerKit() + " selected", ChatColor.GRAY
                + "Left click to select", ChatColor.GRAY + "Right click to view").getItem());

        for (int i = 18; i < 27; i++) {
            RED_KITS.setItem(i, new ItemBuilder().setMaterial(Material.GRAY_STAINED_GLASS_PANE).setDisplayName("§c").getItem());
            BLUE_KITS.setItem(i, new ItemBuilder().setMaterial(Material.GRAY_STAINED_GLASS_PANE).setDisplayName("§c").getItem());
            LIME_KITS.setItem(i, new ItemBuilder().setMaterial(Material.GRAY_STAINED_GLASS_PANE).setDisplayName("§c").getItem());
            YELLOW_KITS.setItem(i, new ItemBuilder().setMaterial(Material.GRAY_STAINED_GLASS_PANE).setDisplayName("§c").getItem());
        }

        RED_KITS.setItem(22, new ItemBuilder().setMaterial(Material.BARRIER).setDisplayName(ChatColor.RED + "Close").getItem());
        BLUE_KITS.setItem(22, new ItemBuilder().setMaterial(Material.BARRIER).setDisplayName(ChatColor.RED + "Close").getItem());
        LIME_KITS.setItem(22, new ItemBuilder().setMaterial(Material.BARRIER).setDisplayName(ChatColor.RED + "Close").getItem());
        YELLOW_KITS.setItem(22, new ItemBuilder().setMaterial(Material.BARRIER).setDisplayName(ChatColor.RED + "Close").getItem());
    }

    @EventHandler
    void onBlockClick(PlayerInteractEvent ev) {
        if (ev.getItem() == null) {
            return;
        }
        if (Main.getInstance().getState() != GameState.STARTING) {
            return;
        }
        if (ev.getAction() == Action.RIGHT_CLICK_AIR
                || ev.getAction() == Action.LEFT_CLICK_AIR
                || ev.getAction() == Action.RIGHT_CLICK_BLOCK
                || ev.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (ev.getItem().getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Select a Kit")) {
                switch (Main.getInstance().getPlayers().get(ev.getPlayer().getUniqueId()).getTeam()) {
                    case RED:
                        ev.getPlayer().openInventory(RED_KITS);
                        break;
                    case BLUE:
                        ev.getPlayer().openInventory(BLUE_KITS);
                        break;
                    case LIME:
                        ev.getPlayer().openInventory(LIME_KITS);
                        break;
                    default:
                        ev.getPlayer().openInventory(YELLOW_KITS);
                }
                ev.setCancelled(true);
            }
        }
    }

    @EventHandler
    void onClick(InventoryClickEvent ev) {
        if (ev.getCurrentItem() == null) {
            return;
        }

        if (ev.getView().getTitle().equals(ChatColor.DARK_PURPLE + "Settings")) {
            if (ev.getCurrentItem().getType() == Material.PLAYER_HEAD) {
                if (Main.getInstance().getSettings().getTeams() == 2) {
                    if (ev.getClick() == ClickType.LEFT) {
                        Main.getInstance().getSettings().setPlayers(ev.getCurrentItem().getAmount() + 2);
                        Main.getInstance().getSettings().setMidFeildKit(Main.getInstance().getSettings().getMidFeildKit() + 1);
                    } else if (ev.getClick() == ClickType.RIGHT) {
                        if (Main.getInstance().getSettings().getPlayers() != 2) {
                            Main.getInstance().getSettings().setPlayers(ev.getCurrentItem().getAmount() - 2);
                        }
                    }
                } else {
                    if (ev.getClick() == ClickType.LEFT) {
                        Main.getInstance().getSettings().setPlayers(ev.getCurrentItem().getAmount() + 4);
                        Main.getInstance().getSettings().setMidFeildKit(Main.getInstance().getSettings().getMidFeildKit() + 1);
                    } else if (ev.getClick() == ClickType.RIGHT) {
                        if (Main.getInstance().getSettings().getPlayers() != 4) {
                            Main.getInstance().getSettings().setPlayers(ev.getCurrentItem().getAmount() - 4);
                        }
                    }
                }
            }
            if (ev.getCurrentItem().getType() == Material.ITEM_FRAME) {
                Main.getInstance().getSettings().setDropFlags(!Main.getInstance().getSettings().isDropFlags());
                ev.getWhoClicked().openInventory(SettingsCommand.SETTINGS);
            }
            if (ev.getCurrentItem().getType() == Material.BEACON) {
                Main.getInstance().getSettings().setShouts(!Main.getInstance().getSettings().isShouts());
            }
            if (ev.getCurrentItem().getType() == Material.PURPLE_CONCRETE) {
                Main.getInstance().getSettings().setTeams(Main.getInstance().getSettings().getTeams() == 2 ? 4 : 2);
                if (Main.getInstance().getSettings().getPlayers() % Main.getInstance().getSettings().getTeams() != 0) {
                    if (Main.getInstance().getSettings().getPlayers() == 2) {
                        Main.getInstance().getSettings().setPlayers(Main.getInstance().getSettings().getPlayers() + 2);
                        Main.getInstance().getSettings().setMidFeildKit(Main.getInstance().getSettings().getMidFeildKit() + 1);
                    } else {
                        Main.getInstance().getSettings().setPlayers(Main.getInstance().getSettings().getPlayers() - 2);
                    }
                }
            }
            if (ev.getCurrentItem().getType() == Material.PURPLE_BANNER) {
                Main.getInstance().getSettings().setFlags(Main.getInstance().getSettings().getFlags() == 1 ? 2 : 1);
            }
            if (ev.getCurrentItem().getType() == Material.PURPLE_BED) {
                if (ev.getClick() == ClickType.LEFT) {
                    if (Main.getInstance().getSettings().getRespawn() < 30) {
                        Main.getInstance().getSettings().setRespawn(Main.getInstance().getSettings().getRespawn() + 5);
                    }
                } else if (ev.getClick() == ClickType.RIGHT) {
                    if (Main.getInstance().getSettings().getRespawn() > 5) {
                        Main.getInstance().getSettings().setRespawn(Main.getInstance().getSettings().getRespawn() - 5);
                    }
                }
            }
            if (ev.getCurrentItem().getType() == Material.NETHER_STAR) {
                Main.getInstance().getSettings().setAutoStart(!Main.getInstance().getSettings().isAutoStart());
            }
            if (ev.getCurrentItem().getType() == Material.IRON_AXE) {
                if (ev.getClick() == ClickType.LEFT) {
                    Main.getInstance().getSettings().setMidFeildKit(Main.getInstance().getSettings().getMidFeildKit() + 1);
                } else if (ev.getClick() == ClickType.RIGHT) {
                    if (Main.getInstance().getSettings().getMidFeildKit() + Main.getInstance().getSettings().getDefenseKit() + Main.getInstance().getSettings().getFlagStealerKit() + Main.getInstance().getSettings().getBowKit() <= Main.getInstance().getSettings().getPlayers() / Main.getInstance().getSettings().getTeams()) {
                        ev.getWhoClicked().sendMessage(ChatColor.RED + "The total number of kits must be equal to or greater than the number of players per team!");
                        ev.setCancelled(true);
                        return;
                    }
                    if (Main.getInstance().getSettings().getMidFeildKit() == 0) {
                        ev.setCancelled(true);
                        return;
                    }
                    Main.getInstance().getSettings().setMidFeildKit(Main.getInstance().getSettings().getMidFeildKit() - 1);
                }
            }
            if (ev.getCurrentItem().getType() == Material.SHIELD) {
                if (ev.getClick() == ClickType.LEFT) {
                    Main.getInstance().getSettings().setDefenseKit(Main.getInstance().getSettings().getDefenseKit() + 1);
                } else if (ev.getClick() == ClickType.RIGHT) {
                    if (Main.getInstance().getSettings().getMidFeildKit() + Main.getInstance().getSettings().getDefenseKit() + Main.getInstance().getSettings().getFlagStealerKit() + Main.getInstance().getSettings().getBowKit() <= Main.getInstance().getSettings().getPlayers() / Main.getInstance().getSettings().getTeams()) {
                        ev.getWhoClicked().sendMessage(ChatColor.RED + "The total number of kits must be equal to or greater than the number of players per team!");
                        ev.setCancelled(true);
                        return;
                    }
                    if (Main.getInstance().getSettings().getDefenseKit() == 0) {
                        ev.setCancelled(true);
                        return;
                    }
                    Main.getInstance().getSettings().setDefenseKit(Main.getInstance().getSettings().getDefenseKit() - 1);
                }
            }
            if (ev.getCurrentItem().getType() == Material.IRON_SWORD) {
                if (ev.getClick() == ClickType.LEFT) {
                    Main.getInstance().getSettings().setFlagStealerKit(Main.getInstance().getSettings().getFlagStealerKit() + 1);
                } else if (ev.getClick() == ClickType.RIGHT) {
                    if (Main.getInstance().getSettings().getMidFeildKit() + Main.getInstance().getSettings().getDefenseKit() + Main.getInstance().getSettings().getFlagStealerKit() + Main.getInstance().getSettings().getBowKit() <= Main.getInstance().getSettings().getPlayers() / Main.getInstance().getSettings().getTeams()) {
                        ev.getWhoClicked().sendMessage(ChatColor.RED + "The total number of kits must be equal to or greater than the number of players per team!");
                        ev.setCancelled(true);
                        return;
                    }
                    Main.getInstance().getSettings().setFlagStealerKit(Main.getInstance().getSettings().getFlagStealerKit() - 1);
                }
            }
            if (ev.getCurrentItem().getType() == Material.BOW) {
                if (ev.getClick() == ClickType.LEFT) {
                    Main.getInstance().getSettings().setBowKit(Main.getInstance().getSettings().getBowKit() + 1);
                } else if (ev.getClick() == ClickType.RIGHT) {
                    if (Main.getInstance().getSettings().getMidFeildKit() + Main.getInstance().getSettings().getDefenseKit() + Main.getInstance().getSettings().getFlagStealerKit() + Main.getInstance().getSettings().getBowKit() <= Main.getInstance().getSettings().getPlayers() / Main.getInstance().getSettings().getTeams()) {
                        ev.getWhoClicked().sendMessage(ChatColor.RED + "The total number of kits must be equal to or greater than the number of players per team!");
                        ev.setCancelled(true);
                        return;
                    }
                    if (Main.getInstance().getSettings().getBowKit() == 0) {
                        ev.setCancelled(true);
                        return;
                    }
                    Main.getInstance().getSettings().setBowKit(Main.getInstance().getSettings().getBowKit() - 1);
                }
            }
            if (ev.getCurrentItem().getType() == Material.BARRIER) {
                ev.getWhoClicked().closeInventory();
            }
            if (ev.getCurrentItem().getType() == Material.STRUCTURE_VOID) {
                Main.getInstance().setSettings(new Settings(8, true, true, 2, 1, 1, 1, 1, 2, 10));
            }

            SettingsCommand.updateInv();
            Main.getInstance().initFlags();
            Main.getInstance().initTeams();
            ev.setCancelled(true);
        } else if (ev.getView().getTitle().equals(ChatColor.RED + "Red Team Kit Selection")) {
            if (ev.getCurrentItem().getType() == Material.BARRIER) {
                ev.getWhoClicked().closeInventory();
                ev.setCancelled(true);
                return;
            }
            if (ev.getCurrentItem().getType() == Material.IRON_AXE) {
                if (ev.getClick() == ClickType.RIGHT) {
                    ev.getWhoClicked().openInventory(MID_FIELD);
                    Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10, 1);
                } else if (ev.getClick() == ClickType.LEFT) {
                    if (redKits.get(KitType.MID_FIELD) < Main.getInstance().getSettings().getMidFeildKit()) {
                        if (Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit() != null) {
                            if (Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit() == KitType.MID_FIELD) {
                                ev.getWhoClicked().sendMessage(ChatColor.RED + "You have already selected this kit!");
                                ev.getWhoClicked().closeInventory();
                                Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.ENTITY_BLAZE_HURT, 5, 1);
                                ev.setCancelled(true);
                                return;
                            }
                            redKits.put(Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit(),
                                    redKits.get(Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit()) - 1);
                        }
                        Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).setKit(KitType.MID_FIELD);
                        ev.getWhoClicked().closeInventory();
                        Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10, 1);
                        ev.getWhoClicked().sendMessage(ChatColor.GREEN + "You have selected the Mid-field Kit!");
                        redKits.put(KitType.MID_FIELD, redKits.get(KitType.MID_FIELD) + 1);
                        updateInvs();
                    } else {
                        ev.getWhoClicked().closeInventory();
                        Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.ENTITY_BLAZE_HURT, 5, 1);
                        ev.getWhoClicked().sendMessage(ChatColor.RED + "Too many people have already selected the Mid-field Kit!");
                    }
                }
            } else if (ev.getCurrentItem().getType() == Material.SHIELD) {
                if (ev.getClick() == ClickType.RIGHT) {
                    ev.getWhoClicked().openInventory(DEFENSE);
                    Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10, 1);
                } else if (ev.getClick() == ClickType.LEFT) {
                    if (redKits.get(KitType.DEFENSE) < Main.getInstance().getSettings().getDefenseKit()) {
                        if (Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit() != null) {
                            if (Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit() == KitType.DEFENSE) {
                                ev.getWhoClicked().sendMessage(ChatColor.RED + "You have already selected this kit!");
                                ev.getWhoClicked().closeInventory();
                                Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.ENTITY_BLAZE_HURT, 5, 1);
                                ev.setCancelled(true);
                                return;
                            }
                            redKits.put(Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit(),
                                    redKits.get(Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit()) - 1);
                        }
                        Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).setKit(KitType.DEFENSE);
                        ev.getWhoClicked().closeInventory();
                        Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10, 1);
                        ev.getWhoClicked().sendMessage(ChatColor.GREEN + "You have selected the Defense Kit!");
                        redKits.put(KitType.DEFENSE, redKits.get(KitType.DEFENSE) + 1);
                        updateInvs();
                    } else {
                        ev.getWhoClicked().closeInventory();
                        Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.ENTITY_BLAZE_HURT, 5, 1);
                        ev.getWhoClicked().sendMessage(ChatColor.RED + "Too many people have already selected the Defense Kit!");
                    }
                }
            } else if (ev.getCurrentItem().getType() == Material.BOW) {
                if (ev.getClick() == ClickType.RIGHT) {
                    ev.getWhoClicked().openInventory(BOW);
                    Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10, 1);
                } else if (ev.getClick() == ClickType.LEFT) {
                    if (redKits.get(KitType.BOW) < Main.getInstance().getSettings().getBowKit()) {
                        if (Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit() != null) {
                            if (Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit() == KitType.BOW) {
                                ev.getWhoClicked().sendMessage(ChatColor.RED + "You have already selected this kit!");
                                ev.getWhoClicked().closeInventory();
                                Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.ENTITY_BLAZE_HURT, 5, 1);
                                ev.setCancelled(true);
                                return;
                            }
                            redKits.put(Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit(),
                                    redKits.get(Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit()) - 1);
                        }
                        Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).setKit(KitType.BOW);
                        ev.getWhoClicked().closeInventory();
                        Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10, 1);
                        ev.getWhoClicked().sendMessage(ChatColor.GREEN + "You have selected the Long-range Kit!");
                        redKits.put(KitType.BOW, redKits.get(KitType.BOW) + 1);
                        updateInvs();
                    } else {
                        ev.getWhoClicked().closeInventory();
                        Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.ENTITY_BLAZE_HURT, 5, 1);
                        ev.getWhoClicked().sendMessage(ChatColor.RED + "Too many people have already selected the Long-range Kit!");
                    }
                }
            } else if (ev.getCurrentItem().getType() == Material.IRON_SWORD) {
                if (ev.getClick() == ClickType.RIGHT) {
                    ev.getWhoClicked().openInventory(FLAG_STEALER);
                    Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10, 1);
                } else if (ev.getClick() == ClickType.LEFT) {
                    if (redKits.get(KitType.FLAG_STEALER) < Main.getInstance().getSettings().getFlagStealerKit()) {
                        if (Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit() != null) {
                            if (Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit() == KitType.FLAG_STEALER) {
                                ev.getWhoClicked().sendMessage(ChatColor.RED + "You have already selected this kit!");
                                ev.getWhoClicked().closeInventory();
                                Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.ENTITY_BLAZE_HURT, 5, 1);
                                ev.setCancelled(true);
                                return;
                            }
                            redKits.put(Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit(),
                                    redKits.get(Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit()) - 1);
                        }
                        Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).setKit(KitType.FLAG_STEALER);
                        ev.getWhoClicked().closeInventory();
                        Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10, 1);
                        ev.getWhoClicked().sendMessage(ChatColor.GREEN + "You have selected the Flag-stealer Kit!");
                        redKits.put(KitType.FLAG_STEALER, redKits.get(KitType.FLAG_STEALER) + 1);
                        updateInvs();
                    } else {
                        ev.getWhoClicked().closeInventory();
                        Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.ENTITY_BLAZE_HURT, 5, 1);
                        ev.getWhoClicked().sendMessage(ChatColor.RED + "Too many people have already selected the Flag-stealer Kit!");
                    }
                }
            }
            ev.setCancelled(true);
        } else if (ev.getView().getTitle().equals(ChatColor.BLUE + "Blue Team Kit Selection")) {
            if (ev.getCurrentItem().getType() == Material.BARRIER) {
                ev.getWhoClicked().closeInventory();
                ev.setCancelled(true);
                return;
            }
            if (ev.getCurrentItem().getType() == Material.IRON_AXE) {
                if (ev.getClick() == ClickType.RIGHT) {
                    ev.getWhoClicked().openInventory(MID_FIELD);
                    Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10, 1);
                } else if (ev.getClick() == ClickType.LEFT) {
                    if (blueKits.get(KitType.MID_FIELD) < Main.getInstance().getSettings().getMidFeildKit()) {
                        if (Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit() != null) {
                            if (Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit() == KitType.MID_FIELD) {
                                ev.getWhoClicked().sendMessage(ChatColor.RED + "You have already selected this kit!");
                                ev.getWhoClicked().closeInventory();
                                Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.ENTITY_BLAZE_HURT, 5, 1);
                                ev.setCancelled(true);
                                return;
                            }
                            blueKits.put(Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit(),
                                    blueKits.get(Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit()) - 1);
                        }
                        Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).setKit(KitType.MID_FIELD);
                        ev.getWhoClicked().closeInventory();
                        Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10, 1);
                        ev.getWhoClicked().sendMessage(ChatColor.GREEN + "You have selected the Mid-field Kit!");
                        blueKits.put(KitType.MID_FIELD, blueKits.get(KitType.MID_FIELD) + 1);
                        updateInvs();
                    } else {
                        ev.getWhoClicked().closeInventory();
                        Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.ENTITY_BLAZE_HURT, 5, 1);
                        ev.getWhoClicked().sendMessage(ChatColor.RED + "Too many people have already selected the Mid-field Kit!");
                    }
                }
            } else if (ev.getCurrentItem().getType() == Material.SHIELD) {
                if (ev.getClick() == ClickType.RIGHT) {
                    ev.getWhoClicked().openInventory(DEFENSE);
                    Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10, 1);
                } else if (ev.getClick() == ClickType.LEFT) {
                    if (blueKits.get(KitType.DEFENSE) < Main.getInstance().getSettings().getDefenseKit()) {
                        if (Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit() != null) {
                            if (Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit() == KitType.DEFENSE) {
                                ev.getWhoClicked().sendMessage(ChatColor.RED + "You have already selected this kit!");
                                ev.getWhoClicked().closeInventory();
                                Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.ENTITY_BLAZE_HURT, 5, 1);
                                ev.setCancelled(true);
                                return;
                            }
                            blueKits.put(Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit(),
                                    blueKits.get(Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit()) - 1);
                        }
                        Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).setKit(KitType.DEFENSE);
                        ev.getWhoClicked().closeInventory();
                        Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10, 1);
                        ev.getWhoClicked().sendMessage(ChatColor.GREEN + "You have selected the Defense Kit!");
                        blueKits.put(KitType.DEFENSE, blueKits.get(KitType.DEFENSE) + 1);
                        updateInvs();
                    } else {
                        ev.getWhoClicked().closeInventory();
                        Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.ENTITY_BLAZE_HURT, 5, 1);
                        ev.getWhoClicked().sendMessage(ChatColor.RED + "Too many people have already selected the Defense Kit!");
                    }
                }
            } else if (ev.getCurrentItem().getType() == Material.BOW) {
                if (ev.getClick() == ClickType.RIGHT) {
                    ev.getWhoClicked().openInventory(BOW);
                    Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10, 1);
                } else if (ev.getClick() == ClickType.LEFT) {
                    if (blueKits.get(KitType.BOW) < Main.getInstance().getSettings().getBowKit()) {
                        if (Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit() != null) {
                            if (Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit() == KitType.BOW) {
                                ev.getWhoClicked().sendMessage(ChatColor.RED + "You have already selected this kit!");
                                ev.getWhoClicked().closeInventory();
                                Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.ENTITY_BLAZE_HURT, 5, 1);
                                ev.setCancelled(true);
                                return;
                            }
                            blueKits.put(Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit(),
                                    blueKits.get(Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit()) - 1);
                        }
                        Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).setKit(KitType.BOW);
                        ev.getWhoClicked().closeInventory();
                        Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10, 1);
                        ev.getWhoClicked().sendMessage(ChatColor.GREEN + "You have selected the Long-range Kit!");
                        blueKits.put(KitType.BOW, blueKits.get(KitType.BOW) + 1);
                        updateInvs();
                    } else {
                        ev.getWhoClicked().closeInventory();
                        Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.ENTITY_BLAZE_HURT, 5, 1);
                        ev.getWhoClicked().sendMessage(ChatColor.RED + "Too many people have already selected the Long-range Kit!");
                    }
                }
            } else if (ev.getCurrentItem().getType() == Material.IRON_SWORD) {
                if (ev.getClick() == ClickType.RIGHT) {
                    ev.getWhoClicked().openInventory(FLAG_STEALER);
                    Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10, 1);
                } else if (ev.getClick() == ClickType.LEFT) {
                    if (blueKits.get(KitType.FLAG_STEALER) < Main.getInstance().getSettings().getFlagStealerKit()) {
                        if (Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit() != null) {
                            if (Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit() == KitType.FLAG_STEALER) {
                                ev.getWhoClicked().sendMessage(ChatColor.RED + "You have already selected this kit!");
                                ev.getWhoClicked().closeInventory();
                                Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.ENTITY_BLAZE_HURT, 5, 1);
                                ev.setCancelled(true);
                                return;
                            }
                            blueKits.put(Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit(),
                                    blueKits.get(Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit()) - 1);
                        }
                        Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).setKit(KitType.FLAG_STEALER);
                        ev.getWhoClicked().closeInventory();
                        Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10, 1);
                        ev.getWhoClicked().sendMessage(ChatColor.GREEN + "You have selected the Flag-stealer Kit!");
                        blueKits.put(KitType.FLAG_STEALER, blueKits.get(KitType.FLAG_STEALER) + 1);
                        updateInvs();
                    } else {
                        ev.getWhoClicked().closeInventory();
                        Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.ENTITY_BLAZE_HURT, 5, 1);
                        ev.getWhoClicked().sendMessage(ChatColor.RED + "Too many people have already selected the Flag-stealer Kit!");
                    }
                }
            }
            ev.setCancelled(true);
        } else if (ev.getView().getTitle().equals(ChatColor.GREEN + "Green Team Kit Selection")) {
            if (ev.getCurrentItem().getType() == Material.BARRIER) {
                ev.getWhoClicked().closeInventory();
                ev.setCancelled(true);
                return;
            }
            if (ev.getCurrentItem().getType() == Material.IRON_AXE) {
                if (ev.getClick() == ClickType.RIGHT) {
                    ev.getWhoClicked().openInventory(MID_FIELD);
                    Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10, 1);
                } else if (ev.getClick() == ClickType.LEFT) {
                    if (greenKits.get(KitType.MID_FIELD) < Main.getInstance().getSettings().getMidFeildKit()) {
                        if (Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit() != null) {
                            if (Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit() == KitType.MID_FIELD) {
                                ev.getWhoClicked().sendMessage(ChatColor.RED + "You have already selected this kit!");
                                ev.getWhoClicked().closeInventory();
                                Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.ENTITY_BLAZE_HURT, 5, 1);
                                ev.setCancelled(true);
                                return;
                            }
                            greenKits.put(Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit(),
                                    greenKits.get(Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit()) - 1);
                        }
                        Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).setKit(KitType.MID_FIELD);
                        ev.getWhoClicked().closeInventory();
                        Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10, 1);
                        ev.getWhoClicked().sendMessage(ChatColor.GREEN + "You have selected the Mid-field Kit!");
                        greenKits.put(KitType.MID_FIELD, greenKits.get(KitType.MID_FIELD) + 1);
                        updateInvs();
                    } else {
                        ev.getWhoClicked().closeInventory();
                        Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.ENTITY_BLAZE_HURT, 5, 1);
                        ev.getWhoClicked().sendMessage(ChatColor.RED + "Too many people have already selected the Mid-field Kit!");
                    }
                }
            } else if (ev.getCurrentItem().getType() == Material.SHIELD) {
                if (ev.getClick() == ClickType.RIGHT) {
                    ev.getWhoClicked().openInventory(DEFENSE);
                    Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10, 1);
                } else if (ev.getClick() == ClickType.LEFT) {
                    if (greenKits.get(KitType.DEFENSE) < Main.getInstance().getSettings().getDefenseKit()) {
                        if (Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit() != null) {
                            if (Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit() == KitType.DEFENSE) {
                                ev.getWhoClicked().sendMessage(ChatColor.RED + "You have already selected this kit!");
                                ev.getWhoClicked().closeInventory();
                                Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.ENTITY_BLAZE_HURT, 5, 1);
                                ev.setCancelled(true);
                                return;
                            }
                            greenKits.put(Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit(),
                                    greenKits.get(Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit()) - 1);
                        }
                        Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).setKit(KitType.DEFENSE);
                        ev.getWhoClicked().closeInventory();
                        Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10, 1);
                        ev.getWhoClicked().sendMessage(ChatColor.GREEN + "You have selected the Defense Kit!");
                        greenKits.put(KitType.DEFENSE, greenKits.get(KitType.DEFENSE) + 1);
                        updateInvs();
                    } else {
                        ev.getWhoClicked().closeInventory();
                        Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.ENTITY_BLAZE_HURT, 5, 1);
                        ev.getWhoClicked().sendMessage(ChatColor.RED + "Too many people have already selected the Defense Kit!");
                    }
                }
            } else if (ev.getCurrentItem().getType() == Material.BOW) {
                if (ev.getClick() == ClickType.RIGHT) {
                    ev.getWhoClicked().openInventory(BOW);
                    Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10, 1);
                } else if (ev.getClick() == ClickType.LEFT) {
                    if (greenKits.get(KitType.BOW) < Main.getInstance().getSettings().getBowKit()) {
                        if (Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit() != null) {
                            if (Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit() == KitType.BOW) {
                                ev.getWhoClicked().sendMessage(ChatColor.RED + "You have already selected this kit!");
                                ev.getWhoClicked().closeInventory();
                                Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.ENTITY_BLAZE_HURT, 5, 1);
                                ev.setCancelled(true);
                                return;
                            }
                            greenKits.put(Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit(),
                                    greenKits.get(Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit()) - 1);
                        }
                        Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).setKit(KitType.BOW);
                        ev.getWhoClicked().closeInventory();
                        Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10, 1);
                        ev.getWhoClicked().sendMessage(ChatColor.GREEN + "You have selected the Long-range Kit!");
                        greenKits.put(KitType.BOW, greenKits.get(KitType.BOW) + 1);
                        updateInvs();
                    } else {
                        ev.getWhoClicked().closeInventory();
                        Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.ENTITY_BLAZE_HURT, 5, 1);
                        ev.getWhoClicked().sendMessage(ChatColor.RED + "Too many people have already selected the Long-range Kit!");
                    }
                }
            } else if (ev.getCurrentItem().getType() == Material.IRON_SWORD) {
                if (ev.getClick() == ClickType.RIGHT) {
                    ev.getWhoClicked().openInventory(FLAG_STEALER);
                    Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10, 1);
                } else if (ev.getClick() == ClickType.LEFT) {
                    if (greenKits.get(KitType.FLAG_STEALER) < Main.getInstance().getSettings().getFlagStealerKit()) {
                        if (Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit() != null) {
                            if (Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit() == KitType.FLAG_STEALER) {
                                ev.getWhoClicked().sendMessage(ChatColor.RED + "You have already selected this kit!");
                                ev.getWhoClicked().closeInventory();
                                Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.ENTITY_BLAZE_HURT, 5, 1);
                                ev.setCancelled(true);
                                return;
                            }
                            greenKits.put(Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit(),
                                    greenKits.get(Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit()) - 1);
                        }
                        Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).setKit(KitType.FLAG_STEALER);
                        ev.getWhoClicked().closeInventory();
                        Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10, 1);
                        ev.getWhoClicked().sendMessage(ChatColor.GREEN + "You have selected the Flag-stealer Kit!");
                        greenKits.put(KitType.FLAG_STEALER, greenKits.get(KitType.FLAG_STEALER) + 1);
                        updateInvs();
                    } else {
                        ev.getWhoClicked().closeInventory();
                        Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.ENTITY_BLAZE_HURT, 5, 1);
                        ev.getWhoClicked().sendMessage(ChatColor.RED + "Too many people have already selected the Flag-stealer Kit!");
                    }
                }
            }
            ev.setCancelled(true);
        } else if (ev.getView().getTitle().equals(ChatColor.YELLOW + "Yellow Team Kit Selection")) {
            if (ev.getCurrentItem().getType() == Material.BARRIER) {
                ev.getWhoClicked().closeInventory();
                ev.setCancelled(true);
                return;
            }
            if (ev.getCurrentItem().getType() == Material.IRON_AXE) {
                if (ev.getClick() == ClickType.RIGHT) {
                    ev.getWhoClicked().openInventory(MID_FIELD);
                    Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10, 1);
                } else if (ev.getClick() == ClickType.LEFT) {
                    if (yellowKits.get(KitType.MID_FIELD) < Main.getInstance().getSettings().getMidFeildKit()) {
                        if (Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit() != null) {
                            if (Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit() == KitType.MID_FIELD) {
                                ev.getWhoClicked().sendMessage(ChatColor.RED + "You have already selected this kit!");
                                ev.getWhoClicked().closeInventory();
                                Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.ENTITY_BLAZE_HURT, 5, 1);
                                ev.setCancelled(true);
                                return;
                            }
                            yellowKits.put(Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit(),
                                    yellowKits.get(Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit()) - 1);
                        }
                        Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).setKit(KitType.MID_FIELD);
                        ev.getWhoClicked().closeInventory();
                        Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10, 1);
                        ev.getWhoClicked().sendMessage(ChatColor.GREEN + "You have selected the Mid-field Kit!");
                        yellowKits.put(KitType.MID_FIELD, yellowKits.get(KitType.MID_FIELD) + 1);
                        updateInvs();
                    } else {
                        ev.getWhoClicked().closeInventory();
                        Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.ENTITY_BLAZE_HURT, 5, 1);
                        ev.getWhoClicked().sendMessage(ChatColor.RED + "Too many people have already selected the Mid-field Kit!");
                    }
                }
            } else if (ev.getCurrentItem().getType() == Material.SHIELD) {
                if (ev.getClick() == ClickType.RIGHT) {
                    ev.getWhoClicked().openInventory(DEFENSE);
                    Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10, 1);
                } else if (ev.getClick() == ClickType.LEFT) {
                    if (yellowKits.get(KitType.DEFENSE) < Main.getInstance().getSettings().getDefenseKit()) {
                        if (Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit() != null) {
                            if (Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit() == KitType.DEFENSE) {
                                ev.getWhoClicked().sendMessage(ChatColor.RED + "You have already selected this kit!");
                                ev.getWhoClicked().closeInventory();
                                Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.ENTITY_BLAZE_HURT, 5, 1);
                                ev.setCancelled(true);
                                return;
                            }
                            yellowKits.put(Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit(),
                                    yellowKits.get(Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit()) - 1);
                        }
                        Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).setKit(KitType.DEFENSE);
                        ev.getWhoClicked().closeInventory();
                        Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10, 1);
                        ev.getWhoClicked().sendMessage(ChatColor.GREEN + "You have selected the Defense Kit!");
                        yellowKits.put(KitType.DEFENSE, yellowKits.get(KitType.DEFENSE) + 1);
                        updateInvs();
                    } else {
                        ev.getWhoClicked().closeInventory();
                        Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.ENTITY_BLAZE_HURT, 5, 1);
                        ev.getWhoClicked().sendMessage(ChatColor.RED + "Too many people have already selected the Defense Kit!");
                    }
                }
            } else if (ev.getCurrentItem().getType() == Material.BOW) {
                if (ev.getClick() == ClickType.RIGHT) {
                    ev.getWhoClicked().openInventory(BOW);
                    Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10, 1);
                } else if (ev.getClick() == ClickType.LEFT) {
                    if (yellowKits.get(KitType.BOW) < Main.getInstance().getSettings().getBowKit()) {
                        if (Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit() != null) {
                            if (Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit() == KitType.BOW) {
                                ev.getWhoClicked().sendMessage(ChatColor.RED + "You have already selected this kit!");
                                ev.getWhoClicked().closeInventory();
                                Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.ENTITY_BLAZE_HURT, 5, 1);
                                ev.setCancelled(true);
                                return;
                            }
                            yellowKits.put(Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit(),
                                    yellowKits.get(Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit()) - 1);
                        }
                        Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).setKit(KitType.BOW);
                        ev.getWhoClicked().closeInventory();
                        Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10, 1);
                        ev.getWhoClicked().sendMessage(ChatColor.GREEN + "You have selected the Long-range Kit!");
                        yellowKits.put(KitType.BOW, yellowKits.get(KitType.BOW) + 1);
                        updateInvs();
                    } else {
                        ev.getWhoClicked().closeInventory();
                        Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.ENTITY_BLAZE_HURT, 5, 1);
                        ev.getWhoClicked().sendMessage(ChatColor.RED + "Too many people have already selected the Long-range Kit!");
                    }
                }
            } else if (ev.getCurrentItem().getType() == Material.IRON_SWORD) {
                if (ev.getClick() == ClickType.RIGHT) {
                    ev.getWhoClicked().openInventory(FLAG_STEALER);
                    Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10, 1);
                } else if (ev.getClick() == ClickType.LEFT) {
                    if (yellowKits.get(KitType.FLAG_STEALER) < Main.getInstance().getSettings().getFlagStealerKit()) {
                        if (Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit() != null) {
                            if (Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit() == KitType.FLAG_STEALER) {
                                ev.getWhoClicked().sendMessage(ChatColor.RED + "You have already selected this kit!");
                                ev.getWhoClicked().closeInventory();
                                Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.ENTITY_BLAZE_HURT, 5, 1);
                                ev.setCancelled(true);
                                return;
                            }
                            yellowKits.put(Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit(),
                                    yellowKits.get(Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).getKit()) - 1);
                        }
                        Main.getInstance().getPlayers().get(ev.getWhoClicked().getUniqueId()).setKit(KitType.FLAG_STEALER);
                        ev.getWhoClicked().closeInventory();
                        Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10, 1);
                        ev.getWhoClicked().sendMessage(ChatColor.GREEN + "You have selected the Flag-stealer Kit!");
                        yellowKits.put(KitType.FLAG_STEALER, yellowKits.get(KitType.FLAG_STEALER) + 1);
                        updateInvs();
                    } else {
                        ev.getWhoClicked().closeInventory();
                        Bukkit.getPlayer(ev.getWhoClicked().getUniqueId()).playSound(ev.getWhoClicked().getLocation(), Sound.ENTITY_BLAZE_HURT, 5, 1);
                        ev.getWhoClicked().sendMessage(ChatColor.RED + "Too many people have already selected the Flag-stealer Kit!");
                    }
                }
            }
            ev.setCancelled(true);
        } else if (ev.getView().getTitle().equals(ChatColor.YELLOW + "Mid-range Kit")
                || ev.getView().getTitle().equals(ChatColor.YELLOW + "Defense Kit")
                || ev.getView().getTitle().equals(ChatColor.YELLOW + "Long-range Kit")
                || ev.getView().getTitle().equals(ChatColor.YELLOW + "Flag-stealer Kit")) {
            ev.setCancelled(true);
        } else if (ev.getSlotType() == InventoryType.SlotType.ARMOR) {
            if (!ev.getWhoClicked().isOp()) {
                ev.setCancelled(true);
            }
        }
    }

}
