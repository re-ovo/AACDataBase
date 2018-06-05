package com.mcmylx.aacdb;


import com.mcmylx.aacdb.listener.AACCheatListener;

public class ConditionalCommandsInjector {

    public static void call(String cmd) {
        AACCheatListener.handleKick(cmd);
    }
}
