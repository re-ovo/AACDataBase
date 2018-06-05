package com.mcmylx.aacdb.database;

import com.mcmylx.aacdb.config.MainConfig;
import com.mcmylx.aacdb.interfaces.Reloadable;
import com.mcmylx.aacdb.utils.LogUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据库连接池系统
 * 基于Hikari-CP
 */
public class DatabaseManager implements Reloadable {
    private HikariDataSource hikariDataSource;

    public DatabaseManager() {
        this.reload();
    }

    @Override
    public void reload() {
        if (MainConfig.DEBUG) {
            return;
        }
        HikariConfig config = new HikariConfig();

        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.setJdbcUrl("jdbc:mysql://" + MainConfig.DB_Adress + ":" + MainConfig.DB_PORT + "/" + MainConfig.DB_NAME + "?autoReconnect=true");
        config.setUsername(MainConfig.DB_USER);
        config.setPassword(MainConfig.DB_PASSWORD);

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("maximumPoolSize", String.valueOf(MainConfig.maxConnections));

        this.hikariDataSource = new HikariDataSource(config);
        LogUtil.log("Reconnect to database...");

        checkConnection();
    }

    public Connection getConnection() {
        try {
            return hikariDataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            LogUtil.log("Cant get connection from connection pool,please check your mysql setting");
        }
        return null;
    }

    public void checkConnection() {
        try {
            Connection connection = this.hikariDataSource.getConnection();
            connection.close();
        } catch (SQLException e) {
            LogUtil.log("&cCant connect to mysql database,please check the config.yml");
            LogUtil.log("&cError Code:&r " + e.getErrorCode());
            MainConfig.DEBUG = true;
        }
    }

    public void close() {
        this.hikariDataSource.close();
    }
}
