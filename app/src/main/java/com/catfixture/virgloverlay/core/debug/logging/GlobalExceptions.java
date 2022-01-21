package com.catfixture.virgloverlay.core.debug.logging;

import com.catfixture.virgloverlay.core.debug.Dbg;

public class GlobalExceptions {
    //TODO IMPLEMENT FILE LOGGING (INTERNAL)
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
