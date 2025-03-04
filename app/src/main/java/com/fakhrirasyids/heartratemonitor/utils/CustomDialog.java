package com.fakhrirasyids.heartratemonitor.utils;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

public class CustomDialog {
    public static void show(Context context, String title, String message, String confirmation, Runnable onRetry) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(confirmation, (d, which) -> {
                    d.dismiss();
                    onRetry.run();
                })
                .setCancelable(false)
                .create();

        dialog.show();
    }
}
