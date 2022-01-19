package com.catfixture.virgloverlay.core.window;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.catfixture.virgloverlay.core.debug.Dbg;
import com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.Const;

public class WindowUtils {

    public static NativeWindow CreateNativeWindow(Context context, int x, int y, int width, int height) {
        try {
            NativeWindow nw = new NativeWindow(context, x, y, width, height);
            Log.d(Const.APP_TAG, "Created window");
            return nw;
        } catch (Exception e) {
            Dbg.Error(e);
        }
        return null;
    }

    public static void AttachSurface(WindowManager wm, NativeWindow nw) {
        wm.addView(nw.GetFirstSurface(), nw.GetParams());
        Log.d(Const.APP_TAG, "Surface attached");
    }
    public static void AttachView(WindowManager wm, NativeWindow nw, View view) {
        wm.addView(view, nw.GetParams());
    }
}
