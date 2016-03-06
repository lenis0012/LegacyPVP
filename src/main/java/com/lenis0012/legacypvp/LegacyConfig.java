package com.lenis0012.legacypvp;

import com.lenis0012.pluginutils.modules.configuration.AbstractConfig;
import com.lenis0012.pluginutils.modules.configuration.ConfigurationModule;
import com.lenis0012.pluginutils.modules.configuration.mapping.ConfigHeader;
import com.lenis0012.pluginutils.modules.configuration.mapping.ConfigKey;
import com.lenis0012.pluginutils.modules.configuration.mapping.ConfigMapper;
import lombok.Getter;
import lombok.Setter;

@ConfigMapper(fileName = "config.yml", header = {
                "LegacyPVP Config File.",
                "",
                "In here you can configure which parts of the legacy pvp you want on your server.",
                "Each legacy pvp aspect is defined as a module, configure them as you like."
        })
@Getter
@Setter
public class LegacyConfig extends AbstractConfig {
    @ConfigHeader("This module increases the protection of armor")
    @ConfigKey(path = "modules.armor")
    private boolean armorModule = true;

    @ConfigHeader({
            "This module removes pvp cooldown.",
            "Also changes back to legacy sword & axe damage",
            "Damage is changed to account for the removal of cooldown on the axe"
    })
    @ConfigKey(path = "modules.cooldown")
    private boolean cooldownModule = true;

    @ConfigKey(path = "update-checker")
    private boolean updateChecker = true;

    public LegacyConfig(ConfigurationModule module) {
        super(module);
    }
}
