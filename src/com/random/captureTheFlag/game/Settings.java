package com.random.captureTheFlag.game;

import com.random.captureTheFlag.Main;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Settings {
    private int players;
    private boolean dropFlags;
    private boolean shouts;
    private int teams;
    private int midFeildKit;
    private int defenseKit;
    private int flagStealerKit;
    private int bowKit;
    private int flags;
    private int respawn;
    private String map;
    private boolean autoStart;
    private boolean isAllowSpectators;

    public Settings(int players, boolean dropFlags, boolean shouts, int teams, int midFeildKit, int defenseKit, int flagStealerKit, int bowKit, int flags, int respawn) {
        FileConfiguration cfg = Main.getInstance().getConfig("settings");
        this.players = players;
        cfg.set("players", players);
        this.dropFlags = dropFlags;
        cfg.set("dropFlags", dropFlags);
        this.shouts = shouts;
        cfg.set("shouts", shouts);
        this.teams = teams;
        cfg.set("teams", teams);
        this.midFeildKit = midFeildKit;
        cfg.set("midFeildKit", midFeildKit);
        this.defenseKit = defenseKit;
        cfg.set("defenseKit", defenseKit);
        this.flagStealerKit = flagStealerKit;
        cfg.set("flagStealerKit", flagStealerKit);
        this.bowKit = bowKit;
        cfg.set("bowKit", bowKit);
        this.flags = flags;
        cfg.set("flags", flags);
        this.respawn = respawn;
        cfg.set("respawn", respawn);
        this.autoStart = true;
        cfg.set("autoStart", true);
        this.map = "default";
        cfg.set("map", "default");
        List<String> maps = new ArrayList<>();
        maps.add("default");
        cfg.set("maps", maps);
        this.isAllowSpectators = false;
        cfg.set("allowSpectators", false);
        cfg.set("enabled", true);

        try {
            cfg.save(new File("./plugins/CaptureTheFlag/settings.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Settings() {
        FileConfiguration cfg = Main.getInstance().getConfig("settings");
        this.players = cfg.getInt("players");
        this.dropFlags = cfg.getBoolean("dropFlags");
        this.shouts = cfg.getBoolean("shouts");
        this.teams = cfg.getInt("teams");
        this.midFeildKit = cfg.getInt("midFeildKit");
        this.defenseKit = cfg.getInt("defenseKit");
        this.flagStealerKit = cfg.getInt("flagStealerKit");
        this.bowKit = cfg.getInt("bowKit");
        this.flags = cfg.getInt("flags");
        this.respawn = cfg.getInt("respawn");
        this.autoStart = cfg.getBoolean("autoStart");
        this.map = cfg.getString("map");
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String getMap() {
        return map;
    }

    public List<String> getMaps() {
        return Main.getInstance().getConfig().getStringList("maps");
    }

    public void setMaps(List<String> maps) {
        update("maps", maps);
    }

    public void setNextMap() {
        List<String> maps = getMaps();
        if (maps.get(maps.size() - 1).equals(map)) {
            this.map = maps.get(0);
            return;
        }
        int i = 0;
        for (String map : maps) {
            i++;
            if (this.map.equals(map)) {
                this.map = maps.get(i);
            }
        }
    }

    public void setPlayers(int players) {
        this.players = players;
        update("players", players);
    }

    public int getPlayers() {
        return players;
    }

    public void setDropFlags(boolean dropFlags) {
        this.dropFlags = dropFlags;
        update("dropFlags", dropFlags);
    }

    public boolean isDropFlags() {
        return dropFlags;
    }

    public void setShouts(boolean shouts) {
        this.shouts = shouts;
        update("shouts", shouts);
    }

    public boolean isShouts() {
        return shouts;
    }

    public void setTeams(int teams) {
        this.teams = teams;
        update("teams", teams);
    }

    public int getTeams() {
        return teams;
    }

    public void setMidFeildKit(int midFeildKit) {
        this.midFeildKit = midFeildKit;
        update("midFeildKit", midFeildKit);
    }

    public int getMidFeildKit() {
        return midFeildKit;
    }

    public void setDefenseKit(int defenseKit) {
        this.defenseKit = defenseKit;
        update("defenseKit", defenseKit);
    }

    public int getDefenseKit() {
        return defenseKit;
    }

    public void setFlagStealerKit(int flagStealerKit) {
        this.flagStealerKit = flagStealerKit;
        update("flagStealerKit", flagStealerKit);
    }

    public int getFlagStealerKit() {
        return flagStealerKit;
    }

    public void setBowKit(int bowKit) {
        this.bowKit = bowKit;
        update("bowKit", bowKit);
    }

    public int getBowKit() {
        return bowKit;
    }

    public void setFlags(int flags) {
        this.flags = flags;
        update("flags", flags);
    }

    public int getFlags() {
        return flags;
    }

    public void setRespawn(int respawn) {
        this.respawn = respawn;
        update("respawn", respawn);
    }

    public int getRespawn() {
        return respawn;
    }

    public boolean isAutoStart() {
        return autoStart;
    }

    public void setAutoStart(boolean autoStart) {
        this.autoStart = autoStart;
        update("autoStart", autoStart);
    }

    public boolean isAllowSpectators() {
        return isAllowSpectators;
    }

    public void setAllowSpectators(boolean allowSpectators) {
        isAllowSpectators = allowSpectators;
    }

    private void update(String path, Object obj) {
        FileConfiguration cfg = Main.getInstance().getConfig("settings");
        cfg.set(path, obj);
        try {
            cfg.save(new File("./plugins/CaptureTheFlag/settings.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
