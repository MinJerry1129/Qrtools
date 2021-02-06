package com.kessi.photopipcollagemaker.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;


import com.kessi.photopipcollagemaker.StartActivity;

import java.io.File;

public class Utils {
    public static Bitmap mBitmap;
    public static int screenHeight;
    public static int screenWidth;
    public static Boolean slim = Boolean.valueOf(false);

    public static void mShare(String filepath, Activity activity) {
        Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse(String.valueOf(filepath)));
        File file = new File(filepath);
        intent.setDataAndType(Uri.fromFile(file), "image/*");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        activity.startActivity(Intent.createChooser(intent, "Share Image using"));
    }


    public static void gotoMain(Context context){
        Intent newIntent = new Intent(context, StartActivity.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(newIntent);
    }

    public static void mediaScanner(Context context, String filePath, String fileName) {
        try {
            if (Build.VERSION.SDK_INT >= 19) {
                MediaScannerConnection.scanFile(context, new String[]{new File( filePath + fileName).getAbsolutePath()},
                        null, new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                            }
                        });
            } else {
                context.sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED", Uri.fromFile(new File( filePath + fileName))));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
