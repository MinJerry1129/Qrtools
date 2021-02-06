package com.xinlan.imageeditlibrary.picchooser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import android.database.Cursor;
import android.os.Environment;

public class Logger {

    private static FileWriter fw;
    private final static Date date = new Date();
    private final static String APP = "GalleryLib";

    public static void log(final Throwable ex) {
        log(ex.getMessage());
        for (StackTraceElement ste : ex.getStackTrace()) {
            log(ste.toString());
        }
    }

    public static void log(final Cursor c) {
        c.moveToFirst();
        String title = "";
        for (int i = 0; i < c.getColumnCount(); i++)
            title += c.getColumnName(i) + " | ";
        log(title);
        while (!c.isAfterLast()) {
            title = "";
            for (int i = 0; i < c.getColumnCount(); i++)
                title += c.getString(i) + " | ";
            log(title);
            c.moveToNext();
        }
    }

    @SuppressWarnings("deprecation")
    public static void log(final String msg) {
        android.util.Log.d(APP, msg);
        try {
            if (fw == null) {
                fw = new FileWriter(new File(
                        Environment.getExternalStorageDirectory().toString() + "/" + APP + ".log"),
                        true);
            }
            date.setTime(System.currentTimeMillis());
            fw.write(date.toLocaleString() + " - " + msg + "\n");
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void finalize() throws Throwable {
        try {
            if (fw != null) fw.close();
        } finally {
            super.finalize();
        }
    }

}
