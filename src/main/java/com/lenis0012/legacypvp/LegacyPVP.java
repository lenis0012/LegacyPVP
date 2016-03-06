package com.lenis0012.legacypvp;

import com.lenis0012.legacypvp.modules.armor.ArmorModule;
import com.lenis0012.legacypvp.modules.cooldown.CooldownModule;
import com.lenis0012.pluginutils.PluginHolder;
import com.lenis0012.pluginutils.modules.configuration.ConfigurationModule;

import java.io.File;

public class LegacyPVP extends PluginHolder {
    private LegacyConfig config;

    public LegacyPVP() {
        super(ConfigurationModule.class);
    }

    @Override
    public void enable() {
        ConfigurationModule configModule = getModule(ConfigurationModule.class);
        this.config = configModule.createCustomConfig(LegacyConfig.class);
        config.reload(); // Load settings
        config.save(); // Save settings :)

        if(config.isArmorModule()) {
            registry.registerModules(ArmorModule.class);
        }
        if(config.isCooldownModule()) {
            registry.registerModules(CooldownModule.class);
        }
    }

    @Override
    public void disable() {
    }

    public LegacyConfig getConfiguration() {
        return config;
    }

    public File getPluginFile() {
        return getFile();
    }
}
