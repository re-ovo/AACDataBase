package com.mcmylx.aacdb.listener;

import com.mcmylx.aacdb.AACDB;
import com.mcmylx.aacdb.aachooker.AACAdditionProHooker;
import com.mcmylx.aacdb.aachooker.AACHooker;
import com.mcmylx.aacdb.config.MainConfig;
import com.mcmylx.aacdb.data.DataType;
import com.mcmylx.aacdb.data.types.AACKick;
import com.mcmylx.aacdb.data.types.AACVL;
import com.mcmylx.aacdb.utils.LogUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;
import java.util.List;

public class JoinListener implements Listener {
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (MainConfig.DEBUG) {
            return;
        }
        //异步查询数据库
        //同步VL
        Bukkit.getScheduler().runTaskAsynchronously(AACDB.getInstance(), () -> {
            AACVL aacvl = AACDB.getInstance().getDataManager().fetchData(player);
            if (aacvl == null) {
                aacvl = new AACVL(player.getUniqueId().toString(), player.getName(), DataType.AACVL, new HashMap<>());
            }
            AACVL finalAacvl = aacvl;
            Bukkit.getScheduler().runTask(AACDB.getInstance(), () -> {
                AACHooker.syncVL(player, finalAacvl.getVls());
                AACAdditionProHooker.syncVL(player, finalAacvl.getVls());
                LogUtil.log("(DEBUG) Sync Player VL Data: " + player.getName());
            });
            if (MainConfig.enable_punishment) {
                Bukkit.getScheduler().runTaskAsynchronously(AACDB.getInstance(), () -> {
                    List<AACKick> aacKickList = AACDB.getInstance().getDataManager().getRecentAACKicks(player);
                    int kickAmount = aacKickList.size();
                    LogUtil.log("DEBUG: Recent Kick times: "+kickAmount);
                    if (kickAmount >= MainConfig.threshold) {
                        //Punishment
                        Bukkit.getScheduler().runTask(AACDB.getInstance(),()->{
                            LogUtil.log("Player " + player.getName() + "'s recent kick times amount are larger than threshold,AAC DB will punish the player");
                            for (String cmd : MainConfig.commands) {
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", player.getName()));
                            }
                        });
                    }
                });
            }
        });
    }
}
