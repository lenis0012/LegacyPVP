package com.lenis0012.legacypvp.modules.general;

import com.lenis0012.legacypvp.LegacyPVP;
import com.lenis0012.updater.api.Updater;
import com.lenis0012.updater.api.Version;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LegacyCommand implements CommandExecutor {
    private final General general;

    public LegacyCommand(General general) {
        this.general = general;
    }

    @Override
    public boolean onCommand(final CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0) {
            reply(sender, "Available arguments: [update]");
            return true;
        }

        String subcommand = args[0].toLowerCase();
        if(!subcommand.equalsIgnoreCase("update")) {
            reply(sender, "Unknown subcommand, type '/legacypvp' for help.");
            return true;
        }

        // Update
        final Updater updater = general.getUpdater();
        final Version version = updater.getNewVersion();
        if(version == null) {
            reply(sender, "Updater is disabled, please enable in config.");
            return true;
        }

        reply(sender, "&aDownloading " + version.getName() + "...");
        Bukkit.getScheduler().runTaskAsynchronously(LegacyPVP.getInstance(), new Runnable() {
            @Override
            public void run() {
                String message = updater.downloadVersion();
                final String response = message == null ? "&aUpdate successful, will be active on reboot." : "&c&lError: &c" + message;
                Bukkit.getScheduler().runTask(LegacyPVP.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        reply(sender, response);
                        if(!(sender instanceof Player)) {
                            return;
                        }

                        final Player player = (Player) sender;
                        ItemStack changelog = updater.getChangelog();
                        if(changelog == null) {
                            reply(player, "&cChangelog isn't available for this version.");
                            return;
                        }

                        ItemStack inHand = player.getInventory().getItemInMainHand();
                        player.getInventory().setItemInMainHand(changelog);
                        if(inHand != null) {
                            player.getInventory().addItem(inHand);
                        }

                        reply(player, "&llenis> &bCheck my changelog out! (I put it in your hand)");
                        player.updateInventory();
                    }
                });
            }
        });
        return true;
    }

    private void reply(CommandSender sender, String message, Object... args) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(message, args)));
    }
}
