package com.catfixture.virgloverlay.core.input.devices;

import static com.catfixture.virgloverlay.core.App.app;

import android.content.Context;

import com.catfixture.virgloverlay.core.overlay.IOverlayFragment;


public abstract class BasicInputDevice implements IInputDevice {
    protected Context context;
    private IOverlayFragment overlayFragment;

    public BasicInputDevice(Context context) {
        this.context = context;
        overlayFragment = Initialize();
    }

    @Override
    public void Show() {
        app.GetOverlayManager().Show(overlayFragment);
    }

    @Override
    public void Hide() {
        app.GetOverlayManager().Hide(overlayFragment);
    }

    public abstract IOverlayFragment Initialize();
}
