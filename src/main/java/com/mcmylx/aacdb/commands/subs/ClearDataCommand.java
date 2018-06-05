package com.mcmylx.aacdb.commands.subs;

import com.mcmylx.aacdb.AACDB;
import com.mcmylx.aacdb.commands.SubCommand;
import com.mcmylx.aacdb.utils.LogUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class ClearDataCommand extends SubCommand {
    public ClearDataCommand() {
        super("clear", "<Player>", "Clear a player's aac data");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("aacdb.cleardata")) {
            LogUtil.pm(sender, "&cYou dont have permission");
            return;
        }
        if(args.length != 2){
            LogUtil.pm(sender,"&cIllegal Command Arg Length");
            return;
        }
        LogUtil.pm(sender,"&aStart to delete "+args[1]+"'s all data....");
        Bukkit.getScheduler().runTaskAsynchronously(AACDB.getInstance(),()-> AACDB.getInstance().getDataManager().clear(args[1]));
        LogUtil.pm(sender,"&aDeleted!");
    }
}
