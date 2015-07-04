package com.tadahtech.pub.pa.data.sql;

import com.tadahtech.pub.pa.PlayerInfo;
import com.tadahtech.pub.pa.ProAnnouncer;
import com.tadahtech.pub.pa.data.StorageManager;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;
import java.util.Map;

/**
 * @author Timothy Andis
 */
public class SQLManager implements StorageManager {

    private QueryThread queryThread;
    private Connection con;
    private int query_count = 0;
    private String host, db, user, pass;
    private int port;
    private String url;

    public SQLManager(String host, String db, String user, String pass, int port) {
        this.queryThread = new QueryThread();
        ProAnnouncer.getInstance().getLogger().info("Hogging the Main Thread for a second, please stand by....");
        long start = System.currentTimeMillis();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + db, user, pass);
            PreparedStatement statement = connection.prepareStatement("CREATE DATAVASE IF NOT EXISTS " + db);
            statement.execute();
        } catch (ClassNotFoundException | SQLException e) {
            ProAnnouncer.getInstance().getLogger().warning("Heyo! You have SQL set as the storageType, but I couldn't connect using the SQL details provided. Please edit those. Disabling...");
            ProAnnouncer.getInstance().getPluginLoader().disablePlugin(ProAnnouncer.getInstance());
            return;
        }
        long end = System.currentTimeMillis();
        long total = end - start;
        ProAnnouncer.getInstance().getLogger().info("OK, Done with the Main Thread! Took: " + total + "ms");
        this.host = host;
        this.db = db;
        this.user = user;
        this.pass = pass;
        this.port = port;
        this.url = "jdbc:mysql://" + host + ":" + port + "/" + db;
        getConnection();
        queryThread.addQuery("CREATE TABLE IF NOT EXISTS `player_info`" +
          "(" +
          "`player` varchar(64) PRIMARY KEY NOT NULL, " +
          "`general` int," +
          "`actionBar` int," +
          "`title` int," +
          ")");
    }

    public Connection getConnection() {
        try {
            if (query_count >= 1000) {
                if (con != null) {
                    con.close();
                }
                con = DriverManager.getConnection(url, user, pass);
                query_count = 0;
            }
            if (con == null || con.isClosed()) {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection(url, user, pass);
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                con = DriverManager.getConnection(url, user, pass);
            } catch (Exception err) {
                err.printStackTrace();
            }
        }

        query_count++;
        return con;
    }

    public ResultSet getResultSet(SQLStatement query) {
        PreparedStatement pst;
        try {
            pst = query.prepare(getConnection());
            pst.execute();
            return pst.getResultSet();
        } catch (Exception err) {
            err.printStackTrace();
            return null;
        }
    }

    @Override
    public void load(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                SQLStatement statement = new SQLStatement("SELECT * FROM `player_info` WHERE `player` = ?");
                statement.set(1, player.getUniqueId().toString());
                ResultSet res = getResultSet(statement);
                try {
                    if (res.next()) {
                        int general = res.getInt("general");
                        int actionBar = res.getInt("actionBar");
                        int title = res.getInt("title");
                        players.newInfo(player, general, actionBar, title);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(ProAnnouncer.getInstance());
    }

    @Override
    public void save(Player player) {
        PlayerInfo info = players.get(player);
        String base = "INSERT INTO `player_info` VALUES(?, ?, ?, ?) ON DUPLICATE KEY UPDATE `general` = ?, `actionBar` = ?, `title` = ?";
        SQLStatement statement = new SQLStatement(base);
        for (Map.Entry<Integer, Object> entry : info.toStatement().entrySet()) {
            statement.set(entry.getKey(), entry.getValue());
        }
        queryThread.addQuery(statement);
        players.logoff(player);
    }
}
