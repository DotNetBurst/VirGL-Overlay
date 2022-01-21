package com.catfixture.virgloverlay.core.input;

import static com.catfixture.virgloverlay.core.input.Devices.SCREEN_KEYBOARD_DEVICE;
import static com.catfixture.virgloverlay.core.input.Devices.TOUCH_CONTROLS_DEVICE;

import android.content.Context;

import com.catfixture.virgloverlay.core.input.data.InputConfig;
import com.catfixture.virgloverlay.core.input.devices.IInputDevice;
import com.catfixture.virgloverlay.core.input.devices.touchControls.TouchControlsDevice;

public class MainInputProvider implements IInputProvider {
    @Override
    public IInputDevice ResolveDevice(Context context, Object arg) {
        InputConfig cfg = (InputConfig) arg;
        int currentDevice = TOUCH_CONTROLS_DEVICE;//TODO

        switch (currentDevice) {
            case TOUCH_CONTROLS_DEVICE:
                return new TouchControlsDevice(context);
            case SCREEN_KEYBOARD_DEVICE:
                return null;
            default:
                return null;
        }
    }
}
