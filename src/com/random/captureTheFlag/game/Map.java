package com.random.captureTheFlag.game;

import com.random.captureTheFlag.Main;
import com.random.captureTheFlag.player.Team;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class Map {
    private final String name;
    private final List<Location> redFlagLocs = new ArrayList<>();
    private final List<Location> blueFlagLocs = new ArrayList<>();
    private final List<Location> greenFlagLocs = new ArrayList<>();
    private final List<Location> yellowFlagLocs = new ArrayList<>();
    private final List<Location> flagLocs = new ArrayList<>();

    public Map(String name, int teams, int flags) {
        this.name = name;
        if (teams == 2) {
            for (int i = 1; i <= flags; i++) {
                redFlagLocs.add(Main.getInstance().getLocation(name + "." + teams + "teams." + flags + "flags.redFlag" + i));
                blueFlagLocs.add(Main.getInstance().getLocation(name + "." + teams + "teams." + flags + "flags.blueFlag" + i));
            }
            flagLocs.addAll(redFlagLocs);
            flagLocs.addAll(blueFlagLocs);
        } else {
            for (int i = 1; i <= flags; i++) {
                redFlagLocs.add(Main.getInstance().getLocation(name + "." + teams + "teams." + flags + "flags.redFlag" + i));
                blueFlagLocs.add(Main.getInstance().getLocation(name + "." + teams + "teams." + flags + "flags.blueFlag" + i));
                greenFlagLocs.add(Main.getInstance().getLocation(name + "." + teams + "teams." + flags + "flags.greenFlag" + i));
                yellowFlagLocs.add(Main.getInstance().getLocation(name + "." + teams + "teams." + flags + "flags.yellowFlag" + i));
            }
            flagLocs.addAll(redFlagLocs);
            flagLocs.addAll(blueFlagLocs);
            flagLocs.addAll(greenFlagLocs);
            flagLocs.addAll(yellowFlagLocs);
        }
    }

    public String getName() {
        return name;
    }

    public List<Location> getRedFlagLocs() {
        return redFlagLocs;
    }

    public List<Location> getBlueFlagLocs() {
        return blueFlagLocs;
    }

    public List<Location> getGreenFlagLocs() {
        return greenFlagLocs;
    }

    public List<Location> getYellowFlagLocs() {
        return yellowFlagLocs;
    }

    public List<Location> getFlagLocs() {
        return flagLocs;
    }
}
