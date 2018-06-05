package com.mcmylx.aacdb.listener;

import com.mcmylx.aacdb.AACDB;
import com.mcmylx.aacdb.aachooker.AACAdditionProHooker;
import com.mcmylx.aacdb.aachooker.AACHooker;
import com.mcmylx.aacdb.config.MainConfig;
import com.mcmylx.aacdb.data.DataType;
import com.mcmylx.aacdb.data.types.AACVL;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;

public class QuitListener implements Listener {
    @EventHandler
    //被踢出游戏，清空VL，防止无限Kick
    public void onKick(PlayerKickEvent e) {
        if (MainConfig.DEBUG) {
            return;
        }
        Player player = e.getPlayer();
        String uuid = player.getUniqueId().toString();
        Bukkit.getScheduler().runTaskLaterAsynchronously(AACDB.getInstance(), () -> AACDB.getInstance().getDataManager().removeVLData(uuid), 20);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onQuit(PlayerQuitEvent e) {
        if (MainConfig.DEBUG) {
            return;
        }
        Player player = e.getPlayer();
        Map<String, Integer> aac_vl = AACHooker.getPlayerVLMap(player);
        if (aac_vl == null) {
            return;
        }
        if (AACAdditionProHooker.ENABLE) {
            Map<String, Integer> aacadp_vl = AACAdditionProHooker.getVLMap(player);
            aac_vl.putAll(aacadp_vl);
        }
        AACVL aacvl = new AACVL(player.getUniqueId().toString(), player.getName(), DataType.AACVL, aac_vl);
        Bukkit.getScheduler().runTaskAsynchronously(AACDB.getInstance(), () -> {
            AACDB.getInstance().getDataManager().saveAACVL(aacvl);
        });
    }
}
