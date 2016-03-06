package com.lenis0012.legacypvp.modules.cooldown;

import com.lenis0012.legacypvp.LegacyPVP;
import com.lenis0012.pluginutils.Module;
import com.lenis0012.pluginutils.misc.Reflection;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Method;

public class CooldownModule extends Module<LegacyPVP> implements Listener {
    private static final Method GET_HANDLE = Reflection.getCBMethod("entity.CraftPlayer", "getHandle");
    private static final Method GET_ATTRIBUTE = Reflection.getNMSMethod("EntityLiving", "getAttributeInstance", Reflection.getNMSClass("IAtrribute"));
    private static final Method SET_ATTRIBUTE = Reflection.getNMSMethod("AttributeInstance", "setValue", double.class);
    private static final Object ATTACK_SPEED_ATTRIBUTE = Reflection.getFieldValue(Reflection.getNMSField("GenericAttributes", "f"), null);

    private static final double MAX_ATTACK_SPEED = 1024.0;

    public CooldownModule(LegacyPVP plugin) {
        super(plugin);
    }

    @Override
    public void enable() {
        register(this);
    }

    @Override
    public void disable() {
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        setAttackSpeed(player, MAX_ATTACK_SPEED);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        final Entity attacker = event.getDamager();
        if(!(attacker instanceof Player)) return;
        final Player player = (Player) attacker;
        final ItemStack inHand = player.getInventory().getItemInMainHand();
        if(inHand == null) return;

        // Calculate base damage
        double baseDamage = event.getDamage(DamageModifier.BASE);
        double currentDamage = getCurrentDamage(inHand.getType());
        if(currentDamage == 0.0) return;

        // Get damage factor applied to base damage
        double damageFactor = baseDamage / currentDamage;

        // Apply factor to legacy damage and set it.
        double legacyDamage = getLegacyDamage(inHand.getType()) * damageFactor;
        event.setDamage(DamageModifier.BASE, legacyDamage);
    }

    public void setAttackSpeed(Player player, double attackSpeed) {
        Object nmsplayer = Reflection.invokeMethod(GET_HANDLE, player);
        Object attribute = Reflection.invokeMethod(GET_ATTRIBUTE, nmsplayer, ATTACK_SPEED_ATTRIBUTE);
        Reflection.invokeMethod(SET_ATTRIBUTE, attribute, attackSpeed);
    }

    private double getLegacyDamage(Material type) {
        switch(type) {
            case WOOD_AXE:
            case GOLD_AXE:
                return 4.0;
            case WOOD_SWORD:
            case GOLD_SWORD:
            case STONE_AXE:
                return 5.0;
            case STONE_SWORD:
            case IRON_AXE:
                return 6.0;
            case IRON_SWORD:
            case DIAMOND_AXE:
                return 7.0;
            case DIAMOND_SWORD:
                return 8.0;
            default:
                return 0.0;
        }
    }

    private double getCurrentDamage(Material type) {
        switch(type) {
            case WOOD_SWORD:
            case GOLD_SWORD:
                return 4.0;
            case STONE_SWORD:
                return 5.0;
            case IRON_SWORD:
                return 6.0;
            case DIAMOND_SWORD:
            case WOOD_AXE:
            case GOLD_AXE:
                return 7.0;
            case STONE_AXE:
            case IRON_AXE:
            case DIAMOND_AXE:
                return 9.0;
            default:
                return 0.0;
        }
    }
}
