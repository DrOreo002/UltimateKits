package com.songoda.ultimatekits.api;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Update implements Listener {

    private final static String VERSION = "1.0";

    private static final String BASE_URL = "http://report.mcupdate.org";

    /**
     * Server received information.
     */
    private static String updateMessage = "";
    private static String latestVersion = "1.0";
    private static boolean upToDate = true;

    private final Plugin pl;

    private final boolean debug = true;

    private URL url;

    /**
     * Interval of time to ping (in minutes)
     */
    private static final int PING_INTERVAL = 15;

    /**
     * The scheduled task
     */
    private volatile BukkitTask task = null;

    public Update(final Plugin plugin) throws IOException {
        if (plugin == null) {
            // Catch if plugin is null. Wouldnt that be strange?
        }
        this.pl = plugin;
        //I should add a custom configuration for MCUpdate itself
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public boolean start() {
        // Is MCUpdate already running?
        if (task != null) {
            return true;
        }
        // Begin hitting the server with glorious data
        task = pl.getServer().getScheduler().runTaskTimerAsynchronously(pl, new Runnable() {
            private boolean firstPost = true;

            public void run() {
                report();
            }
        }, 0, PING_INTERVAL * 1200);
        return true;
    }

    private int getOnlinePlayers() {
        try {
            Method onlinePlayerMethod = Server.class.getMethod("getOnlinePlayers");
            if (onlinePlayerMethod.getReturnType().equals(Collection.class)) {
                return ((Collection<?>) onlinePlayerMethod.invoke(Bukkit.getServer())).size();
            } else {
                return ((Player[]) onlinePlayerMethod.invoke(Bukkit.getServer())).length;
            }
        } catch (Exception ex) {
        }
        return 0;
    }

    public void report() {
        String ver = pl.getDescription().getVersion();
        String name = pl.getDescription().getName();
        int playersOnline = this.getOnlinePlayers();
        boolean onlineMode = Bukkit.getServer().getOnlineMode();
        String serverVersion = Bukkit.getVersion();

        String osname = System.getProperty("os.showableName");
        String osarch = System.getProperty("os.arch");
        String osversion = System.getProperty("os.version");
        String java_version = System.getProperty("java.version");
        int coreCount = Runtime.getRuntime().availableProcessors();

        String report = "{ \"report\": {";
        report += toJson("plugin", name) + ",";
        report += toJson("version", ver) + ",";
        report += toJson("playersonline", playersOnline + "") + ",";
        report += toJson("onlinemode", onlineMode + "") + ",";
        report += toJson("serverversion", serverVersion) + ",";

        report += toJson("osname", osname) + ",";
        report += toJson("osarch", osarch) + ",";
        report += toJson("osversion", osversion) + ",";
        report += toJson("javaversion", java_version) + ",";
        report += toJson("corecount", coreCount + "") + "";

        report += "} }";

        byte[] data = report.getBytes();

        try {

            String a = BASE_URL;
            url = new URL(a);
            URLConnection c = url.openConnection();
            c.setConnectTimeout(2500);
            c.setReadTimeout(3500);

            c.addRequestProperty("User-Agent", "MCUPDATE/" + VERSION);
            c.addRequestProperty("Content-Type", "application/json");
            c.addRequestProperty("Content-Length", Integer.toString(data.length));
            c.addRequestProperty("Accept", "application/json");
            c.addRequestProperty("Connection", "close");

            c.setDoOutput(true);

            OutputStream os = c.getOutputStream();
            os.write(data);
            os.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
            String endData = br.readLine().trim();

            String serverMessage = getString(endData, "message");
            String cVersion = getString(endData, "pl_Version");
            updateMessage = getString(endData, "update_Message");

            if (!serverMessage.equals("ERROR")) {
                if (!ver.equals(cVersion)) {
                    upToDate = false;
                    latestVersion = cVersion;
                }
            }
            br.close();
        } catch (Exception ignored) {
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        String name = pl.getDescription().getName();
        Player p = e.getPlayer();
        if (p.isOp() && upToDate == false) {
            p.sendMessage(format(updateMessage));
        }
    }

    public String getString(String data, String key) {
        String dat = data.replace("{ \"Response\": {\"", "");
        dat = dat.replace("\"} }", "");
        List<String> list = Arrays.asList(dat.split("\",\""));

        for (String stub : list) {
            List<String> list2 = Arrays.asList(stub.split("\":\""));
            if (key.equals(list2.get(0))) {
                return list2.get(1);
            }
        }
        return null;
    }

    public static String toJson(String key, String value) {
        return "\"" + key + "\":\"" + value + "\"";
    }

    public static String format(String format) {
        return ChatColor.translateAlternateColorCodes('&', format);
    }
}