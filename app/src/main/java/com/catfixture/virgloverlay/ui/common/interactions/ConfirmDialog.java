package com.catfixture.virgloverlay.ui.common.interactions;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

public class ConfirmDialog {
    public static void Show(Context context, String title, String iniText, String ok, Runnable onOk, String no, Runnable onNo) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(iniText)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(ok, (dialog, whichButton) -> onOk.run()).setNegativeButton(no, (dialogInterface, i) -> {
                    if ( onNo!=null) onNo.run();
                }).show();
    }
    public static void Show(Context context, String title, String iniText, String ok, Runnable onOk) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(iniText)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(ok, (dialog, whichButton) -> onOk.run())
                .show();
    }
    public static void Show(Context context, String title, String iniText) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(iniText)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Ok", (dialog, whichButton) -> {})
                .show();
    }
}
