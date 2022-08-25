package com.random.captureTheFlag.player;

import com.random.captureTheFlag.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public enum KitType {
    MID_FIELD(new ItemBuilder().setMaterial(Material.IRON_AXE).setUnbreakable(true).setDisplayName(ChatColor.GOLD + "Mid-field Kit").getItem(), new ItemStack[] {
            new ItemBuilder().setMaterial(Material.LEATHER_HELMET).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).setUnbreakable(true).getItem(),
            new ItemBuilder().setMaterial(Material.LEATHER_CHESTPLATE).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).setUnbreakable(true).getItem(),
            new ItemBuilder().setMaterial(Material.IRON_LEGGINGS).setUnbreakable(true).getItem(),
            new ItemBuilder().setMaterial(Material.IRON_BOOTS).setUnbreakable(true).getItem()
    }, new ItemBuilder().setMaterial(Material.IRON_AXE).setUnbreakable(true).getItem(),
            new ItemBuilder().setMaterial(Material.IRON_SWORD).setUnbreakable(true).addEnchantment(Enchantment.DAMAGE_ALL, 1).getItem(),
            new ItemBuilder().setMaterial(Material.CROSSBOW).addEnchantment(Enchantment.MULTISHOT, 1).setUnbreakable(true).getItem(),
            new ItemBuilder().setMaterial(Material.SHIELD).setUnbreakable(true).getItem(),
            new ItemBuilder().setMaterial(Material.COOKED_BEEF).setAmount(8).getItem(),
            new ItemBuilder().setMaterial(Material.ARROW).setAmount(4).getItem(),
            new ItemBuilder().setMaterial(Material.GOLDEN_APPLE).getItem()
            /* fireworks */
    ),
    DEFENSE(new ItemBuilder().setMaterial(Material.SHIELD).setUnbreakable(true).setDisplayName(ChatColor.GOLD + "Defense Kit").getItem(), new ItemStack[] {
            new ItemBuilder().setMaterial(Material.LEATHER_HELMET).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).setUnbreakable(true).getItem(),
            new ItemBuilder().setMaterial(Material.LEATHER_CHESTPLATE).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).setUnbreakable(true).getItem(),
            new ItemBuilder().setMaterial(Material.IRON_LEGGINGS).setUnbreakable(true).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).getItem(),
            new ItemBuilder().setMaterial(Material.IRON_BOOTS).setUnbreakable(true).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).getItem()
    }, new ItemBuilder().setMaterial(Material.WOODEN_AXE).setUnbreakable(true).getItem(),
            new ItemBuilder().setMaterial(Material.STONE_SWORD).setUnbreakable(true).getItem(),
            new ItemBuilder().setMaterial(Material.CROSSBOW).setUnbreakable(true).getItem(),
            new ItemBuilder().setMaterial(Material.SHIELD).setUnbreakable(true).getItem(),
            new ItemBuilder().setMaterial(Material.GOLDEN_CARROT).setAmount(3).getItem()
    ),
    FLAG_STEALER(new ItemBuilder().setMaterial(Material.IRON_SWORD).setDisplayName(ChatColor.GOLD + "Flag-stealer Kit").getItem(), new ItemStack[] {
            new ItemBuilder().setMaterial(Material.LEATHER_HELMET).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).setUnbreakable(true).getItem(),
            new ItemBuilder().setMaterial(Material.LEATHER_CHESTPLATE).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).setUnbreakable(true).getItem(),
            new ItemBuilder().setMaterial(Material.CHAINMAIL_LEGGINGS).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).setUnbreakable(true).getItem(),
            new ItemBuilder().setMaterial(Material.CHAINMAIL_BOOTS).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).setUnbreakable(true).getItem()
    }, new ItemBuilder().setMaterial(Material.IRON_SWORD).addEnchantment(Enchantment.DAMAGE_ALL, 1).setUnbreakable(true).getItem(),
            new ItemBuilder().setMaterial(Material.STONE_AXE).setUnbreakable(true).getItem(),
            new ItemBuilder().setMaterial(Material.GOLDEN_CARROT).setAmount(3).getItem(),
            new ItemBuilder().setMaterial(Material.COOKED_BEEF).setAmount(8).getItem(),
            new ItemBuilder().setMaterial(Material.GOLDEN_APPLE).setAmount(2).getItem()
    ),
    BOW(new ItemBuilder().setMaterial(Material.BOW).setDisplayName(ChatColor.GOLD + "Long-range Kit").getItem(), new ItemStack[] {
            new ItemBuilder().setMaterial(Material.LEATHER_HELMET).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).setUnbreakable(true).getItem(),
            new ItemBuilder().setMaterial(Material.LEATHER_CHESTPLATE).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).setUnbreakable(true).getItem(),
            new ItemBuilder().setMaterial(Material.CHAINMAIL_LEGGINGS).setUnbreakable(true).getItem(),
            new ItemBuilder().setMaterial(Material.CHAINMAIL_BOOTS).setUnbreakable(true).getItem()
    }, new ItemBuilder().setMaterial(Material.STONE_SWORD).setUnbreakable(true).getItem(),
            new ItemBuilder().setMaterial(Material.CROSSBOW).setUnbreakable(true).addEnchantment(Enchantment.PIERCING, 1).getItem(),
            new ItemBuilder().setMaterial(Material.BOW).addEnchantment(Enchantment.ARROW_DAMAGE, 1).setUnbreakable(true).getItem(),
            new ItemBuilder().setMaterial(Material.ARROW).setAmount(12).getItem(),
            new ItemBuilder().setMaterial(Material.SHIELD).setUnbreakable(true).getItem(),
            new ItemBuilder().setMaterial(Material.COOKED_BEEF).setAmount(8).getItem()
    );

    private final ItemStack item;
    private final ItemStack[] armor;
    private final ItemStack[] items;

    KitType(ItemStack item, ItemStack[] armor, ItemStack... items) {
        this.item = item;
        this.armor = armor;
        this.items = items;
    }

    public void apply(CapturePlayer cp) {
        Player player = cp.getPlayer();
        player.getInventory().setHelmet(new ItemBuilder(armor[0])
                .setArmorColor(cp.getTeam() == Team.RED ? Color.RED : cp.getTeam() == Team.BLUE ? Color.BLUE : cp.getTeam() == Team.LIME ? Color.LIME : Color.YELLOW)
                .getItem());
        player.getInventory().setChestplate(new ItemBuilder(armor[1])
                .setArmorColor(cp.getTeam() == Team.RED ? Color.RED : cp.getTeam() == Team.BLUE ? Color.BLUE : cp.getTeam() == Team.LIME ? Color.LIME : Color.YELLOW)
                .getItem());
        player.getInventory().setLeggings(armor[2]);
        player.getInventory().setBoots(armor[3]);
        player.getInventory().addItem(items);
        if (this == KitType.DEFENSE) {
            player.getInventory().addItem(
                    new ItemBuilder().setMaterial(Material.FIREWORK_ROCKET)
                            .setFirework(cp.getTeam() == Team.RED ? Color.RED : cp.getTeam() == Team.BLUE ? Color.BLUE : cp.getTeam() == Team.LIME ? Color.LIME : Color.YELLOW)
                            .setAmount(1).getItem()
            );
        }
    }

    public ItemStack[] getArmor() {
        return armor;
    }

    public ItemStack[] getItems() {
        return items;
    }

    public ItemStack getItem() {
        return item;
    }
}
