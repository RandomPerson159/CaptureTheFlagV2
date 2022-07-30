package com.random.captureTheFlag.player;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;

public enum Team {
    RED("Red", ChatColor.RED), BLUE("Blue",ChatColor.BLUE), LIME("Green", ChatColor.GREEN), YELLOW("Yellow",ChatColor.YELLOW), SPEC("Spectator", ChatColor.DARK_GRAY);

    private Location spawn;
    private final String name;
    private final ChatColor color;
    private Material flag;
    private Material beacon;

    Team(String name, ChatColor color) {
        this.name = name;
        this.color = color;
    }

    public void setSpawn(Location spawn) {
        this.spawn = spawn;
    }

    public Location getSpawn() {
        return spawn;
    }

    public String getName() {
        return name;
    }

    public ChatColor getColor() {
        return color;
    }
}
