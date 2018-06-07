package com.mcmylx.aacdb.commands;

import com.mcmylx.aacdb.commands.subs.*;
import com.mcmylx.aacdb.utils.LogUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class MainCommand implements CommandExecutor {
    private List<SubCommand> subCommands;

    public MainCommand() {
        Bukkit.getPluginCommand("aacdb").setExecutor(this);
        this.subCommands = new ArrayList<>();

        this.subCommands.add(new ClearDataCommand());
        this.subCommands.add(new LookupKickCommand());
        this.subCommands.add(new LookupHeuristicCommand());
        this.subCommands.add(new CallKickCommand());
        this.subCommands.add(new ReloadCommand());

        LogUtil.log("Registered commands");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(label.equalsIgnoreCase("aacdb")){
            if(args.length == 0){
                sender.sendMessage("§m                                                                ");
                sender.sendMessage("§bCommand List:");
                for(SubCommand subCommand : subCommands){
                    sender.sendMessage("§f/aacdb §7"+subCommand.getName()+" §7"+subCommand.getArg()+"§r - §8"+subCommand.getDescription());
                }
                sender.sendMessage("§m                                                                ");
                return true;
            }
            for(SubCommand subCommand : subCommands){
                if(subCommand.getName().equalsIgnoreCase(args[0])){
                    subCommand.execute( sender,args);
                    return true;
                }
            }
            sender.sendMessage(ChatColor.RED+"Cant find command: "+args[0]);
            return true;
        }
        return false;
    }
}
