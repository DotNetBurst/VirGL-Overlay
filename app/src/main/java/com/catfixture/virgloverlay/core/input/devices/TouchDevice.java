package com.catfixture.virgloverlay.core.input.devices;

import static com.catfixture.virgloverlay.core.App.app;

import android.content.Context;
import android.view.KeyEvent;
import android.view.inputmethod.InputConnection;

import com.catfixture.virgloverlay.core.debug.Dbg;
import com.catfixture.virgloverlay.core.input.overlay.TouchDeviceEditorOverlayFragment;
import com.catfixture.virgloverlay.core.input.overlay.TouchDeviceOverlayFragment;
import com.catfixture.virgloverlay.core.overlay.IOverlayFragment;
import com.catfixture.virgloverlay.core.overlay.OverlayManager;
import com.catfixture.virgloverlay.core.utils.math.Int2;
import com.catfixture.virgloverlay.core.vgobridge.VGOBridge;
import com.catfixture.virgloverlay.core.vgobridge.VGOBridgeBuffer;
import com.catfixture.virgloverlay.core.vgobridge.VGOBridgeHandle;
import com.catfixture.virgloverlay.core.vgobridge.VGOBridgeProtocol;


public class TouchDevice implements IInputDevice {
    private final VGOBridge vgoBridge;
    private VGOBridgeHandle handle;
    private Context context;
    private TouchDeviceOverlayFragment touchDeviceOverlayFragment;
    private TouchDeviceEditorOverlayFragment touchDeviceEditorOverlayFragment;
    private VGOBridgeBuffer vgoBridgeBuffer = new VGOBridgeBuffer();
    private Int2 mouseGlobalPos = new Int2(0,0);

    public TouchDevice(Context context) {
        this.context = context;

        vgoBridge = new VGOBridge(8888);
        vgoBridge.OnConnected((handle) -> {
            Dbg.Msg("CREATING INPUT BRIDGE CONNECTION!");
            VGOBridgeBuffer vgoBridgeBuffer = new VGOBridgeBuffer();
            vgoBridgeBuffer.Reset()
                    .WriteByte(VGOBridgeProtocol.PROTOCOL_ENABLE);
            handle.SendBuffer(vgoBridgeBuffer);
            this.handle = handle;
            Dbg.Msg("INPUT BRIDGE CONNECTED!");
        });
        vgoBridge.Start();
    }

    @Override
    public void Destroy() {
        OverlayManager overlayManager = app.GetOverlayManager();
        touchDeviceOverlayFragment.Destroy();
        touchDeviceEditorOverlayFragment.Destroy();
        overlayManager.Remove(touchDeviceOverlayFragment);
        overlayManager.Remove(touchDeviceEditorOverlayFragment);
        overlayManager.Destroy();
        vgoBridge.Stop();
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
    }

    @Override
    public void Hide() {
        Destroy();
    }

    @Override
    public void SendMouseClick(int button) {
        vgoBridgeBuffer.Reset()
                .WriteByte(VGOBridgeProtocol.ACTION_MOUSE_CLICK)
                .WriteInt(button);
        handle.SendBuffer(vgoBridgeBuffer);
    }

    @Override
    public void SendMouseDown(int button) {
        vgoBridgeBuffer.Reset()
                .WriteByte(VGOBridgeProtocol.ACTION_MOUSE_DOWN)
                .WriteInt(button);
        handle.SendBuffer(vgoBridgeBuffer);
    }

    @Override
    public void SendMouseUp(int button) {
        vgoBridgeBuffer.Reset()
                .WriteByte(VGOBridgeProtocol.ACTION_MOUSE_UP)
                .WriteInt(button);
        handle.SendBuffer(vgoBridgeBuffer);
    }

    @Override
    public void SendKeyPressed(int keyCode) {
        Dbg.Msg("DEV_KEY = " + keyCode);
        vgoBridgeBuffer.Reset()
                .WriteByte(VGOBridgeProtocol.ACTION_KEY_PRESSED)
                .WriteInt(keyCode);
        handle.SendBuffer(vgoBridgeBuffer);
    }

    @Override
    public void SendKeyDown(int keyCode) {
        vgoBridgeBuffer.Reset()
                .WriteByte(VGOBridgeProtocol.ACTION_KEY_DOWN)
                .WriteInt(keyCode);
        handle.SendBuffer(vgoBridgeBuffer);
    }

    @Override
    public void SendKeyUp(int keyCode) {
        vgoBridgeBuffer.Reset()
                .WriteByte(VGOBridgeProtocol.ACTION_KEY_UP)
                .WriteInt(keyCode);
        handle.SendBuffer(vgoBridgeBuffer);
    }

    @Override
    public void SendMouseShift(float vericalCos, float horizontalCos) {
        float sens = 6.5f;

        this.mouseGlobalPos.x += vericalCos * sens;
        this.mouseGlobalPos.y += horizontalCos * sens;

        vgoBridgeBuffer.Reset()
                .WriteByte(VGOBridgeProtocol.ACTION_SET_MOUSE_POS)
                .WriteInt(this.mouseGlobalPos.x)
                .WriteInt(this.mouseGlobalPos.y);
        handle.SendBuffer(vgoBridgeBuffer);

    }
}
