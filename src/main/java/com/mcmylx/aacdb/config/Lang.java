package com.mcmylx.aacdb.config;

import com.mcmylx.aacdb.AACDB;
import com.mcmylx.aacdb.interfaces.Reloadable;
import com.mcmylx.aacdb.utils.YamlUtil;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Lang implements Reloadable {
    public static String prefix = "[AACDB]";

    @Override
    public void reload() {
        File file = new File(AACDB.getInstance().getDataFolder(), "lang.yml");
        if(!file.exists()){
            AACDB.getInstance().saveResource("lang.yml",false);
        }
        YamlConfiguration configuration = YamlUtil.load(file);

        prefix = configuration.getString("prefix");
    }
}
