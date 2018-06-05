package com.mcmylx.aacdb;

import com.mcmylx.aacdb.config.MainConfig;
import com.mcmylx.aacdb.utils.LogUtil;
import org.bukkit.Bukkit;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class UpdateChecker {
    public static String getNewestVersion() {
        String Url = "https://api.spigotmc.org/legacy/update.php?resource=57320";
        try {
            URL url = new URL(Url);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            String version = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();
            return version;
        } catch (Exception e) {
            LogUtil.log("Cant check update....please check your internet connection");
        }
        return null;
    }

    public static void start() {
        if (!MainConfig.update) {
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(AACDB.getInstance(), () -> {
            String ver = getNewestVersion();
            if (ver == null) {
                return;
            }
            if (ver.equalsIgnoreCase(AACDB.getInstance().getDescription().getVersion())) {
                Bukkit.getConsoleSender().sendMessage("§aYou are using the newest AACDataBase <3");
            } else {
                Bukkit.getConsoleSender().sendMessage("§cAACDataBase has a update: §f" + ver + "§c ,Please donwload it from spigotmc.org");
            }
        });
    }
}
