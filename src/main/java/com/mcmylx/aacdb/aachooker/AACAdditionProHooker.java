package com.mcmylx.aacdb.aachooker;

import com.mcmylx.aacdb.config.MainConfig;
import com.mcmylx.aacdb.utils.LogUtil;
import de.photon.AACAdditionPro.ModuleType;
import de.photon.AACAdditionPro.api.AACAdditionProApi;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import java.util.HashMap;
import java.util.Map;

public class AACAdditionProHooker {
    public static boolean ENABLE;

    public static void init() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        if (pluginManager.getPlugin("AACAdditionPro") == null) {
            ENABLE = false;
            return;
        }
        if (!pluginManager.getPlugin("AACAdditionPro").isEnabled()) {
            ENABLE = false;
            return;
        }
        ENABLE = true;
        if (ENABLE) {
            LogUtil.log("Hooked with AACAdditionPro");
        }
    }

    public static void syncVL(Player player, Map<String, Integer> vls) {
        if (!MainConfig.enable_vl) {
            return;
        }
        if (!ENABLE) {
            return;
        }
        if (!AACAdditionProApi.isLoaded()) {
            return;
        }
        for (ModuleType moduleType : ModuleType.values()) {
            if (vls.containsKey(moduleType.name())) {
                Number number = vls.get(moduleType.name());
                AACAdditionProApi.setVl(player, moduleType, number.intValue());
            }
        }
    }

    public static Map<String, Integer> getVLMap(Player player) {
        Map<String, Integer> vlMap = new HashMap<>();
        for (ModuleType moduleType : ModuleType.values()) {
            if (moduleType == ModuleType.LOG_BOT || moduleType == ModuleType.PER_HEURISTIC_COMMANDS) {
                continue;
            }
            if (moduleType.name().contains("CONTROL")) {
                continue;
            }
            try {
                int vl = AACAdditionProApi.getVL(player, moduleType);
                vlMap.put(moduleType.name(), vl);
            }catch (Exception e){

            }
        }
        return vlMap;
    }
}
