package com.random.captureTheFlag.listeners;

import com.random.captureTheFlag.Main;
import com.random.captureTheFlag.game.GameState;
import com.random.captureTheFlag.player.Team;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;

public class PermissionsListener implements Listener {
    @EventHandler
    void onItemDrop(PlayerDropItemEvent ev) {
        if (!Main.getInstance().getRegion().contains(ev.getItemDrop().getLocation())) return;
        if (Main.getInstance().getState() == GameState.WAIT) {
            if (!ev.getPlayer().isOp()) {
                ev.setCancelled(true);
            }
            return;
        }
        if (!ev.getPlayer().isOp()) {
            ev.setCancelled(true);
        } else if (Main.getInstance().getState() == GameState.GAME || Main.getInstance().getState() == GameState.STARTING) {
            ev.setCancelled(true);
        }
    }

    @EventHandler
    void onBlockBreak(BlockBreakEvent ev) {
        if (!Main.getInstance().getRegion().contains(ev.getBlock().getLocation())) return;
        if (Main.getInstance().getState() == GameState.WAIT) {
            if (!ev.getPlayer().isOp()) {
                ev.setCancelled(true);
            }
            return;
        }
        if (!ev.getPlayer().isOp()) {
            ev.setCancelled(true);
        } else if (Main.getInstance().getPlayers().get(ev.getPlayer().getUniqueId()) != null
                && Main.getInstance().getPlayers().get(ev.getPlayer().getUniqueId()).getTeam() != Team.SPEC) {
            ev.setCancelled(true);
        }
    }

    @EventHandler
    void onDespawn(ItemDespawnEvent ev) {
        if (!Main.getInstance().getRegion().contains(ev.getEntity().getLocation())) return;
        if (ev.getEntity().getItemStack().getType().toString().contains("_BANNER")) {
            if (Main.getInstance().getState() == GameState.GAME) {
                ev.setCancelled(true);
            }
        }
    }

    @EventHandler
    void onRightClick(PlayerInteractEvent ev) {
        if (!Main.getInstance().getRegion().contains(ev.getPlayer().getLocation())) return;
        if (ev.getItem() != null) {
            if (ev.getItem().getType().toString().contains("_AXE")) {
                if (ev.getAction() != Action.RIGHT_CLICK_BLOCK) return;
                if (ev.getClickedBlock() != null) {
                    if (ev.getClickedBlock().getType().toString().contains("_LOG")
                            || ev.getClickedBlock().getType().toString().contains("_WOOD")
                            || ev.getClickedBlock().getType().toString().contains("COPPER")
                            || ev.getClickedBlock().getType().toString().contains("_STEM")
                            || ev.getClickedBlock().getType().toString().contains("_HYPHAE")
                            || ev.getClickedBlock().getType().toString().contains("_BED")
                            || ev.getClickedBlock().getType().toString().contains("ANVIL")
                            || ev.getClickedBlock().getType().toString().contains("TRAPDOOR")
                            || ev.getClickedBlock().getType() == Material.DISPENSER
                            || ev.getClickedBlock().getType() == Material.SMITHING_TABLE
                            || ev.getClickedBlock().getType() == Material.CARTOGRAPHY_TABLE
                            || ev.getClickedBlock().getType() == Material.CHEST
                            || ev.getClickedBlock().getType() == Material.ENDER_CHEST
                            || ev.getClickedBlock().getType() == Material.DROPPER
                            || ev.getClickedBlock().getType() == Material.GRINDSTONE
                            || ev.getClickedBlock().getType() == Material.CAMPFIRE
                            || (ev.getClickedBlock().getType() == Material.BARREL && !ev.getClickedBlock().getLocation().equals(new Location(Bukkit.getWorld("CTF"), 188, -42, -60)))
                    ) {
                        if (!ev.getPlayer().isOp()) {
                            ev.setCancelled(true);
                        } else if (Main.getInstance().getPlayers().get(ev.getPlayer().getUniqueId()).getTeam() != Team.SPEC) {
                            ev.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    void onSwitchHands(PlayerSwapHandItemsEvent ev) {
        if (!Main.getInstance().getRegion().contains(ev.getPlayer().getLocation())) return;
        if ((ev.getOffHandItem() != null && ev.getOffHandItem().getType().name().contains("_BANNER"))) {
            ev.setCancelled(true);
        }
    }

    @EventHandler
    void onSwitchHands(InventoryClickEvent ev) {
        if (!Main.getInstance().getRegion().contains(ev.getWhoClicked().getLocation())) return;
        if (ev.getCurrentItem() != null && ev.getCurrentItem().getType().name().contains("_BANNER")) {
            ev.setCancelled(true);
        }
    }

    @EventHandler
    void onBlockPlace(BlockPlaceEvent ev) {
        if (!Main.getInstance().getRegion().contains(ev.getBlock().getLocation())) return;
        if (Main.getInstance().getState() == GameState.WAIT) {
            if (!ev.getPlayer().isOp()) {
                ev.setCancelled(true);
            }
        } else if (!ev.getPlayer().isOp()) {
            ev.setCancelled(true);
        } else if (Main.getInstance().getPlayers().get(ev.getPlayer().getUniqueId()).getTeam() != null
                && Main.getInstance().getPlayers().get(ev.getPlayer().getUniqueId()).getTeam() != Team.SPEC) {
            ev.setCancelled(true);
        }
    }

    @EventHandler
    void onBlockBurn(BlockBurnEvent ev) {
        if (!Main.getInstance().getRegion().contains(ev.getBlock().getLocation())) return;
        ev.setCancelled(true);
    }

    @EventHandler
    void onBlockBlowup(BlockExplodeEvent ev) {
        if (!Main.getInstance().getRegion().contains(ev.getBlock().getLocation())) return;
        ev.setCancelled(true);
    }

    @EventHandler
    void onArrowPickup(PlayerPickupArrowEvent ev) {
        if (!Main.getInstance().getRegion().contains(ev.getPlayer().getLocation())) return;
        if (Main.getInstance().getState() == GameState.WAIT) {
            if (!ev.getPlayer().isOp()) {
                ev.setCancelled(true);
            }
        } else if (!ev.getPlayer().isOp()) {
            ev.setCancelled(true);
        } else if (Main.getInstance().getPlayers().get(ev.getPlayer().getUniqueId()).getTeam() != null
                && Main.getInstance().getPlayers().get(ev.getPlayer().getUniqueId()).getTeam() != Team.SPEC) {
            ev.setCancelled(true);
        }
    }

    @EventHandler
    void onFarmland(PlayerInteractEvent ev) {
        if (!Main.getInstance().getRegion().contains(ev.getPlayer().getLocation())) return;
        if (ev.getAction() == Action.PHYSICAL) {
            if (ev.getClickedBlock() == null) return;
            if (ev.getClickedBlock().getType() == Material.FARMLAND) {
                ev.setCancelled(true);
            }
        }
    }

    @EventHandler
    void onHarvest(PlayerHarvestBlockEvent ev) {
        if (!Main.getInstance().getRegion().contains(ev.getPlayer().getLocation())) return;
        if (!ev.getPlayer().isOp()) {
            ev.setCancelled(true);
        } else if (Main.getInstance().getPlayers().get(ev.getPlayer().getUniqueId()).getTeam() != null
                && Main.getInstance().getPlayers().get(ev.getPlayer().getUniqueId()).getTeam() != Team.SPEC) {
            ev.setCancelled(true);
        }
    }
}
