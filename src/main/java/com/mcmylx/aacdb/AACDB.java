package com.mcmylx.aacdb;

import com.mcmylx.aacdb.aachooker.AACAdditionProHooker;
import com.mcmylx.aacdb.aachooker.AACHooker;
import com.mcmylx.aacdb.commands.MainCommand;
import com.mcmylx.aacdb.config.ConfigManager;
import com.mcmylx.aacdb.data.DataManager;
import com.mcmylx.aacdb.database.DatabaseManager;
import com.mcmylx.aacdb.listener.AACCheatListener;
import com.mcmylx.aacdb.listener.JoinListener;
import com.mcmylx.aacdb.listener.QuitListener;
import com.mcmylx.aacdb.utils.LogUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public final class AACDB extends JavaPlugin {
    private static AACDB instance;

    private ConfigManager configManager;
    private DatabaseManager databaseManager;
    private MainCommand mainCommand;
    private DataManager dataManager;


    @Override
    public void onLoad() {
        instance = this;
        this.configManager = new ConfigManager();
        this.configManager.load();
        LogUtil.log("Loaded all config files");
    }

    @Override
    public void onEnable() {
        instance = this;
        long start = System.currentTimeMillis();

        AACHooker.init();
        AACAdditionProHooker.init();

        this.databaseManager = new DatabaseManager();
        this.mainCommand = new MainCommand();
        this.dataManager = new DataManager();
        this.dataManager.createTables();//create tables
        this.regListeners();

        Metrics metrics = new Metrics(this);
        UpdateChecker.start();

        LogUtil.log("Plugin Loaded,total used time: " + (System.currentTimeMillis() - start) + "ms");
    }

    public void regListeners() {
        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new QuitListener(), this);
        Bukkit.getPluginManager().registerEvents(new AACCheatListener(), this);
        LogUtil.log("Registered listeners");
    }

    @Override
    public void onDisable() {
        this.databaseManager.close();
        HandlerList.unregisterAll(this);
        LogUtil.log("Plugin Disabled!");
    }

    public void reload() {
        this.configManager.load(); // reload config
        this.databaseManager.reload(); // reload database pool
        LogUtil.log("Reloaded Plugin");
    }

    public static AACDB getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public MainCommand getMainCommand() {
        return mainCommand;
    }

    public DataManager getDataManager() {
        return dataManager;
    }
}
