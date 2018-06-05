package com.mcmylx.aacdb.utils;

import com.mcmylx.aacdb.config.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class LogUtil {
    public static void log(String info) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.prefix + info));
    }

    public static void pm(CommandSender sender,String info){
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.prefix + info));
    }
}
