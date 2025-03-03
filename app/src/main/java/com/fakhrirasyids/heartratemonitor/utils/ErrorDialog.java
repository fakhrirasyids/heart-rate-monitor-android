package com.fakhrirasyids.heartratemonitor.utils;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.fakhrirasyids.heartratemonitor.R;

public class ErrorDialog {
    public static void show(Context context, String message, Runnable onRetry) {
        new AlertDialog.Builder(context)
                .setTitle(ContextCompat.getString(context, R.string.error_text))
                .setMessage(message)
                .setPositiveButton(ContextCompat.getString(context, R.string.retry_text), (dialog, which) -> {
                    dialog.dismiss();
                    onRetry.run();
                }).show();
    }
}
