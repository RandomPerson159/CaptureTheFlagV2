package com.random.captureTheFlag.util;


import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

import javax.xml.stream.events.Attribute;
import java.util.Arrays;

public class ItemBuilder {

	private final ItemStack item;

	public ItemBuilder() {
		item = new ItemStack(Material.APPLE, 1);
	}

	public ItemBuilder(ItemStack is) {
		item = is;
	}

	public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
		item.addUnsafeEnchantment(enchantment, level);
		return this;
	}

	public ItemStack getItem() {
		return item;
	}

	public ItemBuilder setAmount(int amount) {
		item.setAmount(amount);
		return this;
	}

	public ItemBuilder setDisplayName(String name) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return this;
	}

	public ItemBuilder setFirework(Color color) {
		FireworkMeta meta = (FireworkMeta) item.getItemMeta();
		meta.setPower(1);
		for (int i = 0; i < 7; i++) {
			meta.addEffect(FireworkEffect.builder().withFade(Color.GRAY).withColor(color).withColor(color).withColor(color)
					.withColor(color).withColor(color).withColor(color).with(FireworkEffect.Type.BALL_LARGE).build());
		}
		item.setItemMeta(meta);
		return this;
	}

	public ItemBuilder setArmorColor(Color color) {
		LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
		meta.setColor(color);
		item.setItemMeta(meta);
		return this;
	}

	public ItemBuilder setLore(String... lores) {
		ItemMeta meta = item.getItemMeta();
		meta.setLore(Arrays.asList(lores));
		item.setItemMeta(meta);
		return this;
	}

	public ItemBuilder setMaterial(Material mat) {
		item.setType(mat);
		return this;
	}

	public ItemBuilder setPotionType(PotionType potionType) {
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		meta.setBasePotionData(new PotionData(potionType));
		item.setItemMeta(meta);
		return this;
	}

	public ItemBuilder setPotion(PotionEffect effect, Color color) {
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		meta.addCustomEffect(effect, true);
		meta.setColor(color);
		item.setItemMeta(meta);
		return this;
	}

	public ItemBuilder setUnbreakable(boolean unbreakable) {
		ItemMeta meta = item.getItemMeta();
		meta.setUnbreakable(unbreakable);
		item.setItemMeta(meta);
		return this;
	}

	public ItemBuilder setArrowPotion(PotionEffect type, Color color) {
		PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
		potionMeta.addCustomEffect(type, true);
		potionMeta.setColor(color);
		item.setItemMeta(potionMeta);
		return this;
	}

}
