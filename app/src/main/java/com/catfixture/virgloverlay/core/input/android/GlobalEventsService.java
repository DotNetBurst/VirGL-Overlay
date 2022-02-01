package com.catfixture.virgloverlay.core.input.android;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;

import com.catfixture.virgloverlay.core.debug.Dbg;

public class GlobalEventsService extends AccessibilityService {

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        Dbg.Msg("ACT : " + accessibilityEvent.getAction());
    }

    @Override
    public void onInterrupt() {

    }
}
