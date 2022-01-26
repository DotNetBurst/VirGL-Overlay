package com.catfixture.virgloverlay.core.input.devices;

import static com.catfixture.virgloverlay.core.App.app;

import android.content.Context;
import android.renderscript.Float2;

import com.catfixture.virgloverlay.core.debug.Dbg;
import com.catfixture.virgloverlay.core.input.overlay.TouchDeviceEditorOverlayFragment;
import com.catfixture.virgloverlay.core.input.overlay.TouchDeviceOverlayFragment;
import com.catfixture.virgloverlay.core.overlay.OverlayManager;
import com.catfixture.virgloverlay.core.vgobridge.VGOBridgeHandle;
import com.catfixture.virgloverlay.core.vgobridge.VGOBridgeMarshall;
import com.catfixture.virgloverlay.core.vgobridge.VGOBridgeProtocol;


public class TouchDevice implements IInputDevice {
    private VGOBridgeMarshall vgoBridgeMarshall;
    private VGOBridgeHandle handle;
    private Context context;
    private TouchDeviceOverlayFragment touchDeviceOverlayFragment;
    private TouchDeviceEditorOverlayFragment touchDeviceEditorOverlayFragment;
    private Float2 mouseGlobalPos = new Float2(0,0);

    public TouchDevice(Context context) {
        this.context = context;

        if ( vgoBridgeMarshall != null) {
            vgoBridgeMarshall.Stop();
            vgoBridgeMarshall = null;
        }

        vgoBridgeMarshall = new VGOBridgeMarshall(8888, 25);
        vgoBridgeMarshall.events.onSlaveConnected.addObserver((obs, handle) -> {
            Dbg.Msg("CREATING INPUT BRIDGE CONNECTION!");
            Dbg.Msg("INPUT BRIDGE CONNECTED!");
            //Show();
        });
        vgoBridgeMarshall.Run();
    }

    @Override
    public void Destroy() {
        OverlayManager overlayManager = app.GetOverlayManager();
        touchDeviceOverlayFragment.Destroy();
        touchDeviceEditorOverlayFragment.Destroy();
        overlayManager.Remove(touchDeviceOverlayFragment);
        overlayManager.Remove(touchDeviceEditorOverlayFragment);
        overlayManager.Destroy();
        vgoBridgeMarshall.Stop();
    }

    @Override
    public void Show() {
        OverlayManager overlayManager = app.GetOverlayManager();

        touchDeviceOverlayFragment = new TouchDeviceOverlayFragment(this);
        overlayManager.Add(touchDeviceOverlayFragment);

        touchDeviceEditorOverlayFragment = new TouchDeviceEditorOverlayFragment();
        overlayManager.Add(touchDeviceEditorOverlayFragment);
        overlayManager.onClick.addObserver((observable, o) -> touchDeviceEditorOverlayFragment.SetSelected(-1));
        touchDeviceEditorOverlayFragment.onSetChanged.addObserver((observable, o) -> touchDeviceOverlayFragment.InflateControls());
        touchDeviceEditorOverlayFragment.onClosed.addObserver((observable, o) -> touchDeviceOverlayFragment.OnEditorClosed());

        overlayManager.Show(touchDeviceOverlayFragment);
        overlayManager.Show(touchDeviceEditorOverlayFragment); //TODO LINK1
    }

    @Override
    public void Hide() {
        Destroy();
    }

    @Override
    public void SendMouseClick(int button) {
        vgoBridgeMarshall.PrepareEvent()
                .WriteByte(VGOBridgeProtocol.ACTION_MOUSE_CLICK)
                .WriteInt(button);
    }

    @Override
    public void SendMouseDown(int button) {
        vgoBridgeMarshall.PrepareEvent()
                .WriteByte(VGOBridgeProtocol.ACTION_MOUSE_DOWN)
                .WriteInt(button);
    }

    @Override
    public void SendMouseUp(int button) {
        vgoBridgeMarshall.PrepareEvent()
                .WriteByte(VGOBridgeProtocol.ACTION_MOUSE_UP)
                .WriteInt(button);
    }

    @Override
    public void SendKeyPressed(int keyCode) {
        Dbg.Msg("DEV_KEY = " + keyCode);
        vgoBridgeMarshall.PrepareEvent()
                .WriteByte(VGOBridgeProtocol.ACTION_KEY_PRESSED)
                .WriteInt(keyCode);
    }

    @Override
    public void SendKeyDown(int keyCode) {
        vgoBridgeMarshall.PrepareEvent()
                .WriteByte(VGOBridgeProtocol.ACTION_KEY_DOWN)
                .WriteInt(keyCode);
    }

    @Override
    public void SendKeyUp(int keyCode) {
        vgoBridgeMarshall.PrepareEvent()
                .WriteByte(VGOBridgeProtocol.ACTION_KEY_UP)
                .WriteInt(keyCode);
    }

    @Override
    public void SendMouseShift(float vericalCos, float horizontalCos) {
        float sens = 0.25f;

        this.mouseGlobalPos.x += vericalCos * sens;
        this.mouseGlobalPos.y += horizontalCos * sens;

        Dbg.Msg("GMX = " + this.mouseGlobalPos.x);

        vgoBridgeMarshall.PrepareEvent()
                .WriteByte(VGOBridgeProtocol.ACTION_SET_MOUSE_POS)
                .WriteInt((int) this.mouseGlobalPos.x)
                .WriteInt((int) this.mouseGlobalPos.y);

    }
}
