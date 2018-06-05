package com.mcmylx.aacdb.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class SubCommand {
    private String name;
    private String arg;
    private String description;

    public SubCommand(String name, String arg, String description) {
        this.name = name;
        this.arg = arg;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getArg() {
        return arg;
    }

    public String getDescription() {
        return description;
    }

    public abstract void execute(CommandSender sender, String args[]);
}
