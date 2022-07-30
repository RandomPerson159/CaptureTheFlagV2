package com.random.captureTheFlag.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CapturePlayer {
    private final UUID uuid;
    private Team team;
    private KitType kit;

    public CapturePlayer(UUID uuid) {
        this.uuid = uuid;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setKit(KitType kit) {
        this.kit = kit;
    }

    public KitType getKit() {
        return kit;
    }

    public Team getTeam() {
        return team;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }
}
