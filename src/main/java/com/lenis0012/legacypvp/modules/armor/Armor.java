package com.lenis0012.legacypvp.modules.armor;

import com.google.common.collect.Maps;
import org.bukkit.Material;

import java.util.Map;

public class Armor {
    private static final Map<Material, Armor> armorMap = Maps.newConcurrentMap();

    /**
     * Leather
     */
    public static Armor LEATHER_HELMET = new Armor(Material.LEATHER_HELMET, 1);
    public static Armor LEATHER_CHESTPLATE = new Armor(Material.LEATHER_CHESTPLATE, 3);
    public static Armor LEATHER_LEGGINGS = new Armor(Material.LEATHER_LEGGINGS, 2);
    public static Armor LEATHER_BOOTS = new Armor(Material.LEATHER_BOOTS, 1);

    /**
     * Gold
     */
    public static Armor GOLD_HELMET = new Armor(Material.GOLD_HELMET, 2);
    public static Armor GOLD_CHESTPLATE = new Armor(Material.GOLD_CHESTPLATE, 5);
    public static Armor GOLD_LEGGINGS = new Armor(Material.GOLD_LEGGINGS, 3);
    public static Armor GOLD_BOOTS = new Armor(Material.GOLD_BOOTS, 1);

    /**
     * Chainmail
     */
    public static Armor CHAINMAIL_HELMET = new Armor(Material.CHAINMAIL_HELMET, 2);
    public static Armor CHAINMAIL_CHESTPLATE = new Armor(Material.CHAINMAIL_CHESTPLATE, 5);
    public static Armor CHAINMAIL_LEGGINGS = new Armor(Material.CHAINMAIL_LEGGINGS, 4);
    public static Armor CHAINMAIL_BOOTS = new Armor(Material.CHAINMAIL_BOOTS, 1);

    /**
     * Iron
     */
    public static Armor IRON_HELMET = new Armor(Material.IRON_HELMET, 2);
    public static Armor IRON_CHESTPLATE = new Armor(Material.IRON_CHESTPLATE, 6);
    public static Armor IRON_LEGGINGS = new Armor(Material.IRON_LEGGINGS, 5);
    public static Armor IRON_BOOTS = new Armor(Material.IRON_BOOTS, 2);

    /**
     * Diamond
     */
    public static Armor DIAMOND_HELMET = new Armor(Material.DIAMOND_HELMET, 3);
    public static Armor DIAMOND_CHESTPLATE = new Armor(Material.DIAMOND_CHESTPLATE, 8);
    public static Armor DIAMOND_LEGGINGS = new Armor(Material.DIAMOND_LEGGINGS, 6);
    public static Armor DIAMOND_BOOTS = new Armor(Material.DIAMOND_BOOTS, 3);

    public static Armor UNKNOWN = new Armor(null, 0);

    private final int defensePoints;

    private Armor(Material material, int armorPoints) {
        this.defensePoints = armorPoints;
        if(material == null) return;
        armorMap.put(material, this);
    }

    public int getDefensePoints() {
        return defensePoints;
    }

    public static Armor getForType(Material type) {
        return armorMap.getOrDefault(type, UNKNOWN);
    }
}
