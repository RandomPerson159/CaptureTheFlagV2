package com.random.captureTheFlag;

import com.random.captureTheFlag.commands.*;
import com.random.captureTheFlag.game.*;
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
import org.bukkit.potion.PotionEffectType;
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
    private Location wait;
    private boolean enabled = false;

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
            enabled = getConfig("settings").getBoolean("enabled");
        }

        for (Player all : Bukkit.getOnlinePlayers()) {
            players.put(all.getUniqueId(), new CapturePlayer(all.getUniqueId()));
            if (enabled) {
                all.sendMessage(ChatColor.GREEN + "Capture the Flag has been enabled!");
                all.removePotionEffect(PotionEffectType.GLOWING);
                all.removePotionEffect(PotionEffectType.SLOW);
            }
        }

        Bukkit.getPluginCommand("ctfteams").setExecutor(new AssignCommand());
        Bukkit.getPluginCommand("ctfsetup").setExecutor(new SetupCommand());
        Bukkit.getPluginCommand("ctfteams").setExecutor(new AssignCommand());
        Bukkit.getPluginCommand("ctfhelp").setExecutor(new HelpCommand());
        Bukkit.getPluginCommand("ctfsetup").setExecutor(new SetupCommand());
        Bukkit.getPluginCommand("shout").setExecutor(new ShoutCommand());
        Bukkit.getPluginCommand("ctfstart").setExecutor(new StartCommand());
        Bukkit.getPluginCommand("ctfsettings").setExecutor(new SettingsCommand());
        Bukkit.getPluginCommand("ctfenable").setExecutor(new EnabledCommand());

        if (enabled) {
            PluginManager pm = Bukkit.getPluginManager();
            pm.registerEvents(new ChatListener(), this);
            pm.registerEvents(new FlagListener(), this);
            pm.registerEvents(new JoinListener(), this);
            pm.registerEvents(new PermissionsListener(), this);
            pm.registerEvents(new InvClickListener(), this);
        } else {
            return;
        }

        initFlags();
        initTeams();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (flags.size() != 0) {
                    for (Flag flags : flags) {
                        if (flags.isDropped()) {
                            flags.getItem().getWorld().spawnParticle(Particle.VILLAGER_HAPPY, flags.getItem().getLocation().clone().add(0, 1.5, 0), 5, 1, 20, 1, true);
                            continue;
                        }
                        if (flags.getHolder() != null) {
                            flags.getHolder().getPlayer().getWorld().spawnParticle(Particle.VILLAGER_HAPPY, flags.getHolder().getPlayer().getLocation().clone().add(0, 4, 0), 3, 0.125, 1, 0.125, true);
                        }
                    }
                }
            }
        }.runTaskTimer(this, 1, 1);
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
                flags.add(new Flag(Team.RED, getLocation(settings.getMap() + ".2teams.2flags.redFlag1")));
                flags.add(new Flag(Team.RED, getLocation(settings.getMap() + ".2teams.2flags.redFlag2")));
                flags.add(new Flag(Team.BLUE, getLocation(settings.getMap() + ".2teams.2flags.blueFlag1")));
                flags.add(new Flag(Team.BLUE, getLocation(settings.getMap() + ".2teams.2flags.blueFlag2")));
            } else {
                flags.add(new Flag(Team.RED, getLocation(settings.getMap() + ".4teams.2flags.redFlag1")));
                flags.add(new Flag(Team.RED, getLocation(settings.getMap() + ".4teams.2flags.redFlag2")));
                flags.add(new Flag(Team.BLUE, getLocation(settings.getMap() + ".4teams.2flags.blueFlag1")));
                flags.add(new Flag(Team.BLUE, getLocation(settings.getMap() + ".4teams.2flags.blueFlag2")));
                flags.add(new Flag(Team.LIME, getLocation(settings.getMap() + ".4teams.2flags.greenFlag1")));
                flags.add(new Flag(Team.LIME, getLocation(settings.getMap() + ".4teams.2flags.greenFlag2")));
                flags.add(new Flag(Team.YELLOW, getLocation(settings.getMap() + ".4teams.2flags.yellowFlag1")));
                flags.add(new Flag(Team.YELLOW, getLocation(settings.getMap() + ".4teams.2flags.yellowFlag2")));
            }
        } else if (settings.getFlags() == 1) {
            if (settings.getTeams() == 2) {
                flags.add(new Flag(Team.RED, getLocation(settings.getMap() + ".2teams.1flag.redFlag")));
                flags.add(new Flag(Team.BLUE, getLocation(settings.getMap() + ".2teams.1flag.blueFlag")));
            } else {
                flags.add(new Flag(Team.RED, getLocation(settings.getMap() + ".4teams.1flag.redFlag")));
                flags.add(new Flag(Team.BLUE, getLocation(settings.getMap() + ".4teams.1flag.blueFlag")));
                flags.add(new Flag(Team.LIME, getLocation(settings.getMap() + ".4teams.1flag.greenFlag")));
                flags.add(new Flag(Team.YELLOW, getLocation(settings.getMap() + ".4teams.1flag.yellowFlag")));
            }
        } else {
            if (settings.getTeams() == 2) {
                flags.add(new Flag(Team.RED, getLocation(settings.getMap() + ".2teams.3flags.redFlag1")));
                flags.add(new Flag(Team.RED, getLocation(settings.getMap() + ".2teams.3flags.redFlag2")));
                flags.add(new Flag(Team.RED, getLocation(settings.getMap() + ".2teams.3flags.redFlag3")));
                flags.add(new Flag(Team.BLUE, getLocation(settings.getMap() + ".2teams.3flags.blueFlag1")));
                flags.add(new Flag(Team.BLUE, getLocation(settings.getMap() + ".2teams.3flags.blueFlag2")));
                flags.add(new Flag(Team.BLUE, getLocation(settings.getMap() + ".2teams.3flags.blueFlag3")));
            } else {
                flags.add(new Flag(Team.RED, getLocation(settings.getMap() + ".4teams.3flags.redFlag1")));
                flags.add(new Flag(Team.RED, getLocation(settings.getMap() + ".4teams.3flags.redFlag2")));
                flags.add(new Flag(Team.RED, getLocation(settings.getMap() + ".4teams.3flags.redFlag3")));
                flags.add(new Flag(Team.BLUE, getLocation(settings.getMap() + ".4teams.3flags.blueFlag1")));
                flags.add(new Flag(Team.BLUE, getLocation(settings.getMap() + ".4teams.3flags.blueFlag2")));
                flags.add(new Flag(Team.BLUE, getLocation(settings.getMap() + ".4teams.3flags.blueFlag3")));
                flags.add(new Flag(Team.LIME, getLocation(settings.getMap() + ".4teams.3flags.greenFlag1")));
                flags.add(new Flag(Team.LIME, getLocation(settings.getMap() + ".4teams.3flags.greenFlag2")));
                flags.add(new Flag(Team.LIME, getLocation(settings.getMap() + ".4teams.3flags.greenFlag3")));
                flags.add(new Flag(Team.YELLOW, getLocation(settings.getMap() + ".4teams.3flags.yellowFlag1")));
                flags.add(new Flag(Team.YELLOW, getLocation(settings.getMap() + ".4teams.3flags.yellowFlag2")));
                flags.add(new Flag(Team.YELLOW, getLocation(settings.getMap() + ".4teams.3flags.yellowFlag3")));
            }
        }

        for(Flag flag : flags) {
            flag.put(FlagEvent.RESET, null);
        }
    }

    public void initTeams() {
        if (settings.getTeams() == 2) {
            if (settings.getFlags() == 2) {
                Team.RED.setSpawn(getLocation(settings.getMap() + ".2teams.2flags.redSpawn"));
                Team.BLUE.setSpawn(getLocation(settings.getMap() + ".2teams.2flags.blueSpawn"));
                Team.LIME.setSpawn(getLocation(settings.getMap() + ".2teams.2flags.greenSpawn"));
                Team.YELLOW.setSpawn(getLocation(settings.getMap() + ".2teams.2flags.yellowSpawn"));
                wait = getLocation(settings.getMap() + ".2teams.2flags.wait");
            } else if (settings.getFlags() == 1) {
                Team.RED.setSpawn(getLocation(settings.getMap() + ".2teams.1flag.redSpawn"));
                Team.BLUE.setSpawn(getLocation(settings.getMap() + ".2teams.1flag.blueSpawn"));
                Team.LIME.setSpawn(getLocation(settings.getMap() + ".2teams.1flag.greenSpawn"));
                Team.YELLOW.setSpawn(getLocation(settings.getMap() + ".2teams.1flag.yellowSpawn"));
                wait = getLocation(settings.getMap() + ".2teams.1flag.wait");
            } else {
                Team.RED.setSpawn(getLocation(settings.getMap() + ".2teams.3flags.redSpawn"));
                Team.BLUE.setSpawn(getLocation(settings.getMap() + ".2teams.3flags.blueSpawn"));
                Team.LIME.setSpawn(getLocation(settings.getMap() + ".2teams.3flags.greenSpawn"));
                Team.YELLOW.setSpawn(getLocation(settings.getMap() + ".2teams.3flags.yellowSpawn"));
                wait = getLocation(settings.getMap() + ".2teams.3flags.wait");
            }
        } else {
            if (settings.getFlags() == 2) {
                Team.RED.setSpawn(getLocation(settings.getMap() + ".4teams.2flags.redSpawn"));
                Team.BLUE.setSpawn(getLocation(settings.getMap() + ".4teams.2flags.blueSpawn"));
                Team.LIME.setSpawn(getLocation(settings.getMap() + ".4teams.2flags.greenSpawn"));
                Team.YELLOW.setSpawn(getLocation(settings.getMap() + ".4teams.2flags.yellowSpawn"));
                wait = getLocation(settings.getMap() + ".4teams.2flags.wait");
            } else if (settings.getFlags() == 1) {
                Team.RED.setSpawn(getLocation(settings.getMap() + ".4teams.redSpawn"));
                Team.BLUE.setSpawn(getLocation(settings.getMap() + ".4teams.blueSpawn"));
                Team.LIME.setSpawn(getLocation(settings.getMap() + ".4teams.greenSpawn"));
                Team.YELLOW.setSpawn(getLocation(settings.getMap() + ".4teams.yellowSpawn"));
                wait = getLocation(settings.getMap() + ".4teams.1flag.wait");
            } else {
                Team.RED.setSpawn(getLocation(settings.getMap() + ".4teams.3flags.redSpawn"));
                Team.BLUE.setSpawn(getLocation(settings.getMap() + ".4teams.3flags.blueSpawn"));
                Team.LIME.setSpawn(getLocation(settings.getMap() + ".4teams.3flags.greenSpawn"));
                Team.YELLOW.setSpawn(getLocation(settings.getMap() + ".4teams.3flags.yellowSpawn"));
                wait = getLocation(settings.getMap() + ".4teams.3flags.wait");
            }
        }
    }

    public KitType getRandomKit(Team team) {
        if (team == Team.RED) {
            if (InvClickListener.redKits.get(KitType.MID_FIELD) != settings.getMidFeildKit()) {
                InvClickListener.redKits.put(KitType.MID_FIELD, InvClickListener.redKits.get(KitType.MID_FIELD) + 1);
                return KitType.MID_FIELD;
            } else if (InvClickListener.redKits.get(KitType.DEFENSE) != settings.getDefenseKit()) {
                InvClickListener.redKits.put(KitType.DEFENSE, InvClickListener.redKits.get(KitType.DEFENSE) + 1);
                return KitType.DEFENSE;
            } else if (InvClickListener.redKits.get(KitType.BOW) != settings.getBowKit()) {
                InvClickListener.redKits.put(KitType.BOW, InvClickListener.redKits.get(KitType.BOW) + 1);
                return KitType.BOW;
            } else if (InvClickListener.redKits.get(KitType.FLAG_STEALER) != settings.getFlagStealerKit()) {
                InvClickListener.redKits.put(KitType.FLAG_STEALER, InvClickListener.redKits.get(KitType.FLAG_STEALER) + 1);
                return KitType.FLAG_STEALER;
            }
        } else if (team == Team.BLUE) {
            if (InvClickListener.blueKits.get(KitType.MID_FIELD) != settings.getMidFeildKit()) {
                InvClickListener.blueKits.put(KitType.MID_FIELD, InvClickListener.blueKits.get(KitType.MID_FIELD) + 1);
                return KitType.MID_FIELD;
            } else if (InvClickListener.blueKits.get(KitType.DEFENSE) != settings.getDefenseKit()) {
                InvClickListener.blueKits.put(KitType.DEFENSE, InvClickListener.blueKits.get(KitType.DEFENSE) + 1);
                return KitType.DEFENSE;
            } else if (InvClickListener.blueKits.get(KitType.BOW) != settings.getBowKit()) {
                InvClickListener.blueKits.put(KitType.BOW, InvClickListener.blueKits.get(KitType.BOW) + 1);
                return KitType.BOW;
            } else if (InvClickListener.blueKits.get(KitType.FLAG_STEALER) != settings.getFlagStealerKit()) {
                InvClickListener.blueKits.put(KitType.FLAG_STEALER, InvClickListener.blueKits.get(KitType.FLAG_STEALER) + 1);
                return KitType.FLAG_STEALER;
            }
        } else if (team == Team.LIME) {
            if (InvClickListener.greenKits.get(KitType.MID_FIELD) != settings.getMidFeildKit()) {
                InvClickListener.greenKits.put(KitType.MID_FIELD, InvClickListener.greenKits.get(KitType.MID_FIELD) + 1);
                return KitType.MID_FIELD;
            } else if (InvClickListener.greenKits.get(KitType.DEFENSE) != settings.getDefenseKit()) {
                InvClickListener.greenKits.put(KitType.DEFENSE, InvClickListener.greenKits.get(KitType.DEFENSE) + 1);
                return KitType.DEFENSE;
            } else if (InvClickListener.greenKits.get(KitType.BOW) != settings.getBowKit()) {
                InvClickListener.greenKits.put(KitType.BOW, InvClickListener.greenKits.get(KitType.BOW) + 1);
                return KitType.BOW;
            } else if (InvClickListener.greenKits.get(KitType.FLAG_STEALER) != settings.getFlagStealerKit()) {
                InvClickListener.greenKits.put(KitType.FLAG_STEALER, InvClickListener.greenKits.get(KitType.FLAG_STEALER) + 1);
                return KitType.FLAG_STEALER;
            }
        } else if (team == Team.YELLOW) {
            if (InvClickListener.yellowKits.get(KitType.MID_FIELD) != settings.getMidFeildKit()) {
                InvClickListener.yellowKits.put(KitType.MID_FIELD, InvClickListener.yellowKits.get(KitType.MID_FIELD) + 1);
                return KitType.MID_FIELD;
            } else if (InvClickListener.yellowKits.get(KitType.DEFENSE) != settings.getDefenseKit()) {
                InvClickListener.yellowKits.put(KitType.DEFENSE, InvClickListener.yellowKits.get(KitType.DEFENSE) + 1);
                return KitType.DEFENSE;
            } else if (InvClickListener.yellowKits.get(KitType.BOW) != settings.getBowKit()) {
                InvClickListener.yellowKits.put(KitType.BOW, InvClickListener.yellowKits.get(KitType.BOW) + 1);
                return KitType.BOW;
            }
            InvClickListener.yellowKits.put(KitType.FLAG_STEALER, InvClickListener.yellowKits.get(KitType.FLAG_STEALER) + 1);
            return KitType.FLAG_STEALER;
        }
        return null;
    }

    public void start() {
        int red = 0;
        int blue = 0;
        int green = 0;
        int yellow = 0;

        for (Player all : Bukkit.getOnlinePlayers()) {
            CapturePlayer cp = players.get(all.getUniqueId());
            if (cp != null) {
                if (cp.getTeam() != null && cp.getTeam() != Team.SPEC) {
                    if (settings.getTeams() == 2) {
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
            all.teleport(wait);
            CapturePlayer cp = players.get(all.getUniqueId());
            all.sendMessage(ChatColor.GRAY + "[====================================================]\n"
                    + ChatColor.GOLD + "                   Capture the Flag\n"
                    + ChatColor.GRAY + "     Welcome to Capture the Flag! The game is simple: capture opponent flags! Each team has " + settings.getFlags()
                    + ChatColor.GRAY + (settings.getFlags() == 1 ? " flag. " : " flags. ") + "In order to win, you must capture all opponent flags, without losing "
                    + (settings.getFlags() == 2 ? "both your flags... " : "your flag... ") + ChatColor.GREEN + "\n Have fun!\n"
                    + ChatColor.GRAY + "[====================================================]");
            all.playSound(all.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 7, 1);

            if (cp.getTeam() == null) {
                if (red < settings.getPlayers() / settings.getTeams()) {
                    cp.setTeam(Team.RED);
                    red++;
                } else if (blue < settings.getPlayers() / settings.getTeams()) {
                    cp.setTeam(Team.BLUE);
                    blue++;
                } else if (green < settings.getPlayers() / settings.getTeams()) {
                    cp.setTeam(Team.LIME);
                    green++;
                } else if (yellow < settings.getPlayers() / settings.getTeams()) {
                    cp.setTeam(Team.YELLOW);
                    yellow++;
                } else {
                    cp.setTeam(Team.SPEC);
                }
            }

            if (settings.getTeams() == 2) {
                if (cp.getTeam() == Team.LIME || cp.getTeam() == Team.YELLOW) {
                    if (red < settings.getPlayers() / settings.getTeams()) {
                        cp.setTeam(Team.RED);
                        red++;
                    } else if (blue < settings.getPlayers() / settings.getTeams()) {
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
                        CapturePlayer cp = players.get(all.getUniqueId());
                        if (cp != null) {
                            if (cp.getTeam() != Team.SPEC) {
                                if (cp.getKit() == null) {
                                    cp.setKit(getRandomKit(cp.getTeam()));
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
        }.runTaskTimer(this, 20, 20);
    }

    public void tryEnd() {
        Map<Team, Integer> flagsCaptured = new HashMap<>();
        flagsCaptured.put(Team.RED, 0);
        flagsCaptured.put(Team.BLUE, 0);
        flagsCaptured.put(Team.LIME, 0);
        flagsCaptured.put(Team.YELLOW, 0);

        for (Flag allFlags : flags) {
            if (allFlags.getHome().getBlock().getType() == Material.AIR && allFlags.getHolder() == null && !allFlags.isDropped()) {
                flagsCaptured.put(allFlags.getTeam(), flagsCaptured.get(allFlags.getTeam()) + 1);
            }
        }

        if (flagsCaptured.get(Team.RED) == settings.getFlags()) {
            // Red team flags gone
            flags.removeIf(allFlags -> allFlags.getTeam() == Team.RED);

            for (Player all : Bukkit.getOnlinePlayers()) {
                CapturePlayer allCp = players.get(all.getUniqueId());
                all.sendMessage(ChatColor.GRAY + "[====================================================]\n"
                        + ChatColor.GOLD + "                   Team Eliminated\n \n"
                        + ChatColor.RED + "     Red Team" + ChatColor.GRAY + " was eliminated!\n \n \n"
                        + ChatColor.GRAY + "[====================================================]");
                all.playSound(all.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 7, 1);
                if (allCp == null) continue;
                if (allCp.getTeam() == Team.RED) {
                    for (Flag flags : Main.getInstance().getFlags()) {
                        if (flags.getHolder() == null) continue;
                        if (flags.getHolder().getPlayer().getUniqueId().equals(all.getUniqueId())) {
                            flags.drop(all.getLocation(), allCp);
                        }
                    }

                    allCp.setTeam(Team.SPEC);
                    all.teleport(wait);
                    all.setGameMode(GameMode.SPECTATOR);
                    all.getInventory().clear();
                    all.setHealth(20);
                    all.setSaturation(20);
                    all.sendMessage(ChatColor.GRAY + "Your flags were captured! " + ChatColor.GREEN + "Good game" + ChatColor.GRAY + "!");
                }
            }
        }
        if (flagsCaptured.get(Team.BLUE) == settings.getFlags()) {
            // Blue team flags gone
            flags.removeIf(allFlags -> allFlags.getTeam() == Team.BLUE);

            for (Player all : Bukkit.getOnlinePlayers()) {
                CapturePlayer allCp = players.get(all.getUniqueId());
                all.sendMessage(ChatColor.GRAY + "[====================================================]\n"
                        + ChatColor.GOLD + "                   Team Eliminated\n \n"
                        + ChatColor.BLUE + "     Blue Team" + ChatColor.GRAY + " was eliminated!\n \n \n"
                        + ChatColor.GRAY + "[====================================================]");
                all.playSound(all.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 7, 1);
                if (allCp == null) continue;
                if (allCp.getTeam() == Team.BLUE) {
                    allCp.setTeam(Team.SPEC);
                    all.teleport(wait);
                    all.setGameMode(GameMode.SPECTATOR);
                    all.getInventory().clear();
                    all.setHealth(20);
                    all.setSaturation(20);
                    all.sendMessage(ChatColor.GRAY + "Your flags were captured! " + ChatColor.GREEN + "Good game" + ChatColor.GRAY + "!");
                }
            }

        }
        if (flagsCaptured.get(Team.LIME) == settings.getFlags()) {
            // Lime team flags gone
            flags.removeIf(allFlags -> allFlags.getTeam() == Team.LIME);

            for (Player all : Bukkit.getOnlinePlayers()) {
                CapturePlayer allCp = players.get(all.getUniqueId());
                all.sendMessage(ChatColor.GRAY + "[====================================================]\n"
                        + ChatColor.GOLD + "                   Team Eliminated\n \n"
                        + ChatColor.GREEN + "     Green Team" + ChatColor.GRAY + " was eliminated!\n \n \n"
                        + ChatColor.GRAY + "[====================================================]");
                all.playSound(all.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 7, 1);
                if (allCp == null) continue;
                if (allCp.getTeam() == Team.LIME) {
                    allCp.setTeam(Team.SPEC);
                    all.teleport(wait);
                    all.setGameMode(GameMode.SPECTATOR);
                    all.getInventory().clear();
                    all.setHealth(20);
                    all.setSaturation(20);
                    all.sendMessage(ChatColor.GRAY + "Your flags were captured! " + ChatColor.GREEN + "Good game" + ChatColor.GRAY + "!");
                }
            }

        }
        if (flagsCaptured.get(Team.YELLOW) == settings.getFlags()) {
            // Yellow team flags gone
            flags.removeIf(allFlags -> allFlags.getTeam() == Team.YELLOW);

            for (Player all : Bukkit.getOnlinePlayers()) {
                CapturePlayer allCp = players.get(all.getUniqueId());
                all.sendMessage(ChatColor.GRAY + "[====================================================]\n"
                        + ChatColor.GOLD + "                   Team Eliminated\n \n"
                        + ChatColor.YELLOW + "     Yellow Team" + ChatColor.GRAY + " was eliminated!\n \n \n"
                        + ChatColor.GRAY + "[====================================================]");
                all.playSound(all.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 7, 1);
                if (allCp == null) continue;
                if (allCp.getTeam() == Team.YELLOW) {
                    allCp.setTeam(Team.SPEC);
                    all.teleport(wait);
                    all.setGameMode(GameMode.SPECTATOR);
                    all.getInventory().clear();
                    all.setHealth(20);
                    all.setSaturation(20);
                    all.sendMessage(ChatColor.GRAY + "Your flags were captured! " + ChatColor.GREEN + "Good game" + ChatColor.GRAY + "!");
                }
            }

        }

        if (flags.size() == settings.getFlags() || players.size() == 0) {
            players.clear();

            for (Player all : Bukkit.getOnlinePlayers()) {
                for (Flag bruh : flags) {
                    all.stopAllSounds();
                    all.sendMessage(ChatColor.GRAY + "[====================================================]\n"
                            + "          " + bruh.getTeam().getColor() + bruh.getTeam().getName() + " Team" + ChatColor.GOLD + " Wins!\n \n"
                            + bruh.getTeam().getColor() + "     " + bruh.getTeam().getName() + ChatColor.GRAY + " was the last team standing!"
                            + ChatColor.GREEN + " Good Game\n \n"
                            + ChatColor.GRAY + "[====================================================]");
                    all.playSound(all.getLocation(), Sound.ENTITY_WITHER_DEATH, 7, 1);
                    break;
                }
                all.teleport(Bukkit.getWorld("world").getSpawnLocation());
                all.setGameMode(GameMode.SURVIVAL);
                all.getInventory().clear();
                all.setHealth(20);
                all.setSaturation(20);

                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + all.getName() + " group remove red-team");
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + all.getName() + " group remove blue-team");
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + all.getName() + " group remove spec-team");

                players.put(all.getUniqueId(), new CapturePlayer(all.getUniqueId()));
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

            state = GameState.WAIT;
            initFlags();
            initTeams();

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (settings.isAutoStart() && players.size() == settings.getPlayers()) {
                        start();
                    }
                }
            }.runTaskLater(this, 300);
        }
    }

    public boolean getEnabled() {
        return enabled;
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
