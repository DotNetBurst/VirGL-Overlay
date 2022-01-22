package com.catfixture.virgloverlay.core.input.devices;

import static com.catfixture.virgloverlay.core.App.app;

import android.content.Context;

import com.catfixture.virgloverlay.core.overlay.IOverlayFragment;


public abstract class BasicInputDevice implements IInputDevice {
    protected Context context;
    private IOverlayFragment overlayFragment;

    public BasicInputDevice(Context context, IOverlayFragment overlayFragment) {
        this.context = context;
        this.overlayFragment = overlayFragment;
    }

    @Override
    public void Initialize() {
        Initialize(overlayFragment);
    }

    @Override
    public void Destroy() {
        overlayFragment.Destroy();
    }

    @Override
    public void Show() {
        app.GetOverlayManager().Show(overlayFragment);
    }

    @Override
    public void Hide() {
        app.GetOverlayManager().Hide(overlayFragment);
    }

    protected abstract void Initialize(IOverlayFragment window);
}
