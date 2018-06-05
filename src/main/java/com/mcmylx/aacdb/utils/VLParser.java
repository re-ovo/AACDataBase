package com.mcmylx.aacdb.utils;

import com.google.gson.Gson;
import com.mcmylx.aacdb.data.types.AACVL;

import java.util.Map;

public class VLParser {
    public static String convertToString(AACVL aacvl) {
        Gson gson = new Gson();
        return gson.toJson(aacvl.getVls());
    }

    public static Map<String, Integer> parse(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Map.class);
    }
}
