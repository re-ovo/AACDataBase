package com.mcmylx.aacdb.commands.subs;

import com.mcmylx.aacdb.AACDB;
import com.mcmylx.aacdb.commands.SubCommand;
import com.mcmylx.aacdb.config.MainConfig;
import com.mcmylx.aacdb.data.types.AACKick;
import com.mcmylx.aacdb.utils.LogUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.text.SimpleDateFormat;
import java.util.List;

public class LookupKickCommand extends SubCommand {
    public LookupKickCommand() {
        super("lookupkick", "<player> [pages]", "Look up player's aac kick data");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!MainConfig.enable_kick){
            return;
        }
        if (!sender.hasPermission("aacdb.lookup.kick")) {
            LogUtil.pm(sender, "&cYou dont have permission");
            return;
        }
        if (args.length < 2) {
            LogUtil.pm(sender, "&cIllegal command arg length!");
            return;
        }
        if (args.length == 2) {
            Bukkit.getScheduler().runTaskAsynchronously(AACDB.getInstance(), () -> {
                List<AACKick> aacKicks = AACDB.getInstance().getDataManager().lookupKickData(args[1], 1);
                LogUtil.pm(sender, "&rPlayer &a" + args[1] + "&r's aac kick data | Page: &a1");
                int num = 1;
                for (AACKick aacKick : aacKicks) {
                    LogUtil.pm(sender, formatKickData(num, aacKick));
                    num++;
                }
            });
            return;
        }
        Integer page = Integer.parseInt(args[2]);
        Bukkit.getScheduler().runTaskAsynchronously(AACDB.getInstance(), () -> {
            List<AACKick> aacKicks = AACDB.getInstance().getDataManager().lookupKickData(args[1], page);
            LogUtil.pm(sender, "&rPlayer &a" + args[1] + "&r's aac kick data | Page: &a" + page);
            int num = ((page - 1) * 10) + 1;
            for (AACKick aacKick : aacKicks) {
                LogUtil.pm(sender, formatKickData(num, aacKick));
                num++;
            }
        });
    }

    public String formatKickData(int num, AACKick aacKick) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return "&a● &r[" + num + "] Reason: &a" + aacKick.getReason() + " &rTime: &a" + format.format(aacKick.getTimeStamp());
    }
}
