package com.mcmylx.aacdb.data.types;

import com.mcmylx.aacdb.data.AbstractData;
import com.mcmylx.aacdb.data.DataType;

public class AACHeuristic extends AbstractData {
    private String pattern;
    private double accuracy;
    private long timeStamp;

    public AACHeuristic(String uuid, String player, String pattern, double accuracy, long timeStamp) {
        super(uuid, player, DataType.AACHeuristic);
        this.pattern = pattern;
        this.accuracy = accuracy;
        this.timeStamp = timeStamp;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
