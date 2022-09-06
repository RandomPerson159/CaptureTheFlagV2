package com.random.captureTheFlag.game;

import com.random.captureTheFlag.player.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.List;

public class CaptureBoard implements Listener {
    /*
        [=======Capture the Flag=======]
          Starting in/Playing/Waiting   15
                                        14
          Red Team Flags (|> |> |>):    13
            0 held by Red               12
            0 held by Blue              11
            0 dropped                   10
            0 captured by Blue           9
                                         8
          Blue Team Flags (|> |> |>):    7
            0 Held by Blue               6
            0 Held by Red                5
            0 Dropped                    4
            0 Captured by Red            3
                                         2
        [==============================] 1


     */

    private Scoreboard b = Bukkit.getScoreboardManager().getNewScoreboard();
    private final Objective obj;

    public CaptureBoard() {
        if (b.getObjective("ctf") != null) {
            b.getObjective("ctf").unregister();
        }
        b.registerNewObjective("ctf", "dummy",  ChatColor.GRAY + "[=====" + ChatColor.GOLD + "Capture the Flag" + ChatColor.GRAY + "=====]    ");
        obj = b.getObjective("ctf");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.getScore(" ").setScore(14);
        obj.getScore("  ").setScore(8);
        obj.getScore(ChatColor.RED + "  Red Team Flags " + ChatColor.DARK_GRAY + "(" + ChatColor.GREEN + "|> " + ChatColor.GREEN + "|> " + ChatColor.GREEN + "|>" + ChatColor.DARK_GRAY + ")").setScore(13);
        obj.getScore(ChatColor.BLUE + "  Blue Team Flags " + ChatColor.DARK_GRAY + "(" + ChatColor.GREEN + "|> " + ChatColor.GREEN + "|> " + ChatColor.GREEN + "|>" + ChatColor.DARK_GRAY + ")").setScore(7);
        obj.getScore("   ").setScore(2);
        obj.getScore(ChatColor.GRAY + "[=========================]").setScore(1);
    }

    public void updateBoard(Settings settings, List<Flag> flags, List<Location> flagLocs, GameState state) {
        reset();
        if (settings.getTeams() == 2) {
            if (state == GameState.WAIT) {
                obj.getScore(ChatColor.YELLOW + "  Waiting for more players!").setScore(15);
                return;
            }
            if (settings.getFlags() == 1) {
                obj.setDisplaySlot(null);
                return;
            }
            if (settings.getFlags() == 2) {
                obj.setDisplaySlot(null);
                return;
            }
            if (settings.getFlags() == 3) {
                if (state == GameState.STARTING) {
                    obj.getScore(ChatColor.YELLOW + "  Waiting for more players!").setScore(0);
                    obj.getScore(ChatColor.YELLOW + "  Starting...").setScore(15);
                } else {
                    obj.getScore(ChatColor.YELLOW + "  Starting...").setScore(0);
                    obj.getScore(ChatColor.DARK_AQUA + "  Playing on " + settings.getMap()).setScore(15);
                }

                ChatColor red1 = flagLocs.get(0).getBlock().getType() == Material.AIR ? ChatColor.GRAY : ChatColor.GREEN;
                ChatColor red2 = flagLocs.get(1).getBlock().getType() == Material.AIR ? ChatColor.GRAY : ChatColor.GREEN;
                ChatColor red3 = flagLocs.get(2).getBlock().getType() == Material.AIR ? ChatColor.GRAY : ChatColor.GREEN;
                obj.getScore(ChatColor.RED + "  Red Team Flags " + ChatColor.DARK_GRAY + "(" + red1 + "|> " + red2 + "|> " + red3 + "|>" + ChatColor.DARK_GRAY + ")").setScore(13);

                ChatColor blue1 = flagLocs.get(3).getBlock().getType() == Material.AIR ? ChatColor.GRAY : ChatColor.GREEN;
                ChatColor blue2 = flagLocs.get(4).getBlock().getType() == Material.AIR ? ChatColor.GRAY : ChatColor.GREEN;
                ChatColor blue3 = flagLocs.get(5).getBlock().getType() == Material.AIR ? ChatColor.GRAY : ChatColor.GREEN;
                obj.getScore(ChatColor.BLUE + "  Blue Team Flags " + ChatColor.DARK_GRAY + "(" + blue1 + "|> " + blue2 + "|> " + blue3 + "|>" + ChatColor.DARK_GRAY + ")").setScore(7);

                int redHeldRed = 0;
                int redHeldBlue = 0;
                int redDropped = 0;
                int redCaptured = 0;

                int blueHeldBlue = 0;
                int blueHeldRed = 0;
                int blueDropped = 0;
                int blueCaptured = 0;

                for (Flag flag : flags) {
                    if (flag.getTeam() == Team.RED) {
                        if (flag.isDropped()) {
                            redDropped++;
                        } else if (flag.getHolder() != null) {
                            if (flag.getHolder().getTeam() == Team.RED) {
                                redHeldRed++;
                            } else {
                                redHeldBlue++;
                            }
                        } else if (flag.getHome() == null) {
                            redCaptured++;
                        }
                    } else {
                        if (flag.isDropped()) {
                            blueDropped++;
                        } else if (flag.getHolder() != null) {
                            if (flag.getHolder().getTeam() == Team.BLUE) {
                                blueHeldBlue++;
                            } else {
                                blueHeldRed++;
                            }
                        } else if (flag.getHome() == null) {
                            blueCaptured++;
                        }
                    }
                }

                redCaptured = 3 - (redHeldRed + redHeldBlue + redDropped + redCaptured);
                blueCaptured = 3 - (blueHeldBlue + blueHeldRed + blueDropped + blueCaptured);

                // Red held by red
                obj.getScore(ChatColor.GOLD + "    " + redHeldRed + ChatColor.GRAY + " held by " + ChatColor.GOLD + "Red").setScore(12);
                // Red held by blue
                obj.getScore(ChatColor.GOLD + "    " + redHeldBlue + ChatColor.GRAY + " held by " + ChatColor.GOLD + "Blue").setScore(11);
                // Blue held by blue
                obj.getScore(ChatColor.GOLD + "    " + blueHeldBlue + ChatColor.GRAY + " held by " + ChatColor.GOLD + "Blue ").setScore(6);
                // Blue held by red
                obj.getScore(ChatColor.GOLD + "    " + blueHeldRed + ChatColor.GRAY + " held by " + ChatColor.GOLD + "Red ").setScore(5);
                // Red dropped
                obj.getScore(ChatColor.GOLD + "    " + redDropped + ChatColor.GRAY + " dropped").setScore(10);
                // Blue dropped
                obj.getScore(ChatColor.GOLD + "    " + blueDropped + ChatColor.GRAY + " dropped ").setScore(4);
                // Red captured by blue
                obj.getScore(ChatColor.GOLD + "    " + redCaptured + ChatColor.GRAY + " captured by " + ChatColor.GOLD + "Blue").setScore(9);
                // Blue captured by red
                obj.getScore(ChatColor.GOLD + "    " + blueCaptured + ChatColor.GRAY + " captured by " + ChatColor.GOLD + "Red").setScore(3);
            }
        } else {
            obj.setDisplaySlot(null);
        }
    }

    private void reset() {
        /*
        b.resetScores("ctf");
        obj.getScore(" ").setScore(14);
        obj.getScore("  ").setScore(8);
        obj.getScore("   ").setScore(2);
        obj.getScore(ChatColor.GRAY + "[=========================]").setScore(1);
         */
        // Red Flags  8 possibilities: 111, 110, 100, 101, 000, 001, 011, 010
        obj.getScore(ChatColor.RED + "  Red Team Flags " + ChatColor.DARK_GRAY + "(" + ChatColor.GREEN + "|> " + ChatColor.GREEN + "|> " + ChatColor.GREEN + "|>" + ChatColor.DARK_GRAY + ")" + ChatColor.RED + ":").setScore(0);
        obj.getScore(ChatColor.RED + "  Red Team Flags " + ChatColor.DARK_GRAY + "(" + ChatColor.GREEN + "|> " + ChatColor.GREEN + "|> " + ChatColor.GRAY + "|>" + ChatColor.DARK_GRAY + ")" + ChatColor.RED + ":").setScore(0);
        obj.getScore(ChatColor.RED + "  Red Team Flags " + ChatColor.DARK_GRAY + "(" + ChatColor.GREEN + "|> " + ChatColor.GRAY + "|> " + ChatColor.GRAY + "|>" + ChatColor.DARK_GRAY + ")" + ChatColor.RED + ":").setScore(0);
        obj.getScore(ChatColor.RED + "  Red Team Flags " + ChatColor.DARK_GRAY + "(" + ChatColor.GREEN + "|> " + ChatColor.GRAY + "|> " + ChatColor.GREEN +"|>" + ChatColor.DARK_GRAY + ")" + ChatColor.RED + ":").setScore(0);
        obj.getScore(ChatColor.RED + "  Red Team Flags " + ChatColor.DARK_GRAY + "(" + ChatColor.GRAY + "|> " + ChatColor.GRAY + "|> " + ChatColor.GRAY + "|>" + ChatColor.DARK_GRAY + ")" + ChatColor.RED + ":").setScore(0);
        obj.getScore(ChatColor.RED + "  Red Team Flags " + ChatColor.DARK_GRAY + "(" + ChatColor.GRAY + "|> " + ChatColor.GRAY + "|> " + ChatColor.GREEN + "|>" + ChatColor.DARK_GRAY + ")" + ChatColor.RED + ":").setScore(0);
        obj.getScore(ChatColor.RED + "  Red Team Flags " + ChatColor.DARK_GRAY + "(" + ChatColor.GRAY + "|> " + ChatColor.GREEN + "|> " + ChatColor.GREEN + "|>" + ChatColor.DARK_GRAY + ")" + ChatColor.RED + ":").setScore(0);
        obj.getScore(ChatColor.RED + "  Red Team Flags " + ChatColor.DARK_GRAY + "(" + ChatColor.GRAY + "|> " + ChatColor.GREEN + "|> " + ChatColor.GRAY + "|>" + ChatColor.DARK_GRAY + ")" + ChatColor.RED + ":").setScore(0);
        // Blue Flags
        obj.getScore(ChatColor.BLUE + "  Blue Team Flags " + ChatColor.DARK_GRAY + "(" + ChatColor.GREEN + "|> " + ChatColor.GREEN + "|> " + ChatColor.GREEN + "|>" + ChatColor.DARK_GRAY + ")" + ChatColor.BLUE + ":").setScore(0);
        obj.getScore(ChatColor.BLUE + "  Blue Team Flags " + ChatColor.DARK_GRAY + "(" + ChatColor.GREEN + "|> " + ChatColor.GREEN + "|> " + ChatColor.GRAY + "|>" + ChatColor.DARK_GRAY + ")" + ChatColor.BLUE + ":").setScore(0);
        obj.getScore(ChatColor.BLUE + "  Blue Team Flags " + ChatColor.DARK_GRAY + "(" + ChatColor.GREEN + "|> " + ChatColor.GRAY + "|> " + ChatColor.GRAY + "|>" + ChatColor.DARK_GRAY + ")" + ChatColor.BLUE + ":").setScore(0);
        obj.getScore(ChatColor.BLUE + "  Blue Team Flags " + ChatColor.DARK_GRAY + "(" + ChatColor.GREEN + "|> " + ChatColor.GRAY + "|> " + ChatColor.GREEN +"|>" + ChatColor.DARK_GRAY + ")" + ChatColor.BLUE + ":").setScore(0);
        obj.getScore(ChatColor.BLUE + "  Blue Team Flags " + ChatColor.DARK_GRAY + "(" + ChatColor.GRAY + "|> " + ChatColor.GRAY + "|> " + ChatColor.GRAY + "|>" + ChatColor.DARK_GRAY + ")" + ChatColor.BLUE + ":").setScore(0);
        obj.getScore(ChatColor.BLUE + "  Blue Team Flags " + ChatColor.DARK_GRAY + "(" + ChatColor.GRAY + "|> " + ChatColor.GRAY + "|> " + ChatColor.GREEN + "|>" + ChatColor.DARK_GRAY + ")" + ChatColor.BLUE + ":").setScore(0);
        obj.getScore(ChatColor.BLUE + "  Blue Team Flags " + ChatColor.DARK_GRAY + "(" + ChatColor.GRAY + "|> " + ChatColor.GREEN + "|> " + ChatColor.GREEN + "|>" + ChatColor.DARK_GRAY + ")" + ChatColor.BLUE + ":").setScore(0);
        obj.getScore(ChatColor.BLUE + "  Blue Team Flags " + ChatColor.DARK_GRAY + "(" + ChatColor.GRAY + "|> " + ChatColor.GREEN + "|> " + ChatColor.GRAY + "|>" + ChatColor.DARK_GRAY + ")" + ChatColor.BLUE + ":").setScore(0);

        for (int i = 0; i < 4; i++) {
            // Red held by red
            obj.getScore(ChatColor.GOLD + "    " + i + ChatColor.GRAY + " held by " + ChatColor.GOLD + "Red").setScore(0);
            // Red held by blue
            obj.getScore(ChatColor.GOLD + "    " + i + ChatColor.GRAY + " held by " + ChatColor.GOLD + "Blue").setScore(0);
            // Blue held by blue
            obj.getScore(ChatColor.GOLD + "    " + i + ChatColor.GRAY + " held by " + ChatColor.GOLD + "Blue ").setScore(0);
            // Blue held by red
            obj.getScore(ChatColor.GOLD + "    " + i + ChatColor.GRAY + " held by " + ChatColor.GOLD + "Red ").setScore(0);
            // Red dropped
            obj.getScore(ChatColor.GOLD + "    " + i + ChatColor.GRAY + " dropped").setScore(0);
            // Blue dropped
            obj.getScore(ChatColor.GOLD + "    " + i + ChatColor.GRAY + " dropped ").setScore(0);
            // Red captured by blue
            obj.getScore(ChatColor.GOLD + "    " + i + ChatColor.GRAY + " captured by " + ChatColor.GOLD + "Blue").setScore(0);
            // Blue captured by red
            obj.getScore(ChatColor.GOLD + "    " + i + ChatColor.GRAY + " captured by " + ChatColor.GOLD + "Red").setScore(0);
        }
    }

    public void remove() {
        obj.unregister();
    }

    public Objective getB() {
        return obj;
    }

}
