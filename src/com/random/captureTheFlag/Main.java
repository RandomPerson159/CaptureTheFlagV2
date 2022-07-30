package com.random.captureTheFlag;

import com.random.captureTheFlag.commands.*;
import com.random.captureTheFlag.game.Flag;
import com.random.captureTheFlag.game.FlagEvent;
import com.random.captureTheFlag.game.GameState;
import com.random.captureTheFlag.game.Settings;
import com.random.captureTheFlag.listeners.*;
import com.random.captureTheFlag.player.CapturePlayer;
import com.random.captureTheFlag.player.KitType;
import com.random.captureTheFlag.player.Team;
import com.random.captureTheFlag.util.ItemBuilder;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main extends JavaPlugin {
    private static Main instance;
    private Map<UUID, CapturePlayer> players = new HashMap<>();
    private GameState state = GameState.WAIT;
    private final Set<Flag> flags = new HashSet<>();
    private Settings settings;
    public Location wait;

    public Main() {
        instance = this;
    }

    @Override
    public void onEnable() {
        new File("./plugins/CaptureTheFlag").mkdirs();
        if (!(new File("./plugins/CaptureTheFlag/config.yml").exists())) {
            try {
                new File("./plugins/CaptureTheFlag/config.yml").createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!(new File("./plugins/CaptureTheFlag/stats.yml").exists())) {
            try {
                new File("./plugins/CaptureTheFlag/stats.yml").createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!(new File("./plugins/CaptureTheFlag/banned_words.yml").exists())) {
            try {
                new File("./plugins/CaptureTheFlag/banned_words.yml").createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!(new File("./plugins/CaptureTheFlag/settings.yml").exists())) {
            try {
                new File("./plugins/CaptureTheFlag/settings.yml").createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            settings = new Settings(8, true, true, 2, 1, 1, 1, 1, 2, 10);
        } else {
            settings = new Settings();
        }

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new ChatListener(), this);
        pm.registerEvents(new FlagListener(), this);
        pm.registerEvents(new InvClickListener(), this);
        pm.registerEvents(new JoinListener(), this);
        pm.registerEvents(new PermissionsListener(), this);

        Bukkit.getPluginCommand("teams").setExecutor(new AssignCommand());
        Bukkit.getPluginCommand("ctfhelp").setExecutor(new HelpCommand());
        Bukkit.getPluginCommand("setup").setExecutor(new SetupCommand());
        Bukkit.getPluginCommand("shout").setExecutor(new ShoutCommand());
        Bukkit.getPluginCommand("start").setExecutor(new StartCommand());
        Bukkit.getPluginCommand("settings").setExecutor(new SettingsCommand());

        initFlags();
        initTeams();
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public Location getWait() {
        return wait;
    }

    public Map<UUID, CapturePlayer> getPlayers() {
        return players;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public Set<Flag> getFlags() {
        return flags;
    }

    public void initFlags() {
        for (Flag flag : flags) {
            flag.take(null);
        }
        flags.clear();
        if (settings.getFlags() == 2) {
            if (settings.getTeams() == 2) {
                flags.add(new Flag(Team.RED, getLocation("2teams.2flags.redFlag1")));
                flags.add(new Flag(Team.RED, getLocation("2teams.2flags.redFlag2")));
                flags.add(new Flag(Team.BLUE, getLocation("2teams.2flags.blueFlag1")));
                flags.add(new Flag(Team.BLUE, getLocation("2teams.2flags.blueFlag2")));
            } else {
                flags.add(new Flag(Team.RED, getLocation("4teams.2flags.redFlag1")));
                flags.add(new Flag(Team.RED, getLocation("4teams.2flags.redFlag2")));
                flags.add(new Flag(Team.BLUE, getLocation("4teams.2flags.blueFlag1")));
                flags.add(new Flag(Team.BLUE, getLocation("4teams.2flags.blueFlag2")));
                flags.add(new Flag(Team.LIME, getLocation("4teams.2flags.greenFlag1")));
                flags.add(new Flag(Team.LIME, getLocation("4teams.2flags.greenFlag2")));
                flags.add(new Flag(Team.YELLOW, getLocation("4teams.2flags.yellowFlag1")));
                flags.add(new Flag(Team.YELLOW, getLocation("4teams.2flags.yellowFlag2")));
            }
        } else {
            if (settings.getTeams() == 2) {
                flags.add(new Flag(Team.RED, getLocation("2teams.redFlag")));
                flags.add(new Flag(Team.BLUE, getLocation("2teams.blueFlag")));
            } else {
                flags.add(new Flag(Team.RED, getLocation("4teams.redFlag")));
                flags.add(new Flag(Team.BLUE, getLocation("4teams.blueFlag")));
                flags.add(new Flag(Team.LIME, getLocation("4teams.greenFlag")));
                flags.add(new Flag(Team.YELLOW, getLocation("4teams.yellowFlag")));
            }
        }

        for(Flag flag : flags) {
            flag.put(FlagEvent.RESET, null);
        }
    }

    public void initTeams() {
        if (settings.getTeams() == 2) {
            if (settings.getFlags() == 2) {
                Team.RED.setSpawn(getLocation("2teams.2flags.redSpawn"));
                Team.BLUE.setSpawn(getLocation("2teams.2flags.blueSpawn"));
                Team.LIME.setSpawn(getLocation("2teams.2flags.greenSpawn"));
                Team.YELLOW.setSpawn(getLocation("2teams.2flags.yellowSpawn"));
                wait = getLocation("2teams.2flags.wait");
            } else {
                Team.RED.setSpawn(getLocation("2teams.redSpawn"));
                Team.BLUE.setSpawn(getLocation("2teams.blueSpawn"));
                Team.LIME.setSpawn(getLocation("2teams.greenSpawn"));
                Team.YELLOW.setSpawn(getLocation("2teams.yellowSpawn"));
                wait = getLocation("2teams.wait");
            }
        } else {
            if (settings.getFlags() == 2) {
                Team.RED.setSpawn(getLocation("4teams.2flags.redSpawn"));
                Team.BLUE.setSpawn(getLocation("4teams.2flags.blueSpawn"));
                Team.LIME.setSpawn(getLocation("4teams.2flags.greenSpawn"));
                Team.YELLOW.setSpawn(getLocation("4teams.2flags.yellowSpawn"));
                wait = getLocation("4teams.2flags.wait");
            } else {
                Team.RED.setSpawn(getLocation("4teams.redSpawn"));
                Team.BLUE.setSpawn(getLocation("4teams.blueSpawn"));
                Team.LIME.setSpawn(getLocation("4teams.greenSpawn"));
                Team.YELLOW.setSpawn(getLocation("4teams.yellowSpawn"));
                wait = getLocation("4teams.wait");
            }
        }
    }

    public KitType getRandomKit(Team team) {
        if (team == Team.RED) {
            if (InvClickListener.redKits.get(KitType.MID_FIELD) != Main.getInstance().getSettings().getMidFeildKit()) {
                InvClickListener.redKits.put(KitType.MID_FIELD, InvClickListener.redKits.get(KitType.MID_FIELD) + 1);
                return KitType.MID_FIELD;
            } else if (InvClickListener.redKits.get(KitType.DEFENSE) != Main.getInstance().getSettings().getDefenseKit()) {
                InvClickListener.redKits.put(KitType.DEFENSE, InvClickListener.redKits.get(KitType.DEFENSE) + 1);
                return KitType.DEFENSE;
            } else if (InvClickListener.redKits.get(KitType.BOW) != Main.getInstance().getSettings().getBowKit()) {
                InvClickListener.redKits.put(KitType.BOW, InvClickListener.redKits.get(KitType.BOW) + 1);
                return KitType.BOW;
            } else if (InvClickListener.redKits.get(KitType.FLAG_STEALER) != Main.getInstance().getSettings().getFlagStealerKit()) {
                InvClickListener.redKits.put(KitType.FLAG_STEALER, InvClickListener.redKits.get(KitType.FLAG_STEALER) + 1);
                return KitType.FLAG_STEALER;
            }
        } else if (team == Team.BLUE) {
            if (InvClickListener.blueKits.get(KitType.MID_FIELD) != Main.getInstance().getSettings().getMidFeildKit()) {
                InvClickListener.blueKits.put(KitType.MID_FIELD, InvClickListener.blueKits.get(KitType.MID_FIELD) + 1);
                return KitType.MID_FIELD;
            } else if (InvClickListener.blueKits.get(KitType.DEFENSE) != Main.getInstance().getSettings().getDefenseKit()) {
                InvClickListener.blueKits.put(KitType.DEFENSE, InvClickListener.blueKits.get(KitType.DEFENSE) + 1);
                return KitType.DEFENSE;
            } else if (InvClickListener.blueKits.get(KitType.BOW) != Main.getInstance().getSettings().getBowKit()) {
                InvClickListener.blueKits.put(KitType.BOW, InvClickListener.blueKits.get(KitType.BOW) + 1);
                return KitType.BOW;
            } else if (InvClickListener.blueKits.get(KitType.FLAG_STEALER) != Main.getInstance().getSettings().getFlagStealerKit()) {
                InvClickListener.blueKits.put(KitType.FLAG_STEALER, InvClickListener.blueKits.get(KitType.FLAG_STEALER) + 1);
                return KitType.FLAG_STEALER;
            }
        } else if (team == Team.LIME) {
            if (InvClickListener.greenKits.get(KitType.MID_FIELD) != Main.getInstance().getSettings().getMidFeildKit()) {
                InvClickListener.greenKits.put(KitType.MID_FIELD, InvClickListener.greenKits.get(KitType.MID_FIELD) + 1);
                return KitType.MID_FIELD;
            } else if (InvClickListener.greenKits.get(KitType.DEFENSE) != Main.getInstance().getSettings().getDefenseKit()) {
                InvClickListener.greenKits.put(KitType.DEFENSE, InvClickListener.greenKits.get(KitType.DEFENSE) + 1);
                return KitType.DEFENSE;
            } else if (InvClickListener.greenKits.get(KitType.BOW) != Main.getInstance().getSettings().getBowKit()) {
                InvClickListener.greenKits.put(KitType.BOW, InvClickListener.greenKits.get(KitType.BOW) + 1);
                return KitType.BOW;
            } else if (InvClickListener.greenKits.get(KitType.FLAG_STEALER) != Main.getInstance().getSettings().getFlagStealerKit()) {
                InvClickListener.greenKits.put(KitType.FLAG_STEALER, InvClickListener.greenKits.get(KitType.FLAG_STEALER) + 1);
                return KitType.FLAG_STEALER;
            }
        }
        if (InvClickListener.yellowKits.get(KitType.MID_FIELD) != Main.getInstance().getSettings().getMidFeildKit()) {
            InvClickListener.yellowKits.put(KitType.MID_FIELD, InvClickListener.yellowKits.get(KitType.MID_FIELD) + 1);
            return KitType.MID_FIELD;
        } else if (InvClickListener.yellowKits.get(KitType.DEFENSE) != Main.getInstance().getSettings().getDefenseKit()) {
            InvClickListener.yellowKits.put(KitType.DEFENSE, InvClickListener.yellowKits.get(KitType.DEFENSE) + 1);
            return KitType.DEFENSE;
        } else if (InvClickListener.yellowKits.get(KitType.BOW) != Main.getInstance().getSettings().getBowKit()) {
            InvClickListener.yellowKits.put(KitType.BOW, InvClickListener.yellowKits.get(KitType.BOW) + 1);
            return KitType.BOW;
        }
        InvClickListener.yellowKits.put(KitType.FLAG_STEALER, InvClickListener.yellowKits.get(KitType.FLAG_STEALER) + 1);
        return KitType.FLAG_STEALER;
    }

    public void start() {
        int red = 0;
        int blue = 0;
        int green = 0;
        int yellow = 0;

        for (Player all : Bukkit.getOnlinePlayers()) {
            CapturePlayer cp = Main.getInstance().getPlayers().get(all.getUniqueId());
            if (cp != null) {
                if (cp.getTeam() != null && cp.getTeam() != Team.SPEC) {
                    if (Main.getInstance().getSettings().getTeams() == 2) {
                        if (cp.getTeam() == Team.RED) {
                            red++;
                        } else if (cp.getTeam() == Team.BLUE) {
                            blue++;
                        }
                    } else {
                        if (cp.getTeam() == Team.RED) {
                            red++;
                        } else if (cp.getTeam() == Team.BLUE) {
                            blue++;
                        } else if (cp.getTeam() == Team.LIME) {
                            green++;
                        } else if (cp.getTeam() == Team.YELLOW) {
                            yellow++;
                        }
                    }
                }
            }
        }

        for (Player all : Bukkit.getOnlinePlayers()) {
            all.teleport(Main.getInstance().getWait());
            CapturePlayer cp = Main.getInstance().getPlayers().get(all.getUniqueId());
            all.sendMessage(ChatColor.GRAY + "[====================================================]\n"
                    + ChatColor.GOLD + "                   Capture the Flag\n"
                    + ChatColor.GRAY + "     Welcome to Capture the Flag! The game is simple: capture opponent flags! Each team has " + Main.getInstance().getSettings().getFlags()
                    + ChatColor.GRAY + (Main.getInstance().getSettings().getFlags() == 1 ? " flag. " : " flags. ") + "In order to win, you must capture all opponent flags, without losing "
                    + (Main.getInstance().getSettings().getFlags() == 2 ? "both your flags... " : "your flag... ") + ChatColor.GREEN + "\n Have fun!\n"
                    + ChatColor.GRAY + "[====================================================]");
            all.playSound(all.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 7, 1);

            if (cp.getTeam() == null) {
                if (red < Main.getInstance().getSettings().getPlayers() / Main.getInstance().getSettings().getTeams()) {
                    cp.setTeam(Team.RED);
                    red++;
                } else if (blue < Main.getInstance().getSettings().getPlayers() / Main.getInstance().getSettings().getTeams()) {
                    cp.setTeam(Team.BLUE);
                    blue++;
                } else if (green < Main.getInstance().getSettings().getPlayers() / Main.getInstance().getSettings().getTeams()) {
                    cp.setTeam(Team.LIME);
                    green++;
                } else if (yellow < Main.getInstance().getSettings().getPlayers() / Main.getInstance().getSettings().getTeams()) {
                    cp.setTeam(Team.YELLOW);
                    yellow++;
                } else {
                    cp.setTeam(Team.SPEC);
                }
            }

            if (Main.getInstance().getSettings().getTeams() == 2) {
                if (cp.getTeam() == Team.LIME || cp.getTeam() == Team.YELLOW) {
                    if (red < Main.getInstance().getSettings().getPlayers() / Main.getInstance().getSettings().getTeams()) {
                        cp.setTeam(Team.RED);
                        red++;
                    } else if (blue < Main.getInstance().getSettings().getPlayers() / Main.getInstance().getSettings().getTeams()) {
                        cp.setTeam(Team.BLUE);
                        blue++;
                    } else {
                        cp.setTeam(Team.SPEC);
                    }
                }
            }

            if (cp.getTeam() == Team.SPEC) {
                all.setGameMode(GameMode.SPECTATOR);
            } else {
                all.setGameMode(GameMode.SURVIVAL);
            }

            if (cp.getTeam() != Team.SPEC) {
                all.getInventory().clear();
                all.getInventory().addItem(new ItemBuilder()
                        .setMaterial(Material.CHEST)
                        .setDisplayName(ChatColor.GOLD + "Select a Kit")
                        .setLore(ChatColor.YELLOW + "Click with chest in hand to select a kit!")
                        .getItem());
                all.sendMessage(ChatColor.GRAY + "You are on the " + cp.getTeam().getColor() + cp.getTeam().getName() + ChatColor.GRAY + " team!");
            }
        }
        state = GameState.STARTING;

        new BukkitRunnable() {
            int timer = 60;
            @Override
            public void run() {
                if (state != GameState.STARTING) {
                    this.cancel();
                    return;
                }
                if (timer == 60) {
                    for (Player all : Bukkit.getOnlinePlayers()) {
                        all.sendMessage(ChatColor.YELLOW + "Game starting in " + ChatColor.GOLD + timer + ChatColor.YELLOW + " seconds!");
                        all.playSound(all.getLocation(), Sound.BLOCK_NOTE_BLOCK_SNARE, 7, 1);
                    }
                } else if (timer == 45) {
                    for (Player all : Bukkit.getOnlinePlayers()) {
                        all.sendMessage(ChatColor.GRAY + "[====================================================]\n"
                                + ChatColor.GOLD + "                           Select a Kit\n"
                                + ChatColor.GRAY + "     Each team has a set amount of each kit. Strategize with your team to decide who will get which kit!\n \n"
                                + ChatColor.GRAY + "[====================================================]");
                        all.sendMessage(ChatColor.YELLOW + "Game starting in " + ChatColor.GOLD + timer + ChatColor.YELLOW + " seconds!");
                        all.playSound(all.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 7, 1);
                    }
                } else if (timer == 30) {
                    for (Player all : Bukkit.getOnlinePlayers()) {
                        all.sendMessage(ChatColor.GRAY + "[====================================================]\n"
                                + ChatColor.GOLD + "                     Taking Flags\n"
                                + ChatColor.GRAY + "     To take an opponent flag, you must get to it and click it!  Killing a player with a flag will allow you to pick up that flag.  If you end up with your own flag, return it to an emtpy beam!\n"
                                + ChatColor.GRAY + "[====================================================]");
                        all.sendMessage(ChatColor.YELLOW + "Game starting in " + ChatColor.GOLD + timer + ChatColor.YELLOW + " seconds!");
                        all.playSound(all.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 7, 1);
                    }
                } else if (timer == 15) {
                    for (Player all : Bukkit.getOnlinePlayers()) {
                        all.sendMessage(ChatColor.GRAY + "[====================================================]\n"
                                + ChatColor.GOLD + "                     Capturing Flags\n"
                                + ChatColor.GRAY + "     Capturing flags requires you to have an active flag.  Take the opponent flag and click on your flag with the opponent's in your hand!\n \n"
                                + ChatColor.GRAY + "[====================================================]");
                        all.sendMessage(ChatColor.YELLOW + "Game starting in " + ChatColor.GOLD + timer + ChatColor.YELLOW + " seconds!");
                        all.playSound(all.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 7, 1);
                    }
                } else if (timer == 10) {
                    for (Player all : Bukkit.getOnlinePlayers()) {
                        all.sendMessage(ChatColor.YELLOW + "Game starting in " + ChatColor.GOLD + timer + ChatColor.YELLOW + " seconds!");
                        all.playSound(all.getLocation(), Sound.BLOCK_NOTE_BLOCK_SNARE, 7, 1);
                    }
                } else if (timer <= 5 && timer > 0) {
                    for (Player all : Bukkit.getOnlinePlayers()) {
                        all.sendMessage(ChatColor.YELLOW + "Game starting in " + ChatColor.RED + timer + ChatColor.YELLOW + " seconds!");
                        all.playSound(all.getLocation(), Sound.BLOCK_NOTE_BLOCK_SNARE, 7, 1);
                    }
                } else if (timer == 0) {
                    for (Player all : Bukkit.getOnlinePlayers()) {
                        CapturePlayer cp = Main.getInstance().getPlayers().get(all.getUniqueId());
                        if (cp != null) {
                            if (cp.getTeam() != Team.SPEC) {
                                if (cp.getKit() == null) {
                                    cp.setKit(Main.getInstance().getRandomKit(cp.getTeam()));
                                }
                                all.getInventory().clear();
                                cp.getKit().apply(cp);
                                all.teleport(cp.getTeam().getSpawn());
                            }
                        }
                        all.sendMessage(ChatColor.GRAY + "[====================================================]\n"
                                + ChatColor.GOLD + "                     Game Started\n"
                                + ChatColor.GRAY + "     The last team to have flags wins!\n(Also, you can't take more than 1 flag at a time.)\n \n"
                                + ChatColor.GRAY + "[====================================================]");
                        all.playSound(all.getLocation(), Sound.EVENT_RAID_HORN, 7, 1);
                        all.closeInventory();
                        all.setSaturation(20);
                    }
                    state = GameState.GAME;
                    this.cancel();
                }
                timer--;

            }
        }.runTaskTimer(Main.getInstance(), 20, 20);
    }

    public void tryEnd() {
        Map<Team, Integer> flagsCaptured = new HashMap<>();
        flagsCaptured.put(Team.RED, 0);
        flagsCaptured.put(Team.BLUE, 0);
        flagsCaptured.put(Team.LIME, 0);
        flagsCaptured.put(Team.YELLOW, 0);

        for (Flag allFlags : Main.getInstance().getFlags()) {
            if (allFlags.getHome().getBlock().getType() == Material.AIR && allFlags.getHolder() == null && !allFlags.isDropped()) {
                flagsCaptured.put(allFlags.getTeam(), flagsCaptured.get(allFlags.getTeam()) + 1);
            }
        }

        if (flagsCaptured.get(Team.RED) == Main.getInstance().getSettings().getFlags()) {
            // Red team flags gone
            Main.getInstance().getFlags().removeIf(allFlags -> allFlags.getTeam() == Team.RED);

            for (Player all : Bukkit.getOnlinePlayers()) {
                CapturePlayer allCp = Main.getInstance().getPlayers().get(all.getUniqueId());
                all.sendMessage(ChatColor.GRAY + "[====================================================]\n"
                        + ChatColor.GOLD + "                   Team Eliminated\n \n"
                        + ChatColor.RED + "     Red Team" + ChatColor.GRAY + " was eliminated!\n \n \n"
                        + ChatColor.GRAY + "[====================================================]");
                all.playSound(all.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 7, 1);
                if (allCp == null) continue;
                if (allCp.getTeam() == Team.RED) {
                    allCp.setTeam(Team.SPEC);
                    all.teleport(Main.getInstance().getWait());
                    all.setGameMode(GameMode.SPECTATOR);
                    all.getInventory().clear();
                    all.setHealth(20);
                    all.setSaturation(20);
                    all.sendMessage(ChatColor.GRAY + "Your flags were captured! " + ChatColor.GREEN + "Good game" + ChatColor.GRAY + "!");
                }
            }
        }
        if (flagsCaptured.get(Team.BLUE) == Main.getInstance().getSettings().getFlags()) {
            // Blue team flags gone
            Main.getInstance().getFlags().removeIf(allFlags -> allFlags.getTeam() == Team.BLUE);

            for (Player all : Bukkit.getOnlinePlayers()) {
                CapturePlayer allCp = Main.getInstance().getPlayers().get(all.getUniqueId());
                all.sendMessage(ChatColor.GRAY + "[====================================================]\n"
                        + ChatColor.GOLD + "                   Team Eliminated\n \n"
                        + ChatColor.BLUE + "     Blue Team" + ChatColor.GRAY + " was eliminated!\n \n \n"
                        + ChatColor.GRAY + "[====================================================]");
                all.playSound(all.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 7, 1);
                if (allCp == null) continue;
                if (allCp.getTeam() == Team.BLUE) {
                    allCp.setTeam(Team.SPEC);
                    all.teleport(Main.getInstance().getWait());
                    all.setGameMode(GameMode.SPECTATOR);
                    all.getInventory().clear();
                    all.setHealth(20);
                    all.setSaturation(20);
                    all.sendMessage(ChatColor.GRAY + "Your flags were captured! " + ChatColor.GREEN + "Good game" + ChatColor.GRAY + "!");
                }
            }

        }
        if (flagsCaptured.get(Team.LIME) == Main.getInstance().getSettings().getFlags()) {
            // Lime team flags gone
            Main.getInstance().getFlags().removeIf(allFlags -> allFlags.getTeam() == Team.LIME);

            for (Player all : Bukkit.getOnlinePlayers()) {
                CapturePlayer allCp = Main.getInstance().getPlayers().get(all.getUniqueId());
                all.sendMessage(ChatColor.GRAY + "[====================================================]\n"
                        + ChatColor.GOLD + "                   Team Eliminated\n \n"
                        + ChatColor.GREEN + "     Green Team" + ChatColor.GRAY + " was eliminated!\n \n \n"
                        + ChatColor.GRAY + "[====================================================]");
                all.playSound(all.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 7, 1);
                if (allCp == null) continue;
                if (allCp.getTeam() == Team.LIME) {
                    allCp.setTeam(Team.SPEC);
                    all.teleport(Main.getInstance().getWait());
                    all.setGameMode(GameMode.SPECTATOR);
                    all.getInventory().clear();
                    all.setHealth(20);
                    all.setSaturation(20);
                    all.sendMessage(ChatColor.GRAY + "Your flags were captured! " + ChatColor.GREEN + "Good game" + ChatColor.GRAY + "!");
                }
            }

        }
        if (flagsCaptured.get(Team.YELLOW) == Main.getInstance().getSettings().getFlags()) {
            // Yellow team flags gone
            Main.getInstance().getFlags().removeIf(allFlags -> allFlags.getTeam() == Team.YELLOW);

            for (Player all : Bukkit.getOnlinePlayers()) {
                CapturePlayer allCp = Main.getInstance().getPlayers().get(all.getUniqueId());
                all.sendMessage(ChatColor.GRAY + "[====================================================]\n"
                        + ChatColor.GOLD + "                   Team Eliminated\n \n"
                        + ChatColor.YELLOW + "     Yellow Team" + ChatColor.GRAY + " was eliminated!\n \n \n"
                        + ChatColor.GRAY + "[====================================================]");
                all.playSound(all.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 7, 1);
                if (allCp == null) continue;
                if (allCp.getTeam() == Team.YELLOW) {
                    allCp.setTeam(Team.SPEC);
                    all.teleport(Main.getInstance().getWait());
                    all.setGameMode(GameMode.SPECTATOR);
                    all.getInventory().clear();
                    all.setHealth(20);
                    all.setSaturation(20);
                    all.sendMessage(ChatColor.GRAY + "Your flags were captured! " + ChatColor.GREEN + "Good game" + ChatColor.GRAY + "!");
                }
            }

        }

        if (Main.getInstance().getFlags().size() == Main.getInstance().getSettings().getFlags()) {
            Main.getInstance().getPlayers().clear();

            for (Player all : Bukkit.getOnlinePlayers()) {
                for (Flag bruh : Main.getInstance().getFlags()) {
                    all.stopAllSounds();
                    all.sendMessage(ChatColor.GRAY + "[====================================================]\n"
                            + "          " + bruh.getTeam().getColor() + bruh.getTeam().getName() + " Team" + ChatColor.GOLD + " Wins!\n \n"
                            + bruh.getTeam().getColor() + "     " + bruh.getTeam().getName() + ChatColor.GRAY + " was the last team standing!"
                            + ChatColor.GREEN + " Good Game\n \n"
                            + ChatColor.GRAY + "[====================================================]");
                    all.playSound(all.getLocation(), Sound.ENTITY_WITHER_DEATH, 7, 1);
                    break;
                }
                all.teleport(Main.getInstance().getLocation("lobby"));
                all.setGameMode(GameMode.SURVIVAL);
                if (all.isOp()) {
                    all.setGameMode(GameMode.CREATIVE);
                }
                all.getInventory().clear();
                all.setHealth(20);
                all.setSaturation(20);

                Main.getInstance().getPlayers().put(all.getUniqueId(), new CapturePlayer(all.getUniqueId()));
            }

            InvClickListener.redKits.put(KitType.MID_FIELD, 0);
            InvClickListener.redKits.put(KitType.DEFENSE, 0);
            InvClickListener.redKits.put(KitType.BOW, 0);
            InvClickListener.redKits.put(KitType.FLAG_STEALER, 0);
            InvClickListener.blueKits.put(KitType.MID_FIELD, 0);
            InvClickListener.blueKits.put(KitType.DEFENSE, 0);
            InvClickListener.blueKits.put(KitType.BOW, 0);
            InvClickListener.blueKits.put(KitType.FLAG_STEALER, 0);
            InvClickListener.greenKits.put(KitType.MID_FIELD, 0);
            InvClickListener.greenKits.put(KitType.DEFENSE, 0);
            InvClickListener.greenKits.put(KitType.BOW, 0);
            InvClickListener.greenKits.put(KitType.FLAG_STEALER, 0);
            InvClickListener.yellowKits.put(KitType.MID_FIELD, 0);
            InvClickListener.yellowKits.put(KitType.DEFENSE, 0);
            InvClickListener.yellowKits.put(KitType.BOW, 0);
            InvClickListener.yellowKits.put(KitType.FLAG_STEALER, 0);
            InvClickListener.updateInvs();

            Main.getInstance().setState(GameState.WAIT);
            Main.getInstance().initFlags();
            Main.getInstance().initTeams();

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (Main.getInstance().getSettings().isAutoStart() && Main.getInstance().getPlayers().size() == Main.getInstance().getSettings().getPlayers()) {
                        Main.getInstance().start();
                    }
                }
            }.runTaskLater(this, 300);
        }
    }

    @Override
    public FileConfiguration getConfig() {
        File file = new File("./plugins/CaptureTheFlag/config.yml");
        return YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getConfig(String name) {
        File file = new File("./plugins/CaptureTheFlag/" + name + ".yml");
        return YamlConfiguration.loadConfiguration(file);
    }

    public Location getLocation(String path) {
        FileConfiguration cfg = getConfig();

        if (cfg.getString("locations/" + path + ".world") == null) {
            return new Location(Bukkit.getWorld("world"), 0, 0, 0, 0, 0);
        }

        String world;
        double X;
        double Y;
        double Z;
        float yaw;
        float pitch;

        world = cfg.getString("locations/" + path + ".world");
        X = cfg.getDouble("locations/" + path + ".X");
        Y = cfg.getDouble("locations/" + path + ".Y");
        Z = cfg.getDouble("locations/" + path + ".Z");
        yaw = (float) cfg.getDouble("locations/" + path + ".yaw");
        pitch = (float) cfg.getDouble("locations/" + path + ".pitch");

        return new Location(Bukkit.getWorld(world), X, Y, Z, yaw, pitch);
    }

    public void setLocation(String name, Location loc) {
        FileConfiguration cfg = getConfig();

        cfg.set("locations/" + name + ".world", loc.getWorld().getName());
        cfg.set("locations/" + name + ".X", loc.getX());
        cfg.set("locations/" + name + ".Y", loc.getY());
        cfg.set("locations/" + name + ".Z", loc.getZ());
        cfg.set("locations/" + name + ".yaw", loc.getYaw());
        cfg.set("locations/" + name + ".pitch", loc.getPitch());

        try {
            cfg.save(new File("./plugins/CaptureTheFlag/config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Main getInstance() {
        return instance;
    }
}
