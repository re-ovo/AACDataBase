package com.mcmylx.aacdb.aachooker;

import com.mcmylx.aacdb.config.MainConfig;
import com.mcmylx.aacdb.utils.LogUtil;
import me.konsolas.aac.api.AACAPI;
import me.konsolas.aac.api.AACAPIProvider;
import me.konsolas.aac.api.HackType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import java.util.HashMap;
import java.util.Map;

public class AACHooker {
    public static boolean ENABLE;

    public static void init() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        if (pluginManager.getPlugin("AAC") == null) {
            ENABLE = false;
            return;
        }
        if (!pluginManager.getPlugin("AAC").isEnabled()) {
            ENABLE = false;
            return;
        }
        ENABLE = true;
        if(ENABLE){
            LogUtil.log("Hooked with AAC");
        }
    }

    public static Map<String, Integer> getPlayerVLMap(Player player) {
        if (!AACAPIProvider.isAPILoaded()) {
            return null;
        }
        Map<String, Integer> vlMap = new HashMap<>();
        AACAPI aacapi = AACAPIProvider.getAPI();
        for (HackType hackType : HackType.values()) {
            String name = hackType.getName();
            int vl = aacapi.isEnabled(hackType) ? aacapi.getViolationLevel(player, hackType) : 0;
            vlMap.put(name, vl);
        }
        return vlMap;
    }

    public static void syncVL(Player player, Map<String, Integer> vls) {
        if (!MainConfig.enable_vl) {
            return;
        }
        if (!ENABLE) {
            return;
        }
        AACAPI aacapi = AACAPIProvider.getAPI();
        for (HackType hackType : HackType.values()) {
            if (!aacapi.isEnabled(hackType)) {
                continue;
            }
            if (vls.containsKey(hackType.getName())) {
                Number number = vls.get(hackType.getName());
                aacapi.setViolationLevel(player, hackType,number.intValue());
            }
        }
    }
}
