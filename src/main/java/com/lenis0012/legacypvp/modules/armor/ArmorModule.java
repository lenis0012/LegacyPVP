package com.lenis0012.legacypvp.modules.armor;

import com.lenis0012.legacypvp.LegacyPVP;
import com.lenis0012.pluginutils.Module;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class ArmorModule extends Module<LegacyPVP> implements Listener {
    private static final int LEGACY_ARMOR_REDUCTOR = 4;

    public ArmorModule(LegacyPVP plugin) {
        super(plugin);
    }

    @Override
    public void enable() {
        register(this);
    }

    @Override
    public void disable() {
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onEntityDamage(EntityDamageEvent event) {
        final Entity entity = event.getEntity();
        if(!(entity instanceof Player)) return;
        final Player player = (Player) entity;

        // Apply legacy damage
        double baseDamage = event.getDamage(DamageModifier.BASE);
        int defensePoints = getDefensePoints(player.getInventory());
        double armorDamage = baseDamage * getLegacyDamageFactor(defensePoints);
        event.setDamage(DamageModifier.ARMOR, armorDamage - baseDamage);
    }

    /**
     * Get the damage correction factor for a specified amount of damage.
     * divide the legacy factor by the current factor, because it is later multiplied by that same factor.
     *
     * for example,
     * 20 defense points, 10 damage:
     * legacy: 1 - (4 * 20) / 100 = 0.20
     * wanted damage = 10 * 0.20 = 2
     * current: 1 - 15 / 25 = 0.40
     * correction: 0.20 / 0.40 = 0.5
     * custom damage: 10 * 0.5 = 5
     * actual damage: 5 * 0.4 = 2
     *
     * @param inventory of player
     * @param damage of the attack
     * @return correction damage factor
     */
    private double getDamageCorrectionFactor(PlayerInventory inventory, double damage) {
        int defensePoints = getDefensePoints(inventory);
        double legacy = getLegacyDamageFactor(defensePoints);
        double current = getCurrentDamageFactor(defensePoints, damage);
        return legacy / current;
    }

    private int getDefensePoints(PlayerInventory inventory) {
        int points = 0;
        points += getDefensePoints(inventory.getHelmet());
        points += getDefensePoints(inventory.getChestplate());
        points += getDefensePoints(inventory.getLeggings());
        points += getDefensePoints(inventory.getBoots());
        return points;
    }

    private int getDefensePoints(ItemStack armorPiece) {
        if(armorPiece == null) return 0;
        return Armor.getForType(armorPiece.getType()).getDefensePoints();
    }

    private double getLegacyDamageFactor(int defensePoints) {
        int percent = LEGACY_ARMOR_REDUCTOR * defensePoints;
        return 1.0 - percent / 100.0;
    }

    private double getCurrentDamageFactor(int defensePoints, double damage) {
        return 1.0 - Math.max(defensePoints / 5.0, defensePoints - damage / 2.0) / 25.0;
    }
}
