package com.mcmylx.aacdb.data.types;

import com.mcmylx.aacdb.data.AbstractData;
import com.mcmylx.aacdb.data.DataType;

public class AACKick extends AbstractData {

    private String reason;
    private long timeStamp;

    public AACKick(String uuid, String player, DataType dataType,String reason,long timeStamp) {
        super(uuid, player, dataType);
        this.reason = reason;
        this.timeStamp = timeStamp;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
