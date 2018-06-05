package com.mcmylx.aacdb.data;

import java.util.Arrays;
import java.util.List;

public enum DataType {
    AACVL,
    AACKick,
    AACHeuristic;

    public static DataType getType(String name) {
        DataType[] types = values();
        for (DataType dataType : types) {
            if (dataType.name().equalsIgnoreCase(name)) {
                return dataType;
            }
        }

        return null;
    }
}
