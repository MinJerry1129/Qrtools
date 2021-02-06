package com.kessi.photopipcollagemaker.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Show custom dialog. Custom dialog with it own style!
 */
@SuppressLint("InflateParams")
public class DialogUtils {

    /**
     * Show dialog with string resource
     *
     * @param context      : context that dialog will be shown
     * @param titleResId   : title string resource id
     * @param messageResId : message string resource id
     * @param listener
     * @return
     */
    public static Dialog showDialog(Context context, int titleResId,
                                    int messageResId, DialogOnClickListener listener) {
        String title = context.getResources().getString(titleResId);
        String message = context.getResources().getString(messageResId);

        return showDialog(context, title, message, listener);
    }

    /**
     * Show dialog
     *
     * @param context context that dialog will be shown
     * @param title   title of dialog
     * @param message message of dialog
     * @return
     */
    public static Dialog showDialog(Context context, String title,
                                    String message, final DialogOnClickListener listener) {
        // check context. If not check here, sometimes it can be crashed
        if (context == null)
            return null;
        Activity activity = (Activity) context;
        if (activity.isFinishing())
            return null;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (listener != null) {
                                    listener.onOKButtonOnClick();
                                }
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();

        return alert;
    }

    /**
     * Show confirm dialog with string resource (Yes/No dialog)
     *
     * @param context      that dialog will be shown
     * @param titleResId   title string resource id
     * @param messageResId message string resource id
     * @param listener     handle event when click button Yes/No
     * @return
     */
    public static Dialog showConfirmDialog(Context context, int titleResId,
                                           int messageResId, final ConfirmDialogOnClickListener listener) {
        String title = context.getResources().getString(titleResId);
        String message = context.getResources().getString(messageResId);

        return showConfirmDialog(context, title, message, listener);
    }

    /**
     * Show confirm dialog (Yes/No dialog)
     *
     * @param context  context that dialog will be shown
     * @param title    title of dialog
     * @param message  message of dialog
     * @param listener handle event when click button Yes/No
     * @return
     */
    public static Dialog showConfirmDialog(Context context, String title,
                                           String message, final ConfirmDialogOnClickListener listener) {
        // check context. If not check here, sometimes it can be crashed
        if (context == null)
            return null;
        Activity activity = (Activity) context;
        if (activity.isFinishing())
            return null;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (listener != null)
                                    listener.onOKButtonOnClick();
                            }
                        })
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (listener != null)
                                    listener.onCancelButtonOnClick();
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();

        return alert;
    }

    /**
     * using in confirm dialog
     */
    public static interface ConfirmDialogOnClickListener {
        public void onOKButtonOnClick();

        public void onCancelButtonOnClick();
    }

    /**
     * using in normal dialog
     */
    public static interface DialogOnClickListener {
        public void onOKButtonOnClick();
    }



}
