package com.random.captureTheFlag.util;


import org.bukkit.Location;

import java.util.Objects;

public class Region {
    private final String world;
    private final double minX;
    private final double maxX;
    private final double minZ;
    private final double maxZ;

    public Region(Location loc1, Location loc2) {
        world = loc1.getWorld().getName();
        minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
        minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());

    }

    public Region(String world, double x1, double z1, double x2, double z2) {
        this.world = world;
        minX = Math.min(x1, x2);
        maxX = Math.max(x1, x2);
        minZ = Math.min(z1, z2);
        maxZ = Math.max(z1, z2);
    }

    public double getMinX() {
        return minX;
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMinZ() {
        return minZ;
    }

    public double getMaxZ() {
        return maxZ;
    }

    public String getWorld() {
        return world;
    }

    public boolean contains(Region region) {
        if (region == null) return false;
        return region.getWorld().equals(world)
                && region.getMinX() >= minX && region.getMaxX() <= maxX
                && region.getMinZ() >= minZ && region.getMaxZ() <= maxZ;

    }

    public boolean contains(Location loc) {
        if (loc == null) return false;
        return loc.getWorld().getName().equals(world)
                && loc.getBlockX() > minX && loc.getBlockX() < maxX
                && loc.getBlockZ() > minZ && loc.getBlockZ() < maxZ;
    }

    public boolean overlaps(Region region) {
        return region.getWorld().equals(world) &&
                !(
                        region.getMinX() > maxX
                                || region.getMinZ() > maxZ
                                || minZ > region.getMaxX()
                                || minZ > region.getMaxZ()
                );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Region region = (Region) o;
        return Double.compare(region.minX, minX) == 0 && Double.compare(region.maxX, maxX) == 0 && Double.compare(region.minZ, minZ) == 0 && Double.compare(region.maxZ, maxZ) == 0 && Objects.equals(world, region.world);
    }
}
