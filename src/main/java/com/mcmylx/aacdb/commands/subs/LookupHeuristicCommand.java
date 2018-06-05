package com.mcmylx.aacdb.commands.subs;

import com.mcmylx.aacdb.AACDB;
import com.mcmylx.aacdb.commands.SubCommand;
import com.mcmylx.aacdb.config.MainConfig;
import com.mcmylx.aacdb.data.types.AACHeuristic;
import com.mcmylx.aacdb.utils.LogUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.text.SimpleDateFormat;
import java.util.List;

public class LookupHeuristicCommand extends SubCommand {
    public LookupHeuristicCommand() {
        super("lookupheur", "<player> [pages]", "Look up player's aac heuristic check data");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!MainConfig.enable_heuristic){
            return;
        }
        if (!sender.hasPermission("aacdb.lookup.heuristic")) {
            LogUtil.pm(sender, "&cYou dont have permission");
            return;
        }
        if (args.length < 2) {
            LogUtil.pm(sender, "&cIllegal command arg length!");
            return;
        }
        if (args.length == 2) {
            Bukkit.getScheduler().runTaskAsynchronously(AACDB.getInstance(), () -> {
                List<AACHeuristic> aacHeuristics = AACDB.getInstance().getDataManager().lookHeuristicData(args[1], 1);
                LogUtil.pm(sender, "&rPlayer &a" + args[1] + "&r's aac heuristic data | Page: &a1");
                int num = 1;
                for (AACHeuristic aacHeuristic : aacHeuristics) {
                    LogUtil.pm(sender, formatHeuristicData(num, aacHeuristic));
                    num++;
                }
            });
            return;
        }
        Integer page = Integer.parseInt(args[2]);
        Bukkit.getScheduler().runTaskAsynchronously(AACDB.getInstance(), () -> {
            List<AACHeuristic> aacHeuristics = AACDB.getInstance().getDataManager().lookHeuristicData(args[1], page);
            LogUtil.pm(sender, "&rPlayer &a" + args[1] + "&r's aac heuristic data | Page: &a" + page);
            int num = ((page - 1) * 10) + 1;
            for (AACHeuristic aacHeuristic : aacHeuristics) {
                LogUtil.pm(sender, formatHeuristicData(num, aacHeuristic));
                num++;
            }
        });
    }

    public String formatHeuristicData(int num, AACHeuristic aacHeuristic) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return "&a● &r[" + num + "] Pattern: &a" + aacHeuristic.getPattern()+"&7("+aacHeuristic.getAccuracy()+" %)" + " &rTime: &a" + format.format(aacHeuristic.getTimeStamp());
    }
}
