package com.catfixture.virgloverlay.core.overlay;

import static com.catfixture.virgloverlay.core.AppContext.app;

import android.content.Context;

import com.catfixture.virgloverlay.core.input.devices.IInputDevice;
import com.catfixture.virgloverlay.core.input.overlay.TouchDeviceOverlayFragment;

public class OverlayInitializer {
    public static void Init(Context context, IInputDevice device) {
        OverlayManager overlayManager = app.GetOverlayManager();

        //RENDERING FRAG
        RenderingOverlayFragment renderingOverlayFragment = new RenderingOverlayFragment(context);
        overlayManager.Add(renderingOverlayFragment);
        overlayManager.Show(renderingOverlayFragment);

        //TOUCH CONTROLS DEVICE
        TouchDeviceOverlayFragment touchDeviceOverlayFragment = new TouchDeviceOverlayFragment(context, device);
        overlayManager.Add(touchDeviceOverlayFragment);

        //MAIN CONTROL PANEL
        MainControlsOverlayFragment mainControlsOverlayFragment = new MainControlsOverlayFragment(context);
        overlayManager.Add(mainControlsOverlayFragment);


        overlayManager.Show(touchDeviceOverlayFragment);
        //overlayManager.Show(touchDeviceEditorOverlayFragment); //TODO LINK1

    }
}
