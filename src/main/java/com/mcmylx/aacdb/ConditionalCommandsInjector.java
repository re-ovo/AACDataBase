package com.mcmylx.aacdb;

import com.allatori.annotations.DoNotRename;
import com.mcmylx.aacdb.listener.AACCheatListener;

@DoNotRename
public class ConditionalCommandsInjector {
    @DoNotRename
    public static void call(String cmd) {
        AACCheatListener.handleKick(cmd);
    }
}
