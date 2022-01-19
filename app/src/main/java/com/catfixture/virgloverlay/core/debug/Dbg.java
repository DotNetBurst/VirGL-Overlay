package com.catfixture.virgloverlay.core.debug;

import static com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.Const.APP_TAG;

import android.util.Log;

public class Dbg {
    public static void Error(Throwable x) {
        x.printStackTrace();
    }
    public static void Error(String x) {
        Log.e(APP_TAG, x);
    }

    public static void Msg(String msg) {
        Log.d(APP_TAG, msg);
    }
}
