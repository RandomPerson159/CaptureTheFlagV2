package com.random.captureTheFlag.listeners;

import com.random.captureTheFlag.Main;
import com.random.captureTheFlag.game.GameState;
import com.random.captureTheFlag.player.Team;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class PermissionsListener implements Listener {
    @EventHandler
    void onItemDrop(PlayerDropItemEvent ev) {
        if (Main.getInstance().getState() != GameState.GAME) {
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
        if (Main.getInstance().getState() != GameState.GAME || Main.getInstance().getState() != GameState.STARTING) {
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
    void onSaturationDecrease(FoodLevelChangeEvent ev) {
        if (Main.getInstance().getState() != GameState.GAME) {
            ev.setCancelled(true);
        }
    }

    @EventHandler
    void onDespawn(ItemDespawnEvent ev) {
        if (ev.getEntity().getType().toString().contains("_BANNER")) {
            if (Main.getInstance().getState() == GameState.GAME) {
                ev.setCancelled(true);
            }
        }
    }

    @EventHandler
    void onRightClick(PlayerInteractEvent ev) {
        if (ev.getItem() != null) {
            if (ev.getItem().getType().toString().contains("_AXE")) {
                if (ev.getClickedBlock() != null) {
                    if (ev.getClickedBlock().getType().toString().contains("_LOG")
                            || ev.getClickedBlock().getType().toString().contains("_WOOD")
                            || ev.getClickedBlock().getType().toString().contains("COPPER")
                            || ev.getClickedBlock().getType().toString().contains("_STEM")
                            || ev.getClickedBlock().getType().toString().contains("_HYPHAE")) {
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
        if ((ev.getOffHandItem() != null && ev.getOffHandItem().getType().toString().contains("_BANNER"))
                || (ev.getMainHandItem() != null && ev.getMainHandItem().getType().toString().contains("_BANNER"))) {
            ev.setCancelled(true);
        }
    }

    @EventHandler
    void onBlockPlace(BlockPlaceEvent ev) {
        if (Main.getInstance().getState() != GameState.GAME) {
            if (!ev.getPlayer().isOp()) {
                ev.setCancelled(true);
            }
            return;
        }
        if (!ev.getPlayer().isOp()) {
            ev.setCancelled(true);
        } else if (Main.getInstance().getPlayers().get(ev.getPlayer().getUniqueId()).getTeam() != Team.SPEC) {
            ev.setCancelled(true);
        }
    }

    @EventHandler
    void onBlockBurn(BlockBurnEvent ev) {
        ev.setCancelled(true);
    }

    @EventHandler
    void onBlockBlowup(BlockExplodeEvent ev) {
        ev.setCancelled(true);
    }
}
