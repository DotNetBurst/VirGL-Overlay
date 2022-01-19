package com.catfixture.virgloverlay.core.debug.logging;

import static com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.Const.APP_TAG;

import android.util.Log;

import com.catfixture.virgloverlay.core.debug.Dbg;

public class GlobalExceptions {
    private static Thread.UncaughtExceptionHandler handler = (paramThread, paramThrowable) -> {
        Throwable cause = paramThrowable.getCause();
        Dbg.Error(paramThrowable.getMessage());
        if (cause != null) {
            Dbg.Error("Uncaught exception");
            Dbg.Error(cause);
        }
    };

    public static void Init() {
        Thread.setDefaultUncaughtExceptionHandler(handler);
    }
}
