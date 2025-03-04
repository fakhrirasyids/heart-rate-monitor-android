package com.fakhrirasyids.heartratemonitor.ui.customview.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.fakhrirasyids.heartratemonitor.R;

public class LoadingDialog {
    private static AlertDialog progressDialog;

    public static void show(Context context) {
        if (progressDialog != null && progressDialog.isShowing()) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.progress_dialog, null);
        builder.setView(view);
        builder.setCancelable(false);

        progressDialog = builder.create();
        progressDialog.show();
    }

    public static void hide() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
