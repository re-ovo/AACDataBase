package com.mcmylx.aacdb.data.types;

import com.mcmylx.aacdb.data.AbstractData;
import com.mcmylx.aacdb.data.DataType;

import java.util.Map;

public class AACVL extends AbstractData {
    private Map<String,Integer> vls;

    public AACVL(String uuid, String player, DataType dataType,Map<String,Integer> vl) {
        super(uuid, player, dataType);
        this.vls = vl;
    }

    public Map<String, Integer> getVls() {
        return vls;
    }

    public void setVls(Map<String, Integer> vls) {
        this.vls = vls;
    }
}
