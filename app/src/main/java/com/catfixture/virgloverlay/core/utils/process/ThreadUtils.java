package com.catfixture.virgloverlay.core.utils.process;

import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.catfixture.virgloverlay.core.utils.types.delegates.Action;


public class ThreadUtils {
    public static void Sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {}
    }

    public static void LockThreadUntilUITask (Handler uiRunner, Action<Thread> handleAction) {
        final Thread mutex = Thread.currentThread();
        uiRunner.post(() -> {
            handleAction.Invoke(mutex);
        });
        synchronized (mutex) {
            try {
                mutex.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
