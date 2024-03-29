package com.random.captureTheFlag.listeners;

import com.random.captureTheFlag.Main;
import com.random.captureTheFlag.game.Flag;
import com.random.captureTheFlag.game.GameState;
import com.random.captureTheFlag.player.CapturePlayer;
import com.random.captureTheFlag.player.Team;
import com.random.captureTheFlag.util.ItemBuilder;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class FlagListener implements Listener {
    private final Map<UUID, Long> interactions = new HashMap<>();

    @EventHandler
    void onInteract(PlayerInteractEvent ev) {
        if (Main.getInstance().getState() != GameState.GAME) return;
        if (ev.getClickedBlock() == null) return;
        if (!Main.getInstance().getRegion().contains(ev.getClickedBlock().getLocation())) return;

        if (ev.getAction() == Action.LEFT_CLICK_BLOCK || ev.getAction() == Action.RIGHT_CLICK_BLOCK) {
            CapturePlayer cp = Main.getInstance().getPlayers().get(ev.getPlayer().getUniqueId());
            if (cp == null) return;
            if (cp.getTeam() == Team.SPEC && !ev.getPlayer().isOp()) return;
            if (ev.getClickedBlock().getType() == Material.GRAY_STAINED_GLASS) {
                // Handle Returning
                if (ev.getItem() == null) return;
                if (!ev.getItem().getType().toString().contains("_BANNER")) return;
                for (Location flagLoc : Main.getInstance().getSettings().getMap().getFlagLocs()) {
                    if (flagLoc.getBlock().getLocation().distance(ev.getClickedBlock().getLocation()) < 1.5) {
                        for (Flag flag : Main.getInstance().getFlags()) {
                            if (flag.getHolder() == null) continue;
                            if (flag.getHolder().getPlayer().getUniqueId().equals(ev.getPlayer().getUniqueId())) {
                                if (flag.getStack().isSimilar(ev.getItem())) {
                                    if (flag.getTeam() == cp.getTeam() || (cp.getTeam() == Team.SPEC && cp.getPlayer().isOp())) {
                                        if (!getLocs(flag.getTeam()).contains(flagLoc)) {
                                            ev.getPlayer().sendMessage(ChatColor.RED + "You cannot return your flag to an enemy base!  Return this flag to an empty beam at your base!");
                                            ev.getPlayer().playSound(ev.getPlayer().getLocation(), Sound.ENTITY_BLAZE_HURT, 7, 1);
                                            return;
                                        }
                                        // Return Flag
                                        flag.put(flagLoc, cp);
                                        ev.getPlayer().getInventory().getItemInMainHand().setAmount(ev.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
                                    } else {
                                        if (!getLocs(flag.getTeam()).contains(flagLoc)) {
                                            ev.getPlayer().sendMessage(ChatColor.RED + "You cannot return your flag to an enemy base!  Return this flag to an empty beam at your base!");
                                            ev.getPlayer().playSound(ev.getPlayer().getLocation(), Sound.ENTITY_BLAZE_HURT, 7, 1);
                                            return;
                                        } else {
                                            ev.getPlayer().sendMessage(ChatColor.RED + "You need an active flag to capture an opponents!  Carry it to one of your active flags to capture it.");
                                            ev.getPlayer().playSound(ev.getPlayer().getLocation(), Sound.ENTITY_BLAZE_HURT, 7, 1);
                                        }
                                    }

                                    ev.getPlayer().removePotionEffect(PotionEffectType.GLOWING);
                                    ev.getPlayer().removePotionEffect(PotionEffectType.SLOW);

                                    boolean stillHasFlags = false;
                                    for (Flag flags : Main.getInstance().getFlags()) {
                                        if (flags.getHolder() == null) continue;
                                        if (flags.getHolder().getPlayer().getUniqueId().equals(ev.getPlayer().getUniqueId())) {
                                            stillHasFlags = true;

                                            ev.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 99999, 0));
                                            ev.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999, 0));

                                            ev.getPlayer().getInventory().setBoots(new ItemBuilder().setMaterial(Material.LEATHER_BOOTS)
                                                    .setArmorColor(cp.getTeam() == Team.RED ? Color.RED : cp.getTeam() == Team.BLUE ? Color.BLUE : cp.getTeam() == Team.LIME ? Color.LIME : Color.YELLOW)
                                                    .getItem());
                                            break;
                                        }
                                    }
                                    if (!stillHasFlags) {
                                        cp.getKit().apply(cp);
                                    }
                                    Main.getInstance().updateBoard();
                                    return;
                                }
                            }
                        }
                        return;
                        /*
                        if (flag.getStack().equals(new ItemBuilder(ev.getItem().clone()).setAmount(1).getItem())) {
                            if (flag.getTeam() == cp.getTeam() || cp.getTeam() == Team.SPEC) {
                                if (flag.getHome().getBlock().getType().toString().contains("_BANNER")) {
                                    ev.getPlayer().sendMessage(ChatColor.RED + "You must return this flag to an empty beam!");
                                    ev.getPlayer().playSound(ev.getPlayer().getLocation(), Sound.ENTITY_BLAZE_HURT, 7, 1);
                                    return;
                                }
                                if (flag.getHolder() != null) {
                                    if (flag.getHolder().getPlayer().getUniqueId().equals(cp.getPlayer().getUniqueId())) {
                                        flag.put(cp);
                                        ev.getPlayer().getInventory().getItemInMainHand().setAmount(ev.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
                                    } else {
                                        flag.put(FlagEvent.RETURN1, cp);
                                        ev.getPlayer().getInventory().getItemInMainHand().setAmount(ev.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
                                        for (Flag teamFlag : Main.getInstance().getFlags()) {
                                            if ((teamFlag.getTeam() == cp.getTeam() || cp.getTeam() == Team.SPEC) && teamFlag.getHolder() != null && teamFlag.getHolder().getPlayer().getUniqueId().equals(cp.getPlayer().getUniqueId()) && teamFlag.getHome() != flag.getHome()) {
                                                teamFlag.setHolder(null);
                                                break;
                                            }
                                        }
                                    }
                                } else {
                                    flag.put(FlagEvent.RETURN1, cp);
                                    ev.getPlayer().getInventory().getItemInMainHand().setAmount(ev.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
                                    for (Flag teamFlag : Main.getInstance().getFlags()) {
                                        if ((teamFlag.getTeam() == cp.getTeam() || cp.getTeam() == Team.SPEC) && teamFlag.getHolder() != null && teamFlag.getHolder().getPlayer().getUniqueId().equals(cp.getPlayer().getUniqueId())) {
                                            teamFlag.setHolder(null);
                                            break;
                                        }
                                    }

                                }

                                ev.getPlayer().removePotionEffect(PotionEffectType.GLOWING);
                                ev.getPlayer().removePotionEffect(PotionEffectType.SLOW);

                                boolean stillHasFlags = false;
                                for (Flag flags : Main.getInstance().getFlags()) {
                                    if (flags.getHolder() == null) continue;
                                    if (flags.getHolder().getPlayer().getUniqueId().equals(ev.getPlayer().getUniqueId())) {
                                        stillHasFlags = true;

                                        ev.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 99999, 0));
                                        ev.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999, 0));

                                        ev.getPlayer().getInventory().setBoots(new ItemBuilder().setMaterial(Material.LEATHER_BOOTS)
                                                .setArmorColor(cp.getTeam() == Team.RED ? Color.RED : cp.getTeam() == Team.BLUE ? Color.BLUE : cp.getTeam() == Team.LIME ? Color.LIME : Color.YELLOW)
                                                .getItem());
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
                            ev.getPlayer().sendMessage(ChatColor.RED + "You need to click an active flag to capture an opponent's flag!  Return a flag or go to one of your flags and click it to capture the flag.");
                            ev.getPlayer().playSound(ev.getPlayer().getLocation(), Sound.ENTITY_BLAZE_HURT, 7, 1);
                        }
                        return;
                         */
                    }
                }
            } else if (ev.getClickedBlock().getType().name().contains("_BANNER")) {
                if (interactions.get(ev.getPlayer().getUniqueId()) != null && interactions.get(ev.getPlayer().getUniqueId()) + 100L > System.currentTimeMillis()) {
                    ev.setCancelled(true);
                    return;
                }
                interactions.put(ev.getPlayer().getUniqueId(), System.currentTimeMillis());
                if (Main.getInstance().getSettings().getTeams() == 2) {
                    if (!(ev.getClickedBlock().getType() == Material.RED_BANNER
                            || ev.getClickedBlock().getType() == Material.BLUE_BANNER
                            || ev.getClickedBlock().getType() == Material.RED_STAINED_GLASS
                            || ev.getClickedBlock().getType() == Material.BLUE_STAINED_GLASS)) {
                        return;
                    }
                } else if (Main.getInstance().getSettings().getTeams() == 4) {
                    if (!(ev.getClickedBlock().getType() == Material.RED_BANNER
                            || ev.getClickedBlock().getType() == Material.BLUE_BANNER
                            || ev.getClickedBlock().getType() == Material.LIME_BANNER
                            || ev.getClickedBlock().getType() == Material.YELLOW_BANNER
                            || ev.getClickedBlock().getType() == Material.RED_STAINED_GLASS
                            || ev.getClickedBlock().getType() == Material.BLUE_STAINED_GLASS
                            || ev.getClickedBlock().getType() == Material.LIME_STAINED_GLASS
                            || ev.getClickedBlock().getType() == Material.YELLOW_STAINED_GLASS)) {
                        return;
                    }
                }
                for (Location flagLoc : Main.getInstance().getSettings().getMap().getFlagLocs()) {
                    if (flagLoc.getBlock().getLocation().distance(ev.getClickedBlock().getLocation()) < 1.5) {
                        if (ev.getItem() != null && ev.getItem().getType().toString().contains("_BANNER")) {
                            // Handle Capturing
                            if (getLocs(cp.getTeam()).contains(flagLoc) || (cp.getTeam() == Team.SPEC && cp.getPlayer().isOp())) {
                                for (Flag capturing : Main.getInstance().getFlags()) {
                                    if (capturing.getStack().isSimilar(ev.getItem()) && capturing.getHolder() != null
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

                                            ev.getPlayer().removePotionEffect(PotionEffectType.GLOWING);
                                            ev.getPlayer().removePotionEffect(PotionEffectType.SLOW);

                                            boolean stillHasFlags = false;
                                            for (Flag flags : Main.getInstance().getFlags()) {
                                                if (flags.getHolder() == null) continue;
                                                if (flags.getHolder().getPlayer().getUniqueId().equals(ev.getPlayer().getUniqueId())) {
                                                    stillHasFlags = true;

                                                    ev.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 99999, 0));
                                                    ev.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999, 0));

                                                    ev.getPlayer().getInventory().setBoots(new ItemBuilder().setMaterial(Material.LEATHER_BOOTS)
                                                            .setArmorColor(cp.getTeam() == Team.RED ? Color.RED : cp.getTeam() == Team.BLUE ? Color.BLUE : cp.getTeam() == Team.LIME ? Color.LIME : Color.YELLOW)
                                                            .getItem());
                                                }
                                            }

                                            if (!stillHasFlags) {
                                                cp.getKit().apply(cp);
                                            }

                                            Main.getInstance().updateBoard();
                                            Main.getInstance().tryEnd();
                                        } else {
                                            ev.getPlayer().sendMessage(ChatColor.RED + "You must click an empty beam to return your flag!");
                                            ev.getPlayer().playSound(ev.getPlayer().getLocation(), Sound.ENTITY_BLAZE_HURT, 7, 1);
                                        }
                                        return;
                                    }
                                }
                            } else {
                                ev.getPlayer().sendMessage(ChatColor.RED + "You must use your flag to capture an opponent's!");
                                ev.getPlayer().playSound(ev.getPlayer().getLocation(), Sound.ENTITY_BLAZE_HURT, 7, 1);
                                return;
                            }
                        } else {
                            // Handle Taking
                            if (!getLocs(cp.getTeam()).contains(flagLoc)) {
                                for (Flag taking : Main.getInstance().getFlags()) {
                                    if (taking.getHolder() != null && taking.getHolder().getPlayer().getUniqueId().equals(ev.getPlayer().getUniqueId())) {
                                        ev.getPlayer().sendMessage(ChatColor.RED + "You are already holding a flag!  You must capture or return this flag before picking up another.");
                                        ev.getPlayer().playSound(ev.getPlayer().getLocation(), Sound.ENTITY_BLAZE_HURT, 7, 1);
                                        return;
                                    }
                                }

                                ev.getPlayer().getInventory().clear();
                                ev.getPlayer().getInventory().setBoots(new ItemBuilder().setMaterial(Material.LEATHER_BOOTS)
                                        .setArmorColor(cp.getTeam() == Team.RED ? Color.RED : cp.getTeam() == Team.BLUE ? Color.BLUE : cp.getTeam() == Team.LIME ? Color.LIME : Color.YELLOW)
                                        .getItem());

                                for (Flag taking : Main.getInstance().getFlags()) {
                                    if (taking.getHome() != null && taking.getHome().equals(flagLoc)) {
                                        taking.take(cp);
                                        cp.getPlayer().getInventory().addItem(taking.getStack());
                                    }
                                }

                                ev.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 99999, 0));
                                ev.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999, 0));

                                Main.getInstance().updateBoard();
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
    }

    @EventHandler
    void onItemPickedUp(PlayerPickupItemEvent ev) {
        if (!Main.getInstance().getRegion().contains(ev.getItem().getLocation())) return;
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

                    ev.getPlayer().removePotionEffect(PotionEffectType.GLOWING);
                    ev.getPlayer().removePotionEffect(PotionEffectType.SLOW);

                    ev.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 99999, 0));
                    ev.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999, 0));

                    ev.getPlayer().getInventory().setBoots(new ItemBuilder().setMaterial(Material.LEATHER_BOOTS).setUnbreakable(true)
                            .setArmorColor(cp.getTeam() == Team.RED ? Color.RED : cp.getTeam() == Team.BLUE ? Color.BLUE : cp.getTeam() == Team.LIME ? Color.LIME : Color.YELLOW)
                            .getItem());
                    return;
                }
            }
        }
    }

    @EventHandler
    void onDamage(EntityDamageByEntityEvent ev) {
        if (!Main.getInstance().getRegion().contains(ev.getEntity().getLocation())) return;
        if (ev.getEntity().getType() == EntityType.DROPPED_ITEM) {
            Item item = (Item) ev.getEntity();
            if (item.getItemStack().getType().toString().contains("_BANNER")) {
                ev.setCancelled(true);
            }
        }
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
            SpectralArrow arrow = (SpectralArrow) ev.getDamager();
            if (!(arrow.getShooter() instanceof Player)) return;
            CapturePlayer attacker = Main.getInstance().getPlayers().get(((Player) arrow.getShooter()).getUniqueId());
            if (attacker == null) {
                ev.setCancelled(true);
            } else if ((attacker.getTeam() == Team.SPEC && !attacker.getPlayer().isOp()) || attacker.getTeam() == cp.getTeam()) {
                ev.setCancelled(true);
            }
        }
        if (ev.getDamager() instanceof TippedArrow) {
            TippedArrow arrow = (TippedArrow) ev.getDamager();
            if (!(arrow.getShooter() instanceof Player)) return;
            CapturePlayer attacker = Main.getInstance().getPlayers().get(((Player) arrow.getShooter()).getUniqueId());
            if (attacker == null) {
                ev.setCancelled(true);
            } else if ((attacker.getTeam() == Team.SPEC && !attacker.getPlayer().isOp()) || attacker.getTeam() == cp.getTeam()) {
                ev.setCancelled(true);
            }
        }
        if (ev.getDamager() instanceof SplashPotion) {
            SplashPotion arrow = (SplashPotion) ev.getDamager();
            if (!(arrow.getShooter() instanceof Player)) return;
            CapturePlayer attacker = Main.getInstance().getPlayers().get(((Player) arrow.getShooter()).getUniqueId());
            if (attacker == null) {
                ev.setCancelled(true);
            } else if ((attacker.getTeam() == Team.SPEC && !attacker.getPlayer().isOp()) || attacker.getTeam() == cp.getTeam()) {
                ev.setCancelled(true);
            }
        }
        if (ev.getDamager() instanceof Firework) {
            Firework arrow = (Firework) ev.getDamager();
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
        if (!Main.getInstance().getPlayers().containsKey(ev.getEntity().getUniqueId())) return;
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
                        if (flags.getHolder().getPlayer().getUniqueId().equals(killer.getUniqueId())) {
                            hasFlags = true;
                        }
                    }
                    if (!hasFlags) {
                        killer.getInventory().clear();
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

                    p.removePotionEffect(PotionEffectType.GLOWING);
                    p.removePotionEffect(PotionEffectType.SLOW);

                    killer.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 99999, 0));
                    killer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999, 0));

                    killer.getInventory().setBoots(new ItemBuilder().setMaterial(Material.LEATHER_BOOTS).setUnbreakable(true)
                            .setArmorColor(cpKiller.getTeam() == Team.RED ? Color.RED : cpKiller.getTeam() == Team.BLUE ? Color.BLUE : cpKiller.getTeam() == Team.LIME ? Color.LIME : Color.YELLOW)
                            .getItem());

                    // Flag picked up from kill
                }
            }
        } else {
            for (Flag flag : Main.getInstance().getFlags()) {
                if (flag.getHolder() != null && flag.getHolder().getPlayer().getUniqueId().equals(cp.getPlayer().getUniqueId())) {

                    flag.drop(p.getLocation(), cp);

                    p.removePotionEffect(PotionEffectType.GLOWING);
                    p.removePotionEffect(PotionEffectType.SLOW);
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
        if (!Main.getInstance().getPlayers().containsKey(ev.getPlayer().getUniqueId())) return;
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

    private List<Location> getLocs(Team team) {
        if (team == Team.RED) return Main.getInstance().getSettings().getMap().getRedFlagLocs();
        if (team == Team.BLUE) return Main.getInstance().getSettings().getMap().getBlueFlagLocs();
        if (team == Team.LIME) return Main.getInstance().getSettings().getMap().getGreenFlagLocs();
        if (team == Team.YELLOW) return Main.getInstance().getSettings().getMap().getYellowFlagLocs();
        return new ArrayList<>();
    }

}