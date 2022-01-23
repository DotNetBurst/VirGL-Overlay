package com.catfixture.virgloverlay.core.input.devices.touchControls;

import static com.catfixture.virgloverlay.core.App.app;
import static com.catfixture.virgloverlay.core.input.overlay.TouchControlsEditorOverlayFragment.ID_TOUCH_CONTROLS_EDITOR_OVERLAY;
import static com.catfixture.virgloverlay.core.input.overlay.TouchControlsOverlayFragment.ID_TOUCH_CONTROLS_OVERLAY;

import android.content.Context;
import android.view.KeyEvent;
import android.view.inputmethod.InputConnection;

import com.catfixture.virgloverlay.core.debug.Dbg;
import com.catfixture.virgloverlay.core.input.devices.BasicInputDevice;
import com.catfixture.virgloverlay.core.input.overlay.TouchControlsEditorOverlayFragment;
import com.catfixture.virgloverlay.core.input.overlay.TouchControlsOverlayFragment;
import com.catfixture.virgloverlay.core.overlay.IOverlayFragment;
import com.catfixture.virgloverlay.core.overlay.OverlayManager;


public class TouchControlsDevice extends BasicInputDevice {
    private TouchControlsOverlayFragment touchControlsFragment;
    private TouchControlsEditorOverlayFragment touchControlsEditorFragment;
    private InputConnection inputConnection;

    public TouchControlsDevice(Context context) {
        super(context);
    }

    @Override
    public IOverlayFragment Initialize() {
        OverlayManager overlayManager = app.GetOverlayManager();
        touchControlsFragment = new TouchControlsOverlayFragment(this);
        overlayManager.Add(touchControlsFragment);

        touchControlsEditorFragment = new TouchControlsEditorOverlayFragment();
        overlayManager.Add(touchControlsEditorFragment);

        touchControlsEditorFragment.onSetChanged.addObserver((observable, o) -> touchControlsFragment.InflateControls());
        touchControlsEditorFragment.onClosed.addObserver((observable, o) -> touchControlsFragment.OnEditorClosed());
        overlayManager.onClick.addObserver((observable, o) -> touchControlsEditorFragment.SetSelected(-1));

        overlayManager.Show(ID_TOUCH_CONTROLS_EDITOR_OVERLAY);

        return touchControlsFragment;
    }

    @Override
    public void Destroy() {
        OverlayManager overlayManager = app.GetOverlayManager();
        touchControlsFragment.Destroy();
        touchControlsEditorFragment.Destroy();
        overlayManager.Remove(touchControlsFragment);
        overlayManager.Remove(touchControlsEditorFragment);
    }

    @Override
    public void Setup(InputConnection inputConnection) {
        this.inputConnection = inputConnection;
    }

    @Override
    public void SendKeyEvent(KeyEvent keyEvent) {
        try {
            inputConnection.sendKeyEvent(keyEvent);
        } catch (Exception x) {
            Dbg.Error(x);
        }
    }
}
