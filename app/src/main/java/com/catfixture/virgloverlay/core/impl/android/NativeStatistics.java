package com.catfixture.virgloverlay.core.impl.android;

import static com.catfixture.virgloverlay.core.AppContext.app;
import static com.catfixture.virgloverlay.core.overlay.StatisticsOverlay.ID_STATISTICS_OVERLAY_FRAGMENT;

import android.app.ActivityManager;

import com.catfixture.virgloverlay.core.debug.Dbg;
import com.catfixture.virgloverlay.core.overlay.StatisticsOverlay;

public class NativeStatistics {
    public static void UpdateStatistics(short fps, long renderTime) {
        if ( fps == 0) fps = 1;
        int frameTime = 1000 / fps;

        StatisticsOverlay so = (StatisticsOverlay) app.GetOverlayManager().GetFragment(ID_STATISTICS_OVERLAY_FRAGMENT);
        so.UpdateValues(fps);
    }
}
