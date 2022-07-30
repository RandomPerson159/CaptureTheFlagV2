package com.random.captureTheFlag.listeners;

import com.random.captureTheFlag.Main;
import com.random.captureTheFlag.game.Flag;
import com.random.captureTheFlag.game.FlagEvent;
import com.random.captureTheFlag.game.GameState;
import com.random.captureTheFlag.player.CapturePlayer;
import com.random.captureTheFlag.player.Team;
import com.random.captureTheFlag.util.ItemBuilder;
import org.bukkit.*;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.SpectralArrow;
import org.bukkit.entity.TippedArrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FlagListener implements Listener {
    private final Map<UUID, Long> interactions = new HashMap<>();

    @EventHandler
    void onInteract(PlayerInteractEvent ev) {
        if (Main.getInstance().getState() != GameState.GAME) {
            return;
        }
        final Long lastInteraction = interactions.get(ev.getPlayer().getUniqueId());
        if(lastInteraction != null && lastInteraction + 100L > System.currentTimeMillis()) {
            ev.setCancelled(true);
            return;
        }
        interactions.put(ev.getPlayer().getUniqueId(), System.currentTimeMillis());
        if (ev.getClickedBlock() == null) return;
        CapturePlayer cp = Main.getInstance().getPlayers().get(ev.getPlayer().getUniqueId());
        if (cp == null) return;
        if (cp.getTeam() == Team.SPEC && !ev.getPlayer().isOp()) return;
        if (ev.getClickedBlock().getType() == Material.GRAY_STAINED_GLASS) {
            if (ev.getItem() == null) return;
            if (!ev.getItem().getType().toString().contains("_BANNER")) return;
            for (Flag flag : Main.getInstance().getFlags()) {
                if (flag.getHome().clone().getBlock().getLocation().subtract(0, 1, 0).distance(ev.getClickedBlock().getLocation()) < 1) {
                    if (flag.getStack().equals(new ItemBuilder(ev.getItem().clone()).setAmount(1).getItem())) {
                        if (flag.getTeam() == cp.getTeam() || cp.getTeam() == Team.SPEC) {
                            if (flag.getHolder() != null) {
                                if (flag.getHolder().getPlayer().getUniqueId().equals(cp.getPlayer().getUniqueId())) {
                                    flag.put(FlagEvent.RETURN, cp);
                                    ev.getPlayer().getInventory().getItemInMainHand().setAmount(ev.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);

                                } else {
                                    flag.put(FlagEvent.RETURN1, cp);
                                    ev.getPlayer().getInventory().getItemInMainHand().setAmount(ev.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
                                    for (Flag teamFlag : Main.getInstance().getFlags()) {
                                        if (teamFlag.getTeam() == cp.getTeam() || cp.getTeam() == Team.SPEC && teamFlag.getHolder() != null && teamFlag.getHolder().getPlayer().getUniqueId().equals(cp.getPlayer().getUniqueId())) {
                                            teamFlag.setHolder(null);
                                        }
                                    }
                                }
                            } else {
                                flag.put(FlagEvent.RETURN1, cp);
                                ev.getPlayer().getInventory().getItemInMainHand().setAmount(ev.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
                            }
                            boolean stillHasFlags = false;
                            for (Flag flags : Main.getInstance().getFlags()) {
                                if (flags.getHolder() == null) continue;
                                if (flags.getHolder().getPlayer().getUniqueId().equals(ev.getPlayer().getUniqueId())) {
                                    stillHasFlags = true;
                                }
                            }
                            if (!stillHasFlags) {
                                cp.getKit().apply(cp);
                            }
                        } else {
                            ev.getPlayer().sendMessage(ChatColor.RED + "You cannot return an opponent's flag!");
                            ev.getPlayer().playSound(ev.getPlayer().getLocation(), Sound.ENTITY_BLAZE_HURT, 7, 1);
                        }
                        return;
                    } else if (flag.getTeam() != cp.getTeam()) {
                        ev.getPlayer().sendMessage(ChatColor.RED + "Wrong beam!  You can only return this flag to your base!");
                        ev.getPlayer().playSound(ev.getPlayer().getLocation(), Sound.ENTITY_BLAZE_HURT, 7, 1);
                    } else {
                        ev.getPlayer().sendMessage(ChatColor.RED + "You need to click an active flag to capture an opponent's flag!");
                        ev.getPlayer().playSound(ev.getPlayer().getLocation(), Sound.ENTITY_BLAZE_HURT, 7, 1);
                    }
                    return;
                }
            }
        } else if (ev.getClickedBlock().getType().toString().contains("_BANNER")) {
            for (Flag flag : Main.getInstance().getFlags()) {
                if (flag.getHome().clone().getBlock().getLocation().distance(ev.getClickedBlock().getLocation()) < 1) {
                    if (ev.getItem() != null && ev.getItem().getType().toString().contains("_BANNER")) {
                        // Capturing
                        if (flag.getTeam() == cp.getTeam() || cp.getTeam() == Team.SPEC) {
                            for (Flag capturing : Main.getInstance().getFlags()) {
                                if (capturing.getStack().equals(new ItemBuilder(ev.getItem().clone()).setAmount(1).getItem()) && capturing.getHolder() != null
                                        && capturing.getHolder().getPlayer().getUniqueId().equals(cp.getPlayer().getUniqueId())) {
                                    if (capturing.getTeam() != cp.getTeam()) {
                                        ev.getPlayer().getInventory().getItemInMainHand().setAmount(ev.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
                                        capturing.setHolder(null);

                                        // Flag captured
                                        for (Player all : Bukkit.getOnlinePlayers()) {
                                            all.sendMessage(ChatColor.GRAY + "[====================================================]\n"
                                                    + ChatColor.GOLD + "                    Flag Captured\n \n"
                                                    + cp.getTeam().getColor() + "     " + ev.getPlayer().getName() + ChatColor.GRAY + " captured "
                                                    + capturing.getTeam().getColor() + capturing.getTeam().getName() + ChatColor.GRAY + "'s flag!\n \n"
                                                    + ChatColor.GRAY + "[====================================================]");
                                            all.playSound(all.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 7, 1);
                                        }

                                        boolean stillHasFlags = false;
                                        for (Flag flags : Main.getInstance().getFlags()) {
                                            if (flags.getHolder() == null) continue;
                                            if (flags.getHolder().getPlayer().getUniqueId().equals(ev.getPlayer().getUniqueId())) {
                                                stillHasFlags = true;
                                            }
                                        }
                                        if (!stillHasFlags) {
                                            cp.getKit().apply(cp);
                                        }

                                        Main.getInstance().tryEnd();
                                    } else {
                                        ev.getPlayer().sendMessage(ChatColor.RED + "You must click an empty beam to return your flag!");
                                        ev.getPlayer().playSound(ev.getPlayer().getLocation(), Sound.ENTITY_BLAZE_HURT, 7, 1);
                                    }
                                    return;
                                }
                            }
                        } else {
                            ev.getPlayer().sendMessage(ChatColor.RED + "You must use your flag to capture an opponent's flag!");
                            ev.getPlayer().playSound(ev.getPlayer().getLocation(), Sound.ENTITY_BLAZE_HURT, 7, 1);
                        }
                    } else {
                        // Taking
                        if (flag.getTeam() != cp.getTeam()) {
                            for (Flag taking : Main.getInstance().getFlags()) {
                                if (taking.getHolder() != null && taking.getHolder().getPlayer().getUniqueId().equals(ev.getPlayer().getUniqueId())) {
                                    ev.getPlayer().sendMessage(ChatColor.RED + "You are already holding a flag!  You must capture or return this flag before picking up another.");
                                    ev.getPlayer().playSound(ev.getPlayer().getLocation(), Sound.ENTITY_BLAZE_HURT, 7, 1);
                                    return;
                                }
                            }
                            flag.take(cp);

                            boolean hasFlags = false;
                            for (Flag flags : Main.getInstance().getFlags()) {
                                if (flags.getHolder() == null) continue;
                                if (flags.getHolder().getPlayer().getUniqueId().equals(ev.getPlayer().getUniqueId())) {
                                    hasFlags = true;
                                }
                            }
                            if (!hasFlags) {
                                ev.getPlayer().getInventory().clear();
                            }

                            cp.getPlayer().getInventory().addItem(flag.getStack());
                        } else {
                            ev.getPlayer().sendMessage(ChatColor.RED + "You cannot take your own flag!");
                            ev.getPlayer().playSound(ev.getPlayer().getLocation(), Sound.ENTITY_BLAZE_HURT, 7, 1);
                            return;
                        }
                    }
                    return;
                }
            }
        }
    }

    @EventHandler
    void onItemPickedUp(PlayerPickupItemEvent ev) {
        if (!ev.getItem().getItemStack().getType().toString().contains("_BANNER")) return;
        if (Main.getInstance().getState() != GameState.GAME) {
            return;
        }
        CapturePlayer cp = Main.getInstance().getPlayers().get(ev.getPlayer().getUniqueId());
        if (cp == null) {
            ev.setCancelled(true);
            return;
        }
        if (cp.getTeam() == Team.SPEC) {
            ev.setCancelled(true);
            return;
        }
        for (Flag flag : Main.getInstance().getFlags()) {
            if (flag.isDropped() && flag.getItem().getUniqueId().equals(ev.getItem().getUniqueId())) {
                if (flag.getHolder() == null) {
                    boolean hasFlags = false;
                    for (Flag flags : Main.getInstance().getFlags()) {
                        if (flags.getHolder() == null) continue;
                        if (flags.getHolder().getPlayer().getUniqueId().equals(ev.getPlayer().getUniqueId())) {
                            hasFlags = true;
                        }
                    }
                    if (!hasFlags) {
                        ev.getPlayer().getInventory().clear();
                    }

                    ev.getPlayer().getInventory().addItem(flag.getStack());
                    flag.setHolder(cp);
                    flag.setDropped(false);
                    for (Player all : Bukkit.getOnlinePlayers()) {
                        all.sendMessage(ChatColor.GRAY + "[====================================================]\n"
                                + ChatColor.GOLD + "                    Flag Picked Up\n \n"
                                + cp.getTeam().getColor() + "     " + ev.getPlayer().getName() + ChatColor.GRAY + " picked up "
                                + flag.getTeam().getColor() + flag.getTeam().getName() + ChatColor.GRAY + "'s flag!\n \n"
                                + ChatColor.GRAY + "[====================================================]");
                        all.playSound(all.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 5, 1);
                    }
                    return;
                }
            }
        }
    }

    @EventHandler
    void onDamage(EntityDamageByEntityEvent ev) {
        if (!(ev.getEntity() instanceof Player)) return;
        Player p = (Player) ev.getEntity();
        CapturePlayer cp = Main.getInstance().getPlayers().get(p.getUniqueId());
        if (cp == null) return;
        if (Main.getInstance().getWait().distance(ev.getDamager().getLocation()) < 10) {
            ev.setCancelled(true);
            return;
        }
        if (ev.getDamager() instanceof Player) {
            CapturePlayer attacker = Main.getInstance().getPlayers().get(ev.getDamager().getUniqueId());
            if (attacker == null) {
                ev.setCancelled(true);
                return;
            } else if ((attacker.getTeam() == Team.SPEC && !attacker.getPlayer().isOp()) || attacker.getTeam() == cp.getTeam()) {
                ev.setCancelled(true);
                return;
            }
            return;
        }
        if (ev.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) ev.getDamager();
            if (!(arrow.getShooter() instanceof Player)) return;
            CapturePlayer attacker = Main.getInstance().getPlayers().get(((Player) arrow.getShooter()).getUniqueId());
            if (attacker == null) {
                ev.setCancelled(true);
            } else if ((attacker.getTeam() == Team.SPEC && !attacker.getPlayer().isOp()) || attacker.getTeam() == cp.getTeam()) {
                ev.setCancelled(true);
            }
        }
        if (ev.getDamager() instanceof SpectralArrow) {
            Arrow arrow = (Arrow) ev.getDamager();
            if (!(arrow.getShooter() instanceof Player)) return;
            CapturePlayer attacker = Main.getInstance().getPlayers().get(((Player) arrow.getShooter()).getUniqueId());
            if (attacker == null) {
                ev.setCancelled(true);
            } else if ((attacker.getTeam() == Team.SPEC && !attacker.getPlayer().isOp()) || attacker.getTeam() == cp.getTeam()) {
                ev.setCancelled(true);
            }
        }
        if (ev.getDamager() instanceof TippedArrow) {
            Arrow arrow = (Arrow) ev.getDamager();
            if (!(arrow.getShooter() instanceof Player)) return;
            CapturePlayer attacker = Main.getInstance().getPlayers().get(((Player) arrow.getShooter()).getUniqueId());
            if (attacker == null) {
                ev.setCancelled(true);
            } else if ((attacker.getTeam() == Team.SPEC && !attacker.getPlayer().isOp()) || attacker.getTeam() == cp.getTeam()) {
                ev.setCancelled(true);
            }
        }

    }

    @EventHandler
    void onPlayerDeath(PlayerDeathEvent ev) {
        Player p = ev.getEntity();
        CapturePlayer cp = Main.getInstance().getPlayers().get(p.getUniqueId());
        if (cp == null) {
            ev.setDeathMessage(null);
            return;
        }
        if (Main.getInstance().getState() != GameState.GAME) return;
        if (p.getKiller() != null) {
            Player killer = p.getKiller();
            CapturePlayer cpKiller = Main.getInstance().getPlayers().get(p.getKiller().getUniqueId());
            ev.setDeathMessage(cp.getTeam().getColor() + p.getName() + ChatColor.GRAY + " was killed by " + cpKiller.getTeam().getColor() + killer.getName() + ChatColor.GRAY + ".");

            for (Flag flag : Main.getInstance().getFlags()) {
                if (flag.getHolder() != null && flag.getHolder().getPlayer().getUniqueId().equals(cp.getPlayer().getUniqueId())) {
                    boolean hasFlags = false;
                    for (Flag flags : Main.getInstance().getFlags()) {
                        if (flags.getHolder() == null) continue;
                        if (flags.getHolder().getPlayer().getUniqueId().equals(p.getUniqueId())) {
                            hasFlags = true;
                        }
                    }
                    if (!hasFlags) {
                        p.getInventory().clear();
                    }

                    killer.getInventory().addItem(flag.getStack());
                    flag.setHolder(cpKiller);

                    for (Player all : Bukkit.getOnlinePlayers()) {
                        all.sendMessage(ChatColor.GRAY + "[====================================================]\n"
                                + ChatColor.GOLD + "                    Flag Picked Up\n \n"
                                + cpKiller.getTeam().getColor() + "     " + killer.getName() + ChatColor.GRAY + " killed "
                                + cp.getTeam().getColor() + p.getName() + ChatColor.GRAY + " and picked up " + flag.getTeam().getColor() + flag.getTeam().getName()
                                + " Team" + ChatColor.GRAY + "'s flag!\n \n"
                                + ChatColor.GRAY + "[====================================================]");
                        all.playSound(all.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 5, 1);
                    }
                    // Flag picked up from kill
                }
            }
        } else {
            for (Flag flag : Main.getInstance().getFlags()) {
                if (flag.getHolder() != null && flag.getHolder().getPlayer().getUniqueId().equals(cp.getPlayer().getUniqueId())) {
                    flag.drop(p.getLocation(), cp);
                    // Flag dropped
                }
            }
            ev.setDeathMessage(cp.getTeam().getColor() + p.getName() + ChatColor.GRAY + " died.");
        }
        ev.setKeepInventory(true);
        ev.getDrops().clear();
        ev.setDroppedExp(0);
        p.getInventory().clear();
    }

    @EventHandler
    void respawn(PlayerRespawnEvent ev) {
        if (Main.getInstance().getState() != GameState.GAME) {
            if (Main.getInstance().getState() == GameState.STARTING) {
                ev.getPlayer().teleport(Main.getInstance().getWait());
            } else {
                ev.getPlayer().teleport(Main.getInstance().getLocation("lobby"));
            }
            return;
        }
        Player p = ev.getPlayer();
        CapturePlayer cp = Main.getInstance().getPlayers().get(p.getUniqueId());
        if (cp == null) return;
        if (cp.getTeam() == Team.SPEC) return;
        ev.setRespawnLocation(Main.getInstance().getWait());
        p.teleport(Main.getInstance().getWait());
        p.setSaturation(20);
        p.getInventory().clear();
        new BukkitRunnable() {
            int timer = Main.getInstance().getSettings().getRespawn();
            @Override
            public void run() {
                if (Main.getInstance().getState() != GameState.GAME) {
                    this.cancel();
                    return;
                }
                if (timer == Main.getInstance().getSettings().getRespawn()) {
                    p.sendMessage(ChatColor.YELLOW + "Respawning in " + ChatColor.RED + timer + ChatColor.YELLOW + " seconds...");
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_SNARE, 7, 1);
                } else if (timer <= 5 && timer > 0) {
                    p.sendMessage(ChatColor.YELLOW + "Respawning in " + ChatColor.RED + timer + ChatColor.YELLOW + " seconds...");
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_SNARE, 7, 1);
                } else if (timer == 0) {
                    p.sendMessage(ChatColor.GREEN + "Respawning...");
                    p.teleport(cp.getTeam().getSpawn());
                    cp.getKit().apply(cp);
                    this.cancel();
                }
                timer--;
            }
        }.runTaskTimer(Main.getInstance(), 20, 20);
    }
}
