package com.tadahtech.pub.pa.data.sql;

import com.tadahtech.pub.pa.ProAnnouncer;
import com.tadahtech.pub.pa.data.StorageManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by Timothy Andis
 */
public class QueryThread extends Thread {

    private Queue<Runnable> queue = new ConcurrentLinkedDeque<>();
    private SQLManager sqlManager;

    public QueryThread() {
        setName("Announcer-QueryThread");
        start();
        StorageManager storageManager = ProAnnouncer.getInstance().getSettings().getStorageManager();
        if (storageManager instanceof SQLManager) {
            this.sqlManager = (SQLManager) storageManager;
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(250);
            } catch (InterruptedException ignored) {
            }
            if (queue.peek() != null) {
                queue.poll().run();
            }
        }
    }

    public void addQuery(String s) {
        queue.add(() -> {
            Connection con = null;
            PreparedStatement pst = null;
            try {
                con = sqlManager.getConnection();
                pst = con.prepareStatement(s);
                pst.executeUpdate();
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (pst != null) {
                        pst.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void addQuery(final SQLStatement query) {
        queue.add(() -> {
            Connection con = null;
            PreparedStatement pst = null;
            try {
                con = sqlManager.getConnection();
                pst = query.prepare(con);
                pst.executeUpdate();
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (pst != null) {
                        pst.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }


}
