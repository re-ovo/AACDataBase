package com.mcmylx.aacdb.data;

import com.mcmylx.aacdb.AACDB;
import com.mcmylx.aacdb.config.MainConfig;
import com.mcmylx.aacdb.data.types.AACHeuristic;
import com.mcmylx.aacdb.data.types.AACKick;
import com.mcmylx.aacdb.data.types.AACVL;
import com.mcmylx.aacdb.utils.LogUtil;
import com.mcmylx.aacdb.utils.VLParser;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataManager {

    private String vlTableName;
    private String kickTableName;
    private String heuristicTableName;

    public DataManager() {
        LogUtil.log("Enabled Data Manager System");
    }

    /**
     * Create tables
     */
    public void createTables() {
        if (MainConfig.DEBUG) {
            return;
        }
        //put table name field
        this.vlTableName = MainConfig.table_prefix + "vl";
        this.heuristicTableName = MainConfig.table_prefix + "heuristic";
        this.kickTableName = MainConfig.table_prefix + "kick";

        //create table
        String SQL_TABLE_VL = "CREATE TABLE IF NOT EXISTS " + this.vlTableName + "(`name` varchar(255) NOT NULL, `uuid` varchar(100) NOT NULL, `vl` text NOT NULL, PRIMARY KEY (`uuid`))";
        String SQL_TABLE_KICK = "CREATE TABLE IF NOT EXISTS " + this.kickTableName + "(`timestamp` bigint(8) NOT NULL, `name` varchar(255) NOT NULL, `uuid` varchar(255) NOT NULL, `reason` text NOT NULL, PRIMARY KEY (`timestamp`))";
        String SQL_TABLE_HEURISTIC =
                "CREATE TABLE IF NOT EXISTS " + this.heuristicTableName + "(`timestamp` bigint(8) NOT NULL, `name` varchar(255) NOT NULL, `uuid` varchar(255) NOT NULL, `pattern` varchar(255) NOT NULL, `accuracy` double(16,2) NOT NULL, PRIMARY KEY (`timestamp`))";

        try {
            Connection connection = AACDB.getInstance().getDatabaseManager().getConnection();
            connection.setAutoCommit(false);

            Statement statement = connection.createStatement();
            statement.addBatch(SQL_TABLE_VL);
            statement.addBatch(SQL_TABLE_HEURISTIC);
            statement.addBatch(SQL_TABLE_KICK);
            statement.executeBatch();

            connection.commit();
            statement.close();
            connection.close();
        } catch (SQLException se) {
            se.printStackTrace();
            LogUtil.log("Cant create table!");
        }
    }

    /**
     * fetch player's vl data from database
     *
     * @param player Player Object
     * @return AACVL Object
     */
    public AACVL fetchData(Player player) {
        AACVL aacvl = null;
        try {
            Connection connection = AACDB.getInstance().getDatabaseManager().getConnection();
            String SQL =
                    "SELECT * FROM " + this.vlTableName + " WHERE uuid=?";
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, player.getUniqueId().toString());

            ResultSet resultSet = preparedStatement.executeQuery();


            if (resultSet.next()) {
                String vl = resultSet.getString("vl");
                Map<String, Integer> vlData = VLParser.parse(vl);
                aacvl = new AACVL(player.getUniqueId().toString(), player.getName(), DataType.AACVL, vlData);
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return aacvl;
    }

    /**
     * Save AACVL Object to database
     *
     * @param aacvl AACVL Object
     */
    public void saveAACVL(AACVL aacvl) {
        try {
            Connection connection = AACDB.getInstance().getDatabaseManager().getConnection();
            String SQL =
                    "INSERT INTO " + this.vlTableName + "(uuid, name, vl) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE name=VALUES(name),vl=VALUES(vl)";
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, aacvl.getUuid());
            preparedStatement.setString(2, aacvl.getPlayer());
            preparedStatement.setString(3, VLParser.convertToString(aacvl));
            int rows = preparedStatement.executeUpdate();
            if (rows == 0) {
                LogUtil.log("Cant save AAC VL Data of player: " + aacvl.getPlayer());
            }
            preparedStatement.close();
            connection.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    /**
     * save kick data to database
     *
     * @param aacKick kick object
     */
    public void addKickData(AACKick aacKick) {
        try {
            Connection connection = AACDB.getInstance().getDatabaseManager().getConnection();
            String SQL
                    = "INSERT INTO " + this.kickTableName + "(timestamp,uuid,name,reason) VALUES(?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setLong(1, aacKick.getTimeStamp());
            preparedStatement.setString(2, aacKick.getUuid());
            preparedStatement.setString(3, aacKick.getPlayer());
            preparedStatement.setString(4, aacKick.getReason());
            preparedStatement.execute();
            preparedStatement.close();
            connection.close();
        } catch (Exception se) {
            se.printStackTrace();
            LogUtil.log("Error/Cant inset aackick data into database");
        }
    }

    /**
     * Insert Heuristic data to database
     *
     * @param aacHeuristic heuristic data object
     */
    public void addHeuristicData(AACHeuristic aacHeuristic) {
        try {
            Connection connection = AACDB.getInstance().getDatabaseManager().getConnection();
            String SQL
                    = "INSERT INTO " + this.heuristicTableName + "(timestamp,uuid,name,pattern,accuracy) VALUES(?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);

            preparedStatement.setLong(1, aacHeuristic.getTimeStamp());
            preparedStatement.setString(2, aacHeuristic.getUuid());
            preparedStatement.setString(3, aacHeuristic.getPlayer());
            preparedStatement.setString(4, aacHeuristic.getPattern());
            preparedStatement.setDouble(5, aacHeuristic.getAccuracy());

            preparedStatement.execute();
            preparedStatement.close();
            connection.close();
        } catch (Exception se) {
            se.printStackTrace();
            LogUtil.log("Error/Cant inset aac heuristic data into database");
        }
    }

    /**
     * Remove player's vl data
     *
     * @param uuid player's uuid
     */
    public void removeVLData(String uuid) {
        try {
            Connection connection = AACDB.getInstance().getDatabaseManager().getConnection();
            String SQL
                    = "DELETE FROM " + this.vlTableName + " WHERE uuid='" + uuid+"'";
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.execute();

            preparedStatement.close();
            connection.close();
        } catch (Exception se) {
            se.printStackTrace();
            LogUtil.log("Error/Cant remove vl data into database");
        }
    }

    /**
     * Clear player's all data
     *
     * @param player Player name
     */
    public void clear(String player) {
        try {
            Connection connection = AACDB.getInstance().getDatabaseManager().getConnection();
            Statement statement = connection.createStatement();

            statement.addBatch("DELETE FROM " + this.vlTableName + " WHERE name='" + player+"'");
            statement.addBatch("DELETE FROM " + this.kickTableName + " WHERE name='" + player+"'");
            statement.addBatch("DELETE FROM " + this.heuristicTableName + " WHERE name='" + player+"'");

            statement.executeBatch();

            statement.close();
            connection.close();
        } catch (Exception se) {
            se.printStackTrace();
            LogUtil.log("Error/Cant clear player data into database");
        }
    }

    public List<AACKick> lookupKickData(String player,int page){
        List<AACKick> kicksList = new ArrayList<>();
        int start = 10 * (page - 1);
        String SQL =
                "SELECT * FROM "+this.kickTableName+" WHERE name='"+player+"' ORDER BY timestamp DESC LIMIT "+start+",10";
        try {
            Connection connection = AACDB.getInstance().getDatabaseManager().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                String uuid = resultSet.getString("uuid");
                String name = resultSet.getString("name");
                String reason = resultSet.getString("reason");
                long timestamp = resultSet.getLong("timestamp");

                kicksList.add(new AACKick(uuid,player,DataType.AACKick,reason,timestamp));
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (Exception se) {
            se.printStackTrace();
            LogUtil.log("Error/Cant lookup kick data database");
        }
        return kicksList;
    }

    public List<AACHeuristic> lookHeuristicData(String player,int page){
        List<AACHeuristic> aacHeuristics = new ArrayList<>();
        int start = 10 * (page - 1);
        String SQL =
                "SELECT * FROM "+this.heuristicTableName+" WHERE name='"+player+"' ORDER BY timestamp DESC LIMIT "+start+",10";
        try {
            Connection connection = AACDB.getInstance().getDatabaseManager().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                String uuid = resultSet.getString("uuid");
                String name = resultSet.getString("name");
                String pattern = resultSet.getString("pattern");
                Double accuracy = resultSet.getDouble("accuracy");
                long timestamp = resultSet.getLong("timestamp");

                aacHeuristics.add(new AACHeuristic(uuid,name,pattern,accuracy,timestamp));
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (Exception se) {
            se.printStackTrace();
            LogUtil.log("Error/Cant lookup heuristic data database");
        }
        return aacHeuristics;
    }

    public List<AACKick> getRecentAACKicks(Player player){
        List<AACKick> kicksList = new ArrayList<>();
        long minTime = System.currentTimeMillis() - (3600 * 1000);
        String SQL =
                "SELECT * FROM "+this.kickTableName+" WHERE uuid='"+player.getName()+"' AND timestamp>="+minTime+" ORDER BY timestamp DESC";
        try {
            Connection connection = AACDB.getInstance().getDatabaseManager().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                String uuid = resultSet.getString("uuid");
                String name = resultSet.getString("name");
                String reason = resultSet.getString("reason");
                long timestamp = resultSet.getLong("timestamp");

                kicksList.add(new AACKick(player.getUniqueId().toString(),player.getName(),DataType.AACKick,reason,timestamp));
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (Exception se) {
            se.printStackTrace();
            LogUtil.log("Error/Cant lookup kick data database");
        }
        return kicksList;
    }
}
