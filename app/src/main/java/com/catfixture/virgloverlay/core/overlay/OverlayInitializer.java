package com.catfixture.virgloverlay.core.overlay;

import static com.catfixture.virgloverlay.core.App.app;

import android.content.Context;

import com.catfixture.virgloverlay.core.input.devices.IInputDevice;
import com.catfixture.virgloverlay.core.input.overlay.TouchDeviceEditorOverlayFragment;
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

        TouchDeviceEditorOverlayFragment touchDeviceEditorOverlayFragment = new TouchDeviceEditorOverlayFragment(context);
        overlayManager.Add(touchDeviceEditorOverlayFragment);
        overlayManager.onClick.addObserver((observable, o) -> touchDeviceEditorOverlayFragment.SetSelected(-1));
        touchDeviceEditorOverlayFragment.onSetChanged.addObserver((observable, o) -> touchDeviceOverlayFragment.InflateControls());
        touchDeviceEditorOverlayFragment.onClosed.addObserver((observable, o) -> touchDeviceOverlayFragment.OnEditorClosed());

        //MAIN CONTROL PANEL
        MainControlsOverlayFragment mainControlsOverlayFragment = new MainControlsOverlayFragment(context);
        overlayManager.Add(mainControlsOverlayFragment);


        overlayManager.Show(touchDeviceOverlayFragment);
        overlayManager.Show(touchDeviceEditorOverlayFragment); //TODO LINK1

    }
}
