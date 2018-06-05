package com.mcmylx.aacdb.config;

import com.mcmylx.aacdb.AACDB;
import com.mcmylx.aacdb.interfaces.Reloadable;
import com.mcmylx.aacdb.utils.YamlUtil;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class MainConfig implements Reloadable {

    //database setting
    public static String DB_Adress;
    public static int DB_PORT;
    public static String DB_NAME;
    public static String DB_USER;
    public static String DB_PASSWORD;
    public static int maxConnections = 10;
    public static String table_prefix = "aacdb_";

    //Record data setting
    public static boolean enable_vl;
    public static boolean enable_kick;
    public static boolean enable_heuristic;

    //Update check
    public static boolean update = true;

    //Punishment setting
    public static boolean enable_punishment;
    public static int threshold = 5;
    public static List<String> commands;

    //debug
    public static boolean DEBUG = false;

    private File file;
    private YamlConfiguration configuration;

    @Override
    public void reload() {
        this.file = new File(AACDB.getInstance().getDataFolder(),"config.yml");
        if(!this.file.exists()){
            AACDB.getInstance().saveResource("config.yml",false);
        }
        this.configuration = YamlUtil.load(this.file);

        //Database Config
        DB_Adress = configuration.getString("mysql.address");
        DB_PORT = configuration.getInt("mysql.port");
        DB_NAME = configuration.getString("mysql.database");
        DB_USER = configuration.getString("mysql.user");
        DB_PASSWORD = configuration.getString("mysql.password");
        maxConnections = configuration.getInt("mysql.max_connections");
        table_prefix = configuration.getString("mysql.table_prefix");

        //Record data setting
        enable_vl = configuration.getBoolean("data.record_vl");
        enable_kick = configuration.getBoolean("data.record_aackick");
        enable_heuristic = configuration.getBoolean("data.record_heuristic");

        //Punishment
        enable_punishment = configuration.getBoolean("punishment.enable");
        threshold = configuration.getInt("punishment.threshold");
        commands = configuration.getStringList("punishment.commands");

        //update
        update = configuration.getBoolean("update");
        //debug
        if(configuration.contains("debug")){
            DEBUG = configuration.getBoolean("debug");
        }
    }
}
