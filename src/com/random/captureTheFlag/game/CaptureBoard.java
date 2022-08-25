package com.random.captureTheFlag.game;

import com.random.captureTheFlag.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.Scoreboard;

public class CaptureBoard implements Listener {
    /*
        [=======Capture the Flag=======]
        2 Teams - 3 Flags

        Red Team Flags:
          Flag 1 🚩 - Active
          Flag 2 🚩 - Active
          Flag 3 🚩 - Active

        Blue Team Flags:
          Flag 1 🚩 - Active
          Flag 2 🚩 - Active
          Flag 3 🚩 - Active

        [==============================]


     */
    private Scoreboard b = Bukkit.getScoreboardManager().getMainScoreboard();

    public CaptureBoard() {
        b.registerNewObjective("ctf", Criteria.DUMMY,  ChatColor.BOLD + "" + ChatColor.GOLD + "Capture the Flag");

    }

    public void updateBoard() {
        Settings settings = Main.getInstance().getSettings();
        if (settings.getTeams() == 2) {
            if (settings.getFlags() == 1) {

            }
            if (settings.getFlags() == 2) {

            }
            if (settings.getFlags() == 3) {

            }
        }
        if (settings.getTeams() == 4) {
            if (settings.getFlags() == 1) {

            } else if (settings.getFlags() == 2) {

            } else if (settings.getFlags() == 3) {

            }

        }
    }

    public void remove() {
        b.getObjective("ctf").unregister();
    }

    public Scoreboard getB() {
        return b;
    }

}
