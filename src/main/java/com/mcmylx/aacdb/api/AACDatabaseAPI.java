package com.mcmylx.aacdb.api;

import com.mcmylx.aacdb.AACDB;

import java.sql.Connection;

public class AACDatabaseAPI {
    public static void reload(){
        AACDB.getInstance().reload();
    }

    public static Connection getConnection(){
        return AACDB.getInstance().getDatabaseManager().getConnection();
    }
}
