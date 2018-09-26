package com.mcmylx.aacdb.listener;

import com.mcmylx.aacdb.AACDB;
import com.mcmylx.aacdb.config.MainConfig;
import com.mcmylx.aacdb.data.DataType;
import com.mcmylx.aacdb.data.types.AACHeuristic;
import com.mcmylx.aacdb.data.types.AACKick;
import com.mcmylx.aacdb.utils.LogUtil;
import me.konsolas.aac.api.HackType;
import me.konsolas.aac.api.PlayerViolationCommandEvent;
import me.konsolas.aac.api.PlayerViolationEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class AACCheatListener implements Listener {
    //@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    //Removed this listener,because AAC removed the heuristics check in 3.5.0
    public void onHack(PlayerViolationEvent e) {
        if (MainConfig.DEBUG) {
            return;
        }
        if (e.getHackType() != HackType.HEURISTICS) {
            return;
        }
        try {
            Player player = e.getPlayer();
            String verbose = e.getMessage();
            String[] sp = verbose.split(" ");
            String pattern = sp[6];
            Double accuracy = Double.valueOf(sp[8].substring(0, sp[8].length() - 1));

            AACHeuristic aacHeuristic = new AACHeuristic(player.getUniqueId().toString(), player.getName(), pattern, accuracy, System.currentTimeMillis());

            Bukkit.getScheduler().runTaskAsynchronously(AACDB.getInstance(), () -> AACDB.getInstance().getDataManager().addHeuristicData(aacHeuristic));
        } catch (Exception ex) {
            ex.printStackTrace();
            LogUtil.log("Cant handle heuristic event");
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onKick(PlayerViolationCommandEvent e) {
        if (MainConfig.DEBUG) {
            return;
        }
        String cmd = e.getCommand();
        handleKick(cmd);
    }

    public static void handleKick(String cmd) {
        if (!cmd.toLowerCase().startsWith("aackick")) {
            return;
        }
        String[] sp = cmd.split("\\s+");
        if (sp.length < 3) {
            return;
        }
        Player player = Bukkit.getPlayer(sp[1]);
        StringBuilder reason = new StringBuilder();
        for (int i = 2; i < sp.length; i++) {
            reason.append(sp[i]);
            if (i + 1 != sp.length) {
                reason.append(" ");
            }
        }
        LogUtil.log("Kick-> Reason: " + reason + " Player: " + player.getName());
        AACKick aacKick = new AACKick(player.getUniqueId().toString(), player.getName(), DataType.AACKick, reason.toString(), System.currentTimeMillis());
        Bukkit.getScheduler().runTaskAsynchronously(AACDB.getInstance(), () -> AACDB.getInstance().getDataManager().addKickData(aacKick));
    }
}
