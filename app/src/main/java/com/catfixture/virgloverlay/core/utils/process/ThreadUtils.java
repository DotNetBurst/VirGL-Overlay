package com.catfixture.virgloverlay.core.utils.process;

public class ThreadUtils {
    public static void Sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {}
    }
}
