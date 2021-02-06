package com.kessi.photopipcollagemaker.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;

public class CommonDialog {
    public static void showDialogConfirm(Activity activity, int message, String yesText, String noText, OnClickListener onYes, OnClickListener onNo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(true);
        builder.setMessage(message);
        builder.setPositiveButton((CharSequence) yesText, onYes);
        builder.setNegativeButton((CharSequence) noText, onNo);
        builder.create().show();
    }

    public static void showInfoDialog(Activity activity, int message, String yesText, OnClickListener onYes) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(true);
        builder.setMessage(message);
        builder.setPositiveButton((CharSequence) yesText, onYes);
        builder.create().show();
    }
}
