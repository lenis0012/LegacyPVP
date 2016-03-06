package com.lenis0012.legacypvp.modules.general;

import com.lenis0012.legacypvp.LegacyPVP;
import com.lenis0012.pluginutils.Module;
import com.lenis0012.updater.api.Updater;
import com.lenis0012.updater.api.UpdaterFactory;
import com.lenis0012.updater.api.Version;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class General extends Module<LegacyPVP> implements Listener {
    private static final String UPDATE_MESSAGE = "&f&l[LoginSecurity] &eThere is a new update available! %s for %s\n" +
            "Type &6/lac update &eto update now.";

    private Updater updater;

    public General(LegacyPVP plugin) {
        super(plugin);
    }

    @Override
    public void enable() {
        // Commands & listeners
        register(new LegacyCommand(this), "legacypvp");
        register(this);

        // Updater
        UpdaterFactory factory = new UpdaterFactory(plugin);
        this.updater = factory.newUpdater(plugin.getPluginFile(), plugin.getConfiguration().isUpdateChecker());
    }

    @Override
    public void disable() {
    }

    public Updater getUpdater() {
        return updater;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        if(player.hasPermission("legacypvp.update")) {
            Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, new Runnable() {
                @Override
                public void run() {
                    boolean update = updater.hasUpdate();
                    if(update) {
                        Bukkit.getScheduler().runTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                Version version = updater.getNewVersion();
                                String message = ChatColor.translateAlternateColorCodes('&',
                                        String.format(UPDATE_MESSAGE, version.getName(), version.getServerVersion()));
                                player.sendMessage(message);
                            }
                        });
                    }
                }
            }, 41L);
        }
    }
}
