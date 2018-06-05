package com.mcmylx.aacdb.data;

public class AbstractData {

    private String uuid;
    private String player;
    private DataType dataType;

    public AbstractData(String uuid, String player, DataType dataType) {
        this.uuid = uuid;
        this.player = player;
        this.dataType = dataType;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }
}
