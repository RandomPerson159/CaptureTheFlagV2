package com.random.captureTheFlag.game;

import com.random.captureTheFlag.player.Team;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;

public class CaptureBoard {
    private Scoreboard b = Bukkit.getScoreboardManager().getMainScoreboard();

    public CaptureBoard() {
        for (Team teams : Team.values()) {
            if (b.getTeam(teams.getName()) == null) {
                b.registerNewTeam(teams.getName()).setColor(teams.getColor());
            }
        }
    }

    public Scoreboard getB() {
        return b;
    }

}
