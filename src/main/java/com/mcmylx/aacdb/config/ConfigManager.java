package com.mcmylx.aacdb.config;

import com.mcmylx.aacdb.AACDB;

public class ConfigManager {

    private MainConfig mainConfig;
    private Lang lang;

    public ConfigManager() {
        AACDB aacdb = AACDB.getInstance();
        if(aacdb.getDataFolder().exists()){
            aacdb.getDataFolder().mkdirs();
        }
        this.mainConfig = new MainConfig();
        this.lang = new Lang();
    }

    public void load(){
        this.mainConfig.reload();
        this.lang.reload();
    }

    public MainConfig getMainConfig() {
        return mainConfig;
    }

    public Lang getLang() {
        return lang;
    }
}
