package com.mcmylx.aacdb.commands.subs;

import com.mcmylx.aacdb.AACDB;
import com.mcmylx.aacdb.commands.SubCommand;
import com.mcmylx.aacdb.utils.LogUtil;
import org.bukkit.command.CommandSender;

public class ReloadCommand extends SubCommand {
    public ReloadCommand() {
        super("reload", "", "Reload Plugin");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("aacdb.reload")) {
            LogUtil.pm(sender, "&cYou dont have permission");
            return;
        }
        AACDB.getInstance().reload();
        LogUtil.pm(sender, "&aReloaded!");
    }
}
