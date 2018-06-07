package com.mcmylx.aacdb.commands.subs;

import com.mcmylx.aacdb.AACDB;
import com.mcmylx.aacdb.commands.SubCommand;
import com.mcmylx.aacdb.data.DataType;
import com.mcmylx.aacdb.data.types.AACKick;
import com.mcmylx.aacdb.utils.LogUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CallKickCommand extends SubCommand {
    public CallKickCommand() {
        super("callkick", "<Player> <Reason>", "Fire a kick event(design for other kick command)");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("aacdb.callkick")) {
            LogUtil.pm(sender, "&cYou dont have permission");
            return;
        }
        if (args.length < 3) {
            LogUtil.log("Not enough command");
            return;
        }
        Player player = Bukkit.getPlayer(args[1]);
        if (player == null) {
            LogUtil.log("Player " + args[1] + " is offline");
            return;
        }
        StringBuilder reason = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            reason.append(args[i]);
            if (i != (args.length - 1)) {
                reason.append(" ");
            }
        }
        LogUtil.pm(sender,"Kick-> Reason: " + reason + " Player: " + player.getName());
        AACKick aacKick = new AACKick(player.getUniqueId().toString(), player.getName(), DataType.AACKick, reason.toString(), System.currentTimeMillis());
        Bukkit.getScheduler().runTaskAsynchronously(AACDB.getInstance(), () -> AACDB.getInstance().getDataManager().addKickData(aacKick));
    }
}
