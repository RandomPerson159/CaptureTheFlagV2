package com.random.captureTheFlag.game;

import com.random.captureTheFlag.player.Team;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;

public class CaptureBoard {
    private final Scoreboard b = Bukkit.getScoreboardManager().getMainScoreboard();

    public CaptureBoard() {
        for (Team teams : Team.values()) {
            b.registerNewTeam(teams.getName()).setColor(teams.getColor());
        }
    }

    public Scoreboard getB() {
        return b;
    }
}
