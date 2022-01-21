package com.catfixture.virgloverlay.core.input.devices.touchControls;

import android.content.Context;

import com.catfixture.virgloverlay.core.input.data.InputConfig;
import com.catfixture.virgloverlay.core.input.devices.BasicInputDevice;
import com.catfixture.virgloverlay.core.input.windows.IInputWindow;
import com.catfixture.virgloverlay.core.input.windows.touchControls.TouchControlsWindow;


public class TouchControlsDevice extends BasicInputDevice {

    public TouchControlsDevice(Context context) {
        super(context, new TouchControlsWindow(context));
    }

    @Override
    protected void Initialize(IInputWindow window) {

    }
}
