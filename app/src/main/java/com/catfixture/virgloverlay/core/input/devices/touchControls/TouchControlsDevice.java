package com.catfixture.virgloverlay.core.input.devices.touchControls;

import android.content.Context;

import com.catfixture.virgloverlay.core.input.devices.BasicInputDevice;
import com.catfixture.virgloverlay.core.input.windows.touchControls.TouchControlsOverlayFragment;
import com.catfixture.virgloverlay.core.overlay.IOverlayFragment;


public class TouchControlsDevice extends BasicInputDevice {

    public TouchControlsDevice(Context context) {
        super(context, new TouchControlsOverlayFragment());
    }

    @Override
    protected void Initialize(IOverlayFragment window) {

    }
}
