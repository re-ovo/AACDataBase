package com.mcmylx.aacdb.utils;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;

public class YamlUtil {
    public static YamlConfiguration load(File file) {
        YamlConfiguration yamlConfiguration = null;
        try {
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            yamlConfiguration = YamlConfiguration.loadConfiguration((reader));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return yamlConfiguration;
    }
}
